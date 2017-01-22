package com.example.fragment;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.example.teabaike.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MapFragment extends Fragment {

	private MapView mapView;

	private BaiduMap mBaidumap;// ��ͼ��Ӧ����

	private GeoCoder gc;// �������ĺ�����

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// ��ʹ��SDK�����֮ǰ��ʼ��context��Ϣ������ApplicationContext
		// ע��÷���Ҫ��setContentView����֮ǰʵ��
		SDKInitializer.initialize(getActivity().getApplicationContext());

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		super.onCreateView(inflater, container, savedInstanceState);

		View view = inflater.inflate(R.layout.fragment_map, null);

		mapView = (MapView) view.findViewById(R.id.bmapView);

		mBaidumap = mapView.getMap();

		return view;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		// ��ִ��onDestroyʱִ��mMapView.onDestroy()��ʵ�ֵ�ͼ�������ڹ���
		mapView.onDestroy();
	}

	@Override
	public void onResume() {
		super.onResume();
		// ��ִ��onResumeʱִ��mMapView. onResume ()��ʵ�ֵ�ͼ�������ڹ���
		mapView.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
		// ��ִ��onPauseʱִ��mMapView. onPause ()��ʵ�ֵ�ͼ�������ڹ���
		mapView.onPause();
	}

}
