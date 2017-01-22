package com.example.widget;

import android.content.Context;
import android.graphics.PointF;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class MyViewPager extends ViewPager {

	/** ����ʱ���µĵ� **/
	PointF downP = new PointF();
	/** ����ʱ��ǰ�ĵ� **/
	PointF curP = new PointF();

	public MyViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MyViewPager(Context context) {
		super(context);

	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent arg0) {

		// �����ش����¼������λ�õ�ʱ�򣬷���true��
		// ˵����onTouch�����ڴ˿ؼ�������ִ�д˿ؼ���onTouchEvent
		return true;
	}

	@Override
	public boolean onTouchEvent(MotionEvent arg0) {

		// ÿ�ν���onTouch�¼�����¼��ǰ�İ��µ�����
		curP.x = arg0.getX();

		curP.y = arg0.getY();

		switch (arg0.getAction()) {

		case MotionEvent.ACTION_DOWN:

			// ��¼����ʱ�������

			downP.x = arg0.getX();
			downP.y = arg0.getY();

			// �˾������Ϊ��֪ͨ���ĸ�ViewPager���ڽ��е��Ǳ��ؼ��Ĳ�������Ҫ�����Ĳ������и���
			getParent().requestDisallowInterceptTouchEvent(true);

			break;

		case MotionEvent.ACTION_MOVE:
			// �˾������Ϊ��֪ͨ���ĸ�ViewPager���ڽ��е��Ǳ��ؼ��Ĳ�������Ҫ�����Ĳ������и���
			getParent().requestDisallowInterceptTouchEvent(true);

			break;

		case MotionEvent.ACTION_UP:
			
			// ��upʱ�ж��Ƿ��º����ֵ�����Ϊһ���㣬�����һ���㣬��ִ�е���¼�
			
			if(downP.x == curP.x && downP.y == curP.y){
				
				
			}
						

			break;

		default:
			break;
		}

		return super.onTouchEvent(arg0);
	}

}
