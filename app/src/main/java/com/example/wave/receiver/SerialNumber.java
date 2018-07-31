package com.example.wave.receiver;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wave.receiver.Activities.Congratulations;
import com.example.wave.receiver.Activities.My_Files;
import com.example.wave.receiver.Notification.Config;
import com.example.wave.receiver.Notification.NotificationUtils;
import com.example.wave.receiver.Server.Pairingserverobjects;
import com.example.wave.receiver.Server.ServerAPIS;
import com.example.wave.receiver.Sessions.SessionsManager;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import es.dmoral.toasty.Toasty;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Mahi on 3/7/2018.
 */

public class SerialNumber extends AppCompatActivity {
    TextView splittext;
    ProgressDialog progressDialog;
    private String androidDeviceId;
    TimerTask task;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    Dialog dialog;
    public static String serial;
    TextView text1, text2, text3, text4;
    String ts, regId;
    public boolean status = false;
    String serialnumber;
    String ssid, model, msgnot;
    public static String message;
    String device_id;
    private SessionsManager sessions;

    public static String login_details = "details";
    SharedPreferences sharedPreferences;

    //   LocationManager locationManager;
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_serialnumber);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        text1 = findViewById(R.id.serialnumberfirstbox);
        splittext = findViewById(R.id.url2);
        text2 = findViewById(R.id.serialnumbersecondbox);
        text3 = findViewById(R.id.serialnumberthirdbox);
        text4 = findViewById(R.id.serialnumberfourthbox);
        ssid = getIntent().getStringExtra("wifiname");
        //ssid = "Anitha";

        sessions = new SessionsManager(getApplicationContext());
        if (sessions.isLoggedIn() == false) {

            sharedPreferences = getSharedPreferences("mypref", Context.MODE_PRIVATE);
            SharedPreferences.Editor edit = sharedPreferences.edit();
            edit.putString(login_details, WiFiConnections.SSIDname);
            Log.e("wificonnection", WiFiConnections.SSIDname);
            edit.apply();

        }

        Intent intent = getIntent();
        String hh = intent.getStringExtra("message");
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                // checking for type intent filter
                if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications
                    FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);

                    // displayFirebaseRegId();

                } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    // new push notification is received

                    msgnot = intent.getStringExtra("message");
                    message = msgnot;
                    sessions.setLogin(true);
                    Intent intent1 = new Intent(getApplicationContext(), Congratulations.class);
                    startActivity(intent1);
                }
            }

        };

        get_alldata();
        send_data();
    }

    public void send_data() {
        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .build();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading ...");
        progressDialog.show();
        JSONObject changepass = new JSONObject();
        try {


            if (sessions.isLoggedIn()) {
                Log.e("checklogger", SSID());
                changepass.put("ssid", SSID());
                changepass.put("deviceid", device_id);
                changepass.put("devicename", model);
                changepass.put("timestamp", ts);
                changepass.put("devicetoken", regId);
                changepass.put("status", status);

            } else {
                changepass.put("ssid", ssid);
                changepass.put("deviceid", device_id);
                changepass.put("devicename", model);
                changepass.put("timestamp", ts);
                changepass.put("devicetoken", regId);
                changepass.put("status", status);
            }
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
                Log.e("onresponsemethod ", response.body().getResponse() + response.body().getUsername());
                progressDialog.dismiss();

                if (response.body().getResponse().equals("2")) {
                    String username = response.body().getUsername();
                    SerialNumber.message = username;
                    Toast.makeText(SerialNumber.this, "Successfully Registered details", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getApplicationContext(), Congratulations.class);
                    startActivity(intent);
                } else if (response.body().getResponse().equals("3")) {
                    //Toast.makeText(SerialNumber.this, "Successfully Registered details", Toast.LENGTH_SHORT).show();
                    Toasty.success(getApplicationContext(), "Successfully Registered details", Toast.LENGTH_SHORT, true).show();

                } else if (response.body().getResponse().equals("0")) {

                    Toast.makeText(SerialNumber.this, "Device Already Available", Toast.LENGTH_SHORT).show();
                    //    alert("Failed", "Failed to Register Try again");
                }

            }

            @Override
            public void onFailure(Call<Pairingserverobjects> call, Throwable t) {
                progressDialog.dismiss();
                Log.e("failedloger", "failed to changepassword" + "\n" + t);
                alert("Failed !", "Failed to Register please Try again!");
                //  Toast.makeText(SerialNumber.this, "failed" + t, Toast.LENGTH_SHORT).show();

            }
        });

    }

    public void get_alldata() {
        androidDeviceId = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        regId = pref.getString("regId", null);

        // textView.setText("Your Device ID Address: " + androidDeviceId);
        Log.e("id", " " + androidDeviceId);

        serialnumber = androidDeviceId;
        serial = serialnumber;
        String val = "4";   // use 4 here to insert spaces every 4 characters
        final String result = serialnumber.replaceAll("(.{" + val + "})", "$1 ").trim();
        device_id = result;
        Log.e("split", " " + result);
        String[] array = result.split(" ");
        text1.setText(array[0]);
        text2.setText(array[1]);
        text3.setText(array[2]);
        text4.setText(array[3]);

        Long tsLong = System.currentTimeMillis() / 1000;
        ts = tsLong.toString();
        model = android.os.Build.MODEL;
        Log.e("jhvjhvsbvhj", ssid + "\n" + regId + "\n" + serialnumber + "\n" + model + "\n" + ts + "\n" + status);

    }

    @Override
    public void onBackPressed() {

    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();

        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.REGISTRATION_COMPLETE));
        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));
        // clear the notification area when the app is opened
        NotificationUtils.clearNotifications(getApplicationContext());
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

    public void alert(String message, String msg) {
        dialog = new Dialog(SerialNumber.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.register_alert);
        dialog.setCancelable(false);
        TextView textView = dialog.findViewById(R.id.unpair_alerttitle);
        TextView textView_msg = dialog.findViewById(R.id.unpair_alerttext);
        textView.setText(message);
        textView_msg.setText(msg);
        final TextView textView_ok = dialog.findViewById(R.id.alertbutton_cancel);
        final RelativeLayout ok = dialog.findViewById(R.id.relate_one);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send_data();
                dialog.dismiss();
            }
        });

        dialog.show();
    }

}


        /*
        Calendar cal = Calendar.getInstance();
        TimeZone tz = cal.getTimeZone();

*//* debug: is it local time? *//*
        Log.d("Time zone: ", tz.getDisplayName());

*//* date formatter in local timezone *//*
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        sdf.setTimeZone(tz);

*//* print your timestamp and double check it's the date you expect *//*
        long timestamp = Long.parseLong(ts);
        String localTime = sdf.format(new Date(timestamp * 1000)); // I assume your timestamp is in seconds and you're converting to milliseconds?
        Log.e("makjnccn", String.valueOf(localTime));
        Toast.makeText(getApplicationContext(), "" + localTime, Toast.LENGTH_SHORT).show();
     */
//send_data();