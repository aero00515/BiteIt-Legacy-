package edu.utboy.biteit.asynctasks;

import idv.andylin.tw.andyandroidlibs.utils.MyLog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;

import edu.utboy.biteit.asynctasks.requestqueues.SampleRequestQueue;

public class VerifyUserRequest {
	/**
	 * static Singleton instance
	 */
	private static VerifyUserRequest mVerifyUserRequest;
	private VolleyTaskManager mVolleyTaskManager;

	/**
	 * Private constructor for singleton
	 */
	private VerifyUserRequest() {
		mVolleyTaskManager = VolleyTaskManager.getInstance();
	}

	/**
	 * Static getter method for retrieving the singleton instance
	 */
	public static VerifyUserRequest getInstance() {
		if (mVerifyUserRequest == null) {
			synchronized (VerifyUserRequest.class) {
				if (mVerifyUserRequest == null) {
					mVerifyUserRequest = new VerifyUserRequest();
				}
			}
		}
		return mVerifyUserRequest;
	}

	public void verifyUser(Context context, JSONObject requestBody,
			Response.Listener<JSONObject> responseListener,
			Response.ErrorListener errorListener) {
		if (isInit()) {
			String url = mVolleyTaskManager.getServerUrl()
					+ mVolleyTaskManager.getApiVerifyUser() + "/";
			JsonObjectRequest jsonRequest = new JsonObjectRequest(
					Request.Method.POST, url, requestBody, responseListener,
					errorListener);
			SampleRequestQueue.getSampleRequestQueue(context)
					.addToRequestQueue(jsonRequest);
		}
	}

	public JSONObject getVerifyUserJson(Bundle newUserData)
			throws JSONException {
		String name = newUserData.getString("first_name");
		String email = newUserData.getString("email");
		String thirdId = newUserData.getString("third_id");
		String loginType = newUserData.getString("login_type");
		String[] friends = newUserData.getStringArray("friends");
		JSONArray userFriends = NewUserRequest.getInstance()
				.createUserFriendsJsonArray(friends);
		JSONObject json = new JSONObject();
		json.put("first_name", name);
		json.put("email", email);
		json.put("third_id", thirdId);
		json.put("login_type", loginType);
		json.put("friends", userFriends);
		return json;
	}

	private boolean isInit() {
		if (mVolleyTaskManager.getServerUrl() == null) {
			MyLog.e(VerifyUserRequest.class,
					"VolleyTaskManager needs to be init before using request!");
			return false;
		}
		return true;
	}

}
