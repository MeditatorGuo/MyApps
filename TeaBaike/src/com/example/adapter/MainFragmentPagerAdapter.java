package com.example.adapter;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;

public class MainFragmentPagerAdapter extends FragmentPagerAdapter {

	private List<Fragment> list = null;

	public MainFragmentPagerAdapter(FragmentManager fm, List<Fragment> list) {

		super(fm);
		this.list = list;

	}

	@Override
	public Fragment getItem(int arg0) {
		return list.get(arg0);
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object instantiateItem(View container, int position) {
		return super.instantiateItem(container, position);
	}

}
