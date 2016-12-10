package edu.utboy.biteit.asynctasks;

import idv.andylin.tw.andyandroidlibs.utils.MyLog;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;

import edu.utboy.biteit.asynctasks.requestqueues.SampleRequestQueue;
import edu.utboy.biteit.utils.AuthToken;

public class FBPStatusRequest {

	/**
	 * static Singleton instance
	 */
	private static FBPStatusRequest mFBPStatusRequest;
	private VolleyTaskManager mVolleyTaskManager;

	/**
	 * Private constructor for singleton
	 */
	private FBPStatusRequest() {
		mVolleyTaskManager = VolleyTaskManager.getInstance();
	}

	/**
	 * Static getter method for retrieving the singleton instance
	 */
	public static FBPStatusRequest getInstance() {
		if (mFBPStatusRequest == null) {
			synchronized (FBPStatusRequest.class) {
				if (mFBPStatusRequest == null) {
					mFBPStatusRequest = new FBPStatusRequest();
				}
			}
		}
		return mFBPStatusRequest;
	}

	public void getFeedbackPointStatusRequest(Context context, long id,
			Response.Listener<String> responseListener,
			Response.ErrorListener errorListener) {
		if (isInit()) {
			String url = mVolleyTaskManager.getServerUrl()
					+ mVolleyTaskManager.getApiFeedbackPointStatus()
					+ "/?user_id=" + id;
			StringRequest request = new StringRequest(Request.Method.GET, url,
					responseListener, errorListener) {
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
					.addToRequestQueue(request);
		}
	}

	private boolean isInit() {
		if (mVolleyTaskManager.getServerUrl() == null) {
			MyLog.e(FBPStatusRequest.class,
					"VolleyTaskManager needs to be init before using request!");
			return false;
		}
		return true;
	}

}
