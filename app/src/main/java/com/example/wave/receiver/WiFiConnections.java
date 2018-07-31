package com.example.wave.receiver;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.drawable.Animatable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wave.receiver.ModelClasses.Wifimodel;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import es.dmoral.toasty.Toasty;

//import com.example.wave.receiver.Adapters.Wifiscanadapter;

/**
 * Created by Mahi on 3/8/2018.
 */

public class WiFiConnections extends AppCompatActivity {

    public static String SSIDname;
    Timer timer = new Timer();
    TextView textView;
    final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 125;
    List<ScanResult> wifiList;
    private WifiManager wifi;
    TimerTask task;
    Wifier wifier;
    Dialog dialog_connect;
    AlertDialog alertDialog;
    List<Wifimodel> values;
    int netCount = 0;
    Handler h = new Handler();
    int delay = 60 * 1000; //1 second=1000 milisecond, 15*1000=15seconds
    Runnable runnable;
    ProgressDialog progressDialog;
    int currentPosition = -1;
    RecyclerView recyclerView;
    Wifiadapter wifiScanAdapter;
    private static String TAG = "WifiFragment";
    private String password = null;
    Button button_scan;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wificonnections);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        textView = (TextView) findViewById(R.id.setupWiFi);
        recyclerView = (RecyclerView) findViewById(R.id.wifiRecyclerView);
        checkandAskPermission();
        wifier = new Wifier();

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressDialog = new ProgressDialog(WiFiConnections.this);
                progressDialog.setMessage("Scanning wifi connections");
                progressDialog.show();
                progressDialog.setCancelable(false);
            }
        });

        wifi = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (wifi.isWifiEnabled() == false) {
            Toast.makeText(getApplicationContext(), "Wifi is disabled enabling...", Toast.LENGTH_LONG).show();
            wifi.setWifiEnabled(true);
        }

    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    private void checkandAskPermission() {
        List<String> permissionsNeeded = new ArrayList<String>();

        final List<String> permissionsList = new ArrayList<String>();
        if (!addPermission(permissionsList, Manifest.permission.ACCESS_COARSE_LOCATION))
            permissionsNeeded.add("");


        if (permissionsList.size() > 0) {
            if (permissionsNeeded.size() > 0) {
                // Need Rationale
                String message = "You need to grant access for network " + permissionsNeeded.get(0);
                for (int i = 0; i < permissionsNeeded.size(); i++)
                    message = message + ", " + permissionsNeeded.get(i);
                showMessageOKCancel(message,
                        new DialogInterface.OnClickListener() {
                            @RequiresApi(api = Build.VERSION_CODES.M)
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                                        REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
                            }
                        });
                return;
            }

            requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                    REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
            return;
        }
        // initVideo();
    }


    private boolean addPermission(List<String> permissionsList, String permission) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (getApplicationContext().checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                permissionsList.add(permission);
                if (!shouldShowRequestPermissionRationale(permission))
                    return false;
            }
        }
        return true;
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(WiFiConnections.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", okListener)
                .create()
                .show();
    }

    public void connectWiFi(String SSID, String password, String Security) {
        try {

            Log.d("ssidpassnsec", "Item clicked, SSID " + SSID + " Security : " + Security);

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

            WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
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


    @Override
    protected void onResume() {
        super.onResume();
        wifi.startScan();
        registerReceiver(wifier, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));

    }

    @Override
    protected void onPause() {

        unregisterReceiver(wifier);
        // h.removeCallbacks(runnable);
        super.onPause();
        //timer.cancel();
    }

    public class Wifiadapter extends RecyclerView.Adapter<Wifiadapter.ViewHolder> {
        private List<Wifimodel> wifilist = new ArrayList<Wifimodel>();
        private Context context;
        //        private View.OnClickListener clickListener;
        int item_position, click_position;

        public Wifiadapter(List<Wifimodel> wifilist, Context context)
        {
            this.wifilist = wifilist;
            this.context = context;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_activity_wificonnections, null);
            final ViewHolder viewHolder = new ViewHolder(v, context);
            return viewHolder;
        }

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {

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
            int itemCount = wifilist.size();

            return itemCount;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            LinearLayout linearLayout;
            ImageView vImage, vlock;
            TextView vName, vpsk;
            Context context;
            int row_index = -1;

            public ViewHolder(View v, Context context) {
                super(v);
                linearLayout = v.findViewById(R.id.linear_list);
                vName = v.findViewById(R.id.txt_name);
                vpsk = v.findViewById(R.id.txt_psk);
                vImage = v.findViewById(R.id.img_wifi);
                vlock = v.findViewById(R.id.img_pass);
                v.setOnClickListener(new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onClick(View v)
                    {

                        item_position = getAdapterPosition();

                        Log.e("clickposition", String.valueOf(wifilist.get(item_position).getName()));
                        if (wifilist.get(item_position).getCapabilities().equals("[ESS]"))
                        {
                            connectWiFi(String.valueOf(wifilist.get(item_position).getName()), "", wifilist.get(item_position).getCapabilities());
                            bigdia(String.valueOf(wifilist.get(item_position).getName()));
                            notifyDataSetChanged();


                        } else {
                            LayoutInflater li = LayoutInflater.from(WiFiConnections.this);
                            final View promptsView = li.inflate(R.layout.auth_dialog, null);

                            alertDialog = new AlertDialog.Builder(WiFiConnections.this).create();
                            alertDialog.setView(promptsView);
                            alertDialog.setCancelable(false);

                            final EditText userInput = (EditText) promptsView
                                    .findViewById(R.id.edit_pass);
                            Button ok = (Button) promptsView.findViewById(R.id.btn_connecct);
                            Button can = (Button) promptsView.findViewById(R.id.btn_cancel);
                            TextView ssidText = (TextView) promptsView.findViewById(R.id.text_connect);
                            ssidText.setText("Connecting to " + wifilist.get(item_position).getName());
                           /* TextView security = (TextView) promptsView.findViewById(R.id.textViewSecurity);
                            security.setText("Security for Network is:\n" + wifilist.get(item_position).getCapabilities());
*/
                            Log.e("SecurityNetworkis", wifilist.get(item_position).getCapabilities());
                            ok.setOnClickListener(new View.OnClickListener()
                            {
                                @Override
                                public void onClick(View v)
                                {
                                    password = userInput.getText().toString();
                                    if (password.length() > 0)
                                    {
                                        connectWiFi(String.valueOf(wifilist.get(item_position).getName()), password, wifilist.get(item_position).getCapabilities());
                                        bigdia(String.valueOf(wifilist.get(item_position).getName()));

                                        alertDialog.dismiss();

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

                    }
                });

            }
        }
    }


    public void bigdia(String wiif) {
        ImageView imageView;
        TextView wifiname;
        dialog_connect = new Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        dialog_connect.setCancelable(false);
        dialog_connect.setContentView(R.layout.activity_wificonnecting);
        imageView = (ImageView) dialog_connect.findViewById(R.id.wifiConnector);
        wifiname = (TextView) dialog_connect.findViewById(R.id.connector_name);
        wifiname.setText("Connecting to " + wiif);
        ((Animatable) imageView.getDrawable()).start();
        dialog_connect.show();
        congt(wiif);
    }

    public void congt(final String wifi) {
        task = new TimerTask() {

            @Override
            public void run() {

                runOnUiThread(new Runnable() {
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // timer.cancel();
    }


    public void connection(String wifiname) {
        Log.e("kkkkkkk", wifiname);
        ConnectivityManager connManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();

        if (mWifi.isConnected()) {
            String ssid = wifiInfo.getSSID();
            String SSID = ssid.replace("\"", "");
            Log.e("cleanssid", SSID);

            if (SSID.equals(wifiname)) {
                SSIDname = SSID;
                Intent intent = new Intent(getApplicationContext(), SerialNumber.class);
                intent.putExtra("wifiname", SSID);
                startActivity(intent);

                dialog_connect.dismiss();
            } else {
                Toasty.error(this, "Not Connected to" + wifiname, Toast.LENGTH_SHORT, true).show();
                dialog_connect.dismiss();

            }
            // Do whatever
        } else {
            Toasty.error(this, "Not Connected to" + wifiname, Toast.LENGTH_SHORT, true).show();

            //  Toast.makeText(this, "Not Connected to" + wifiname, Toast.LENGTH_SHORT).show();
            dialog_connect.dismiss();
        }

    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }

    public class Wifier extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            wifi.startScan();
            wifiList = wifi.getScanResults();
            values = new ArrayList<Wifimodel>();

            for (int i = 0; i < wifiList.size(); i++) {
                Wifimodel d = new Wifimodel();
                d.setName(wifiList.get(i).SSID.toString());
                d.setCapabilities(wifiList.get(i).capabilities);
                d.setLevel(wifiList.get(i).level);
                int kk = wifiList.get(i).level;
                Log.e("ssiddetails", String.valueOf(kk));
                Log.e("capabledetails", wifiList.get(i).capabilities);
                values.add(d);
            }
            Log.e("valuessize", String.valueOf(values.size()));
            wifiScanAdapter = new Wifiadapter(values, getApplicationContext());
            recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            recyclerView.setAdapter(wifiScanAdapter);
            //Toast.makeText(getApplicationContext(), "" + values.get(i), Toast.LENGTH_SHORT).show();
            wifiScanAdapter.notifyDataSetChanged();
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }

            if (values.size() < 1) {
                Toasty.error(getApplicationContext(), "No Available connections", Toast.LENGTH_SHORT, true).show();

                //    Toast.makeText(WiFiConnections.this, "No Available connectioons", Toast.LENGTH_SHORT).show();
            }

        }
    }

    public String SSID() {

        ConnectivityManager connManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        String ssid = wifiInfo.getSSID();
        String SSID = ssid.replace("\"", "");
        return SSID;

    }

    public boolean connections() {

        ConnectivityManager connManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        if (mWifi.isConnected()) {
            return true;
        } else {
            return false;
        }
    }

}
