package com.example.wave.receiver.Activities;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wave.receiver.Notification.Config;
import com.example.wave.receiver.Notification.NotificationUtils;
import com.example.wave.receiver.R;
import com.example.wave.receiver.SerialNumber;
import com.example.wave.receiver.Sessions.SessionsManager;
import com.example.wave.receiver.WiFiConnections;
import com.google.firebase.messaging.FirebaseMessaging;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

public class My_Files extends AppCompatActivity {

    private SharedPreferences prefs;
    private SessionsManager sessions;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    public static boolean check = true;
    TabLayout tabLayout;
    ViewPager viewPager;
    AlertDialog alertDialog;
    ImageView image1;
    RelativeLayout rl1;
    Dialog dialog;
    TextView textView1;
    Toolbar toolbar;
    DrawerLayout drawer;

    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Config.PUSH_NOTIFICATIONLOGOUT);
        intentFilter.addAction(Config.PUSH_NOTIFICATION_DATA);
        return intentFilter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my__files);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        dialog = new Dialog(My_Files.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        MyUploadsController.getInstance().fetchvideos(getApplicationContext());
        check = false;
        viewPager = (ViewPager) findViewById(R.id.viewpager1);
        createViewpager(viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tablayout1);
        tabLayout.setupWithViewPager(viewPager);
        sessions = new SessionsManager(getApplicationContext());
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, final Intent intent) {
                final String action = intent.getAction();

                // checking for type intent filter
                if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications
                    FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);


                } else if (Config.PUSH_NOTIFICATION_DATA.equals(action))
                {
                    //MyUploadsController.getInstance().fetchvideos(getApplicationContext());
                    String msgnot = intent.getStringExtra("message");
                    String message = msgnot;
                    Log.e("PUSH_NOTIFICATION_DATA", message);

                    if (dialog.isShowing())
                    {
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

                            MyUploadsController.getInstance().uploadImages.clear();
                            MyUploadsController.getInstance().uploadvideos.clear();
                            MyUploadsController.getInstance().fetchvideos(getApplicationContext());
                            // textView_ok.setTextColor(getResources().getColor(R.color.white));
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
                    sessions.setLogin(true);

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
                            dialog.dismiss();
                            Intent intent1 = new Intent(getApplicationContext(), SerialNumber.class);
                            startActivity(intent1);
                        }
                    });

                    dialog.show();
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            Intent intent = new Intent(getApplicationContext(), SerialNumber.class);
                            startActivity(intent);
                        }
                    }, 3000);
                }
            }

        };

        createTabIcons();

        initilize();
        initilizedrawer();
    }

    private void createViewpager(ViewPager viewPager) {
        ViewpagerAdapter adapter = new ViewpagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new MyAlbum(), "Tab 1");
        adapter.addFrag(new MyPlaylist(), "Tab 2");
        viewPager.setAdapter(adapter);
        viewPager.beginFakeDrag();
    }

    private void createTabIcons() {

        TextView tabone = (TextView) LayoutInflater.from(this)
                .inflate(R.layout.custom_tab, null);
        tabone.setText("My Gallery");

        tabone.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_image, 0, 0, 0);
        tabLayout.getTabAt(0).setCustomView(tabone);


        TextView tabtwo = (TextView) LayoutInflater.from(this)
                .inflate(R.layout.custom_tab, null);
        tabtwo.setText("My Videos");
        tabtwo.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_playlist, 0, 0, 0);
        tabLayout.getTabAt(1).setCustomView(tabtwo);
    }

    class ViewpagerAdapter extends FragmentPagerAdapter {

        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();


        public ViewpagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    private void initilize() {
    }

    private void initilizedrawer() {
    }

    @Override
    protected void onPause() {
        super.onPause();
        check = true;
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        check = true;
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

    // This method will be called when a MessageEvent is posted (in the UI thread for Toast)
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(Congratulations.MessageEvent event) {
        Log.e("filesevent", "" + event.message);
        String resultData = event.message.trim();
        if (resultData.equals("refreshMoney")) {
            Log.e("sidemenuMessageevent", "call" + event.message);
        }
    }

    @Override
    public void onBackPressed() {
    }
}
