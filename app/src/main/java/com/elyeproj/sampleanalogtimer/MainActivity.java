package com.elyeproj.sampleanalogtimer;

/*
 * Copyright 2016 Elye Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.elyeproj.analogtimerlibrary.AnalogTimerView;

public class MainActivity extends AppCompatActivity implements OnClickListener {

    public static final String SAVE_STATE_CURRENT_TIME = "SaveStateCurrentTime";
    public static final String SAVE_STATE_IS_RUNNING = "SaveStateIsRunning";
    AnalogTimerView myTimer;
    Button startButton;
    Button stopButton;
    Button resetButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        myTimer = (AnalogTimerView) findViewById(R.id.myTimer);
        startButton = (Button) findViewById(R.id.start_button);
        stopButton = (Button) findViewById(R.id.stop_button);
        resetButton = (Button) findViewById(R.id.reset_button);

        startButton.setOnClickListener(this);
        stopButton.setOnClickListener(this);
        resetButton.setOnClickListener(this);
    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        myTimer.setTime(savedInstanceState.getInt(SAVE_STATE_CURRENT_TIME, 0));
        if (savedInstanceState.getBoolean(SAVE_STATE_IS_RUNNING, false)) {
            myTimer.startTimer();
        }
    }
    
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SAVE_STATE_CURRENT_TIME, myTimer.getTime());
        outState.putBoolean(SAVE_STATE_IS_RUNNING, myTimer.isRunning());
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
        case R.id.start_button:
            myTimer.startTimer();
            break;
        case R.id.stop_button:
            myTimer.stopTimer();
            break;
        case R.id.reset_button:
            myTimer.resetTimer();
            break;
        }
    }
}