package edu.utboy.biteit.ui;

import idv.andylin.tw.andyandroidlibs.utils.MyLog;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.viewpagerindicator.CirclePageIndicator;

import edu.utboy.biteit.FeedbackUpdateService;
import edu.utboy.biteit.FoundNewStoreActivity;
import edu.utboy.biteit.R;
import edu.utboy.biteit.asynctasks.RatingFeedbackRequest;
import edu.utboy.biteit.asynctasks.VolleyTaskManager;
import edu.utboy.biteit.models.FeedbackPoint;
import edu.utboy.biteit.models.PhotoComment;
import edu.utboy.biteit.models.StoreInfo;
import edu.utboy.biteit.models.dao.FeedbackPointDAO;
import edu.utboy.biteit.models.dao.PhotoCommentDAO;
import edu.utboy.biteit.models.dao.StoreInfoDAO;
import edu.utboy.biteit.ui.adapters.StoreInfoPicAdapter;
import edu.utboy.biteit.ui.pages.RatingFragment;
import edu.utboy.biteit.ui.pages.StoreInfoPicFragment;

public class StoreInfoActivity extends ActionBarActivity {

	public static final String STORE_ID_TAG = "store_id_tag";

	@Deprecated
	public static final String STORE_INFO_TAG = "store_info_tag";
	@Deprecated
	public static final String STORE_PHOTO_COMMENT_TAG = "store_photo_comment_tag";

	private static final int STORE_INFO_FEEDBACK_RESULT = 1;
	private static final int STORE_INFO_TAKE_PICTURE_RESULT = 2;

	// private static final int STATE_ONSCREEN = 0;
	// private static final int STATE_OFFSCREEN = 1;
	// private static final int STATE_RETURNING = 2;

	private StoreInfo storeInfo;
	private List<PhotoComment> photoComments;

	private ViewPager picPager;
	private CirclePageIndicator cpi;
	private StoreInfoPicAdapter mStoreInfoPicAdapter;
	private List<Fragment> storeInfoPicFragments;
	private RatingBar ratingClean;
	private RatingBar ratingService;
	private RatingBar ratingAtmos;
	private RatingBar ratingFavor;
	private RatingBar ratingUnknow;
	private TextView storeInfoTotalRate;
	private TextView storeInfoAddress;
	private TextView storeInfoPhone;
	private ImageButton storePhoneCall;
	private Menu menu;
	private ImageButton storeTakePicture;
	private StoreInfoDAO storeInfoDAO;
	private PhotoCommentDAO photoCommentDAO;
	private long storeId;

	// private View mStickyView;
	// private View mPlaceholderView;
	// private ObservableScrollView mObservableScrollView;
	// private ScrollSettleHandler mScrollSettleHandler = new
	// ScrollSettleHandler();

	// private int mMinRawY = 0;
	// private int mState = STATE_ONSCREEN;
	// private int mQuickReturnHeight;
	// private int mMaxScrollY;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		storeInfoDAO = new StoreInfoDAO(this);
		photoCommentDAO = new PhotoCommentDAO(this);
		setContentView(R.layout.activity_store_info);
		picPager = (ViewPager) findViewById(R.id.activity_store_info_pic_pager);
		cpi = (CirclePageIndicator) findViewById(R.id.indicator);
		ratingClean = (RatingBar) findViewById(R.id.activity_store_info_clean_rating);
		ratingService = (RatingBar) findViewById(R.id.activity_store_info_service_rating);
		ratingAtmos = (RatingBar) findViewById(R.id.activity_store_info_atmos_rating);
		ratingFavor = (RatingBar) findViewById(R.id.activity_store_info_flavor_rating);
		ratingUnknow = (RatingBar) findViewById(R.id.activity_store_info_unknow_rating);
		storeInfoTotalRate = (TextView) findViewById(R.id.activity_store_info_total_rating);
		storeInfoAddress = (TextView) findViewById(R.id.activity_store_info_address);
		storeInfoPhone = (TextView) findViewById(R.id.activity_store_info_phone);
		storeTakePicture = (ImageButton) findViewById(R.id.activity_store_info_pic_null);
		storePhoneCall = (ImageButton) findViewById(R.id.activity_store_info_call);

		assert getActionBar() != null;
		getActionBar().setDisplayHomeAsUpEnabled(true);

