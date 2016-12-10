package edu.utboy.biteit.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.viewpagerindicator.LinePageIndicator;

import edu.utboy.biteit.R;
import edu.utboy.biteit.models.StoreInfo;
import edu.utboy.biteit.models.dao.StoreInfoDAO;
import edu.utboy.biteit.ui.pages.CommentFragment;
import edu.utboy.biteit.ui.pages.RatingFragment;

public class StoreInfoFeedbackActivity extends FragmentActivity {

	public final static String STORE_INFO_FEEDBACK_RATING = "store_info_feedback_rating";
	public final static String STORE_INFO_FEEDBACK_COMMENT = "store_info_feedback_video_record";

	private ViewPager feedbackPager;
	private FeedbackPagerAdapter mFeedbackPagerAdapter;
	private LinePageIndicator lpi;

	private ImageView closeImageView;
	private ImageView applyImageView;

	private RatingFragment ratingFragment;
	private CommentFragment commentFragment;
	private StoreInfo storeInfo;
	private long storeId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		int currentOrientation = getResources().getConfiguration().orientation;
		if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
		} else {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
		}
		setContentView(R.layout.activity_store_info_feedback);
		feedbackPager = (ViewPager) findViewById(R.id.activity_store_info_feedback_pager);
		lpi = (LinePageIndicator) findViewById(R.id.feedback_pager_indicator);
		closeImageView = (ImageView) findViewById(R.id.activity_store_info_feedback_close);
		applyImageView = (ImageView) findViewById(R.id.activity_store_info_feedback_apply);

		StoreInfoDAO storeInfoDao = new StoreInfoDAO(getApplicationContext());
		storeInfoDao.open();
		if (savedInstanceState != null) {
			storeId = savedInstanceState.getLong(
					StoreInfoActivity.STORE_ID_TAG, 0);
			storeInfo = storeInfoDao.get(storeId);
		} else {
			storeId = getIntent().getLongExtra(StoreInfoActivity.STORE_ID_TAG,
					0);
			if (storeId != 0) {
				savedInstanceState = new Bundle();
				savedInstanceState.putLong(StoreInfoActivity.STORE_ID_TAG,
						storeId);
				storeInfo = storeInfoDao.get(storeId);
			}
		}
		Bundle ratingData = new Bundle();
		ratingData.putFloat(RatingFragment.RATING_CLAEN,
				storeInfo.getRatingClean());
		ratingData.putFloat(RatingFragment.RATING_SERVICE,
				storeInfo.getRatingService());
		ratingData.putFloat(RatingFragment.RATING_ATMOS,
				storeInfo.getRatingAtmos());
		ratingData.putFloat(RatingFragment.RATING_FAVOR,
				storeInfo.getRatingFlavor());
		ratingData.putFloat(RatingFragment.RATING_UNKNOW,
				storeInfo.getRatingUnkonw());
		commentFragment = new CommentFragment();
		ratingFragment = new RatingFragment();
		ratingFragment.setArguments(ratingData);
		mFeedbackPagerAdapter = new FeedbackPagerAdapter(
				getSupportFragmentManager());

		feedbackPager.setAdapter(mFeedbackPagerAdapter);
		lpi.setViewPager(feedbackPager);
		closeImageView.setOnClickListener(cancelListener);
		applyImageView.setOnClickListener(applyListener);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		storeId = savedInstanceState.getLong(StoreInfoActivity.STORE_ID_TAG);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putLong(StoreInfoActivity.STORE_ID_TAG, storeId);
		super.onSaveInstanceState(outState);
	}

	private OnClickListener applyListener = new OnClickListener() {

		@Override
		public void onClick(View v) {

			Intent data = new Intent();

			Bundle ratingData = ratingFragment.getRatingData();
			String commentData = commentFragment.getComment();

			data.putExtra(STORE_INFO_FEEDBACK_RATING, ratingData);
			data.putExtra(STORE_INFO_FEEDBACK_COMMENT, commentData);

			setResult(RESULT_OK, data);
			finish();
			overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
		}

	};

	private OnClickListener cancelListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			setResult(RESULT_CANCELED);
			finish();
			overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
		}
	};

	private class FeedbackPagerAdapter extends FragmentPagerAdapter {

		List<Fragment> fragments;

		public FeedbackPagerAdapter(FragmentManager fm) {
			super(fm);
			fragments = new ArrayList<Fragment>();
			fragments.add(ratingFragment);
			fragments.add(commentFragment);
		}

		@Override
		public int getCount() {
			return fragments.size();
		}

		@Override
		public Fragment getItem(int position) {
			return fragments.get(position);
		}
	};

}
