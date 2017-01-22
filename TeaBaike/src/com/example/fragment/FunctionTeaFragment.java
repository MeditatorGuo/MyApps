package com.example.fragment;

import com.example.teabaike.R;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class FunctionTeaFragment extends Fragment {
	/** �����ı��� */
	private EditText editText_funtea_searchEdit;
	/** ������ť */
	private ImageView imageView_funtea_searchBtn;
	/** ������������ */
	private TextView textView_funtea_serachtea;
	/** �ҵ��ղ� */
	private TextView textView_funtea_mycollect;
	/** �鿴���ʼ�¼ */
	private TextView textView_funtea_selectlog;
	/** ��Ȩ��Ϣ */
	private TextView textView_funtea_copyright;
	/** ������� */
	private TextView textView_funtea_feedback;
	/** ��ά�� */
	private TextView textView_funtea_qrcode;
	/** ��ͼ */
	private TextView textView_funtea_map;

	MyClickListener mListener;
	/** �ӿڻص����ͣ���Activity������ */
	MyButtonClickListener buttonListener;

	/** �༭�򶯻����� */
	Animation shake;

	public FunctionTeaFragment() {

	}

	public FunctionTeaFragment(Context context) {

		buttonListener = (MyButtonClickListener) context;

		shake = AnimationUtils.loadAnimation(context, R.anim.shake);// ���ض�����Դ�ļ�
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		mListener = new MyClickListener();

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(R.layout.fragment_fun_tea, null);
		editText_funtea_searchEdit = (EditText) view
				.findViewById(R.id.editText_funtea_searchEdit);
		imageView_funtea_searchBtn = (ImageView) view
				.findViewById(R.id.imageView_funtea_searchBtn);
		textView_funtea_serachtea = (TextView) view
				.findViewById(R.id.textView_funtea_serachtea);
		textView_funtea_mycollect = (TextView) view
				.findViewById(R.id.textView_funtea_mycollect);
		textView_funtea_selectlog = (TextView) view
				.findViewById(R.id.textView_funtea_selectlog);
		textView_funtea_copyright = (TextView) view
				.findViewById(R.id.textView_funtea_copyright);
		textView_funtea_feedback = (TextView) view
				.findViewById(R.id.textView_funtea_feedback);
		textView_funtea_qrcode = (TextView) view
				.findViewById(R.id.textView_funtea_qrcode);

		textView_funtea_map = (TextView) view
				.findViewById(R.id.textView_funtea_map);

		imageView_funtea_searchBtn.setOnClickListener(mListener);
		textView_funtea_serachtea.setOnClickListener(mListener);
		textView_funtea_mycollect.setOnClickListener(mListener);
		textView_funtea_selectlog.setOnClickListener(mListener);
		textView_funtea_copyright.setOnClickListener(mListener);
		textView_funtea_feedback.setOnClickListener(mListener);

		textView_funtea_qrcode.setOnClickListener(mListener);

		textView_funtea_map.setOnClickListener(mListener);

		return view;

	}

	class MyClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {

			switch (v.getId()) {

			case R.id.imageView_funtea_searchBtn:

				String searchStr = editText_funtea_searchEdit.getText()
						.toString();

				if (!"".equals(searchStr)) {

					buttonListener.onMyButtonClick(7, searchStr);

				} else {

					editText_funtea_searchEdit.startAnimation(shake); // ��������Ŷ���Ч��
				}

				break;

			case R.id.textView_funtea_serachtea:
				buttonListener.onMyButtonClick(7, "��");

				break;
			case R.id.textView_funtea_mycollect:

				buttonListener.onMyButtonClick(1, null);
				break;
			case R.id.textView_funtea_selectlog:

				buttonListener.onMyButtonClick(2, null);
				break;
			case R.id.textView_funtea_copyright:

				buttonListener.onMyButtonClick(3, null);
				break;
			case R.id.textView_funtea_feedback:

				buttonListener.onMyButtonClick(4, null);
				break;

			case R.id.textView_funtea_qrcode:
				
				buttonListener.onMyButtonClick(5, null);
				break;

			case R.id.textView_funtea_map:
				buttonListener.onMyButtonClick(6, null);
				break;

			default:
				break;
			}

		}

	}

	/**
	 * ������activity����ʱ�����ص��Ľӿ�
	 * 
	 */
	public interface MyButtonClickListener {

		/**
		 * ��������ҳ�浥������
		 * 
		 * @param titleTag
		 *            title���
		 * @param text
		 *            ���������ҳ�棬��ô�Ѵ�ֵ����ȥ������дnull����
		 */

		public void onMyButtonClick(int titleTag, String text);
	}

}
