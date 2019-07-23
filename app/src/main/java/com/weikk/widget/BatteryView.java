package com.weikk.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class BatteryView extends View
{
    private int height;
    private int mColor;
    private int mPower = 100;
    private int orientation;
    private int width;

    public BatteryView(Context paramContext)
    {
        super(paramContext);
    }

    public BatteryView(Context paramContext, AttributeSet paramAttributeSet)
    {
        super(paramContext, paramAttributeSet);
        this.mColor = 0x00D577;
        this.orientation = 0;
        this.mPower = 100;
        this.width = getMeasuredWidth();
        this.height = getMeasuredHeight();
    }

    private void drawHorizontalBattery(Canvas paramCanvas)
    {
        Paint localPaint = new Paint();
        localPaint.setColor(this.mColor);
        localPaint.setStyle(Style.STROKE);
        float f1 = this.width / 20.0F;
        float f2 = f1 / 2.0F;
        localPaint.setStrokeWidth(f1);
        RectF localRectF1 = new RectF(f2, f2, this.width - f1 - f2, this.height - f2);
        localPaint.setColor(-16777216);
        paramCanvas.drawRect(localRectF1, localPaint);
        localPaint.setStrokeWidth(0.0F);
        localPaint.setStyle(Style.FILL);
        RectF localRectF2 = new RectF(f1, f1, (this.width - f1 * 2.0F) * this.mPower / 100.0F, this.height - f1);
        if (this.mPower < 20)
            localPaint.setColor(-65536);
        if ((this.mPower >= 20) && (this.mPower < 30))
            localPaint.setColor(-256);
        if (this.mPower >= 30)
            localPaint.setColor(-16711936);
        paramCanvas.drawRect(localRectF2, localPaint);
        RectF localRectF3 = new RectF(this.width - f1, 0.25F * this.height, this.width, 0.75F * this.height);
        localPaint.setColor(-16777216);
        paramCanvas.drawRect(localRectF3, localPaint);
    }

    private void drawVerticalBattery(Canvas paramCanvas)
    {
        Paint localPaint = new Paint();
        localPaint.setColor(this.mColor);
        localPaint.setStyle(Style.STROKE);
        float f1 = this.height / 20.0F;
        float f2 = f1 / 2.0F;
        localPaint.setStrokeWidth(f1);
        int i = (int)(0.5F + f1);
        RectF localRectF1 = new RectF(f2, f2 + i, this.width - f2, this.height - f2);
        localPaint.setColor(-1);
        paramCanvas.drawRect(localRectF1, localPaint);
        localPaint.setStrokeWidth(0.0F);
        RectF localRectF2 = new RectF(f1, (this.height - i - f1) * (100 - this.mPower) / 100.0F + (f1 + i), this.width - f1, this.height - f1);
        if (this.mPower < 20)
            localPaint.setColor(-65536);
        if ((this.mPower >= 20) && (this.mPower < 30))
            localPaint.setColor(-256);
        if (this.mPower >= 30)
            localPaint.setColor(-16711936);
        localPaint.setStyle(Style.FILL);
        paramCanvas.drawRect(localRectF2, localPaint);
        localPaint.setColor(-1);
        paramCanvas.drawRect(new RectF(this.width / 4.0F, 0.0F, 0.75F * this.width, i), localPaint);
    }

    public int getPower()
    {
        return this.mPower;
    }

    protected void onDraw(Canvas paramCanvas)
    {
        super.onDraw(paramCanvas);
        if (this.orientation == 0)
        {
            drawHorizontalBattery(paramCanvas);
            return;
        }
        drawVerticalBattery(paramCanvas);
    }

    protected void onMeasure(int paramInt1, int paramInt2)
    {
        super.onMeasure(paramInt1, paramInt2);
        this.width = getMeasuredWidth();
        this.height = getMeasuredHeight();
    }

    public void setColor(int paramInt)
    {
        this.mColor = paramInt;
        invalidate();
    }

    public void setPower(int paramInt)
    {
        this.mPower = paramInt;
        if (this.mPower < 0)
            this.mPower = 100;
        invalidate();
    }
}
