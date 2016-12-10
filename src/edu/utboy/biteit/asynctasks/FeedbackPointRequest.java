package edu.utboy.biteit.asynctasks;

import idv.andylin.tw.andyandroidlibs.utils.MyLog;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;

import edu.utboy.biteit.asynctasks.requestqueues.SampleRequestQueue;
import edu.utboy.biteit.asynctasks.requests.MyJsonObjectRequest;

public class FeedbackPointRequest {

	/**
	 * static Singleton instance
	 */
	private static FeedbackPointRequest mFeedbackPointRequest;
	private VolleyTaskManager mVolleyTaskManager;

	/**
	 * Private constructor for singleton
	 */
	private FeedbackPointRequest() {
		mVolleyTaskManager = VolleyTaskManager.getInstance();
	}

	/**
	 * Static getter method for retrieving the singleton instance
	 */
	public static FeedbackPointRequest getInstance() {
		if (mFeedbackPointRequest == null) {
			synchronized (FeedbackPointRequest.class) {
				if (mFeedbackPointRequest == null) {
					mFeedbackPointRequest = new FeedbackPointRequest();
				}
			}
		}
		return mFeedbackPointRequest;
	}

	public void updateFeedbackPoint(Context context, long id,
			JSONObject requestBody,
			Response.Listener<JSONObject> responseListener,
			Response.ErrorListener errorListener) {
		if (isInit()) {
			String url = mVolleyTaskManager.getServerUrl()
					+ mVolleyTaskManager.getApiFeedbackPoint() + "/" + id + "/";
			MyLog.d(FeedbackPointRequest.class, "[url] " + url);
			MyLog.d(FeedbackPointRequest.class,
					"[request body] " + requestBody.toString());
			MyJsonObjectRequest jsonRequest = new MyJsonObjectRequest(
					Request.Method.PUT, url, requestBody, responseListener,
					errorListener);
			SampleRequestQueue.getSampleRequestQueue(context)
					.addToRequestQueue(jsonRequest);
		}
	}

	public void addFeedbackPoint(Context context, JSONObject requestBody,
			Response.Listener<JSONObject> responseListener,
			Response.ErrorListener errorListener) {
		if (isInit()) {
			String url = mVolleyTaskManager.getServerUrl()
					+ mVolleyTaskManager.getApiFeedbackPoint() + "/";
			MyJsonObjectRequest jsonRequest = new MyJsonObjectRequest(
					Request.Method.POST, url, requestBody, responseListener,
					errorListener);
			SampleRequestQueue.getSampleRequestQueue(context)
					.addToRequestQueue(jsonRequest);
		}
	}

	public void getFeedbackPoint(Context context,
			Response.Listener<JSONObject> responseListener,
			Response.ErrorListener errorListener) {
		if (isInit()) {
			String url = mVolleyTaskManager.getServerUrl()
					+ mVolleyTaskManager.getApiFeedbackPoint() + "/";
			MyJsonObjectRequest jsonRequest = new MyJsonObjectRequest(
					Request.Method.GET, url, null, responseListener,
					errorListener);
			SampleRequestQueue.getSampleRequestQueue(context)
					.addToRequestQueue(jsonRequest);
		}
	}

	public void getUserFeedbackPoint(Context context, long id,
			Response.Listener<JSONObject> responseListener,
			Response.ErrorListener errorListener) {
		if (isInit()) {
			String url = mVolleyTaskManager.getServerUrl()
					+ mVolleyTaskManager.getApiFeedbackPoint() + "/" + id;
			MyJsonObjectRequest jsonRequest = new MyJsonObjectRequest(
					Request.Method.GET, url, null, responseListener,
					errorListener);
			SampleRequestQueue.getSampleRequestQueue(context)
					.addToRequestQueue(jsonRequest);
		}
	}

	public JSONObject createFeedbackPointRequestBody(int userId, int newStore,
			int fillEmpty, int reportError, int ratingFeedback,
			int photoFeedback, int commentFeedback) throws JSONException {
		int totalScore = newStore * VolleyTaskManager.STORE_RATIO + fillEmpty
				* VolleyTaskManager.FILL_RATIO + reportError
				* VolleyTaskManager.REPORT_RATIO + ratingFeedback
				* VolleyTaskManager.RATING_RATIO + photoFeedback
				* VolleyTaskManager.PHOTO_RATIO + commentFeedback
				* VolleyTaskManager.COMMENT_RATIO;
		JSONObject json = new JSONObject();
		json.put("user_id", userId);
		json.put("new_store", newStore);
		json.put("fill_empty", fillEmpty);
		json.put("report_error", reportError);
		json.put("rating_feedback", ratingFeedback);
		json.put("photo_feedback", photoFeedback);
		json.put("comment_feedback", commentFeedback);
		json.put("total_score", totalScore);
		return json;
	}

	private boolean isInit() {
		if (mVolleyTaskManager.getServerUrl() == null) {
			MyLog.e(FeedbackPointRequest.class,
					"VolleyTaskManager needs to be init before using request!");
			return false;
		}
		return true;
	}

}
