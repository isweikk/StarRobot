package com.weikk.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;

import com.weikk.R;

public class RockerView extends View {

	//固定摇杆背景圆形的X,Y坐标以及半径
	private float mRockerBg_X;
	private float mRockerBg_Y;
	private float mRockerBg_R;
	//摇杆的X,Y坐标以及摇杆的半径
	private float mRockerBtn_X;
	private float mRockerBtn_Y;
	private float mRockerBtn_R;
	private Bitmap mBmpRockerBg;
	private Bitmap mBmpRockerBtn;
	
	private PointF mCenterPoint;

	OnRockerListener mOnRockerListener = null;
	public void setRockerListener(OnRockerListener onRockerListener) {
		mOnRockerListener = onRockerListener;
	}
	public OnRockerListener getRockerListener() {
		return mOnRockerListener;
	}

	public RockerView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		// 获取bitmap
		mBmpRockerBg = BitmapFactory.decodeResource(context.getResources(), R.drawable.rocker_bg);
		mBmpRockerBtn = BitmapFactory.decodeResource(context.getResources(), R.drawable.rocker_btn);
		
		getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
			
			// 调用该方法时可以获取view实际的宽getWidth()和高getHeight()
			@Override
			public boolean onPreDraw() {
				// TODO Auto-generated method stub
				getViewTreeObserver().removeOnPreDrawListener(this); 
				
				Log.e("RockerView", getWidth() + "/" +  getHeight());
				mCenterPoint = new PointF(getWidth() / 2, getHeight() / 2);
				mRockerBg_X = mCenterPoint.x;
				mRockerBg_Y = mCenterPoint.y;
				
				mRockerBtn_X = mCenterPoint.x;
				mRockerBtn_Y = mCenterPoint.y;
				
				float tmp_f = mBmpRockerBg.getWidth() / (float)(mBmpRockerBg.getWidth() + mBmpRockerBtn.getWidth());
				mRockerBg_R = tmp_f * getWidth() / 2;
				mRockerBtn_R = (1.0f - tmp_f)* getWidth() / 2;
				
				return true;
			}
		});
	
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				while(true){
					
					//系统调用onDraw方法刷新画面
					RockerView.this.postInvalidate();
					
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}).start();
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		canvas.drawBitmap(mBmpRockerBg, null, 
				new Rect((int)(mRockerBg_X - mRockerBg_R), 
						(int)(mRockerBg_Y - mRockerBg_R), 
						(int)(mRockerBg_X + mRockerBg_R), 
						(int)(mRockerBg_Y + mRockerBg_R)), 
				null);
		canvas.drawBitmap(mBmpRockerBtn, null, 
				new Rect((int)(mRockerBtn_X - mRockerBtn_R), 
						(int)(mRockerBtn_Y - mRockerBtn_R), 
						(int)(mRockerBtn_X + mRockerBtn_R), 
						(int)(mRockerBtn_Y + mRockerBtn_R)), 
				null);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE) {
			double tempRadian = 0;
			float tempDragPropotion = 0;
			// 当触屏区域不在活动范围内
			if (Math.sqrt(Math.pow((mRockerBg_X - (int) event.getX()), 2) + Math.pow((mRockerBg_Y - (int) event.getY()), 2)) >= mRockerBg_R) {
				//得到摇杆与触屏点所形成的角度
				tempRadian = getRadian(mRockerBg_X, mRockerBg_Y, event.getX(), event.getY());
				tempDragPropotion = 1;
				//保证内部小圆运动的长度限制
				getXY(mRockerBg_X, mRockerBg_Y, mRockerBg_R, tempRadian);
			} else {//如果小球中心点小于活动区域则随着用户触屏点移动即可
				mRockerBtn_X = (int) event.getX();
				mRockerBtn_Y = (int) event.getY();
				//得到摇杆与触屏点所形成的角度
				tempRadian = getRadian(mRockerBg_X, mRockerBg_Y, mRockerBtn_X, mRockerBtn_Y);
				tempDragPropotion = getRadius(mRockerBg_X, mRockerBg_Y, mRockerBtn_X, mRockerBtn_Y)/mRockerBg_R;
				if (tempDragPropotion > 1) {
					tempDragPropotion = 1;
				}
			}
			if(mOnRockerListener != null) {
				mOnRockerListener.reportPosition(mRockerBtn_X - mCenterPoint.x, mRockerBtn_Y - mCenterPoint.y);
				mOnRockerListener.onRocker((float)radian2Angle(tempRadian), tempDragPropotion);
			}
		} else if (event.getAction() == MotionEvent.ACTION_UP) {
			//当释放按键时摇杆要恢复摇杆的位置为初始位置
			mRockerBtn_X = mCenterPoint.x;
			mRockerBtn_Y = mCenterPoint.y;
			if(mOnRockerListener != null) {
				mOnRockerListener.reportPosition(0, 0);
				mOnRockerListener.onRocker(0, 0);
			}
		}
		return true;
	}
	
	/***
	 * 得到两点之间的弧度
	 */
	public double getRadian(float px1, float py1, float px2, float py2) {
		//得到两点X的距离
		double x = px2 - px1;
		//得到两点Y的距离
		double y = py2 - py1;
		//算出斜边长
		double xie = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
		//得到这个角度的余弦值（通过三角函数中的定理 ：邻边/斜边=角度余弦值）
		double cosAngle = x / xie;
		//通过反余弦定理获取到其角度的弧度
		double rad = Math.acos(cosAngle);
		//注意：当触屏的位置Y坐标<摇杆的Y坐标我们要取反值-0~-180
		if (py2 < py1) {
			rad = -rad;
		}
		return rad;
	}
	
	/**
	 * 
	 * @param R  圆周运动的旋转点
	 * @param centerX 旋转点X
	 * @param centerY 旋转点Y
	 * @param rad 旋转的弧度
	 */
	public void getXY(float centerX, float centerY, float R, double rad) {
		//获取圆周运动的X坐标 
		mRockerBtn_X = (float) (R * Math.cos(rad)) + centerX;
		//获取圆周运动的Y坐标
		mRockerBtn_Y = (float) (R * Math.sin(rad)) + centerY;
	}

	/**
	 * 弧度转角度
	 *
	 * @param radian 弧度
	 * @return 角度[0, 360)
	 */
	private double radian2Angle(double radian) {
		double tmp = Math.round(radian / Math.PI * 180);
		return tmp >= 0 ? tmp : 360 + tmp;
	}


	public float getRadius(float centerX, float centerY, float touchX, float touchY) {
		//得到x/y方向的距离
		float x = touchX - centerX;
		float y = touchY - centerY;
		//算出斜边长，即触点半径
		return (float) Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
	}
}
