package edu.utboy.biteit.models;

import org.json.JSONException;
import org.json.JSONObject;

import android.net.Uri;
import android.os.Bundle;

import com.google.gson.Gson;

public class PhotoComment {

	public final static String STORE_ID = "photocomment_store_id";
	public final static String USER_ID = "photocomment_user_id";
	public final static String URI = "photocomment_uri";
	public final static String COMMENT = "photocomment_comment";

	private long storeId;
	private long userId;
	private Uri uriString;
	private String comment;

	public PhotoComment(Bundle data) {
		storeId = data.getLong(STORE_ID);
		userId = data.getLong(USER_ID);
		uriString = Uri.parse(data.getString(URI));
		comment = data.getString(COMMENT);
	}

	public PhotoComment(long storeId, long userId, Uri uri, String comment) {
		this.storeId = storeId;
		this.userId = userId;
		this.uriString = uri;
		this.comment = comment;
	}

	public long getStoreId() {
		return storeId;
	}

	public void setStoreId(long storeId) {
		this.storeId = storeId;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public Uri getUri() {
		return uriString;
	}

	public void setUri(Uri uri) {
		this.uriString = uri;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public JSONObject saveAsJson() throws JSONException {
		JSONObject data = new JSONObject();
		data.put(STORE_ID, getStoreId());
		data.put(USER_ID, getUserId());
		data.put(URI, getUri().toString());
		data.put(COMMENT, getComment());
		return data;
	}

	public Bundle save() {
		Bundle data = new Bundle();
		data.putLong(STORE_ID, getStoreId());
		data.putLong(USER_ID, getUserId());
		data.putString(URI, getUri().toString());
		data.putString(COMMENT, getComment());
		return data;
	}

	@Override
	public String toString() {
		return new Gson().toJson(PhotoComment.this);
	}
}