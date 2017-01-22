package com.example.teabaike;

import java.util.ArrayList;
import java.util.List;

import com.example.adapter.GuidePagerAdapter;
import com.example.config.MyConfig;
import com.example.helper.SharedPreferencesHelper;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class GuideActivity extends Activity {

	/** viewPgaer�ؼ� */
	private ViewPager viewPager_guide;
	/** ����ҳС�� */
	private LinearLayout linear_guide_dots;
	/** �ײ�ͼƬ */
	private ImageView[] dots;
	/** ��֪������ҳ���� */
	private int pageCount;
	/** ����ÿ��������һ��״̬���� */
	private int currentIndex;

	/** viewPager������ */
	private GuidePagerAdapter guidePagerAdapter;

	/** ����������Դ */
	private List<View> views;

	SharedPreferencesHelper sph;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.activity_guide);

		initialData();

		initialLayout();

	}

	/**
	 * ��ʼ������
	 */
	private void initialData() {
		/* ���ñ�ʶ����ʾ���������������ҳ */
		sph = new SharedPreferencesHelper(GuideActivity.this);

		sph.putInt(MyConfig.IS_FIRST_RUN, MyConfig.NOT_FIRST);

		/* С������Դ */
		pageCount = 3;

		dots = new ImageView[pageCount];

		currentIndex = 0;
		/* ����viewpager����Դ */
		views = new ArrayList<View>();
	}

	@SuppressLint("InflateParams")
	private void initialLayout() {

		viewPager_guide = (ViewPager) findViewById(R.id.viewPager_guide);

		LayoutInflater inflater = LayoutInflater.from(this);

		Class<R.drawable> clazz = R.drawable.class;// ׼������R.drawable����Դ

		for (int i = 0; i < pageCount; i++) {

			View view = inflater.inflate(R.layout.guide_content, null);

			LinearLayout linear_guide_showImg = (LinearLayout) view
					.findViewById(R.id.linear_guide_showImg);

			int imageID = 0;

			try {

				imageID = clazz.getDeclaredField("slide" + (i + 1)).getInt(
						R.drawable.slide1);

			} catch (Exception e) {

				e.printStackTrace();

			}

			linear_guide_showImg.setBackgroundResource(imageID);

			views.add(view);

		}

		guidePagerAdapter = new GuidePagerAdapter(views, this);

		viewPager_guide.setAdapter(guidePagerAdapter);

		viewPager_guide.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {

				dots[arg0].setEnabled(false);

				dots[currentIndex].setEnabled(true);

				currentIndex = arg0;

			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});

		/* ����С������ */
		linear_guide_dots = (LinearLayout) findViewById(R.id.linear_guide_dots);

		for (int i = 0; i < pageCount; i++) {// ѭ��ȡ��С��ͼƬ

			dots[i] = (ImageView) linear_guide_dots.getChildAt(i);

			if (i == 0) {
				dots[i].setEnabled(false);// ����Ϊ��ɫ����ѡ��״̬
			} else {
				dots[i].setEnabled(true);// ����Ϊ��ɫ
			}
		}

	}

}
