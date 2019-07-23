package com.weikk.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class LampSeekBar extends View
{
    private static final String TAG = LampSeekBar.class.getSimpleName();
    private int[] colorArray;
    private int endColor = -1;
    private LinearGradient linearGradient;
    private float mRadius;
    private float maxCount;
    private int middleColor = -7829368;
    protected OnStateChangeListener onStateChangeListener;
    private Paint paint;
    private float progress;
    private float sBottom;
    private float sHeight;
    private float sLeft;
    private float sRight;
    private float sTop;
    private float sWidth;
    private int startColor = -16777216;
    private int thumbBorderColor = -1;
    private int thumbColor = -16777216;
    private float x;
    private float y;

    public LampSeekBar(Context paramContext)
    {
        this(paramContext, null);
    }

    public LampSeekBar(Context paramContext, AttributeSet paramAttributeSet)
    {
        super(paramContext, paramAttributeSet);
        int[] arrayOfInt = new int[3];
        arrayOfInt[0] = this.startColor;
        arrayOfInt[1] = this.middleColor;
        arrayOfInt[2] = this.endColor;
        this.colorArray = arrayOfInt;
        this.maxCount = 255.0F;
        this.paint = new Paint();
    }

    private void drawBackground(Canvas paramCanvas)
    {
        RectF localRectF = new RectF(this.sLeft, this.sTop, this.sRight, this.sBottom);
        this.linearGradient = new LinearGradient(this.sLeft, this.sTop, this.sWidth, this.sHeight, this.colorArray, null, Shader.TileMode.MIRROR);
        this.paint.setAntiAlias(true);
        this.paint.setStyle(Paint.Style.FILL);
        this.paint.setShader(this.linearGradient);
        paramCanvas.drawRoundRect(localRectF, this.sWidth / 2.0F, this.sWidth / 2.0F, this.paint);
    }

    private void drawCircle(Canvas paramCanvas)
    {
        Paint localPaint = new Paint();
        float f1;
        if (this.y < this.mRadius)
        {
            f1 = this.mRadius;
            this.y = f1;
//            if (this.y <= this.sHeight - this.mRadius)
//                break ;
        }
        for (float f2 = this.sHeight - this.mRadius; ; f2 = this.y)
        {
            this.y = f2;
            localPaint.setAntiAlias(true);
            localPaint.setStyle(Paint.Style.FILL);
            localPaint.setColor(this.thumbColor);
            paramCanvas.drawCircle(this.x, this.y, this.mRadius, localPaint);
            localPaint.setStyle(Paint.Style.STROKE);
            localPaint.setColor(this.thumbBorderColor);
            localPaint.setStrokeWidth(2.0F);
            paramCanvas.drawCircle(this.x, this.y, this.mRadius, localPaint);
            break;
        }
    }

    protected void onDraw(Canvas paramCanvas)
    {
        super.onDraw(paramCanvas);
        int i = getMeasuredHeight();
        int j = getMeasuredWidth();
        this.mRadius = (j / 2.0F);
        this.sLeft = (0.25F * j);
        this.sRight = (0.75F * j);
        this.sTop = 0.0F;
        this.sBottom = i;
        this.sWidth = (this.sRight - this.sLeft);
        this.sHeight = (this.sBottom - this.sTop);
        this.x = (j / 2.0F);
        this.y = ((float)(1.0D - 0.01D * this.progress) * this.sHeight);
        drawBackground(paramCanvas);
        drawCircle(paramCanvas);
        this.paint.reset();
    }

    protected void onMeasure(int paramInt1, int paramInt2)
    {
        try
        {
            super.onMeasure(paramInt1, paramInt2);
            setMeasuredDimension(getMeasuredWidth(), getMeasuredHeight());
            return;
        }
        finally
        {
            ;
        }
    }

    public boolean onTouchEvent(MotionEvent paramMotionEvent)
    {
        this.y = paramMotionEvent.getY();
        this.progress = (128.0F * ((this.sHeight - this.y) / this.sHeight));
        switch (paramMotionEvent.getAction())
        {
            case 0:
            default:
            case 1:
            case 2:
        }
        while (true)
        {
            if (this.onStateChangeListener != null)
                this.onStateChangeListener.OnStateChangeListener(this, this.progress);
            setProgress(this.progress);
            invalidate();

        }
    }

    public void setColor(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
    {
        this.startColor = paramInt1;
        this.middleColor = paramInt2;
        this.endColor = paramInt3;
        this.thumbColor = paramInt4;
        this.thumbBorderColor = paramInt5;
        this.colorArray[0] = paramInt1;
        this.colorArray[1] = paramInt2;
        this.colorArray[2] = paramInt3;
    }

    public void setOnStateChangeListener(OnStateChangeListener paramOnStateChangeListener)
    {
        this.onStateChangeListener = paramOnStateChangeListener;
    }

    public void setProgress(float paramFloat)
    {
        this.progress = paramFloat;
        invalidate();
    }

    public interface OnStateChangeListener
    {
        public void OnStateChangeListener(View paramView, float paramFloat);

        public void onStopTrackingTouch(View paramView, float paramFloat);
    }
}