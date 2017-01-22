package com.hipad.smarthome.utils;

import com.hipad.smarthome.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class AppointSlideSwitch extends View implements OnTouchListener {

	private Bitmap bg_on, bg_off, slide_img;
	/**
	 * ����ʱ��x�͵�ǰ��x
	 */
	private float downX, nowX;

	/**
	 * ��¼�û��Ƿ��ڻ���
	 */
	private boolean onSlip = false;

	/**
	 * ��ǰ��״̬
	 */
	public boolean nowStatus = false;

	/**
	 * �����ӿ�
	 */
	private OnAppointChangedListener listener;

	public AppointSlideSwitch (Context context,AttributeSet attr) {
		super(context,attr);
		bg_on = BitmapFactory.decodeResource(getResources(), R.drawable.switch_on_img_blue);
		bg_off = BitmapFactory.decodeResource(getResources(), R.drawable.switch_off_img);
		slide_img = BitmapFactory.decodeResource(getResources(), R.drawable.switch_slidebtn);
		setOnTouchListener(this);
	}
	
	public AppointSlideSwitch (Context context) {
		super(context);
	}

	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		Matrix matrix = new Matrix();
		Paint paint = new Paint();
		float x = 0;

		if (onSlip) {// �Ƿ����ڻ���״̬,
			if (nowX >= bg_on.getWidth())// �Ƿ񻮳�ָ����Χ,�����û����ܵ���ͷ,����������ж�
				x = bg_on.getWidth() - slide_img.getWidth() / 2;// ��ȥ����1/2�ĳ���
			else
				x = nowX - slide_img.getWidth() / 2;
		} else {
			if (nowStatus) {// ���ݵ�ǰ��״̬���û����xֵ
				x = bg_on.getWidth() - slide_img.getWidth();
			} else {
				x = 0;
			}
		}

		// �Ի��黬�������쳣���������û������
		if (x < 0) {
			x = 0;
		} else if (x > bg_on.getWidth() - slide_img.getWidth()) {
			x = bg_on.getWidth() - slide_img.getWidth();
		}

		// ����nowX���ñ����������߹�״̬
		if (nowX < bg_on.getWidth() - slide_img.getWidth()) {
			canvas.drawBitmap(bg_off, matrix, paint);// �����ر�ʱ�ı���
		} else{
			canvas.drawBitmap(bg_on, matrix, paint);// ������ʱ�ı���
		}

//		context.showInfoLog("onDraw x = "+x+", nowX = "+nowX+", bg_on.getWidth() = "+bg_on.getWidth());
		// ��������
		canvas.drawBitmap(slide_img, x, 0, paint);
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN: {
			if (event.getX() > bg_off.getWidth()
					|| event.getY() > bg_off.getHeight()) {
				return false;
			} else {
				onSlip = true;
				downX = event.getX();
				nowX = downX;
			}
			break;
		}
		case MotionEvent.ACTION_MOVE: {
			nowX = event.getX();
			break;
		}
		case MotionEvent.ACTION_UP: {
			onSlip = false;
			if (event.getX() >= (bg_on.getWidth() / 2)) {
				nowStatus = true;
				nowX = bg_on.getWidth() - slide_img.getWidth();
			} else {
				nowStatus = false;
				nowX = 0;
			}

			if (listener != null) {
				listener.OnChanged(AppointSlideSwitch.this, nowStatus);
			}
			break;
		}
		}
//		context.showInfoLog("onTouch nowX = "+nowX);
		// ˢ�½���
		invalidate();
		return true;
	}

	/**
	 * ΪWiperSwitch����һ�����������ⲿ���õķ���
	 * 
	 * @param listener
	 */
	public void setOnChangedListener(OnAppointChangedListener listener) {
		this.listener = listener;
	}

	/**
	 * ���û������صĳ�ʼ״̬�����ⲿ����
	 * 
	 * @param checked
	 */
	public void setChecked(boolean checked) {
		if (checked) {
			nowX = bg_off.getWidth();
		} else {
			nowX = 0;
		}
		nowStatus = checked;
	}

	/**
	 * �ص��ӿ�
	 * 
	 * @author len
	 * 
	 */
	public interface OnAppointChangedListener {
		public void OnChanged(AppointSlideSwitch slideSwitch, boolean checkState);
	}
}
