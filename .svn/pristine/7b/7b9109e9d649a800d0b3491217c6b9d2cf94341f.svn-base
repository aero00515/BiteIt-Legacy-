package edu.utboy.biteit.ui.adapters;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class StoreInfoPicAdapter extends FragmentStatePagerAdapter {

	private List<Fragment> views;

	public StoreInfoPicAdapter(FragmentManager fm, List<Fragment> views) {
		super(fm);
		this.views = views;
	}

	@Override
	public int getCount() {
		return views.size();
	}

	@Override
	public Fragment getItem(int position) {
		return views.get(position);
	}
}
