package edu.utboy.biteit.ui.drawer;

import idv.andylin.tw.andyandroidlibs.utils.MyLog;

import java.util.List;

import android.net.Uri;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import edu.utboy.biteit.R;
import edu.utboy.biteit.asynctasks.FBPStatusRequest;
import edu.utboy.biteit.models.FeedbackPoint;
import edu.utboy.biteit.models.dao.FeedbackPointDAO;
import edu.utboy.biteit.utils.AuthUser;
import edu.utboy.biteit.utils.BaseActivity;

public class AchievementsActivity extends BaseActivity {

	private FeedbackPointDAO feedbackPointDAO;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_achievements);
		feedbackPointDAO = new FeedbackPointDAO(getApplicationContext());
		feedbackPointDAO.open();

		final WebView mFBPStatusImage = (WebView) findViewById(R.id.activity_achievements_status_image);
		TextView foundNewTimes = (TextView) findViewById(R.id.new_stort_times);
		TextView fillEmptyTimes = (TextView) findViewById(R.id.fill_empty_times);
		TextView reportErrorTimes = (TextView) findViewById(R.id.report_error_times);
		TextView ratingFeedbackTimes = (TextView) findViewById(R.id.rating_feedback_times);
		TextView photoFeedbackTimes = (TextView) findViewById(R.id.photo_feedback_times);
		TextView commentFeedbackTimes = (TextView) findViewById(R.id.comment_feedback_times);

		WebSettings webSetting = mFBPStatusImage.getSettings();
		webSetting.setLoadWithOverviewMode(true);
		webSetting.setUseWideViewPort(true);
		webSetting.setBuiltInZoomControls(true);
		webSetting.setDisplayZoomControls(false);

		List<FeedbackPoint> fbps = feedbackPointDAO.get();
		if (fbps.size() == 0) {
			// TODO [Fatal Error] means user hasn't create at first reg.
			// Shouldn't happen.
			MyLog.e(AchievementsActivity.class,
					"[Fatal Error] means user hasn't create at first reg.");

			// TODO remove this statement when publish
			feedbackPointDAO.save(new FeedbackPoint(0, 0, 0, 0, 0, 0));
		} else {
			FBPStatusRequest.getInstance().getFeedbackPointStatusRequest(
					getApplicationContext(), AuthUser.getId(),
					new Response.Listener<String>() {

						@Override
						public void onResponse(String response) {
							if (response != null && !response.contains("error")) {
								MyLog.d(AchievementsActivity.class, response);
								response = response.replaceAll("\"", "");
								mFBPStatusImage.loadUrl(Uri.parse(
										response + "&chdl=First|Prev|You")
										.toString());
							} else {
								MyLog.e(AchievementsActivity.class, ""
										+ response);
							}
						}
					}, new Response.ErrorListener() {

						@Override
						public void onErrorResponse(VolleyError error) {
							MyLog.e(AchievementsActivity.class, new String(
									error.networkResponse.data));
						}
					});
			FeedbackPoint feedbackPoint = fbps.get(0);
			foundNewTimes.setText(String.valueOf(feedbackPoint.getNewStore()));
			fillEmptyTimes
					.setText(String.valueOf(feedbackPoint.getFillEmpty()));
			reportErrorTimes.setText(String.valueOf(feedbackPoint
					.getReportError()));
			ratingFeedbackTimes.setText(String.valueOf(feedbackPoint
					.getRatingFeedback()));
			photoFeedbackTimes.setText(String.valueOf(feedbackPoint
					.getPhotoFeedback()));
			commentFeedbackTimes.setText(String.valueOf(feedbackPoint
					.getCommentFeedback()));
		}

	}

}
