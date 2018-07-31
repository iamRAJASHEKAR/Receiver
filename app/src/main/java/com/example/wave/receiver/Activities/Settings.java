package com.example.wave.receiver.Activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.wave.receiver.Fragments.Information;
import com.example.wave.receiver.Fragments.Networkfrag;
import com.example.wave.receiver.Fragments.Settingsinfo;
import com.example.wave.receiver.Fragments.Softwareupdate;
import com.example.wave.receiver.Notification.Config;
import com.example.wave.receiver.Notification.NotificationUtils;
import com.example.wave.receiver.R;
import com.example.wave.receiver.SerialNumber;
import com.example.wave.receiver.WiFiConnections;
import com.google.firebase.messaging.FirebaseMessaging;

public class Settings extends AppCompatActivity {
    Button button, button2, button3, button4;
    RelativeLayout relativeLayout;
    Dialog dialog;
    Boolean btn_one = false, btn_two = false, btn_three = false, btn_four = false;
    FrameLayout frameLayout;
    TextView textView;
    private BroadcastReceiver mRegistrationBroadcastReceiver;

    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Config.PUSH_NOTIFICATIONLOGOUT);
        intentFilter.addAction(Config.PUSH_NOTIFICATION_DATA);
        return intentFilter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        dialog = new Dialog(Settings.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        button = (Button) findViewById(R.id.btn_settings);
        button2 = (Button) findViewById(R.id.btn_network);
        button3 = (Button) findViewById(R.id.btn_sofup);
        button4 = (Button) findViewById(R.id.btn_information);
        frameLayout = (FrameLayout) findViewById(R.id.frame);
        //textView=(TextView) findViewById(R.id.dinchik);
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        Settingsinfo ol = new Settingsinfo();
        fragmentTransaction.replace(R.id.frame, ol);
        fragmentTransaction.commit();

        oneactive();

        button.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                btn_one = true;
                button.setBackgroundColor(getResources().getColor(R.color.colorBrown));
                button.setTextColor(getResources().getColor(R.color.white));
                android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
                android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                Settingsinfo ol = new Settingsinfo();
                fragmentTransaction.replace(R.id.frame, ol);
                fragmentTransaction.commit();

                if (btn_two) {
                    button2.setTextColor(getResources().getColor(R.color.black));
                    button2.setBackgroundColor(Color.TRANSPARENT);
                    btn_two = false;

                } else if (btn_three) {
                    button3.setTextColor(getResources().getColor(R.color.black));
                    button3.setBackgroundColor(Color.TRANSPARENT);
                    btn_three = false;
                } else if (btn_four) {
                    button4.setTextColor(getResources().getColor(R.color.black));
                    button4.setBackgroundColor(Color.TRANSPARENT);
                    btn_four = false;
                }


            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_two = true;
                button2.setBackgroundColor(getResources().getColor(R.color.colorBrown));
                button2.setTextColor(getResources().getColor(R.color.white));

                android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
                android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                Networkfrag ol = new Networkfrag();
                fragmentTransaction.replace(R.id.frame, ol);
                fragmentTransaction.commit();


       /*         Intent intent = new Intent(Settings.this, WiFiConnections.class);
                startActivity(intent);
       */

       /* android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
                android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                Softwareupdate ol = new Softwareupdate();
                fragmentTransaction.replace(R.id.frame, ol);
                fragmentTransaction.commit();
*/
                if (btn_one) {
                    button.setTextColor(getResources().getColor(R.color.black));
                    button.setBackgroundColor(Color.TRANSPARENT);
                    btn_one = false;
                } else if (btn_three) {
                    button3.setTextColor(getResources().getColor(R.color.black));
                    button3.setBackgroundColor(Color.TRANSPARENT);
                    btn_three = false;

                } else if (btn_four) {
                    button4.setTextColor(getResources().getColor(R.color.black));
                    button4.setBackgroundColor(Color.TRANSPARENT);
                    btn_four = false;
                }
            }
        });


        button3.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                btn_three = true;
                button3.setBackgroundColor(getResources().getColor(R.color.colorBrown));
                button3.setTextColor(getResources().getColor(R.color.white));

                android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
                android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                Softwareupdate ol = new Softwareupdate();
                fragmentTransaction.replace(R.id.frame, ol);
                fragmentTransaction.commit();

                if (btn_one) {
                    button.setTextColor(getResources().getColor(R.color.black));
                    button.setBackgroundColor(Color.TRANSPARENT);
                    btn_one = false;
                } else if (btn_two) {
                    button2.setTextColor(getResources().getColor(R.color.black));
                    button2.setBackgroundColor(Color.TRANSPARENT);
                    btn_two = false;


                } else if (btn_four) {
                    button4.setTextColor(getResources().getColor(R.color.black));
                    button4.setBackgroundColor(Color.TRANSPARENT);
                    btn_four = false;
                }
                //      textView.setText("Dinchik");
                //relativeLayout.setBackgroundColor(Color.TRANSPARENT);

            }
        });

        button4.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                btn_four = true;
                button4.setBackgroundColor(getResources().getColor(R.color.colorBrown));
                button4.setTextColor(getResources().getColor(R.color.white));

                android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
                android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                Information ol = new Information();
                fragmentTransaction.replace(R.id.frame, ol);
                fragmentTransaction.commit();

                if (btn_one) {
                    button.setTextColor(getResources().getColor(R.color.black));
                    button.setBackgroundColor(Color.TRANSPARENT);
                    btn_one = false;
                } else if (btn_two) {
                    button2.setTextColor(getResources().getColor(R.color.black));
                    button2.setBackgroundColor(Color.TRANSPARENT);
                    btn_two = false;


                } else if (btn_three) {
                    button3.setTextColor(getResources().getColor(R.color.black));
                    button3.setBackgroundColor(Color.TRANSPARENT);
                    btn_three = false;
                }
                //      textView.setText("Dinchik");
                //relativeLayout.setBackgroundColor(Color.TRANSPARENT);

            }
        });


        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, final Intent intent) {
                final String action = intent.getAction();

                // checking for type intent filter
                if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications
                    FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);


                } else if (Config.PUSH_NOTIFICATION_DATA.equals(action)) {
                    MyUploadsController.getInstance().fetchvideos(getApplicationContext());
                    String msgnot = intent.getStringExtra("message");
                    String message = msgnot;
                    Log.e("PUSH_NOTIFICATION_DATA", message);

                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    dialog.setContentView(R.layout.unpair_alert);
                    TextView textView_title = dialog.findViewById(R.id.unpair_alerttitle);
                    TextView textView_msg = dialog.findViewById(R.id.unpair_alerttext);
                    textView_title.setText(message);
                    textView_msg.setText(message + "Successfully");
                    final TextView textView_ok = dialog.findViewById(R.id.alertbutton_cancel);
                    final RelativeLayout ok = dialog.findViewById(R.id.relate_one);
                    ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        /*    MyUploadsController.getInstance().uploadImages.clear();
                            MyUploadsController.getInstance().uploadvideos.clear();
                            MyUploadsController.getInstance().fetchvideos(getApplicationContext());
                        */    // textView_ok.setTextColor(getResources().getColor(R.color.white));
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            if (dialog.isShowing()) {
                                dialog.dismiss();
                            }
                        }
                    }, 3000);
                } else if (Config.PUSH_NOTIFICATIONLOGOUT.equals(action)) {
                    // new push notification is received

                    String msgnot = intent.getStringExtra("message");
                    String message = msgnot;
                    Log.e("myfilesfiree", message);
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    dialog.setContentView(R.layout.unpair_alert);
                    dialog.setCancelable(false);
                    TextView textView = dialog.findViewById(R.id.unpair_alerttitle);
                    TextView textView_msg = dialog.findViewById(R.id.unpair_alerttext);
                    textView.setText(message);
                    textView_msg.setText(message + " " + "Successfully");
                    final TextView textView_ok = dialog.findViewById(R.id.alertbutton_cancel);
                    final RelativeLayout ok = dialog.findViewById(R.id.relate_one);
                    ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                          /*  dialog.dismiss();
                            Intent intent1 = new Intent(getApplicationContext(), SerialNumber.class);
                            startActivity(intent1);*/
                        }
                    });

                    dialog.show();
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            if (dialog.isShowing()) {

                                dialog.dismiss();
                            }
                            Intent intent = new Intent(getApplicationContext(), SerialNumber.class);
                            startActivity(intent);
                        }
                    }, 3000);
                }
            }

        };


    }

    public void oneactive() {
        btn_one = true;
        button.setBackgroundColor(getResources().getColor(R.color.colorBrown));
        button.setTextColor(getResources().getColor(R.color.white));

    }

    @Override
    protected void onResume() {
        super.onResume();
        //        Toast.makeText(this, "hmmm", Toast.LENGTH_SHORT).show();
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.REGISTRATION_COMPLETE));
        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives

        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                makeGattUpdateIntentFilter());

        // clear the notification area when the app is opened
        NotificationUtils.clearNotifications(getApplicationContext());

    }

    @Override
    protected void onPause() {

        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);

    }
}

