package edu.utboy.biteit.asynctasks;

import idv.andylin.tw.andyandroidlibs.utils.MyLog;

import java.util.HashMap;
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
import edu.utboy.biteit.asynctasks.requests.MyJsonObjectRequest;
import edu.utboy.biteit.utils.AuthToken;

public class UserDetailRequest {
	/**
	 * static Singleton instance
	 */
	private static UserDetailRequest instance;
	private VolleyTaskManager mVolleyTaskManager;

	/**
	 * Private constructor for singleton
	 */
	private UserDetailRequest() {
		mVolleyTaskManager = VolleyTaskManager.getInstance();
	}

	/**
	 * Static getter method for retrieving the singleton instance
	 */
	public static UserDetailRequest getInstance() {
		if (instance == null) {
			synchronized (UserDetailRequest.class) {
				if (instance == null) {
					instance = new UserDetailRequest();
				}
			}
		}
		return instance;
	}

	public void getUserDetail(final Context context,
			final Response.Listener<JSONObject> requestListener,
			final Response.ErrorListener errorListener) {
		if (isInit()) {
			String url = mVolleyTaskManager.getServerUrl()
					+ mVolleyTaskManager.getApiUserDetail() + "/";
			MyJsonObjectRequest jsonRequest = new MyJsonObjectRequest(
					Request.Method.GET, url, null, requestListener,
					errorListener);
			SampleRequestQueue.getSampleRequestQueue(context)
					.addToRequestQueue(jsonRequest);
		}
	}

	public void getUserDetail(final Context context, final long id,
			final Response.Listener<JSONObject> requestListener,
			final Response.ErrorListener errorListener) {
		if (isInit()) {
			String url = mVolleyTaskManager.getServerUrl()
					+ mVolleyTaskManager.getApiUserDetail() + "/" + id;
			MyJsonObjectRequest jsonRequest = new MyJsonObjectRequest(
					Request.Method.GET, url, null, requestListener,
					errorListener);
			SampleRequestQueue.getSampleRequestQueue(context)
					.addToRequestQueue(jsonRequest);
		}
	}

	public void updateDetail(final Context context, final long id,
			final JSONObject userDetailRequestBody,
			final Response.Listener<JSONObject> requestListener,
			final Response.ErrorListener errorListener) {
		if (isInit()) {
			String url = mVolleyTaskManager.getServerUrl()
					+ mVolleyTaskManager.getApiUserDetail() + "/" + id;
			JsonObjectRequest jsonRequest = new JsonObjectRequest(
					Request.Method.PUT, url, userDetailRequestBody,
					requestListener, errorListener) {
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

	public JSONObject createUserDetailJsonObject(String gcmId)
			throws JSONException {
		JSONObject json = new JSONObject();
		json.put("gcm_id", gcmId);
		return json;
	}

	public JSONObject createUserDetailJsonObject(String loginType,
			String thirdId, String gcmId) throws JSONException {
		JSONObject json = new JSONObject();
		json.put("login_type", loginType);
		json.put("third_id", thirdId);
		json.put("gcm_id", gcmId);
		return json;
	}

	private boolean isInit() {
		if (mVolleyTaskManager.getServerUrl() == null) {
			MyLog.e(UserDetailRequest.class,
					"VolleyTaskManager needs to be init before using request!");
			return false;
		}
		return true;
	}
}
