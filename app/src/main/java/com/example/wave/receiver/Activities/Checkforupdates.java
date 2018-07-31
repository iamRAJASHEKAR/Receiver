package com.example.wave.receiver.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.wave.receiver.R;

import java.util.Timer;
import java.util.TimerTask;

public class Checkforupdates extends AppCompatActivity {
    TimerTask task, timerTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkforupdates);
        toast();
        timer();
    }

    public void timer() {
        timerTask = new TimerTask() {

            @Override
            public void run() {
                //     Toasty.error(getApplicationContext(), "OTA not implemented", Toast.LENGTH_LONG, true).show();
               /* Intent intent = new Intent(getApplicationContext(), Settings.class);
                startActivity(intent);
               */
                finishscreen();
            }
        };
        Timer t = new Timer();
        t.schedule(timerTask, 5000);
    }

    private void finishscreen() {


        this.finish();
    }

    public void toast() {
        task = new TimerTask() {

            @Override
            public void run() {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(Checkforupdates.this, "OTA not implemented", Toast.LENGTH_SHORT).show();
                        // Toasty.error(getApplicationContext(), "OTA not implemented", Toast.LENGTH_SHORT, true).show();

                    }
                });
            }
        };
        Timer t = new Timer();
        t.schedule(task, 2000);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        task.cancel();
        timerTask.cancel();
    }
}

