package com.example.wave.receiver;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wave.receiver.ModelClasses.Wifimodel;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Network extends AppCompatActivity {


    public static String SSIDname;
    Timer timer = new Timer();
    TextView textView;
    final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 125;
    List<ScanResult> wifiList;
    private WifiManager wifi;
    TimerTask task;
    Wifibroad wifier;
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
    Wifiadapternew wifiScanAdapter;
    private static String TAG = "WifiFragment";
    private String password = null;
    Button button_scan;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network);

        recyclerView = (RecyclerView) findViewById(R.id.wifiRecyclerView);
        wifier = new Wifibroad();
        wifi = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (wifi.isWifiEnabled() == false) {
            Toast.makeText(getApplicationContext(), "Wifi is disabled enabling...", Toast.LENGTH_LONG).show();
            wifi.setWifiEnabled(true);
        }

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressDialog = new ProgressDialog(Network.this);
                progressDialog.setMessage("Scanning wifi connections");
                progressDialog.show();
                progressDialog.setCancelable(false);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(wifier, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));

    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(wifier);
    }

    public class Wifiadapternew extends RecyclerView.Adapter<Wifiadapternew.Viewholder> {
        private List<Wifimodel> wifilist = new ArrayList<Wifimodel>();
        private Context context;
        //        private View.OnClickListener clickListener;
        int item_position, click_position;

        public Wifiadapternew(List<Wifimodel> values, Context context) {
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
        public void onBindViewHolder(Wifiadapternew.Viewholder holder, int position) {
            holder.vName.setText(wifilist.get(position).getName());
            holder.vImage.setBackground(getResources().getDrawable(R.drawable.ic_wifi_black));
            holder.vlock.setBackground(getResources().getDrawable(R.drawable.ic_pswd_black));
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
            return 0;
        }

        public class Viewholder extends RecyclerView.ViewHolder {
            LinearLayout linearLayout;
            ImageView vImage, vlock;
            TextView vName, vpsk;

            public Viewholder(View v, Context context) {
                super(v);
            }
        }
    }

    public class Wifibroad extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            wifi.startScan();
            wifiList = wifi.getScanResults();
            values = new ArrayList<Wifimodel>();

            for (int i = 0; i < wifiList.size(); i++) {
                Wifimodel d = new Wifimodel();
                d.setName(wifiList.get(i).SSID.toString());
                d.setCapabilities(wifiList.get(i).capabilities);
                Log.e("ssiddetails", wifiList.get(i).SSID.toString());
                Log.e("capabledetails", wifiList.get(i).capabilities);
                values.add(d);
            }
            Log.e("helloworld", String.valueOf(values.size()));

            wifiScanAdapter = new Wifiadapternew(values, getApplicationContext());
            recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            recyclerView.setAdapter(wifiScanAdapter);
            //Toast.makeText(getApplicationContext(), "" + values.get(i), Toast.LENGTH_SHORT).show();
            wifiScanAdapter.notifyDataSetChanged();
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }

            if (values.size() < 1) {
                Toast.makeText(Network.this, "No Available connectioons", Toast.LENGTH_SHORT).show();
            }

        }
    }

}
