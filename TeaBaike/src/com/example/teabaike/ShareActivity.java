package com.example.teabaike;

import java.util.Timer;
import java.util.TimerTask;


import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Toast;

public class ShareActivity extends Activity {

	private ImageView imageView_share_bg;
	/** �ж��Ƿ���˫�� */
	private boolean isDouble = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_share);
		imageView_share_bg = (ImageView) findViewById(R.id.imageView_share_bg);
		imageView_share_bg.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				onKeyDown(4, null);

			}
		});

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			
			Timer tExit = null;
			
			if (isDouble == false) {
				
				isDouble = true; // ׼���˳�
				
				Toast.makeText(this, "������λ��ͨ���ٰ�һ�η�����һҳ", Toast.LENGTH_SHORT)
						.show();
				
				tExit = new Timer();
				
				tExit.schedule(new TimerTask() {
					@Override
					public void run() {
						
						isDouble = false; // ȡ���˳�
						
					}
					
				}, 2000); // ���2������û�а��·��ؼ�����������ʱ��ȡ�����ղ�ִ�е�����

			} else {
				
				this.finish();
				
				overridePendingTransition(R.anim.push_down_in,
						R.anim.push_down_out);// �������Ƴ�Ч��
			}
		}

		return false;

	}

}
