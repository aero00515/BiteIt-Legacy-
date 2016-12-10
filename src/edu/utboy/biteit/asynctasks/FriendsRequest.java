package edu.utboy.biteit.asynctasks;

import idv.andylin.tw.andyandroidlibs.utils.MyLog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import edu.utboy.biteit.asynctasks.requestqueues.SampleRequestQueue;
import edu.utboy.biteit.models.Friend;
import edu.utboy.biteit.utils.AuthToken;

public class FriendsRequest {

	/**
	 * static Singleton instance
	 */
	private static FriendsRequest mFriendsRequest;
	private VolleyTaskManager mVolleyTaskManager;

	/**
	 * Private constructor for singleton
	 */
	private FriendsRequest() {
		mVolleyTaskManager = VolleyTaskManager.getInstance();
	}

	/**
	 * Static getter method for retrieving the singleton instance
	 */
	public static FriendsRequest getInstance() {
		if (mFriendsRequest == null) {
			synchronized (FriendsRequest.class) {
				if (mFriendsRequest == null) {
					mFriendsRequest = new FriendsRequest();
				}
			}
		}
		return mFriendsRequest;
	}

	public void updateFriendInfo(Context context, long id,
			JSONObject requestBody,
			Response.Listener<JSONObject> responseListener,
			Response.ErrorListener errorListener) {
		if (isInit()) {
			String url = mVolleyTaskManager.getServerUrl()
					+ mVolleyTaskManager.getApiFriend() + "/" + id;
			JsonObjectRequest jsonRequest = new JsonObjectRequest(
					Request.Method.PUT, url, requestBody, responseListener,
					errorListener) {
				@Override
				public Map<String, String> getHeaders() throws AuthFailureError {
					HashMap<String, String> maps = new HashMap<String, String>();
					String token = AuthToken.getAuthToken();
					if (!token.isEmpty()) {
						maps.put("Authorization", "Token " + token);
					} else {
						VolleyLog.e("string",
								"token null: plz check auth again!");
					}
					return maps;
				}
			};
			SampleRequestQueue.getSampleRequestQueue(context)
					.addToRequestQueue(jsonRequest);
		}
	}

	public void addFriend(Context context, JSONObject requestBody,
			Response.Listener<JSONObject> responseListener,
			Response.ErrorListener errorListener) {
		if (isInit()) {
			String url = mVolleyTaskManager.getServerUrl()
					+ mVolleyTaskManager.getApiFriend() + "/";
			JsonObjectRequest jsonRequest = new JsonObjectRequest(
					Request.Method.POST, url, requestBody, responseListener,
					errorListener) {
				@Override
				public Map<String, String> getHeaders() throws AuthFailureError {
					HashMap<String, String> maps = new HashMap<String, String>();
					String token = AuthToken.getAuthToken();
					if (!token.isEmpty()) {
						maps.put("Authorization", "Token " + token);
					} else {
						VolleyLog.e("string",
								"token null: plz check auth again!");
					}
					return maps;
				}
			};
			SampleRequestQueue.getSampleRequestQueue(context)
					.addToRequestQueue(jsonRequest);
		}
	}

	public void getFriendRequest(Context context, long id,
			Response.Listener<JSONObject> responseListener,
			Response.ErrorListener errorListener) {
		if (isInit()) {
			String url = mVolleyTaskManager.getServerUrl()
					+ mVolleyTaskManager.getApiFriend() + "/" + id;
			JsonObjectRequest jsonRequest = new JsonObjectRequest(
					Request.Method.GET, url, null, responseListener,
					errorListener) {
				@Override
				public Map<String, String> getHeaders() throws AuthFailureError {
					HashMap<String, String> maps = new HashMap<String, String>();
					String token = AuthToken.getAuthToken();
					if (!token.isEmpty()) {
						maps.put("Authorization", "Token " + token);
					} else {
						VolleyLog.e("string",
								"token null: plz check auth again!");
					}
					return maps;
				}
			};
			SampleRequestQueue.getSampleRequestQueue(context)
					.addToRequestQueue(jsonRequest);
		}
	}

	public JSONObject createFriendJsonObject(long userId,
			long friendId, boolean isFocused, boolean isBlocked)
			throws JSONException {
		JSONObject json = new JSONObject();
		json.put("user_id", userId);
		json.put("friend_id", friendId);
		json.put("is_focused", isFocused);
		json.put("is_blocked", isBlocked);
		return json;
	}

	public JSONObject getQueryAll() {
		return new JSONObject();
	}

	public List<Friend> getFriendsUsingResponse(JSONObject response) {
		List<Friend> friends = new ArrayList<Friend>();
		// TODO fill friend objects
		return friends;
	}

	private boolean isInit() {
		if (mVolleyTaskManager.getServerUrl() == null) {
			MyLog.e(FriendsRequest.class,
					"VolleyTaskManager needs to be init before using request!");
			return false;
		}
		return true;
	}

}
