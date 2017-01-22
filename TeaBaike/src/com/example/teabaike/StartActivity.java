package com.example.teabaike;

import com.example.config.MyConfig;
import com.example.helper.SharedPreferencesHelper;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

public class StartActivity extends Activity {
	/**
	 * ÿ��������ʾ3���ӵĽ��棬Ȼ������ǲ��ǵ�һ�����У�����ǵ�һ�����У���������ҳ�档���򣬽���������
	 */

	private SharedPreferencesHelper sph;

	Handler handler = new Handler() {

		@SuppressLint("HandlerLeak")
		public void handleMessage(android.os.Message msg) {

			switch (msg.what) {

			case 0x123:
				// �����ǵ�һ�����У���ת�����������
				SkipActivity(MainActivity.class);

				break;

			case 0x234:
				// ��һ������
				SkipActivity(GuideActivity.class);

				break;

			default:
				break;
			}

		};

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);

		// ����ȫ����ʾ
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.activity_start);

		initialData();

	}

	private void initialData() {

		sph = new SharedPreferencesHelper(StartActivity.this);

		int isFirst = sph.getInt(MyConfig.IS_FIRST_RUN);

		if (isFirst == MyConfig.NOT_FIRST) {
			// ���ǵ�һ������
			handler.sendEmptyMessageDelayed(0x123, 2000);

		} else {
			// ��һ������
			handler.sendEmptyMessageDelayed(0x234, 2000);

		}

	}

	/**
	 * ��ת����һ��Activity
	 * 
	 * @param clazz
	 */
	public void SkipActivity(Class<?> clazz) {

		Intent intent = new Intent(this, clazz);

		startActivity(intent);

		this.finish();
	}

}
