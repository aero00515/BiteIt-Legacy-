package edu.utboy.biteit.asynctasks;

import idv.andylin.tw.andyandroidlibs.utils.MyLog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import edu.utboy.biteit.asynctasks.requestqueues.StoreInfoRequestQueue;
import edu.utboy.biteit.models.StoreInfo;
import edu.utboy.biteit.utils.AuthToken;

public class StoreInfoRequest {

	/**
	 * static Singleton instance
	 */
	private static StoreInfoRequest mVerifyUserRequest;
	private VolleyTaskManager mVolleyTaskManager;
	private String url;

	private String storeInfoNextPage = "";

	/**
	 * Private constructor for singleton
	 */
	private StoreInfoRequest() {
		mVolleyTaskManager = VolleyTaskManager.getInstance();
	}

	/**
	 * Static getter method for retrieving the singleton instance
	 */
	public static StoreInfoRequest getInstance() {
		if (mVerifyUserRequest == null) {
			synchronized (StoreInfoRequest.class) {
				if (mVerifyUserRequest == null) {
					mVerifyUserRequest = new StoreInfoRequest();
				}
			}
		}
		return mVerifyUserRequest;
	}

	public synchronized void storeInfoRequest(Context context, Bundle params,
			Response.Listener<JSONObject> responseListener,
			Response.ErrorListener errorListener) {
		if (isInit()) {
			String param = "";
			if (params != null) {
				Set<String> keys = params.keySet();
				Iterator<String> ki = keys.iterator();
				for (int i = 0; i < keys.size(); i++) {
					if (ki.hasNext()) {
						String key = ki.next();
						if (i == 0) {
							param += "?";
						} else {
							param += "&";
						}
						param += key + "=" + params.get(key).toString();
					}
				}
			}

			url = mVolleyTaskManager.getServerUrl()
					+ mVolleyTaskManager.getApiStoreInfo() + param;
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
			StoreInfoRequestQueue.getSampleRequestQueue(context)
					.addToRequestQueue(jsonRequest);
		}
	}

	public JSONObject getNearbyStoreRequestBodyWithIds(
			List<StoreInfo> storeInfos) throws JSONException {
		JSONObject json = new JSONObject();
		JSONArray jsonStoreArray = new JSONArray();
		for (StoreInfo storeInfo : storeInfos) {
			JSONObject jsonStore = new JSONObject();
			jsonStore.put("store_id", storeInfo.getStoreId());
			jsonStoreArray.put(jsonStore);
		}
		json.put("store_ids", jsonStoreArray);
		return json;
	}

	public JSONObject getNearbyStoreRequestBody(String firstLevel,
			String thirdLevel, String lnglat, String radius)
			throws JSONException {
		JSONObject json = new JSONObject();
		json.put("first_level", firstLevel);
		json.put("third_level", thirdLevel);
		json.put("lnglat", lnglat);
		json.put("radius", radius);
		return json;
	}

	public Bundle getNearbyStoreParams(String firstLevel, String thirdLevel,
			String lnglat, String radius) throws JSONException {
		Bundle data = new Bundle();
		data.putString("first_level", firstLevel);
		data.putString("third_level", thirdLevel);
		data.putString("location", lnglat);
		data.putString("radius", radius);
		return data;
	}

	public Bundle getNextPrevParams(String url) {
		Bundle data = new Bundle();
		Uri u = Uri.parse(url);
		for (String key : u.getQueryParameterNames()) {
			data.putString(key, u.getQueryParameter(key));
		}
		return data;
	}

	public JSONObject getQueryAll() {
		return new JSONObject();
	}

	private boolean isInit() {
		if (mVolleyTaskManager.getServerUrl() == null) {
			MyLog.e(StoreInfoRequest.class,
					"VolleyTaskManager needs to be init before using request!");
			return false;
		}
		return true;
	}

	public List<StoreInfo> getStoreInfoByResponse(JSONObject json) {

		List<StoreInfo> storeInfos = new ArrayList<StoreInfo>();

		try {
			setStoreInfoNextPage((json.has("next") && !json.isNull("next")) ? json
					.getString("next") : null);

			JSONArray results = json.getJSONArray("results");

			for (int i = 0; i < results.length(); i++) {
				JSONObject storeObj = results.getJSONObject(i);
				Bundle data = new Bundle();
				data.putLong(StoreInfo.STORE_ID, storeObj.getLong("id"));
				data.putString(StoreInfo.GOOGLE_STORE_ID,
						storeObj.getString("google_store_id"));
				data.putString(StoreInfo.NAME, storeObj.getString("name"));
				data.putString(StoreInfo.ADDRESS, storeObj.getString("address"));
				data.putString(StoreInfo.PHONE, storeObj.getString("phone"));
				data.putDouble(StoreInfo.LONGITUDE,
						storeObj.getDouble("longitude"));
				data.putDouble(StoreInfo.LATITUDE,
						storeObj.getDouble("latitude"));
				data.putString(StoreInfo.FIRST_LEVEL,
						storeObj.getString("first_level"));
				data.putString(StoreInfo.THIRD_LEVEL,
						storeObj.getString("third_level"));
				data.putFloat(StoreInfo.RATING_CLEAN,
						(float) storeObj.getDouble("ratingClean"));
				data.putFloat(StoreInfo.RATING_SERVICE,
						(float) storeObj.getDouble("ratingService"));
				data.putFloat(StoreInfo.RATING_ATMOS,
						(float) storeObj.getDouble("ratingAtmos"));
				data.putFloat(StoreInfo.RATING_FLAVOR,
						(float) storeObj.getDouble("ratingFlavor"));
				data.putFloat(StoreInfo.RATING_UNKNOW,
						(float) storeObj.getDouble("ratingUnknow"));
				data.putBoolean(StoreInfo.IS_FAVOR,
						storeObj.getBoolean("is_favor"));
				data.putBoolean(StoreInfo.IS_BLOCKED,
						storeObj.getBoolean("is_blocked"));
				String[] tags = { "" };
				if (storeObj.has("tags") && !storeObj.isNull("tags")) {
					String t = storeObj.getString("tags");
					tags = t.split(",");
				}
				data.putStringArray(StoreInfo.TAGS, tags);

				StoreInfo info = new StoreInfo(data);
				storeInfos.add(info);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return storeInfos;
	}

	public String getStoreInfoNextPage() {
		return storeInfoNextPage;
	}

	public void setStoreInfoNextPage(String storeInfoNextPage) {
		this.storeInfoNextPage = storeInfoNextPage;
	}

}
