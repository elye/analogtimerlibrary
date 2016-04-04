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
import android.util.AttributeSet;
import android.view.View;

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

    public void setMovingDegree(int moving) {
        movingDegree = moving;
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int minw = getPaddingLeft() + getPaddingRight() + getSuggestedMinimumWidth();
        int w = resolveSizeAndState(minw, widthMeasureSpec, 1);
        int h = resolveSizeAndState(MeasureSpec.getSize(w), heightMeasureSpec, 0);
        setMeasuredDimension(w, h);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

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

        canvas.drawBitmap(facadeBitmap, 0, 0, null);
        canvas.save();
        canvas.rotate(movingDegree, middleX, middleY);
        canvas.drawBitmap(handBitmap, 0, 0, null);
        canvas.restore();

    }

    private void drawMainLine(Canvas canvas, int middleX, int middleY, int length, Path path) {
        path.moveTo(middleX, middleY - radius * 9 / 10);
        path.lineTo(middleX, middleY - radius * 9 / 10 + length);
        canvas.drawPath(path, linePaint);
    }
}

