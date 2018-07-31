package com.example.wave.receiver;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.wave.receiver.Activities.Congratulations;

import com.example.wave.receiver.Sessions.SessionsManager;
import com.nabinbhandari.android.permissions.PermissionHandler;
import com.zl.reik.dilatingdotsprogressbar.DilatingDotsProgressBar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements LocationListener {
    DilatingDotsProgressBar dilatingDotsProgressBar;
    TimerTask task;
    int count;
    ProgressDialog progressDialog;
    private SessionsManager sessions;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    String message;
    final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 125;
    int i = count;
    public static Double late, lone;
    LocationManager locationManager;
    SharedPreferences sharedPreferences;

    /* Developed by Mr.Rajashekar Reddy AND Mr.mahindra*/

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        dilatingDotsProgressBar = (DilatingDotsProgressBar) findViewById(R.id.dots);
        dilatingDotsProgressBar.showNow();
        sessions = new SessionsManager(getApplicationContext());


        int secondsDelayed = 3;
        new Handler().postDelayed(new Runnable() {
            public void run() {

                if (Build.VERSION.SDK_INT >= 23) {
                    requestCameraAndStorage();
                } else {
                    connection();
                    getLocation();
                    finish();
                }
            }
        }, secondsDelayed * 1000);
    }

    public void connection() {

        ConnectivityManager connManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();

        if (mWifi.isConnected()) {
            String ssid = wifiInfo.getSSID();
            String SSID = ssid.replace("\"", "");
            if (sessions.isLoggedIn()) {
                Intent intent = new Intent(getApplicationContext(), SerialNumber.class);
                startActivity(intent);
                finish();
            } else {
                Intent intent = new Intent(getApplicationContext(), WiFiConnections.class);
                startActivity(intent);

                finish();
            }
        } else {
            Intent intent = new Intent(getApplicationContext(), WiFiConnections.class);
            startActivity(intent);
        }

    }

    void getLocation() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 101);

        }

        try {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 5, this);
        } catch (SecurityException e) {
            e.printStackTrace();
        }

    }

    public void requestCameraAndStorage() {


        com.nabinbhandari.android.permissions.Permissions.check(this, new String[]{
                        Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                "We need Gallery access to upload images into server...", new
                        com.nabinbhandari.android.permissions.Permissions.Options()
                        .setRationaleDialogTitle("Info"),
                new PermissionHandler() {
                    @Override
                    public void onGranted() {
                        getLocation();
                        connection();
                        //finish();
                    }

                    @Override
                    public void onDenied(Context context, ArrayList<String> deniedPermissions) {
                        //    Toast.makeText(context, "Camera+Storage Denied:\n" + Arrays.toString(deniedPermissions.toArray()), Toast.LENGTH_SHORT).show();
                        Log.e("abcdefdg", Arrays.toString(deniedPermissions.toArray()));
                       /* android.permission.CALL_PHONE
                        android.permission.WRITE_EXTERNAL_STORAGE
*/
                        if (Arrays.toString(deniedPermissions.toArray()).equals("android.permission.CALL_PHONE")) {
                            Toast.makeText(context, "You Denied Camera ", Toast.LENGTH_LONG).show();

                        } else {
                            Toast.makeText(context, "You Denied External ", Toast.LENGTH_LONG).show();
                        }

                    }

                    @Override
                    public boolean onBlocked(Context context, ArrayList<String> blockedList) {
                        Toast.makeText(context, "Camera+Storage blocked:\n" + Arrays.toString(blockedList.toArray()),
                                Toast.LENGTH_SHORT).show();
                        return false;
                    }

                    @Override
                    public void onJustBlocked(Context context, ArrayList<String> justBlockedList,
                                              ArrayList<String> deniedPermissions) {
                        Toast.makeText(context, "Camera+Storage just blocked:\n" + Arrays.toString(deniedPermissions.toArray()),
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }


    public void congt() {

        task = new TimerTask() {

            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(getApplicationContext(), WiFiConnections.class);
                        startActivity(intent);
                        // connection();
                    }
                });
            }
        };
        Timer t = new Timer();
        t.schedule(task, 3000);
    }

    @Override
    public void onBackPressed() {

    }

    @Override
    protected void onPause() {


        //   LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onLocationChanged(Location location) {
        Double lat = location.getLatitude();
        Double lon = location.getLongitude();
        late = lat;
        lone = lon;

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }


}
/*
    private void displayFirebaseRegId() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        String regId = pref.getString("regId", null);

        Log.e("uiuiuiuiu", "Firebase reg id: " + regId);
        String androidDeviceId = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);

        if (!TextUtils.isEmpty(regId)) {

            Toast.makeText(this, "" + regId + androidDeviceId, Toast.LENGTH_SHORT).show();
            //txtRegId.setText("Firebase Reg Id: " + regId);
            Log.e("jvavbv", regId + "\n" + androidDeviceId);
        } else {
            Toast.makeText(this, "no token", Toast.LENGTH_SHORT).show();

        }
        //txtRegId.setText("Firebase Reg Id is not received yet!");

    }
*//*
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
@Override
public void onReceive(Context context, Intent intent) {
        // checking for type intent filter
        if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
        // gcm successfully registered
        // now subscribe to `global` topic to receive app wide notifications
        FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);
        } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
        // new push notification is received

        message = intent.getStringExtra("message");

        Toast.makeText(getApplicationContext(), "Push notification: " + message, Toast.LENGTH_LONG).show();

        // txtMessage.setText(message);
        }
        }

        };*/