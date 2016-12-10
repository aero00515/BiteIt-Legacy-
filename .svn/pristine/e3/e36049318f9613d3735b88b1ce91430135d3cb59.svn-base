package edu.utboy.biteit.ui.tutorials;

import idv.andylin.tw.andyandroidlibs.utils.MyLog;

import java.util.Arrays;

import org.json.JSONArray;
import org.json.JSONException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.facebook.FacebookRequestError;
import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphObject;
import com.facebook.widget.LoginButton;
import com.google.gson.Gson;

import edu.utboy.biteit.R;

public class MultiLoginFragment extends Fragment {
	private final int LOCAL_SIGN_IN = 10;
	private OnLoginCallback mOnLoginCallback;
//	private SignInButton googleSignInBtn;

	public MultiLoginFragment(OnLoginCallback mOnLoginCallback) {
		this.mOnLoginCallback = mOnLoginCallback;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_multi_login,
				container, false);
		LoginButton authButton = (LoginButton) rootView
				.findViewById(R.id.authButton);
//		googleSignInBtn = (SignInButton) rootView
//				.findViewById(R.id.sign_in_button);

		authButton.setFragment(this);
		authButton.setReadPermissions(Arrays.asList("user_friends", "email",
				"user_about_me", "user_location", "user_tagged_places",
				"read_friendlists"));

//		googleSignInBtn.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				Intent intent = new Intent();
//				intent.setClass(getActivity(), GoogleLoginActivity.class);
//				startActivityForResult(intent, LOCAL_SIGN_IN);
//			}
//		});
		return rootView;
	}

	private void doGetUserRequest(final Session fbAuthToken) {
		Request request = new Request(fbAuthToken, "/me", null, HttpMethod.GET,
				new Request.Callback() {

					@Override
					public void onCompleted(Response response) {
						MyLog.d(MultiLoginFragment.class,
								"[fb query response] me: "
										+ response.toString());
						GraphObject go = response.getGraphObject();
						FacebookRequestError fre = response.getError();
						if (go != null) {
							String id = go.asMap().get("id").toString();
							String email = go.asMap().get("email").toString();
							String name = go.asMap().get("name").toString();

							final Bundle data = new Bundle();
							data.putString(OnLoginCallback.LOGIN_TYPE,
									"facebook");
							data.putString(OnLoginCallback.FB_USER_ID, id);
							data.putString(OnLoginCallback.FB_NAME, name);
							data.putString(OnLoginCallback.FB_EMAIL, email);
							data.putString(OnLoginCallback.TEST_GRAPH_USER,
									new Gson().toJson(go.asMap()));

							new Request(fbAuthToken, "/me/friends", null,
									HttpMethod.GET, new Request.Callback() {
										public void onCompleted(
												Response response) {
											/* handle the result */
											MyLog.d(MultiLoginFragment.class,
													"[fb query response] friends: "
															+ response
																	.toString());
											GraphObject go = response
													.getGraphObject();
											FacebookRequestError fre = response
													.getError();
											if (go != null) {
												JSONArray jsonFriends = (JSONArray) go
														.asMap().get("data");
												String[] friendIds = new String[jsonFriends
														.length()];
												for (int i = 0; i < jsonFriends
														.length(); i++) {
													try {
														friendIds[i] = jsonFriends
																.getJSONObject(i).getString("id");
														MyLog.d(MultiLoginFragment.class,
																"f: "
																		+ friendIds[i]);
													} catch (JSONException e) {
														e.printStackTrace();
													}
												}
												data.putStringArray("friends",
														friendIds);
												mOnLoginCallback.onLogin(data);
											} else if (fre != null) {
												MyLog.e(MultiLoginFragment.class,
														fre.getErrorMessage());
											}

										}
									}).executeAsync();
						} else if (fre != null) {
							MyLog.e(MultiLoginFragment.class,
									fre.getErrorMessage());
						}
					}
				});
		Request.executeBatchAsync(request);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == LOCAL_SIGN_IN) {
			if (resultCode == Activity.RESULT_OK) {
				Bundle userInfo = data.getExtras();
				mOnLoginCallback.onLogin(userInfo);
			} else {
				Toast.makeText(getActivity(), "Result fail", Toast.LENGTH_SHORT)
						.show();
			}
		} else if (Session.getActiveSession().onActivityResult(getActivity(),
				requestCode, resultCode, data)) {
			// is Facebook auth
			if (Session.getActiveSession().isOpened()) {
				doGetUserRequest(Session.getActiveSession());
			}
		}
	}

	public interface OnLoginCallback {

		public static final String LOGIN_TYPE = "login_type";

		public static final String FB_USER_ID = "fb_user_id";
		public static final String FB_NAME = "fb_name";
		public static final String FB_EMAIL = "fb_email";
		public static final String TEST_GRAPH_USER = "test_graph_user";

		public static final String GOOGLE_USER_ID = "google_user_id";
		public static final String GOOGLE_NAME = "google_name";
		public static final String GOOGLE_EMAIL = "google_email";
		public static final String GOOGLE_USER_PHOTO = "google_user_photo";

		public void onLogin(Bundle data);
	}

}
