package edu.utboy.biteit.asynctasks;

import idv.andylin.tw.andyandroidlibs.utils.MyLog;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;

import edu.utboy.biteit.asynctasks.requestqueues.SampleRequestQueue;
import edu.utboy.biteit.asynctasks.requests.MyJsonObjectRequest;
import edu.utboy.biteit.utils.AuthUser;

public class RatingFeedbackRequest {

	/**
	 * static Singleton instance
	 */
	private static RatingFeedbackRequest mRatingFeedbackRequest;
	private VolleyTaskManager mVolleyTaskManager;

	/**
	 * Private constructor for singleton
	 */
	private RatingFeedbackRequest() {
		mVolleyTaskManager = VolleyTaskManager.getInstance();
	}

	/**
	 * Static getter method for retrieving the singleton instance
	 */
	public static RatingFeedbackRequest getInstance() {
		if (mRatingFeedbackRequest == null) {
			synchronized (RatingFeedbackRequest.class) {
				if (mRatingFeedbackRequest == null) {
					mRatingFeedbackRequest = new RatingFeedbackRequest();
				}
			}
		}
		return mRatingFeedbackRequest;
	}

	public void sendFeedback(Context context, JSONObject requestBody,
			Response.Listener<JSONObject> responseListener,
			Response.ErrorListener errorListener) {
		if (isInit()) {
			String url = mVolleyTaskManager.getServerUrl()
					+ mVolleyTaskManager.getApiFeedback() + "/";
			MyJsonObjectRequest jsonRequest = new MyJsonObjectRequest(
					Request.Method.POST, url, requestBody, responseListener,
					errorListener);
			SampleRequestQueue.getSampleRequestQueue(context)
					.addToRequestQueue(jsonRequest);
		}
	}

	public JSONObject createFeedbackJson(long storeId, float ratingClean,
			float ratingService, float ratingAtmos, float ratingFlavor,
			float ratingUnknow, String comment) throws JSONException {
		JSONObject json = new JSONObject();
		json.put("store_id", storeId);
		json.put("user_id", AuthUser.getId());
		json.put("ratingClean", ratingClean);
		json.put("ratingService", ratingService);
		json.put("ratingAtmos", ratingAtmos);
		json.put("ratingFlavor", ratingFlavor);
		json.put("ratingUnknow", ratingUnknow);
		json.put("comment", comment);
		json.put("attend_no", 0);
		return json;
	}

	private boolean isInit() {
		if (mVolleyTaskManager.getServerUrl() == null) {
			MyLog.e(RatingFeedbackRequest.class,
					"VolleyTaskManager needs to be init before using request!");
			return false;
		}
		return true;
	}

}
