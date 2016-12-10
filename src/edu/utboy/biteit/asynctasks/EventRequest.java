package edu.utboy.biteit.asynctasks;

import idv.andylin.tw.andyandroidlibs.utils.MyLog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.android.volley.Response;

public class EventRequest {

	/**
	 * static Singleton instance
	 */
	private static EventRequest mEventRequest;

	/**
	 * Private constructor for singleton
	 */
	private EventRequest() {
	}

	/**
	 * Static getter method for retrieving the singleton instance
	 */
	public static EventRequest getInstance() {
		if (mEventRequest == null) {
			synchronized (EventRequest.class) {
				if (mEventRequest == null) {
					mEventRequest = new EventRequest();
				}
			}
		}
		return mEventRequest;
	}

	public void getEventPointsRequest(final Context context, final long id,
			final Response.Listener<JSONObject> responseListener,
			final Response.ErrorListener errorListener) {
		FeedbackPointRequest.getInstance().getFeedbackPoint(context,
				new Response.Listener<JSONObject>() {

					@Override
					public void onResponse(final JSONObject response) {
						MyLog.d(EventRequest.class, "" + response.toString());
						try {
							dealAllResponse(context, id, responseListener,
									errorListener, response);
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				}, errorListener);
	}

	private void dealAllResponse(final Context context, final long id,
			final Response.Listener<JSONObject> responseListener,
			final Response.ErrorListener errorListener,
			final JSONObject response) throws JSONException {
		JSONArray results = response.getJSONArray("results");
		boolean hasUser = false;
		for (int i = 0; i < results.length(); i++) {
			JSONObject result = results.getJSONObject(i);
			if (result.getLong("user_id") == id) {
				hasUser = true;
				break;
			}
		}
		if (hasUser) {
			responseListener.onResponse(response);
		} else {
			dealUserResponse(context, id, responseListener, errorListener,
					response);
		}
	}

	private void dealUserResponse(final Context context, final long id,
			final Response.Listener<JSONObject> responseListener,
			final Response.ErrorListener errorListener,
			final JSONObject response) {
		FeedbackPointRequest.getInstance().getUserFeedbackPoint(context, id,
				new Response.Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject responseUser) {
						try {
							response.getJSONArray("results").put(responseUser);
							responseListener.onResponse(response);
						} catch (JSONException e) {
							e.printStackTrace();
						}

					}
				}, errorListener);
	}

	public JSONObject createFriendJsonObject(long userId, long friendId,
			boolean isFocused, boolean isBlocked) throws JSONException {
		JSONObject json = new JSONObject();
		json.put("user_id", userId);
		json.put("friend_id", friendId);
		json.put("is_focused", isFocused);
		json.put("is_blocked", isBlocked);
		return json;
	}

}
