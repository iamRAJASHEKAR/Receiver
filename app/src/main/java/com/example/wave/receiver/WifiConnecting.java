package com.example.wave.receiver;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.wave.receiver.Activities.Congratulations;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Mahi on 3/7/2018.
 */

public class WifiConnecting extends AppCompatActivity {
    TimerTask task;

    ImageView progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wificonnecting);
        progressBar = (ImageView) findViewById(R.id.wifiConnector);
        ((Animatable) progressBar.getDrawable()).start();
        //  congt();
    }

    public void congt() {

        task = new TimerTask() {

            @Override
            public void run() {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(getApplicationContext(), Congratulations.class);
                        startActivity(intent);
                        finish();
                        //    Toast.makeText(, "OTA not implemented", Toast.LENGTH_SHORT).show();
                        // Toasty.error(getApplicationContext(), "OTA not implemented", Toast.LENGTH_SHORT, true).show();

                    }
                });
            }
        };
        Timer t = new Timer();
        t.schedule(task, 3000);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        task.cancel();
    }
}
