package com.example.wave.receiver.Fragments;

import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.text.format.Formatter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wave.receiver.MainActivity;
import com.example.wave.receiver.Notification.Config;
import com.example.wave.receiver.R;
import com.example.wave.receiver.SerialNumber;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Locale;

import static android.content.Context.WIFI_SERVICE;
import static android.provider.Telephony.ThreadsColumns.ERROR;

public class Information extends Fragment {
    TextView textView_data, textView_username, textView_model, textView_serial, textView_ip, textView_mac, textView_locaion;
    TextView system_version, app_version, firmware, name, fccid;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_information, container, false);
        textView_data = view.findViewById(R.id.text_data);
        textView_username = view.findViewById(R.id.username_values);
        textView_model = view.findViewById(R.id.model_value);
        system_version = view.findViewById(R.id.system_value);
        firmware = view.findViewById(R.id.firmware_value);
        name = view.findViewById(R.id.name_value);
        fccid = view.findViewById(R.id.fccid_value);
        app_version = view.findViewById(R.id.application_value);
        textView_locaion = view.findViewById(R.id.location_value);
        textView_serial = view.findViewById(R.id.serial_value);
        textView_ip = view.findViewById(R.id.ipadres_value);
        textView_mac = view.findViewById(R.id.macadress_value);
        textView_mac.setSelected(true);
        textView_serial.setSelected(true);
        textView_locaion.setSelected(true);
        textView_username.setSelected(true);
        set_data();
        setmodel_value();
        serial();
        set_ip();
        username();
        appver();
        //14.666581, 77.613387
        Double lat = MainActivity.late;
        Double lan = MainActivity.lone;
//        Log.e("hghjghgh", String.valueOf(lat + lan));
        if (lat != null && lan != null) {
            get_address(lat, lan);
        } else {
            textView_locaion.setText("Address not found");
        }
        return view;
    }

    public static boolean externalMemoryAvailable() {
        return android.os.Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public static String getAvailableInternalMemorySize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSizeLong();
        long availableBlocks = stat.getAvailableBlocksLong();
        return formatSize(availableBlocks * blockSize);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public static String getTotalInternalMemorySize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSizeLong();
        long totalBlocks = stat.getBlockCountLong();
        return formatSize(totalBlocks * blockSize);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public static String getAvailableExternalMemorySize() {
        if (externalMemoryAvailable()) {
            File path = Environment.getExternalStorageDirectory();
            StatFs stat = new StatFs(path.getPath());
            long blockSize = stat.getBlockSizeLong();
            long availableBlocks = stat.getAvailableBlocksLong();
            return formatSize(availableBlocks * blockSize);
        } else {
            return ERROR;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public static String getTotalExternalMemorySize() {
        if (externalMemoryAvailable()) {
            File path = Environment.getExternalStorageDirectory();
            StatFs stat = new StatFs(path.getPath());
            long blockSize = stat.getBlockSizeLong();
            long totalBlocks = stat.getBlockCountLong();

            long value = totalBlocks * blockSize;

            Log.e("blockSize", String.valueOf(value));
            return formatSize(totalBlocks * blockSize);

        } else {
            return ERROR;
        }
    }

    public static String formatSize(long size) {
        String suffix = null;

        if (size >= 1024) {
            // suffix = "KB";
            size /= 1024;
            if (size >= 1024) {
                //       suffix = "MB";
                size /= 1024;
            }
        }

        StringBuilder resultBuffer = new StringBuilder(Long.toString(size));

        int commaOffset = resultBuffer.length() - 3;
        while (commaOffset > 0) {
            resultBuffer.insert(commaOffset, ',');
            commaOffset -= 3;
        }

        if (suffix != null) resultBuffer.append(suffix);
        return resultBuffer.toString();
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public void set_data() {

        String total = getTotalExternalMemorySize();
        Log.e("totalmemo", total);
        String totaln = total.replace(",", "");
        String avail = getAvailableExternalMemorySize();
        Log.e("totalavailable", avail);

        String availn = avail.replace(",", "");
        Double tot = (Double.parseDouble(totaln) / 1024);
        Log.e("doubletot", String.valueOf(tot));
        Double ava = (Double.parseDouble(availn) / 1024);
        Log.e("kjdnvkjvb", String.valueOf(ava));
        Double res = ((ava % tot));
        String available = String.valueOf(ava);

        Log.e("hjdhjs", String.valueOf(available));
        String finalt = String.valueOf(tot);
        String total_finaly = finalt.substring(0, finalt.indexOf("."));
        String available_finaly = available.substring(0, available.indexOf("."));

        Log.e("checkchedam", available_finaly + " GB" + " free of total " + total_finaly + " GB");
        textView_data.setText(available_finaly + " GB" + " free of total " + total_finaly + " GB");

    }

    public void setmodel_value() {
        String str = android.os.Build.MODEL;
        textView_model.setText(str);
    }

    public void username() {
        String str = SerialNumber.message;
        textView_username.setText(str);
    }


    public void set_ip() {
        WifiManager wm = (WifiManager) getActivity().getApplicationContext().getSystemService(WIFI_SERVICE);
        WifiInfo wifiInfo = wm.getConnectionInfo();
        String ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
        textView_mac.setText(wifiInfo.getMacAddress());
        textView_ip.setText(ip);
    }

    public void serial() {
        String androidDeviceId = Settings.Secure.getString(getActivity().getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);

        // textView.setText("Your Device ID Address: " + androidDeviceId);
        Log.e("id", " " + androidDeviceId);

        String serialnumber = androidDeviceId;
        String serial = serialnumber;
        String val = "4";   // use 4 here to insert spaces every 4 characters
        final String result = serialnumber.replaceAll("(.{" + val + "})", "$1 ").trim();
        //device_id = result;
        textView_serial.setText(result);
        Log.e("split", " " + result);
       /* String[] array = result.split(" ");
        text1.setText(array[0]);
        text2.setText(array[1]);
        text3.setText(array[2]);
        text4.setText(array[3]);*/

    }

    public void get_address(Double latitude, Double longitude) {
        Log.e("nvsjhcbjsh", "jhcbjac");
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(getActivity(), Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
            String postalCode = addresses.get(0).getPostalCode();
            String knownName = addresses.get(0).getFeatureName();

            textView_locaion.setText(address);
       /*     Log.e("bkjsnbksdnjvksjn", address + city + state);
            Toast.makeText(getActivity(), "" + city + state + country, Toast.LENGTH_SHORT).show();
*/
        } catch (IOException e) {
            Toast.makeText(getActivity(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    public void appver() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        int versionnum = Build.VERSION.SDK_INT;
        String versionRelease = Build.VERSION.RELEASE;
        name.setText(manufacturer);
        firmware.setText(model);
        fccid.setText(versionRelease);
        Log.e("system_version", String.valueOf(versionnum));

        PackageInfo pInfo = null;
        try {
            pInfo = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
            String version = String.valueOf(pInfo.versionCode);
            app_version.setText(version);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        Field[] fields = Build.VERSION_CODES.class.getFields();
        String osName = fields[Build.VERSION.SDK_INT ].getName();
        Log.e("Android OsName:", osName);
        system_version.setText(String.valueOf(osName+" "+versionnum));
    }
}
