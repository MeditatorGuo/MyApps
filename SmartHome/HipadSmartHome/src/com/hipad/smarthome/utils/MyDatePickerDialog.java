package com.hipad.smarthome.utils;

import com.hipad.smarthome.R;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.TextView;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Color;

public class MyDatePickerDialog extends AlertDialog implements OnClickListener,
		OnDateChangedListener {

	private static DatePicker mDatePicker = null;
	private OnDateSetListener mCallBack = null;

	private static final String YEAR = "year";
	private static final String MONTH = "month";
	private static final String DAY = "day";
	private View view;

	public interface OnDateSetListener {
		void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth);
	}

	public MyDatePickerDialog(Context context, OnDateSetListener callBack, int year,
			int monthOfYear, int dayOfMonth) {
		this(context, 0, callBack, year, monthOfYear, dayOfMonth);
	}

	public MyDatePickerDialog(Context context, int theme, OnDateSetListener callBack,
			int year, int monthOfYear, int dayOfMonth) {
		super(context, theme);
		mCallBack = callBack;
		Context themeContext = getContext();
		// setButton(BUTTON_POSITIVE, "confirm", (OnClickListener)null);
		// setButton(BUTTON_NEGATIVE,"cancel",(OnClickListener)null);
		LayoutInflater inflater = (LayoutInflater) themeContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = inflater.inflate(R.layout.appointment_date_picker, null);
		mDatePicker = (DatePicker) view.findViewById(R.id.datePicker);
		mDatePicker.init(year, monthOfYear, dayOfMonth, this);

		setTitle(context.getResources().getString(R.string.appointment_date_title));
		setButton();
	}

	@Override
	public void onDateChanged(DatePicker arg0, int year, int month, int day) {
		// TODO Auto-generated method stub
		mDatePicker.init(year, month, day, null);
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		// TODO Auto-generated method stub

	}

	public void myShow() {
		// �Լ�ʵ��show��������Ҫ��Ϊ�˰�setContentView�����ŵ�show�������棬����ᱨ��
		show();
		setContentView(view);
	}

	private void setTitle(String title) {
		// ��ȡ�Լ������title���ֲ���ֵ��
		((TextView) view.findViewById(R.id.date_picker_title)).setText(title);
	}

	private void setButton() {
		// ��ȡ�Լ��������Ӧ��ť�����ü�����ֱ�ӵ��ù���ʱ��������CallBack�ӿڣ�Ϊ��ʡ����û���Լ�д�ӿڣ�ֱ����֮ǰ���ඨ��õģ�ͬʱ�رնԻ���
		view.findViewById(R.id.btn_cancel).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						dismiss();
					}
				});

		view.findViewById(R.id.btn_confirm).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						if (mCallBack != null) {
							mDatePicker.clearFocus();
							mCallBack.onDateSet(mDatePicker,mDatePicker.getYear(),mDatePicker.getMonth(),mDatePicker.getDayOfMonth());
						}
						dismiss();
					}
				});
	}

}
