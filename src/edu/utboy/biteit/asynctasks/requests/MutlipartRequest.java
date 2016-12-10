package edu.utboy.biteit.asynctasks.requests;

import idv.andylin.tw.andyandroidlibs.utils.MyLog;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyLog;

import edu.utboy.biteit.utils.AuthToken;

public class MutlipartRequest extends Request<JSONObject> {

	private MultipartEntityBuilder entityBuilder;

	private Response.Listener<JSONObject> responseListener;
	private File file;
	private JSONObject json;

	public MutlipartRequest(String url, ErrorListener errorListener,
			Response.Listener<JSONObject> responseListener, File file,
			JSONObject json) {
		super(Request.Method.POST, url, errorListener);
		entityBuilder = MultipartEntityBuilder.create();
		entityBuilder.setCharset(Charset.defaultCharset());
		entityBuilder.setBoundary("-----------------"
				+ System.currentTimeMillis());
		this.responseListener = responseListener;
		this.file = file;
		this.json = json;
		try {
			build();
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	private void build() throws JSONException, UnsupportedEncodingException {
		entityBuilder.addPart("photo", new FileBody(file));
		entityBuilder.addPart("user_id",
				new StringBody(String.valueOf(json.getInt("user_id")),
						ContentType.MULTIPART_FORM_DATA));
		entityBuilder.addTextBody("comment",
				URLEncoder.encode(json.getString("comment"), "UTF-8"));
		entityBuilder.addPart("store_id",
				new StringBody(String.valueOf(json.getInt("store_id")),
						ContentType.MULTIPART_FORM_DATA));
		MyLog.d(MutlipartRequest.class, "comment " + URLEncoder.encode(json.getString("comment"), "UTF-8"));
		int attendNo = 0;
		if (json.has("attend_no")) {
			attendNo = json.getInt("attend_no");
		}
		entityBuilder.addPart("attend_no",
				new StringBody(String.valueOf(attendNo),
						ContentType.MULTIPART_FORM_DATA));

	}

	@Override
	public Map<String, String> getHeaders() throws AuthFailureError {
		HashMap<String, String> maps = new HashMap<String, String>();
		String token = AuthToken.getAuthToken();
		if (!token.isEmpty()) {
			maps.put("Authorization", "Token " + token);
			// maps.put("Content-Type", "multipart/form-data");
		} else {
			VolleyLog.e("string", "token null: plz check auth again!");
		}
		return maps;
	}

	@Override
	public String getBodyContentType() {
		return entityBuilder.build().getContentType().getValue();
	}

	@Override
	public byte[] getBody() throws AuthFailureError {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			entityBuilder.build().writeTo(baos);
		} catch (IOException e) {
			VolleyLog.e("IOException writing to ByteArrayOutputStream");
			e.printStackTrace();
		}
		return baos.toByteArray();
	}

	@Override
	protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
		JSONObject json = new JSONObject();
		try {
			json.put("is_uploaded", getErrorListener() != null ? true : false);
		} catch (JSONException e) {
			VolleyLog.e("JSONException");
			e.printStackTrace();
		}
		return Response.success(json, getCacheEntry());
	}

	@Override
	protected void deliverResponse(JSONObject response) {
		responseListener.onResponse(response);
	}

}