		storeInfoDAO.open();
		photoCommentDAO.open();
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				this).build();
		ImageLoader.getInstance().init(config);

		// The view holder's setting
		// mObservableScrollView = (ObservableScrollView)
		// findViewById(R.id.activity_store_info_scroll_view);
		// mStickyView = findViewById(R.id.activity_store_info_function);
		// mPlaceholderView =
		// findViewById(R.id.activity_store_info_placeholder);

		storeInfoPicFragments = new ArrayList<Fragment>();
		mStoreInfoPicAdapter = new StoreInfoPicAdapter(
				getSupportFragmentManager(), storeInfoPicFragments);
		picPager.setAdapter(mStoreInfoPicAdapter);
		cpi.setViewPager(picPager);

		storeId = getIntent().getLongExtra(STORE_ID_TAG, 0);
		MyLog.d(StoreInfoTakePictureActivity.class, "" + storeId);
		// initObservableScrollView();
		initStoreInfo(storeId);

		// function3 = (Button)
		// findViewById(R.id.activity_store_info_function_btn3);
		// function3.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// function3.setClickable(false);
		// Intent intent = new Intent();
		// intent.setClass(StoreInfoActivity.this,
		// StoreInfoFeedbackActivity.class);
		// intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		// startActivityForResult(intent, STORE_INFO_FEEDBACK_RESULT);
		// overridePendingTransition(R.anim.abc_fade_in,
		// R.anim.abc_fade_out);
		// }
		// });
	}

	/*
	 * private void initObservableScrollView() {
	 * mObservableScrollView.setCallbacks(mObservableScrollViewCallbacks);
	 * mObservableScrollView.getViewTreeObserver().addOnGlobalLayoutListener(
	 * new ViewTreeObserver.OnGlobalLayoutListener() {
	 * 
	 * @Override public void onGlobalLayout() { mObservableScrollViewCallbacks
	 * .onScrollChanged(mObservableScrollView .getScrollY()); mMaxScrollY =
	 * mObservableScrollView .computeVerticalScrollRange() -
	 * mObservableScrollView.getHeight(); mQuickReturnHeight =
	 * mStickyView.getHeight(); } }); }
	 */

	private void initStoreInfo(long storeId) {
		// Get StoreInfo pass from MainActivity
		if (storeId > 0) {
			// has store
			storeInfo = storeInfoDAO.get(storeId);
			photoComments = photoCommentDAO.getStorePhotoComments(storeId);
			float ratingAtmos = storeInfo.getRatingAtmos();
			float ratingClean = storeInfo.getRatingClean();
			float ratingFlavor = storeInfo.getRatingFlavor();
			float ratingService = storeInfo.getRatingService();
			float ratingUnknow = storeInfo.getRatingUnkonw();
			float total = (ratingAtmos + ratingClean + ratingFlavor
					+ ratingService + ratingUnknow) / 5;
			this.ratingClean.setRating(ratingClean);
			this.ratingService.setRating(ratingService);
			this.ratingAtmos.setRating(ratingAtmos);
			this.ratingFavor.setRating(ratingFlavor);
			this.ratingUnknow.setRating(ratingUnknow);
			storeInfoTotalRate
					.setText(String.valueOf((Math.rint(total * 10) / 10)));
			storeInfoAddress.setText(storeInfo.getAddress());
			storeInfoPhone.setText(storeInfo.getPhone());
			getActionBar().setTitle(storeInfo.getName());
			MyLog.d(StoreInfoActivity.class,
					"init is favor:" + storeInfo.isFavor());
			MyLog.d(StoreInfoActivity.class,
					"init is block:" + storeInfo.isBlocked());
			if (storeInfo.getPhone() != null && !storeInfo.getPhone().isEmpty()) {
				storePhoneCall.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent intent = new Intent(Intent.ACTION_CALL);
						intent.setData(Uri.parse("tel:" + storeInfo.getPhone()));
						startActivity(intent);
					}
				});
			}

			// TODO show block view
			if (photoComments.size() == 0) {
				storeTakePicture.setVisibility(View.VISIBLE);
			} else {
				storeTakePicture.setVisibility(View.GONE);
			}
			getAllData();
			mStoreInfoPicAdapter.notifyDataSetChanged();
		} else {
			// TODO: Fatal error for no store
			MyLog.e(StoreInfoActivity.class, "[Fatal Error] No Store");
		}
	}

	private void getAllData() {
		for (PhotoComment photoComment : photoComments) {
			Bundle data = new Bundle();
			data.putString(StoreInfoPicFragment.STORE_INFO_PIC_URI,
					VolleyTaskManager.getInstance().getMediaRoot()
							+ photoComment.getUri().toString());
			data.putString(StoreInfoPicFragment.STORE_INFO_PIC_COMMENT,
					photoComment.getComment());
			Fragment fragment = new StoreInfoPicFragment();
			fragment.setArguments(data);
			storeInfoPicFragments.add(fragment);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.store_info, menu);
		boolean isCreate = super.onCreateOptionsMenu(menu);
		this.menu = menu;
		MyLog.d(StoreInfoActivity.class, "menu is favor:" + storeInfo.isFavor());
		MyLog.d(StoreInfoActivity.class,
				"menu is block:" + storeInfo.isBlocked());
		if (storeInfo.isFavor()) {
			menu.findItem(R.id.action_focused).setVisible(true);
			menu.findItem(R.id.action_blocked).setVisible(false);
		}
		if (storeInfo.isBlocked()) {
			menu.findItem(R.id.action_blocked).setVisible(true);
			menu.findItem(R.id.action_focused).setVisible(false);
		}
		return isCreate;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		switch (item.getItemId()) {
		case R.id.action_take_picture:
			Intent intentTakePicture = new Intent();
			intentTakePicture.setClass(StoreInfoActivity.this,
					StoreInfoTakePictureActivity.class);
			intentTakePicture.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
			intentTakePicture.putExtra(STORE_ID_TAG, storeId);
			startActivityForResult(intentTakePicture,
					STORE_INFO_TAKE_PICTURE_RESULT);
			overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
			return true;
		case R.id.action_feedback:
			Intent intent = new Intent();
			intent.setClass(StoreInfoActivity.this,
					StoreInfoFeedbackActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
			intent.putExtra(STORE_ID_TAG, storeId);
			startActivityForResult(intent, STORE_INFO_FEEDBACK_RESULT);
			overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
			return true;
		case R.id.action_focused:
			storeInfo.setFavor(!storeInfo.isFavor());
			if (storeInfo.isFavor()) {
				menu.findItem(R.id.action_blocked).setVisible(false);
			} else {
				menu.findItem(R.id.action_blocked).setVisible(true);
			}
			MyLog.d(StoreInfoActivity.class,
					"focused is favor:" + storeInfo.isFavor());
			MyLog.d(StoreInfoActivity.class,
					"focused is block:" + storeInfo.isBlocked());
			storeInfoDAO.updateIsBlocked(storeInfo);
			return true;
		case R.id.action_blocked:
			storeInfo.setBlocked(!storeInfo.isBlocked());
			if (storeInfo.isBlocked()) {
				// TODO add animation for blocked
				menu.findItem(R.id.action_focused).setVisible(false);
			} else {
				menu.findItem(R.id.action_focused).setVisible(true);
			}
			MyLog.d(StoreInfoActivity.class,
					"block is favor:" + storeInfo.isFavor());
			MyLog.d(StoreInfoActivity.class,
					"block is block:" + storeInfo.isBlocked());
			storeInfoDAO.updateIsBlocked(storeInfo);
			return true;
		case R.id.action_report:
			Intent intentReport = new Intent();
			intentReport.setAction(FoundNewStoreActivity.REPORT_ERROR);
			intentReport.setClass(this, FoundNewStoreActivity.class);
			intentReport.putExtra(STORE_ID_TAG, storeId);
			startActivity(intentReport);
			return true;
		case android.R.id.home:
			finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case STORE_INFO_FEEDBACK_RESULT:
			if (resultCode == RESULT_OK) {
				Bundle ratingData = data
						.getBundleExtra(StoreInfoFeedbackActivity.STORE_INFO_FEEDBACK_RATING);
				String comment = data
						.getStringExtra(StoreInfoFeedbackActivity.STORE_INFO_FEEDBACK_COMMENT);
				float ratingClean = ratingData
						.getFloat(RatingFragment.RATING_CLAEN);
				float ratingService = ratingData
						.getFloat(RatingFragment.RATING_SERVICE);
				float ratingAtmos = ratingData
						.getFloat(RatingFragment.RATING_ATMOS);
				float ratingFavor = ratingData
						.getFloat(RatingFragment.RATING_FAVOR);
				float ratingUnknow = ratingData
						.getFloat(RatingFragment.RATING_UNKNOW);

				// Update the feedback point by service
				Intent intentFbpUpdate = new Intent();
				intentFbpUpdate.setClass(getApplicationContext(),
						FeedbackUpdateService.class);
				intentFbpUpdate.setAction(FeedbackUpdateService.UPDATE);
				FeedbackPointDAO fpDAO = new FeedbackPointDAO(
						getApplicationContext());
				fpDAO.open();
				FeedbackPoint fbp = fpDAO.get().get(0);
				if (comment != null && !comment.isEmpty()) {
					fbp.setCommentFeedback(fbp.getCommentFeedback() + 1);
				}

				if (ratingClean != this.storeInfo.getRatingClean()
						&& ratingAtmos != this.storeInfo.getRatingAtmos()
						&& ratingFavor != this.storeInfo.getRatingFlavor()
						&& ratingService != this.storeInfo.getRatingService()
						&& ratingUnknow != this.storeInfo.getRatingUnkonw()) {
					fbp.setRatingFeedback(fbp.getRatingFeedback() + 1);
				}

				intentFbpUpdate.putExtras(fbp.save());
				startService(intentFbpUpdate);

				try {
					JSONObject requestBody = RatingFeedbackRequest
							.getInstance().createFeedbackJson(storeId,
									ratingClean, ratingService, ratingAtmos,
									ratingFavor, ratingUnknow, comment);
					RatingFeedbackRequest.getInstance().sendFeedback(
							getApplicationContext(), requestBody,
							new Response.Listener<JSONObject>() {

								@Override
								public void onResponse(JSONObject response) {
									MyLog.d(StoreInfoActivity.class, ""
											+ response.toString());
								}
							}, new Response.ErrorListener() {

								@Override
								public void onErrorResponse(VolleyError error) {
									MyLog.d(StoreInfoActivity.class,
											"" + error.toString());
								}
							});
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
			}
			break;
		case STORE_INFO_TAKE_PICTURE_RESULT:
			if (resultCode == RESULT_OK) {

				String[] photoCommentData = data
						.getStringArrayExtra(StoreInfoTakePictureActivity.STORE_INFO_FEEDBACK_PHOTO_COMMENT);
				for (String photo : photoCommentData) {
					try {
						JSONObject photoJson = new JSONObject(photo);
						MyLog.d(StoreInfoActivity.class, photoJson.toString());
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			}
			break;

		default:
			break;
		}
	}

	/*
	 * private ObservableScrollView.Callbacks mObservableScrollViewCallbacks =
	 * new ObservableScrollView.Callbacks() {
	 * 
	 * @Override public void onScrollChanged(int scrollY) { scrollY =
	 * Math.min(mMaxScrollY, scrollY); mScrollSettleHandler.onScroll(scrollY);
	 * int rawY = mPlaceholderView.getTop() - scrollY; int translationY = 0;
	 * 
	 * switch (mState) { case STATE_OFFSCREEN: if (rawY <= mMinRawY) { mMinRawY
	 * = rawY; } else { mState = STATE_RETURNING; } translationY = rawY; break;
	 * 
	 * case STATE_ONSCREEN: if (rawY < -mQuickReturnHeight) { mState =
	 * STATE_OFFSCREEN; mMinRawY = rawY; } translationY = rawY; break;
	 * 
	 * case STATE_RETURNING: translationY = (rawY - mMinRawY) -
	 * mQuickReturnHeight; if (translationY > 0) { translationY = 0; mMinRawY =
	 * rawY - mQuickReturnHeight; }
	 * 
	 * if (rawY > 0) { mState = STATE_ONSCREEN; translationY = rawY; }
	 * 
	 * if (translationY < -mQuickReturnHeight) { mState = STATE_OFFSCREEN;
	 * mMinRawY = rawY; } break; } mStickyView.animate().cancel();
	 * mStickyView.setTranslationY(translationY + scrollY); }
	 * 
	 * @Override public void onDownMotionEvent() {
	 * mScrollSettleHandler.setSettleEnabled(false); }
	 * 
	 * @Override public void onUpOrCancelMotionEvent() {
	 * mScrollSettleHandler.setSettleEnabled(true);
	 * mScrollSettleHandler.onScroll(mObservableScrollView.getScrollY()); } };
	 * 
	 * private class ScrollSettleHandler extends HandlerCallBack { private
	 * static final int SETTLE_DELAY_MILLIS = 100;
	 * 
	 * private int mSettledScrollY = Integer.MIN_VALUE; private boolean
	 * mSettleEnabled;
	 * 
	 * public void onScroll(int scrollY) { if (mSettledScrollY != scrollY) { //
	 * Clear any pending messages and post delayed
	 * dataHandler.removeMessages(0); dataHandler.sendEmptyMessageDelayed(0,
	 * SETTLE_DELAY_MILLIS); mSettledScrollY = scrollY; } }
	 * 
	 * public void setSettleEnabled(boolean settleEnabled) { mSettleEnabled =
	 * settleEnabled; }
	 * 
	 * @Override public void handleDataMessage(Message msg) { // Handle the
	 * scroll settling. if (STATE_RETURNING == mState && mSettleEnabled) { int
	 * mDestTranslationY; if (mSettledScrollY - mStickyView.getTranslationY() >
	 * mQuickReturnHeight / 2) { mState = STATE_OFFSCREEN; mDestTranslationY =
	 * Math.max(mSettledScrollY - mQuickReturnHeight,
	 * mPlaceholderView.getTop()); } else { mDestTranslationY = mSettledScrollY;
	 * }
	 * 
	 * mMinRawY = mPlaceholderView.getTop() - mQuickReturnHeight -
	 * mDestTranslationY; mStickyView.animate().translationY(mDestTranslationY);
	 * } mSettledScrollY = Integer.MIN_VALUE; // reset
	 * 
	 * } }
	 */

}