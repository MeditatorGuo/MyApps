package com.example.fragment;



import com.example.teabaike.ContentActivity;
import com.example.teabaike.R;
import com.example.widget.XListView;
import com.example.widget.XListView.IXListViewListener;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

@SuppressLint("InflateParams")
public abstract class BaseListFragment extends Fragment implements
		IXListViewListener {

	/** �Զ���listView */
	protected XListView listview;

	/** �����������listview���� */
	protected View view;
	/** inflater��䲼���� */

	LayoutInflater mInflater;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		mInflater = inflater;

		view = inflater.inflate(R.layout.fragment_content, null);

		listview = (XListView) view.findViewById(R.id.listView_contentfragment);

		listview.setPullLoadEnable(true);

		listview.setPullRefreshEnable(false);

		return super.onCreateView(inflater, container, savedInstanceState);
	}

	/**
	 * ��ת��ContentActivityҳ��
	 * 
	 * @param idStr
	 */

	public void SkipContentActivity(String idStr) {

		Intent intent = new Intent(getActivity(), ContentActivity.class);

		intent.putExtra("id", idStr);

		startActivity(intent);
	}

}
