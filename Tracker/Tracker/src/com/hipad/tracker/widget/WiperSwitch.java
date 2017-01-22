package com.hipad.tracker.widget;

import com.hipad.tracker.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * 
 * @author guowei
 *
 */
public class WiperSwitch extends View {

	/** ���û�����ͼƬ */
	private Bitmap bgOn, bgOff, slideButtonBG;
	/** ���ÿ��ص�״̬����/�رա� Ĭ�ϣ��ر� */
	private boolean currentState = false;
	/** ��ǰ��������ƶ����� */
	private int currentX;
	/** ��¼��ǰ�����黬����״̬��Ĭ�ϣ�false */
	private boolean isSliding = false;
	/** ����״̬�ı���� */
	private OnToggleStateChangeListener mListener;

	public WiperSwitch(Context context) {
		super(context);
		init();
	}

	public WiperSwitch(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	private void init() {
		// ����ͼƬ��Դ
		bgOn = BitmapFactory.decodeResource(getResources(),
				R.drawable.swich_on_background);
		bgOff = BitmapFactory.decodeResource(getResources(),
				R.drawable.swich_off_background);
		slideButtonBG = BitmapFactory.decodeResource(getResources(),
				R.drawable.swich_action);
	}

	/**
	 * �ƶ�Ч���Ĵ���
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN: // ��ָ����
			currentX = (int) event.getX();
			isSliding = true;
			break;
		case MotionEvent.ACTION_MOVE: // ��ָ�ƶ�
			currentX = (int) event.getX();
			break;
		case MotionEvent.ACTION_UP: // ��ָ̧��
			isSliding = false;
			// �жϵ�ǰ�����飬ƫ������һ�ߣ��������������ĵ�<���������ĵ㣬����Ϊ�ر�״̬
			int bgCenter = bgOn.getWidth() / 2;
			boolean state = currentX >= bgCenter; // �ı���״̬
			// ��ָ̧��ʱ���ص����������ص�ǰ�Ŀ���״̬
			if (state != currentState && mListener != null) {
				mListener.onToggleStateChange(state);
			}
			currentState = state;
			break;
		default:
			break;
		}
		invalidate(); // ˢ�¿ؼ����÷��������onDraw(Canvas canvas)����
		return true; // �Լ������¼������ø��ฺ�������¼�
	}

	/**
	 * ������ǰ�ؼ����ʱ�ص�
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		// ���ÿ��صĿ�͸�
		setMeasuredDimension(bgOn.getWidth(), bgOn.getHeight());
	}

	/**
	 * ���ƿؼ�
	 */
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		// 1,�������ر������Ƶ��ؼ�

		if (currentX < (bgOn.getWidth() / 2)) {
			canvas.drawBitmap(bgOff, 0, 0, null);
		} else {
			canvas.drawBitmap(bgOn, 0, 0, null);// ������ʱ�ı���
		}

		// 2�����ƻ�������ʾ��λ�ã�������ر�
		if (isSliding) {
			int left = currentX - slideButtonBG.getWidth() / 2; // ������ָ���㣬�������slidingButton������ƶ����м�
			if (left < 0) {
				left = 0;
			} else if (left > bgOn.getWidth() - slideButtonBG.getWidth()) {
				left = bgOn.getWidth() - slideButtonBG.getWidth();
			}
			canvas.drawBitmap(slideButtonBG, left, 0, null);
		} else {
			if (currentState) {
				canvas.drawBitmap(bgOn, 0, 0, null);
				// ���ƴ�״̬
				canvas.drawBitmap(slideButtonBG, bgOn.getWidth()
						- slideButtonBG.getWidth(), 0, null);
			} else {
				// ���ƹر�״̬
				canvas.drawBitmap(bgOff, 0, 0, null);
				canvas.drawBitmap(slideButtonBG, 0, 0, null);
			}
		}
	}

	public void setToggleState(boolean b) {

		if (currentState != b) {
			currentState = b;
			invalidate();// �ػ��ؼ�
		}

	}

	/**
	 * �������ü�������
	 * 
	 * @param listener
	 */
	public void setOnToggleStateChangeListener(
			OnToggleStateChangeListener listener) {
		this.mListener = listener;
	}

	public interface OnToggleStateChangeListener {
		public void onToggleStateChange(boolean b);

	}

}