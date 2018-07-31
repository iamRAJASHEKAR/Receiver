package com.example.wave.receiver.Activities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.wave.receiver.Notification.Config;
import com.example.wave.receiver.Notification.NotificationUtils;
import com.example.wave.receiver.R;
import com.example.wave.receiver.SerialNumber;
import com.example.wave.receiver.Server.FetchImages;
import com.example.wave.receiver.Sessions.SessionsManager;
import com.google.firebase.messaging.FirebaseMessaging;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import es.dmoral.toasty.Toasty;

/**
 * Created by Mahi on 4/4/2018.
 */

public class Activity_display_myvideos extends AppCompatActivity {
    VideoView videoView;
    ProgressBar progressBar;
    private SessionsManager sessions;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    Dialog dialog;
    Dialog alert;
    ProgressDialog progressDialog;
    //private static ProgressDialog progressDialog;

    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Config.PUSH_NOTIFICATIONLOGOUT);
        intentFilter.addAction(Config.PUSH_NOTIFICATION_DATA);
        return intentFilter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_videos);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        dialog = new Dialog(Activity_display_myvideos.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        sessions = new SessionsManager(getApplicationContext());
        String videourl = getIntent().getStringExtra("newurl");
        progressDialog = new ProgressDialog(Activity_display_myvideos.this);
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
                    Toasty.success(getApplicationContext(), message, Toast.LENGTH_SHORT, true).show();
                    /*String msgnot = intent.getStringExtra("message");
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

                            dialog.dismiss();
                        }
                    });
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            if (dialog.isShowing()) {
                                dialog.dismiss();
                            }
                        }
                    }, 3000);
                    dialog.show();*/
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
                          /*  dialog.dismiss();
                            Intent intent1 = new Intent(getApplicationContext(), SerialNumber.class);
                            startActivity(intent1);*/
                        }
                    });

                    dialog.show();
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            dialog.dismiss();
                            Intent intent = new Intent(getApplicationContext(), SerialNumber.class);
                            startActivity(intent);
                        }
                    }, 3000);
                }
            }

        };
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        progressBar.setVisibility(View.VISIBLE);
        videoView = (VideoView) findViewById(R.id.grid_item_videos);
        init(videourl);
    }

    public void init(String url) {
        if (MyUploadsController.getInstance().uploadvideos.size() > 0) {
            Uri videoUri = Uri.parse(url);/*
            progressDialog.setMessage("Loading Video");
            progressDialog.setCancelable(false);
            progressDialog.show();*/
            videoView.setVideoURI(videoUri);
            MediaController mediaController = new MediaController(Activity_display_myvideos.this);
            mediaController.setPadding(0, 0, 0, 55);
            videoView.start();
            videoView.setMediaController(mediaController);
            videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    //TODO Auto-generated method stub
                    progressDialog.dismiss();
                    progressBar.setVisibility(View.GONE);
                    mp.start();
                    mp.setLooping(true);
                    mp.setOnInfoListener(new MediaPlayer.OnInfoListener() {
                        @Override
                        public boolean onInfo(MediaPlayer mp, int what, int extra) {
                            if (what == MediaPlayer.MEDIA_INFO_BUFFERING_START)
                                progressBar.setVisibility(View.VISIBLE);
                            if (what == MediaPlayer.MEDIA_INFO_BUFFERING_END)
                                progressBar.setVisibility(View.GONE);
                            return false;
                        }
                    });

                    mp.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
                        @Override
                        public void onVideoSizeChanged(MediaPlayer mp, int arg1,
                                                       int arg2) {
                            // TODO Auto-generated method stub
                            mp.start();
                            progressBar.setVisibility(View.INVISIBLE);
                            // progressBar.setVisibility( arg2 == 100 ? View.VISIBLE : View.VISIBLE );

                          /*  if (arg2 == mp.getDuration()){
                                progressBar.setVisibility(View.VISIBLE);
                            } else {
                                progressBar.setVisibility(View.INVISIBLE);
                            }*/

                        }
                    });

                    mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        public void onCompletion(MediaPlayer mp) {
                            finish();
                            // Do whatever u need to do here
                            /*Intent intent = new Intent(getApplicationContext(),MyPlaylist.class);
                            startActivity(intent);*/

                        }
                    });
                    /*@Override
                    public void onBufferingUpdate(MediaPlayer mp, int percent){
                    progressBar.setVisibility( percent == 100 ? View.INVISIBLE : View.INVISIBLE );
                }*/
                }

            });

          /*  @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                if (playbackState == ExoPlayer.STATE_BUFFERING){
                    progressBar.setVisibility(View.VISIBLE);
                } else {
                    progressBar.setVisibility(View.INVISIBLE);
                }
            }
*/
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.REGISTRATION_COMPLETE));
        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives

        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                makeGattUpdateIntentFilter());

        // clear the notification area when the app is opened
        NotificationUtils.clearNotifications(getApplicationContext());
        EventBus.getDefault().register(this);

    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        EventBus.getDefault().unregister(this);

    }

    public ArrayList<FetchImages> sortFeedbackBasedOnTime(ArrayList<FetchImages> feedbacks) {

        Collections.sort(feedbacks, new Comparator<FetchImages>() {
            @Override
            public int compare(FetchImages s1, FetchImages s2) {
                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                int compareResult = 0;
                try {
                    Date arg0Date = format.parse(s1.getTimestamp());
                    Date arg1Date = format.parse(s2.getTimestamp());
                    compareResult = arg0Date.compareTo(arg1Date);
                } catch (ParseException e) {
                    e.printStackTrace();
                    compareResult = s1.getTimestamp().compareTo(s2.getTimestamp());
                }
                return compareResult;
            }
        });
        // return feedbacks;
        Collections.reverse(feedbacks);
        return feedbacks;

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(Congratulations.MessageEvent event) {
        Log.e("checkmsgman", "" + event.message);
        String resultData = event.message.trim();
        if (resultData.equals("refreshImages")) {
            for (int i = 0; i < MyUploadsController.getInstance().uploadedimages.size(); i++) {
                if (i == 0) {
                    Log.e("kjdsnvkjvj", String.valueOf(sortFeedbackBasedOnTime(MyUploadsController.getInstance().uploadedimages).size()));
                    String type = MyUploadsController.getInstance().uploadedimages.get(i).getFileType();
                    if (type.equals("video")) {
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        init(MyUploadsController.getInstance().uploadedimages.get(i).getUrl());
                        //               Toast.makeText(getApplicationContext(), "video", Toast.LENGTH_SHORT).show();
                    } else {
                        Myphotos.moving = true;
                        Intent intent = new Intent(getApplicationContext(), Myphotos.class);
                        startActivity(intent);
                        //             Toast.makeText(getApplicationContext(), "image", Toast.LENGTH_SHORT).show();
                    }
                }
            }

        } else if (resultData.equals("refreshemptyImages")) {

            Intent intent = new Intent(getApplicationContext(), Congratulations.class);
            startActivity(intent);/*
            alert("No Files Found", "Add some files to display");*/
        }
    }

    public void alert(String message, String msg) {
        dialog = new Dialog(Activity_display_myvideos.this);
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
        ok.setVisibility(View.INVISIBLE);
        dialog.show();
    }

    @Override
    public void onBackPressed() {

    }
}