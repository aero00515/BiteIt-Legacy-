package edu.utboy.biteit.asynctasks.requests;

import idv.andylin.tw.andyandroidlibs.utils.MyLog;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import com.android.volley.AuthFailureError;
import com.android.volley.VolleyLog;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.JsonObjectRequest;

import edu.utboy.biteit.utils.AuthToken;

public class MyJsonObjectRequest extends JsonObjectRequest {

	public MyJsonObjectRequest(String url, JSONObject jsonRequest,
			Listener<JSONObject> listener, ErrorListener errorListener) {
		super(url, jsonRequest, listener, errorListener);
	}

	public MyJsonObjectRequest(int method, String url, JSONObject jsonRequest,
			Listener<JSONObject> listener, ErrorListener errorListener) {
		super(method, url, jsonRequest, listener, errorListener);
	}

	@Override
	public Map<String, String> getHeaders() throws AuthFailureError {
		HashMap<String, String> maps = new HashMap<String, String>();
		String token = AuthToken.getAuthToken();
		if (!token.isEmpty()) {
			maps.put("Authorization", "Token " + token);
			MyLog.d(MyJsonObjectRequest.class, "token " + token);
		} else {
			VolleyLog.e("string", "token null: plz check auth again!");
		}
		return maps;
	}

}
