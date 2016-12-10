package edu.utboy.biteit.asynctasks;

import idv.andylin.tw.andyandroidlibs.utils.MyLog;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Bundle;
import android.util.Base64;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import edu.utboy.biteit.SyncUserDataService;
import edu.utboy.biteit.asynctasks.requestqueues.SampleRequestQueue;

public class NewUserRequest {
	/**
	 * static Singleton instance
	 */
	private static NewUserRequest instance;
	private VolleyTaskManager mVolleyTaskManager;

	/**
	 * Private constructor for singleton
	 */
	private NewUserRequest() {
		mVolleyTaskManager = VolleyTaskManager.getInstance();
	}

	/**
	 * Static getter method for retrieving the singleton instance
	 */
	public static NewUserRequest getInstance() {
		if (instance == null) {
			synchronized (NewUserRequest.class) {
				if (instance == null) {
					instance = new NewUserRequest();
				}
			}
		}
		return instance;
	}

	public synchronized void newUser(final Context context,
			final JSONObject userRequestBody,
			final JSONObject userDetailRequestBody,
			final JSONArray userFriends,
			final Response.Listener<JSONObject> requestListener,
			final Response.ErrorListener errorListener) {
		if (isInit()) {
			String url = mVolleyTaskManager.getServerUrl()
					+ mVolleyTaskManager.getApiNewAccount() + "/";
			JsonObjectRequest jsonRequest = new JsonObjectRequest(
					Request.Method.POST, url, userRequestBody,
					new Response.Listener<JSONObject>() {

						@Override
						public void onResponse(JSONObject response) {
							VolleyLog.d(response.toString());
							try {
								userDetailRequestBody.put("user_id",
										response.getInt("id"));
								makeDetail(context, userRequestBody,
										userDetailRequestBody, userFriends,
										requestListener, errorListener);
							} catch (JSONException e) {
								e.printStackTrace();
							}
						}
					}, errorListener);
			SampleRequestQueue.getSampleRequestQueue(context)
					.addToRequestQueue(jsonRequest);
		}
	}

	private void makeDetail(final Context context,
			final JSONObject userRequestBody,
			final JSONObject userDetailRequestBody,
			final JSONArray userFriends,
			final Response.Listener<JSONObject> requestListener,
			final Response.ErrorListener errorListener) {
		String url = mVolleyTaskManager.getServerUrl()
				+ mVolleyTaskManager.getApiUserDetail() + "/";
		JsonObjectRequest jsonRequest = new JsonObjectRequest(
				Request.Method.POST, url, userDetailRequestBody,
				new Response.Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						VolleyLog.d(response.toString());
						try {
							userDetailRequestBody.put("friends", userFriends);
						} catch (JSONException e) {
							e.printStackTrace();
						}
						getAuthToken(context, userRequestBody,
								userDetailRequestBody, requestListener,
								errorListener);
					}
				}, errorListener) {
			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {
				HashMap<String, String> maps = new HashMap<String, String>();
				String content = "";
				try {
					String username = userRequestBody.getString("username");
					String password = userRequestBody.getString("password");
					content = Base64.encodeToString(
							(username + ":" + password).getBytes(),
							Base64.DEFAULT);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				maps.put("Authorization", "Basic " + content);
				return maps;
			}

		};
		SampleRequestQueue.getSampleRequestQueue(context).addToRequestQueue(
				jsonRequest);
	}

	private void getAuthToken(Context context,
			final JSONObject userRequestBody,
			final JSONObject userDetailRequestBody,
			Response.Listener<JSONObject> responseListener,
			Response.ErrorListener errorListener) {
		String url = mVolleyTaskManager.getServerUrl()
				+ mVolleyTaskManager.getAuth() + "/";
		JsonObjectRequest jsonRequest = new JsonObjectRequest(
				Request.Method.POST, url, userDetailRequestBody,
				responseListener, errorListener) {
			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {
				HashMap<String, String> maps = new HashMap<String, String>();
				String content = "";
				try {
					String username = userRequestBody.getString("username");
					String password = userRequestBody.getString("password");
					content = Base64.encodeToString(
							(username + ":" + password).getBytes(),
							Base64.DEFAULT);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				maps.put("Authorization", "Basic " + content);
				return maps;
			}

		};
		SampleRequestQueue.getSampleRequestQueue(context).addToRequestQueue(
				jsonRequest);
	}

	public JSONObject getNewUserRequestBody(String userName, String password,
			String firstName, String lastName, String email)
			throws JSONException {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("username", userName);
		jsonObject.put("password", password);
		jsonObject.put("first_name", firstName);
		jsonObject.put("last_name", lastName);
		jsonObject.put("email", email);
		// jsonObject.put("third",
		// createUserDetailJsonObject(loginType, thirdId, gcmId));
		return jsonObject;
	}

	public JSONObject createUserDetailJsonObject(String loginType,
			String thirdId, String gcmId) throws JSONException {
		JSONObject json = new JSONObject();
		json.put("login_type", loginType);
		json.put("third_id", thirdId);
		json.put("gcm_id", gcmId);
		return json;
	}

	public JSONArray createUserFriendsJsonArray(String[] friends) {
		JSONArray json = new JSONArray();
		for (String friend : friends) {
			json.put(friend);
		}
		return json;
	}

	public Bundle getUserTokenData(JSONObject response, JSONObject userDetail,
			String password) throws JSONException {
		Bundle data = new Bundle();
		JSONObject userInfo = response.getJSONObject("user_info");
		String username = userInfo.getString("username");
		String content = username + ":" + password;

		data.putString(SyncUserDataService.SHARED_PREFERENCED_BASE, content);
		data.putString(SyncUserDataService.SHARED_PREFERENCED_TOKEN,
				response.getString("token"));
		data.putInt(SyncUserDataService.SHARED_PREFERENCED_ID,
				userInfo.getInt("id"));
		data.putString(SyncUserDataService.SHARED_PREFERENCED_USERNAME,
				userInfo.getString("username"));
		data.putString(SyncUserDataService.SHARED_PREFERENCED_NAME,
				userInfo.getString("first_name"));
		data.putString(SyncUserDataService.SHARED_PREFERENCED_EMAIL,
				userInfo.getString("email"));
		data.putString(SyncUserDataService.SHARED_PREFERENCED_LOGIN_TYPE,
				userDetail.getString("login_type"));
		data.putString(SyncUserDataService.SHARED_PREFERENCED_THIRD_ID,
				userDetail.getString("third_id"));
		data.putString(SyncUserDataService.SHARED_PREFERENCED_GCM_ID,
				userDetail.getString("gcm_id"));
		return data;
	}

	private boolean isInit() {
		if (mVolleyTaskManager.getServerUrl() == null) {
			MyLog.e(NewUserRequest.class,
					"VolleyTaskManager needs to be init before using request!");
			return false;
		}
		return true;
	}
}
