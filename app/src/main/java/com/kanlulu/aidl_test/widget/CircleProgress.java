package com.kanlulu.aidl_test.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.kanlulu.aidl_test.R;

/**
 * Created by kanlulu
 * DATE: 2018/12/4 9:17
 * 前端带圆点的环形进度条
 */
public class CircleProgress extends View {
    private static final String TAG = "CircleProgress";

    private Paint mPaint;
    private int backCircleWith = 20;
    private int radius;
    private float currentAngle;
    private float currentPointAngle;
    private float sweepAngle;
    public Handler progressHandler;
    public Handler pointHandler;

    public CircleProgress(Context context) {
        super(context);
        init();
    }

    public CircleProgress(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CircleProgress(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        progressHandler = new Handler();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        radius = (getHeight() - backCircleWith) / 2;
        paintBackCircle(canvas);

        paintProgress(canvas);
        paintProgressPoint(canvas);
        if (currentAngle != sweepAngle || currentPointAngle != sweepAngle) invalidate();//重新绘制
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    /**
     * 背景圆环
     *
     * @param canvas
     */
    private void paintBackCircle(Canvas canvas) {
        mPaint.setColor(getResources().getColor(R.color.circleBackColor));
        mPaint.setStrokeWidth(backCircleWith);
        mPaint.setStyle(Paint.Style.STROKE);
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, radius, mPaint);
    }

    /**
     * 画进度圆环
     *
     * @param canvas
     */
    private void paintProgress(Canvas canvas) {
        mPaint.setColor(getResources().getColor(R.color.circleProgressColor));
        mPaint.setStrokeWidth(backCircleWith);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeCap(Paint.Cap.ROUND);

        RectF rectF = new RectF(backCircleWith / 2, backCircleWith / 2, 2 * radius + backCircleWith / 2, 2 * radius + backCircleWith / 2);
        currentAngle += 5;
        if (currentAngle >= sweepAngle) currentAngle = sweepAngle;
        canvas.drawArc(rectF, 180, currentAngle, false, mPaint);
    }

    /**
     * 画进度前端的圆点
     *
     * @param canvas
     */
    private void paintProgressPoint(Canvas canvas) {
        mPaint.setColor(getResources().getColor(R.color.circlePoint));
        mPaint.setStrokeWidth(backCircleWith - 8);
        mPaint.setStyle(Paint.Style.STROKE);
        if (sweepAngle > 359 || sweepAngle <= 0) return;

        currentPointAngle += 5;
        if (currentPointAngle >= sweepAngle) currentPointAngle = sweepAngle;
        double x = getWidth() / 2 - radius * Math.cos(currentPointAngle * Math.PI / 180);
        double y = getHeight() / 2 - radius * Math.sin(currentPointAngle * Math.PI / 180);
        canvas.drawPoint((float) x, (float) y, mPaint);
    }

    public void setProgress(float creditProgress) {
        if (pointHandler != null) pointHandler.removeCallbacksAndMessages(null);
        if (progressHandler != null) progressHandler.removeCallbacksAndMessages(null);
        currentPointAngle = 0;
        currentAngle = 0;
        sweepAngle = 360 * creditProgress / 100;
        invalidate();
    }
}
