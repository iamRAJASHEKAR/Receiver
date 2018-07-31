package com.example.wave.receiver.Activities;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
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

import java.io.IOException;
import java.lang.annotation.Target;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import es.dmoral.toasty.Toasty;

public class Myphotos extends AppCompatActivity {
    Timer timer;
    private ImageView imageView;
    private ViewPagerCustomDuration mPager;
    private int movingPosition;
    Dialog dialog;
    SlidingImageAdapter slidingImageAdapter;
    public static String chal;
    public static boolean moving = false;
    private SessionsManager sessions;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    Boolean isLoaded = false;

    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Config.PUSH_NOTIFICATIONLOGOUT);
        intentFilter.addAction(Config.PUSH_NOTIFICATION_DATA);
        return intentFilter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myphotos);
        mPager = (ViewPagerCustomDuration) findViewById(R.id.pager);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        dialog = new Dialog(Myphotos.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
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


                } else if (Config.PUSH_NOTIFICATION_DATA.equals(action)) {
                    //  MyUploadsController.getInstance().fetchvideos(getApplicationContext());
                    final ArrayList<FetchImages> arrayList = MyUploadsController.getInstance().uploadImages;
                    //MyUploadsController.getInstance().uploadvideos.clear();
                    Log.e("PUSH_NOTIFICATIONARRAY", String.valueOf(MyUploadsController.getInstance().uploadImages.size()));
                    if (arrayList.size() > 0) {
                    }
                    String msgnot = intent.getStringExtra("message");
                    String message = msgnot;

                    Toasty.success(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                    Log.e("PUSH_NOTIFICATION_DATA", message);

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
                        /*    dialog.dismiss();
                            Intent intent1 = new Intent(getApplicationContext(), SerialNumber.class);
                            startActivity(intent1);
                        */
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
        if (moving == true) {

            init();
        }
    }


    public void init() {
        Log.e("albumSize", "" + MyUploadsController.getInstance().uploadImages.size());
        /* for(int i=0;i<MyUploadsController.getInstance().uploadImages.size();i++)
            MyUploadsController.getInstance().uploadImages.add(MyUploadsController.getInstance().uploadImages.get(i));
*/
        // final ArrayList<FetchImages> arrayList = MyUploadsController.getInstance().uploadImages;

        if (MyUploadsController.getInstance().uploadImages.size() > 0) {

            final int totalImagesCount = MyUploadsController.getInstance().uploadImages.size();
            // Intent intent = getIntent();
//            int selectedPos = intent.getExtras().getInt("pos");
            movingPosition = 0;
            slidingImageAdapter = new SlidingImageAdapter(getApplicationContext(), MyUploadsController.getInstance().uploadImages);
            mPager.setAdapter(slidingImageAdapter);
            slidingImageAdapter.notifyDataSetChanged();
//            Log.e("checkarraysize", String.valueOf(arrayList.size()));
            mPager.setCurrentItem(movingPosition, true);
            mPager.setScrollDurationFactor(15);
            final long period = 5000;
            final long delayPeriod = 5000;

            if (timer != null) {
                timer.cancel();
                timer.purge();
                timer = null;
            }
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    Log.e("inrunmethosize", String.valueOf(MyUploadsController.getInstance().uploadImages.size()));

                    int positionForImageLoadCheck = 0;
                    if (MyUploadsController.getInstance().uploadImages != null && MyUploadsController.getInstance().uploadImages.size() > 0) {
                        positionForImageLoadCheck = movingPosition % MyUploadsController.getInstance().uploadImages.size();
                    }
                    Log.e("positionFor", "" + positionForImageLoadCheck);
                    if (MyUploadsController.getInstance().uploadImages.size() > 0 && MyUploadsController.getInstance().uploadImages.get(positionForImageLoadCheck).getLoaded() != null &&
                            MyUploadsController.getInstance().uploadImages.get(positionForImageLoadCheck).getLoaded()) {
                        if (movingPosition == 5000) {
                            movingPosition = 0;
                        } else {
                            movingPosition++;
                        }
                        runOnUiThread(new Runnable()
                        {
                            @Override
                            public void run() {
                                if (movingPosition == 0) {
                                    slidingImageAdapter.notifyDataSetChanged();
                                    mPager.setCurrentItem(movingPosition, false);
                                    isLoaded = false;
                                } else {

                                    slidingImageAdapter.notifyDataSetChanged();
                                    mPager.setCurrentItem(movingPosition, true);
                                    isLoaded = false;
                                }
                            }
                        });
                    }

                    // do your task here
                }
            }, delayPeriod, period);
            // Auto start of viewpager
        }
    }

    @Override
    protected void onStop() {

        //.stopAutoCycle();

        super.onStop();

    }


    public class SlidingImageAdapter extends PagerAdapter {

        private int Loops_COUNT = 5000;
        private LayoutInflater inflater;
        private Context context;


        public SlidingImageAdapter(Context context, ArrayList<FetchImages> urlList) {
            this.context = context;
            inflater = LayoutInflater.from(context);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getCount() {
            if (MyUploadsController.getInstance().uploadImages != null && MyUploadsController.getInstance().uploadImages.size() > 0) {
                if (MyUploadsController.getInstance().uploadImages.size() == 1) {
                    return 1;
                }
                return Loops_COUNT;
                // return urlList.size();
            } else {
                return 0;
            }
        }

        @Override
        public Object instantiateItem(ViewGroup view, int position) {
            View imageLayout = inflater.inflate(R.layout.slidingimages_layout, view, false);

            if (MyUploadsController.getInstance().uploadImages != null && MyUploadsController.getInstance().uploadImages.size() > 0) {
                position = position % MyUploadsController.getInstance().uploadImages.size();
            } else {
                return null;
            }
            final FetchImages image = MyUploadsController.getInstance().uploadImages.get(position);

            assert imageLayout != null;
            final ImageView imageView = (ImageView) imageLayout.findViewById(R.id.image_slidelayout);
            imageView.setAdjustViewBounds(true);
            // progressBar.setVisibility(View.VISIBLE);
            final int finalPosition = position;
            Glide.with(getApplicationContext())
                    .load(image.getUrl().toString())
                    .placeholder(R.drawable.animation)
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, com.bumptech.glide.request.target.Target<GlideDrawable> target, boolean isFirstResource) {
                            image.setLoaded(false);
                            MyUploadsController.getInstance().uploadImages.set(finalPosition, image);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, com.bumptech.glide.request.target.Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            isLoaded = true;
                            image.setLoaded(true);
                            MyUploadsController.getInstance().uploadImages.set(finalPosition, image);
                            return false;

                        }

                    })
                    .into(imageView);
            //  Glide.with(context).load(image.getUrl().toString()).into(imageView)
            /*
            Glide.with(context).load(image.getUrl().toString()).into(imageView).onResourceReady(Drawable resource,Object model, Target<Drawable>
            );*/
            view.addView(imageLayout, 0);
            return imageLayout;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view.equals(object);
        }

        @Override
        public void restoreState(Parcelable state, ClassLoader loader) {
        }

        @Override
        public Parcelable saveState() {
            return null;
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

    //for sorting the arraylist
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
                        moving = false;
                        Intent intent = new Intent(getApplicationContext(), Activity_display_myvideos.class);
                        intent.putExtra("newurl", MyUploadsController.getInstance().uploadedimages.get(i).getUrl());
                        startActivity(intent);
                    } else {
                        Log.e("filesevent1", "call" + event.message);
                        final ArrayList<FetchImages> arrayList = MyUploadsController.getInstance().uploadImages;
                        Log.e("IAMTRYING", " " + arrayList.size());
                        if (arrayList.size() > 0) {
                            moving = true;
                            init();
                        }
                    }
                }
            }
        } else if (resultData.equals("refreshemptyImages")) {
            Log.e("checkempty", "" + event.message);
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    Toasty.error(getApplicationContext(), "No files to Show", Toast.LENGTH_SHORT, true).show();
                    Intent intent = new Intent(getApplicationContext(), Congratulations.class);
                    startActivity(intent);
                    finish();
                }
            }, 5000);
        }
    }

    @Override
    public void onBackPressed() {

    }
}
