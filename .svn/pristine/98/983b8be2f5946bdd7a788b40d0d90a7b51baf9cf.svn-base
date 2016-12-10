package edu.utboy.biteit.asynctasks;

import idv.andylin.tw.andyandroidlibs.utils.MyLog;

import org.json.JSONObject;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;

import edu.utboy.biteit.asynctasks.requestqueues.SampleRequestQueue;
import edu.utboy.biteit.asynctasks.requests.MyJsonObjectRequest;

public class UserBasicRequest {

	/**
	 * static Singleton instance
	 */
	private static UserBasicRequest mUserBasicRequest;
	private VolleyTaskManager mVolleyTaskManager;

	/**
	 * Private constructor for singleton
	 */
	private UserBasicRequest() {
		mVolleyTaskManager = VolleyTaskManager.getInstance();
	}

	/**
	 * Static getter method for retrieving the singleton instance
	 */
	public static UserBasicRequest getInstance() {
		if (mUserBasicRequest == null) {
			synchronized (UserBasicRequest.class) {
				if (mUserBasicRequest == null) {
					mUserBasicRequest = new UserBasicRequest();
				}
			}
		}
		return mUserBasicRequest;
	}

	public void getUserBasic(Context context, long id,
			Response.Listener<JSONObject> responseListener,
			Response.ErrorListener errorListener) {
		if (isInit()) {
			String url = mVolleyTaskManager.getServerUrl()
					+ mVolleyTaskManager.getApiUserBasic() + "/" + id;
			MyJsonObjectRequest jsonRequest = new MyJsonObjectRequest(
					Request.Method.GET, url, null, responseListener,
					errorListener);
			SampleRequestQueue.getSampleRequestQueue(context)
					.addToRequestQueue(jsonRequest);
		}
	}

	private boolean isInit() {
		if (mVolleyTaskManager.getServerUrl() == null) {
			MyLog.e(UserBasicRequest.class,
					"VolleyTaskManager needs to be init before using request!");
			return false;
		}
		return true;
	}

}
