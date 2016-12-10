package edu.utboy.biteit.asynctasks;

import idv.andylin.tw.andyandroidlibs.utils.MyLog;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
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
import edu.utboy.biteit.models.StoreInfo;
import edu.utboy.biteit.utils.AuthToken;

public class StoreFriendsRequest {

	/**
	 * static Singleton instance
	 */
	private static StoreFriendsRequest mStoreFriendsRequest;
	private VolleyTaskManager mVolleyTaskManager;
	private String url;

	/**
	 * Private constructor for singleton
	 */
	private StoreFriendsRequest() {
		mVolleyTaskManager = VolleyTaskManager.getInstance();
	}

	/**
	 * Static getter method for retrieving the singleton instance
	 */
	public static StoreFriendsRequest getInstance() {
		if (mStoreFriendsRequest == null) {
			synchronized (StoreFriendsRequest.class) {
				if (mStoreFriendsRequest == null) {
					mStoreFriendsRequest = new StoreFriendsRequest();
				}
			}
		}
		return mStoreFriendsRequest;
	}

	/**
	 * Use PhotoCommentRequest to judge the user id.
	 */
	@Deprecated
	public void storeFriendsRequest(Context context, JSONObject requestBody,
			Response.Listener<JSONObject> responseListener,
			Response.ErrorListener errorListener) {
		if (isInit()) {
			url = mVolleyTaskManager.getServerUrl()
					+ mVolleyTaskManager.getApiStorePhotoComment();
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

	public JSONObject getStoreFriendJsonObject(List<Friend> friends,
			List<StoreInfo> canViewStoreInfos) throws JSONException {
		JSONObject json = new JSONObject();
		JSONArray jsonFriendsArray = new JSONArray();
		for (Friend friend : friends) {
			JSONObject jsonFriend = new JSONObject();
			jsonFriend.put("username", friend.getUsername());
			jsonFriendsArray.put(jsonFriend);
		}
		JSONArray jsonStoreInfoArray = new JSONArray();
		for (StoreInfo storeInfo : canViewStoreInfos) {
			JSONObject jsonStoreInfo = new JSONObject();
			jsonStoreInfo.put("store_id", storeInfo.getStoreId());
			jsonStoreInfoArray.put(jsonStoreInfo);
		}
		json.put("firends", jsonFriendsArray);
		json.put("store_infos", jsonStoreInfoArray);
		return json;
	}

	public JSONObject getQueryAll() {
		return new JSONObject();
	}

	private boolean isInit() {
		if (mVolleyTaskManager.getServerUrl() == null) {
			MyLog.e(StoreFriendsRequest.class,
					"VolleyTaskManager needs to be init before using request!");
			return false;
		}
		return true;
	}

}
