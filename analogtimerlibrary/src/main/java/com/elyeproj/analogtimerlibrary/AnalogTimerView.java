package com.elyeproj.analogtimerlibrary;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Path.Direction;
import android.graphics.Shader;
import android.os.Build.VERSION_CODES;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;

import java.util.Timer;
import java.util.TimerTask;

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

public class AnalogTimerView extends View {

    private static final int ONE_SECOND = 1000;
    private static final int DEFAULT_MAX_TIME = 60;
    private static final int ONE_CYCLE_DEGREE = 360;
    private Paint gradientPaint;
    private Paint handPaint;
    private Paint linePaint;
    private Path miniPath = new Path();
    private Path minorPath = new Path();
    private Path majorPath = new Path();
    private Path handPath = new Path();
    private Bitmap handBitmap;
    private Bitmap facadeBitmap;
    private float radius;
    private int movingDegree = 0;
    private Timer timerCounter = null;
    final Handler handler = new Handler();
    private int timerCount = 0;
    private int maxTime = DEFAULT_MAX_TIME;

    public AnalogTimerView(Context context) {
        super(context);
        init();
    }

    public AnalogTimerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AnalogTimerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(VERSION_CODES.LOLLIPOP)
    public AnalogTimerView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        linePaint.setColor(Color.BLACK);
        linePaint.setStrokeWidth(3);
        linePaint.setStyle(Style.STROKE);
        handPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
        gradientPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
    }

    public void startTimer() {
        if (timerCounter == null) {
            timerCounter = new Timer();
            timerCounter.schedule(new TimerTask() {
                @Override
                public void run() {UpdateGUI();}
            }, 0, ONE_SECOND);
        }
    }

    public void stopTimer() {
        if (timerCounter != null) {
            timerCounter.cancel();
            timerCounter = null;
        }
    }

    public void resetTimer() {
        setTime(0);
    }

    public int getTime() {
        return timerCount;
    }

    public void setTime(int timerCount) {
        this.timerCount = timerCount;
        updateTimerUI();
    }

    public boolean isRunning() {
        return timerCounter != null;
    }

    public void setMaxTime(int maxTime) {
        this.maxTime = maxTime;
    }

    private void UpdateGUI() {
        handler.post(myRunnable);
        timerCount++;
    }

    final Runnable myRunnable = new Runnable() {
        public void run() {
            updateTimerUI();
        }
    };

    private void updateTimerUI() {
        if (timerCount >= maxTime) {
            timerCount = 0;
        }
        setMovingDegree(timerCount * ONE_CYCLE_DEGREE/maxTime);
    }

    public void setMovingDegree(int moving) {
        movingDegree = moving;
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int minw = getPaddingLeft() + getPaddingRight() + getSuggestedMinimumWidth();
        int minh = getPaddingTop() + getPaddingBottom() + getSuggestedMinimumHeight();

        int w = resolveSizeAndState(minw, widthMeasureSpec, 1);
        int h = resolveSizeAndState(minh, heightMeasureSpec, 1);

        if (w == 0) w = h;
        if (h == 0) h = w;

        setMeasuredDimension(w, h);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        if (h == 0 || w == 0) return;

        // Account for padding
        float xpad = (float) (getPaddingLeft() + getPaddingRight());
        float ypad = (float) (getPaddingTop() + getPaddingBottom());

        float ww = (float) w - xpad;
        float hh = (float) h - ypad;
        handPath.reset();
        majorPath.reset();
        miniPath.reset();
        minorPath.reset();
        radius = Math.min(ww, hh) / 2;
        handPaint
                .setShader(new LinearGradient(0, 0, 0, getHeight(), Color.BLACK, Color.WHITE,
                        Shader.TileMode.CLAMP));
        gradientPaint
                .setShader(new LinearGradient(0, 0, getWidth(), getHeight(), Color.BLACK, Color
                        .WHITE, Shader.TileMode.CLAMP));

        drawClockFacade(w, h);
        drawClockHand(w, h);
    }

    private void drawClockHand(int w, int h) {
        handBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(handBitmap);

        int middleX = canvas.getWidth() / 2;
        int middleY = canvas.getHeight() / 2;
        handPath.addCircle(middleX, middleY, radius * 1 / 30, Direction.CCW);
        handPath.moveTo(middleX, middleY);
        handPath.lineTo(middleX - radius * 1 / 30, middleY - radius * 9 / 10 + radius * 2 / 10);
        handPath.lineTo(middleX, middleY - radius * 9 / 10 + radius * 1 / 10);
        handPath.lineTo(middleX + radius * 1 / 30, middleY - radius * 9 / 10 + radius * 2 / 10);
        handPath.lineTo(middleX, middleY);
        canvas.drawPath(handPath, handPaint);
    }

    private void drawClockFacade(int w, int h) {
        facadeBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(facadeBitmap);
        int middleX = canvas.getWidth() / 2;
        int middleY = canvas.getHeight() / 2;

        canvas.drawCircle(middleX, middleY, radius, gradientPaint);
        gradientPaint
                .setShader(new LinearGradient(0, 0, getWidth(), getHeight(), Color.WHITE, Color
                        .BLACK, Shader.TileMode.CLAMP));
        canvas.drawCircle(middleX, middleY, radius * 19 / 20, gradientPaint);

        gradientPaint.setColor(0xFFEEEEEE);
        gradientPaint.setShader(null);
        canvas.drawCircle(middleX, middleY, radius * 9 / 10, gradientPaint);

        canvas.save();
        for (int i = 0; i < 60; i++) {
            drawMainLine(canvas, middleX, middleY, (int) (radius * 1 / 30), miniPath);
            if (i % 15 == 0) {
                drawMainLine(canvas, middleX, middleY, (int) (radius * 1 / 15), majorPath);
            } else if (i % 5 == 0) {
                drawMainLine(canvas, middleX, middleY, (int) (radius * 1 / 20), minorPath);
            }
            canvas.rotate(6, middleX, middleY);
        }
        canvas.restore();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int middleX = canvas.getWidth() / 2;
        int middleY = canvas.getHeight() / 2;

        if (facadeBitmap != null && handBitmap != null) {
            canvas.drawBitmap(facadeBitmap, 0, 0, null);
            canvas.save();
            canvas.rotate(movingDegree, middleX, middleY);
            canvas.drawBitmap(handBitmap, 0, 0, null);
            canvas.restore();
        }

    }

    private void drawMainLine(Canvas canvas, int middleX, int middleY, int length, Path path) {
        path.moveTo(middleX, middleY - radius * 9 / 10);
        path.lineTo(middleX, middleY - radius * 9 / 10 + length);
        canvas.drawPath(path, linePaint);
    }

}

