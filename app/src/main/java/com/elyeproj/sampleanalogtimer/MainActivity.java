package com.elyeproj.sampleanalogtimer;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.elyeproj.analogtimerlibrary.AnalogTimerView;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    AnalogTimerView myTimer;
    private Timer myTimerCounter = null;
    int secondCount = 0;
    final Handler myHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myTimer = (AnalogTimerView)findViewById(R.id.myTimer);
    }

    @Override
    protected void onResume() {
        super.onResume();

        myTimerCounter = new Timer();
        myTimerCounter.schedule(new TimerTask() {
            @Override
            public void run() {UpdateGUI();}
        }, 0, 1000);

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (myTimerCounter != null) {
            myTimerCounter.cancel();
            myTimerCounter = null;
        }
    }

    private void UpdateGUI() {
        myHandler.post(myRunnable);
        secondCount ++;
    }

    final Runnable myRunnable = new Runnable() {
        public void run() {
            if (secondCount > 60) {
                secondCount = 0;
                myTimerCounter.cancel();
                myTimerCounter = null;
            }
            myTimer.setMovingDegree(secondCount * 6);
        }
    };
}
