package edu.utboy.biteit.ui.adapters;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class FragmentViewPagerAdapter extends FragmentStatePagerAdapter {

	private List<Fragment> slideshowViews;

	public FragmentViewPagerAdapter(FragmentManager fm, List<Fragment> views) {
		super(fm);
		slideshowViews = views;
	}

	@Override
	public Fragment getItem(int position) {
		return slideshowViews.get(position);
	}

	@Override
	public int getCount() {
		return slideshowViews.size();
	}

	@Override
	public float getPageWidth(int position) {
		if (getCount() < 6 && getCount() > 0) {
			float ratio = 1 / (float) getCount();
			return ratio;
		} else {
			return 0.17f;
		}
	}

	@Override
	public int getItemPosition(Object object) {
		return POSITION_NONE;
	}

}