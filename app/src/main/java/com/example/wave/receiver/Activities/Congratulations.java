package com.example.wave.receiver.Activities;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wave.receiver.R;
import com.example.wave.receiver.SerialNumber;
import com.example.wave.receiver.Server.FetchImages;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import es.dmoral.toasty.Toasty;

public class Congratulations extends AppCompatActivity {

    Button button_ok, button_settings;
    TextView textView;
    Handler m_handler;
    Runnable m_handlerTask;
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_congratulations);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        Toasty.error(getApplicationContext(), "Failed to register", Toast.LENGTH_SHORT, true);
        dialog = new Dialog(Congratulations.this);
        textView = findViewById(R.id.text_paired);
        textView.setText(SerialNumber.message);
        MyUploadsController.getInstance().fetchvideos(getApplicationContext());

       /* new Handler().postDelayed(new Runnable()
        {
            public void run() {

                Intent intent = new Intent(getApplicationContext(), My_Files.class);
                startActivity(intent);
            }
        }, 5000);*/
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
                        if (dialog.isShowing()) {
                            dialog.dismiss();
                        }
                        Myphotos.moving = false;
                        String url = MyUploadsController.getInstance().uploadedimages.get(i).getUrl();
                        Intent intent = new Intent(getApplicationContext(), Activity_display_myvideos.class);
                        intent.putExtra("newurl", url);
                        startActivity(intent);
                        //  Toast.makeText(getApplicationContext(), "video", Toast.LENGTH_SHORT).show();
                    } else if (type.equals("image")) {
                        Myphotos.moving = true;
                        if (dialog.isShowing()) {
                            dialog.dismiss();
                        }
                        Intent intent=new Intent(getApplicationContext(),Myphotos.class);
                        startActivity(intent);
                        //Toast.makeText(getApplicationContext(), "image", Toast.LENGTH_SHORT).show();
                    }
                }
            }

        } else if (resultData.equals("refreshemptyImages")) {
            alert("No Files Found", "Add some files to display");
        }
    }

    public void alert(String message, String msg) {
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

    @Override
    protected void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);

    }

    public static class MessageEvent
    {
        public final String message;

        public MessageEvent(String message)
        {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }

    @Override
    public void onBackPressed() {

    }
}
