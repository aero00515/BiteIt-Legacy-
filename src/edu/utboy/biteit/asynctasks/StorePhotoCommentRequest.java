package edu.utboy.biteit.asynctasks;

import idv.andylin.tw.andyandroidlibs.utils.MyLog;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import edu.utboy.biteit.asynctasks.requestqueues.SampleRequestQueue;
import edu.utboy.biteit.utils.AuthToken;

public class StorePhotoCommentRequest {

	/**
	 * static Singleton instance
	 */
	private static StorePhotoCommentRequest mVerifyUserRequest;
	private VolleyTaskManager mVolleyTaskManager;
	private String url;

	/**
	 * Private constructor for singleton
	 */
	private StorePhotoCommentRequest() {
		mVolleyTaskManager = VolleyTaskManager.getInstance();
	}

	/**
	 * Static getter method for retrieving the singleton instance
	 */
	public static StorePhotoCommentRequest getInstance() {
		if (mVerifyUserRequest == null) {
			synchronized (StorePhotoCommentRequest.class) {
				if (mVerifyUserRequest == null) {
					mVerifyUserRequest = new StorePhotoCommentRequest();
				}
			}
		}
		return mVerifyUserRequest;
	}

	/**
	 * @param context
	 * @param storeId
	 * @param responseListener
	 * @param errorListener
	 */
	@Deprecated
	public void storeInfoPhotoCommentRequest(Context context, long storeId,
			Response.Listener<JSONObject> responseListener,
			Response.ErrorListener errorListener) {
		if (isInit()) {
			url = mVolleyTaskManager.getServerUrl()
					+ mVolleyTaskManager.getApiStorePhotoComment() + "?"
					+ "store_id=" + storeId;
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

	public JSONObject getQueryAll() {
		return new JSONObject();
	}

	private boolean isInit() {
		if (mVolleyTaskManager.getServerUrl() == null) {
			MyLog.e(StorePhotoCommentRequest.class,
					"VolleyTaskManager needs to be init before using request!");
			return false;
		}
		return true;
	}

}
