package com.example.wave.receiver.Fragments;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wave.receiver.Activities.My_Files;
import com.example.wave.receiver.Notification.Config;
import com.example.wave.receiver.R;
import com.example.wave.receiver.SerialNumber;
import com.example.wave.receiver.Server.Pairingserverobjects;
import com.example.wave.receiver.Server.ServerAPIS;
import com.example.wave.receiver.WiFiConnections;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import es.dmoral.toasty.Toasty;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.Context.CONNECTIVITY_SERVICE;

public class Networkfrag extends Fragment {
    Scanning scanning;
    final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 125;
    List<ScanResult> wifiList;
    private WifiManager wifi;
    List<MycLASS> values;
    Wifiscanadap wifiscanadap;
    int netCount = 0;
    TimerTask task;
    String ts, regId, device_id, serialnumber, ssid_net, model;
    public boolean status = false;
    private String androidDeviceId;
    AlertDialog alertDialog;
    RecyclerView recyclerView;
    //WifiScanAdapter wifiScanAdapter;
    private static String TAG = "WifiFragment";
    private String password = null;
    Button button_scan;
    ProgressDialog progressDialog, progress_connect;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_networkfrag, container, false);
        scanning = new Scanning();
        wifi = (WifiManager) getActivity().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        //Check wifi enabled or not
        if (wifi.isWifiEnabled() == false) {
            Toast.makeText(getActivity(), "Wifi is disabled enabling...", Toast.LENGTH_LONG).show();
            wifi.setWifiEnabled(true);
        }
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        recyclerView = getActivity().findViewById(R.id.wifiRecyclerView);
        get_alldata();
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("loading");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    @Override
    public void onResume() {
        super.onResume();
        wifi.startScan();
        getActivity().registerReceiver(scanning, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));

    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(scanning);
    }

    public class Scanning extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            wifi.startScan();
            wifiList = wifi.getScanResults();
            values = new ArrayList<MycLASS>();
            for (int i = 0; i < wifiList.size(); i++) {
                MycLASS mycls = new MycLASS();
                mycls.setName(wifiList.get(i).SSID.toString());
                mycls.setCapabilities(wifiList.get(i).capabilities);
                mycls.setLevel(wifiList.get(i).level);
                values.add(mycls);
                Log.e("checkvaluesize", (String) values.get(i).getName());
            }
            wifiscanadap = new Wifiscanadap(values, getActivity());
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
            recyclerView.setAdapter(wifiscanadap);
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            if (values.size() < 1) {
                Toasty.error(getActivity(), "No available WIFI connections", Toast.LENGTH_SHORT, true).show();
            }
            Log.e("wifiscanadap", "calledscanning" + values.size());
        }
    }

    public class Wifiscanadap extends RecyclerView.Adapter<Wifiscanadap.Viewholder> {
        private List<MycLASS> wifilist = new ArrayList<MycLASS>();
        private Context context;
        int wifi_position;

        public Wifiscanadap(List<MycLASS> wifilist, Context context) {
            this.wifilist = wifilist;
            this.context = context;
        }

        @Override
        public Viewholder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_activity_wificonnections, null);
            final Viewholder viewholder = new Viewholder(v, context);
            return viewholder;
        }

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void onBindViewHolder(Viewholder holder, int position) {
            holder.vName.setText(wifilist.get(position).getName());
            holder.vImage.setBackground(getResources().getDrawable(R.drawable.ic_wifi_black));


            if (wifilist.get(position).getLevel() > -50) {
                holder.vImage.setBackground(getResources().getDrawable(R.drawable.wifi_strengthfull));
            } else if (wifilist.get(position).getLevel() > -60) {
                holder.vImage.setBackground(getResources().getDrawable(R.drawable.wifi_strength3));

            } else if (wifilist.get(position).getLevel() > -70) {
                holder.vImage.setBackground(getResources().getDrawable(R.drawable.wifi_strength2));

            } else if (wifilist.get(position).getLevel() > -90) {
                holder.vImage.setBackground(getResources().getDrawable(R.drawable.wifi_strength1));
            } else {
                holder.vImage.setBackground(getResources().getDrawable(R.drawable.wifi_strength0));

            }

            holder.vlock.setBackground(getResources().getDrawable(R.drawable.ic_pswd_black));
            if (connections() == true) {
                if (wifilist.get(position).getName().equals(SSID())) {
                    holder.vImage.setBackground(getResources().getDrawable(R.drawable.wifi_newconected));
                    holder.vName.setTextColor(getResources().getColor(R.color.colorPrimary));
                }

            }

            if (wifilist.get(position).getCapabilities().equals("[ESS]")) {
                holder.vlock.setVisibility(View.INVISIBLE);
                holder.vpsk.setVisibility(View.INVISIBLE);
            } else {
                holder.vlock.setVisibility(View.VISIBLE);
                holder.vpsk.setVisibility(View.VISIBLE);
            }


        }

        @Override
        public int getItemCount() {
            return values.size();
        }

        public class Viewholder extends RecyclerView.ViewHolder {
            LinearLayout linearLayout;
            ImageView vImage, vlock;
            TextView vName, vpsk;

            public Viewholder(View v, Context context) {
                super(v);
                linearLayout = v.findViewById(R.id.linear_list);
                vName = v.findViewById(R.id.txt_name);
                vpsk = v.findViewById(R.id.txt_psk);
                vImage = v.findViewById(R.id.img_wifi);
                vlock = v.findViewById(R.id.img_pass);

                v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        wifi_position = getAdapterPosition();
                        alertDialog = new AlertDialog.Builder(getActivity()).create();
                        if (alertDialog.isShowing()) {
                            alertDialog.dismiss();
                        }
                        if (wifilist.get(wifi_position).getCapabilities().equals("[ESS]")) {
                            connectWiFi(String.valueOf(wifilist.get(wifi_position).getName()), "", wifilist.get(wifi_position).getCapabilities());

                            bigdia(String.valueOf(wifilist.get(wifi_position).getName()));
                            notifyDataSetChanged();
                        }
                        LayoutInflater li = LayoutInflater.from(getActivity());
                        final View promptsView = li.inflate(R.layout.auth_dialog, null);
                        alertDialog.setView(promptsView);
                        alertDialog.setCancelable(false);

                        final EditText userInput = (EditText) promptsView
                                .findViewById(R.id.edit_pass);
                        Button ok = (Button) promptsView.findViewById(R.id.btn_connecct);
                        Button can = (Button) promptsView.findViewById(R.id.btn_cancel);
                        TextView ssidText = (TextView) promptsView.findViewById(R.id.text_connect);
                        ssidText.setText("Connecting to " + wifilist.get(wifi_position).getName());
                           /* TextView security = (TextView) promptsView.findViewById(R.id.textViewSecurity);
                            security.setText("Security for Network is:\n" + wifilist.get(item_position).getCapabilities());
*/
                        Log.e("SecurityNetworkis", wifilist.get(wifi_position).getCapabilities());
                        ok.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                password = userInput.getText().toString();
                                if (password.length() > 0) {
                                    connectWiFi(String.valueOf(wifilist.get(wifi_position).getName()), password, wifilist.get(wifi_position).getCapabilities());
                                    alertDialog.dismiss();
                                    bigdia(String.valueOf(wifilist.get(wifi_position).getName()));

                                    notifyDataSetChanged();
                                } else {
                                    userInput.setError("Enter Password");
                                    //   alertDialog.dismiss();
                                }
                            }
                        });
                        can.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                alertDialog.dismiss();
                                notifyDataSetChanged();
                            }
                        });
                        alertDialog.show();

                    }
                    //    Toast.makeText(getActivity(), "hmm" + getAdapterPosition(), Toast.LENGTH_SHORT).show();
                });
            }
        }

    }

    private void bigdia(final String s) {
        progress_connect = new ProgressDialog(getActivity());
        progress_connect.setMessage("Connecting to " + s);
        progress_connect.setCancelable(false);
        progress_connect.show();
        congt(s);
    }

    public void congt(final String wifi) {
        task = new TimerTask() {

            @Override
            public void run() {

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        connection(wifi);
                    }
                });
            }
        };
        Timer t = new Timer();
        t.schedule(task, 10000);
    }

    public void connection(String wifiname) {
        Log.e("kkkkkkk", wifiname);
        ConnectivityManager connManager = (ConnectivityManager) getActivity().getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        WifiManager wifiManager = (WifiManager) getActivity().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();

        if (mWifi.isConnected()) {
            String ssid = wifiInfo.getSSID();
            String SSID = ssid.replace("\"", "");
            Log.e("networkfragssid", SSID);

            if (SSID.equals(wifiname)) {
                progress_connect.dismiss();
                ssid_net = wifiname;
            /*    get_alldata();
                send_data();*/

                Toast.makeText(getActivity(), " Connected to" + wifiname, Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getActivity(), SerialNumber.class);
                startActivity(i);

            } else {
                progress_connect.dismiss();
                Toast.makeText(getActivity(), "Not Connected to" + wifiname, Toast.LENGTH_SHORT).show();
            }
        } else {
            progress_connect.dismiss();
            Toast.makeText(getActivity(), "Not Connected to" + wifiname, Toast.LENGTH_SHORT).show();
        }

    }

    public void connectWiFi(String SSID, String password, String Security) {
        try {

            Log.e("ssidpassnsec", "Item clicked, SSID " + SSID + " Security : " + Security);

            String networkSSID = SSID;
            String networkPass = password;

            WifiConfiguration conf = new WifiConfiguration();
            conf.SSID = "\"" + networkSSID + "\"";   // Please note the quotes. String should contain ssid in quotes
            conf.status = WifiConfiguration.Status.ENABLED;
            conf.priority = 40;

            if (Security.toUpperCase().contains("WEP")) {
                Log.v("rht", "Configuring WEP");
                conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
                conf.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
                conf.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
                conf.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
                conf.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.SHARED);
                conf.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
                conf.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
                conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
                conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);

                if (networkPass.matches("^[0-9a-fA-F]+$")) {
                    conf.wepKeys[0] = networkPass;
                } else {
                    conf.wepKeys[0] = "\"".concat(networkPass).concat("\"");
                }

                conf.wepTxKeyIndex = 0;

            } else if (Security.toUpperCase().contains("WPA")) {
                Log.v(TAG, "Configuring WPA");

                conf.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
                conf.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
                conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
                conf.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
                conf.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
                conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
                conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
                conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
                conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);

                conf.preSharedKey = "\"" + networkPass + "\"";

            } else {
                Log.v("opennetwork", "Configuring OPEN network");
                conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
                conf.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
                conf.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
                conf.allowedAuthAlgorithms.clear();
                conf.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
                conf.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
                conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
                conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
                conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
                conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            }

            WifiManager wifiManager = (WifiManager) getActivity().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            int networkId = wifiManager.addNetwork(conf);

            Log.v(TAG, "Add result " + networkId);

            List<WifiConfiguration> list = wifiManager.getConfiguredNetworks();
            for (WifiConfiguration i : list) {
                if (i.SSID != null && i.SSID.equals("\"" + networkSSID + "\"")) {
                    Log.v("mytagone", "WifiConfiguration SSID " + i.SSID);

                    boolean isDisconnected = wifiManager.disconnect();
                    Log.v("disconwifi", "isDisconnected : " + isDisconnected);

                    boolean isEnabled = wifiManager.enableNetwork(i.networkId, true);
                    Log.v("isenablewifi", "isEnabled : " + isEnabled);

                    boolean isReconnected = wifiManager.reconnect();
                    Log.v("reconnectwifi", "isReconnected : " + isReconnected);
                    //connection(SSID);
                    break;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String SSID() {
        ConnectivityManager connManager = (ConnectivityManager) getActivity().getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        WifiManager wifiManager = (WifiManager) getActivity().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        String ssid = wifiInfo.getSSID();
        String SSID = ssid.replace("\"", "");
        return SSID;

    }

    public boolean connections() {
        ConnectivityManager connManager = (ConnectivityManager) getActivity().getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        WifiManager wifiManager = (WifiManager) getActivity().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        if (mWifi.isConnected()) {
            return true;
        } else {
            return false;
        }
    }

    public void get_alldata() {
        androidDeviceId = Settings.Secure.getString(getActivity().getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);
        SharedPreferences pref = getActivity().getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        regId = pref.getString("regId", null);

        // textView.setText("Your Device ID Address: " + androidDeviceId);
        Log.e("id", " " + androidDeviceId);

        serialnumber = androidDeviceId;
        //serial = serialnumber;
        String val = "4";   // use 4 here to insert spaces every 4 characters
        final String result = serialnumber.replaceAll("(.{" + val + "})", "$1 ").trim();
        device_id = result;

        Long tsLong = System.currentTimeMillis() / 1000;
        ts = tsLong.toString();
        model = android.os.Build.MODEL;
        /* ssid = SSID();*/
        Log.e("getalldatanet", ssid_net + "\n" + regId + "\n" + serialnumber + "\n" + model + "\n" + ts + "\n" + status);
    }

    public void send_data() {
        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .build();
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Regestering...");
        progressDialog.show();
        JSONObject changepass = new JSONObject();
        try {
            changepass.put("ssid", ssid_net);
            changepass.put("deviceid", device_id);
            changepass.put("devicename", model);
            changepass.put("timestamp", ts);
            changepass.put("devicetoken", regId);
            changepass.put("status", status);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ServerAPIS.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ServerAPIS api = retrofit.create(ServerAPIS.class);
        Call<Pairingserverobjects> changepsd = api.sendingdata(changepass);
        changepsd.enqueue(new retrofit2.Callback<Pairingserverobjects>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(Call<Pairingserverobjects> call, Response<Pairingserverobjects> response) {
                Log.e("senddtanetres", response.body().getResponse() + response.body().getUsername());
                progressDialog.dismiss();

                if (response.body().getResponse().equals("2")) {
                    String username = response.body().getUsername();
                    SerialNumber.message = username;
                    Toast.makeText(getActivity(), "Successfully Registered details", Toast.LENGTH_LONG).show();
/*
                    Intent intent = new Intent(getActivity(), My_Files.class);
                    startActivity(intent);*/
                } else if (response.body().getResponse().equals("3")) {
                    Toasty.success(getActivity(), "Successfully Registered details", Toast.LENGTH_SHORT, true).show();

                    //     Toast.makeText(getActivity(), "Successfully Registered details", Toast.LENGTH_SHORT).show();

                } else if (response.body().getResponse().equals("0")) {

                    Toast.makeText(getActivity(), "Device Already Available", Toast.LENGTH_SHORT).show();
                    //    alert("Failed", "Failed to Register Try again");
                }
            }

            @Override
            public void onFailure(Call<Pairingserverobjects> call, Throwable t) {
                progressDialog.dismiss();
                Toasty.error(getActivity(), "Failed to register", Toast.LENGTH_SHORT, true).show();
                Log.e("failedloger", "" + "\n" + t);
            }
        });

    }


}
