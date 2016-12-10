package edu.utboy.biteit;

import idv.andylin.tw.andyandroidlibs.utils.MyLog;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.google.gson.Gson;

import edu.utboy.biteit.ui.tutorials.MultiLoginFragment;
import edu.utboy.biteit.ui.tutorials.MultiLoginFragment.OnLoginCallback;

public class GoogleLoginActivity extends Activity implements
		ConnectionCallbacks, OnConnectionFailedListener {
	public static final int RC_SIGN_IN = 100;
	private static final int STATE_DEFAULT = 0;
	private static final int STATE_SIGN_IN = 1;
	private static final int STATE_IN_PROGRESS = 2;
	private static final String SAVED_PROGRESS = "sign_in_progress";

	// We use mSignInProgress to track whether user has clicked sign in.
	// mSignInProgress can be one of three values:
	//
	// STATE_DEFAULT: The default state of the application before the user
	// has clicked 'sign in', or after they have clicked
	// 'sign out'. In this state we will not attempt to
	// resolve sign in errors and so will display our
	// Activity in a signed out state.
	// STATE_SIGN_IN: This state indicates that the user has clicked 'sign
	// in', so resolve successive errors preventing sign in
	// until the user has successfully authorized an account
	// for our app.
	// STATE_IN_PROGRESS: This state indicates that we have started an intent to
	// resolve an error, and so we should not start further
	// intents until the current intent completes.
	private int mSignInProgress;

	private GoogleApiClient mGoogleApiClient;
	private ProgressDialog connectionProgressDialog;
	private ConnectionResult mConnectionResult;

	// private SignInButton signInBtn;
	// private Button signOutBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_plus_client);

		if (savedInstanceState != null) {
			mSignInProgress = savedInstanceState.getInt(SAVED_PROGRESS,
					STATE_DEFAULT);
		}

		mGoogleApiClient = new GoogleApiClient.Builder(this)
				.addConnectionCallbacks(this)
				.addOnConnectionFailedListener(this).addApi(Plus.API)
				.addScope(Plus.SCOPE_PLUS_LOGIN).build();

		connectionProgressDialog = new ProgressDialog(this);
		connectionProgressDialog.setMessage("Signing in...");

		mSignInProgress = STATE_SIGN_IN;
		mGoogleApiClient.connect();
		// if (!mGoogleApiClient.isConnecting()) {
		// mSignInClicked = true;
		// resolveSignInError();
		// }

		// signOutBtn = (Button) findViewById(R.id.signout_btn);
		// signOutBtn.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// if (mGoogleApiClient.isConnected()) {
		// Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
		// mGoogleApiClient.disconnect();
		// mGoogleApiClient.connect();
		// }
		// }
		// });
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt(SAVED_PROGRESS, mSignInProgress);
	}

	/* A helper method to resolve the current ConnectionResult error. */
	private void resolveSignInError() {
		if (mConnectionResult != null) {
			connectionProgressDialog.show();
			try {
				mSignInProgress = STATE_IN_PROGRESS;
				// startIntentSenderForResult(mConnectionResult.getResolution()
				// .getIntentSender(), RC_SIGN_IN, null, 0, 0, 0);
				mConnectionResult.startResolutionForResult(this, RC_SIGN_IN);
			} catch (SendIntentException e) {
				// The intent was canceled before it was sent. Return to the
				// default
				// state and attempt to connect to get an updated
				// ConnectionResult.
				e.printStackTrace();
				mSignInProgress = STATE_SIGN_IN;
				mGoogleApiClient.connect();
			}
		}
	}

	@Override
	public void finish() {
		super.finish();
		if (mGoogleApiClient.isConnected()) {
			mGoogleApiClient.disconnect();
		}
	}

	@Override
	public void onConnectionFailed(ConnectionResult result) {
		if (mSignInProgress != STATE_IN_PROGRESS) {
			// We do not have an intent in progress so we should store the
			// latest
			// error resolution intent for use when the sign in button is
			// clicked.
			mConnectionResult = result;
			// mSignInIntent = result.getResolution();
			MyLog.e(GoogleLoginActivity.class,
					"error code: " + result.getErrorCode());

			if (mSignInProgress == STATE_SIGN_IN) {
				// STATE_SIGN_IN indicates the user already clicked the sign in
				// button
				// so we should continue processing errors until the user is
				// signed in
				// or they click cancel.
				resolveSignInError();
			}
		}
	}

	@Override
	public void onConnected(Bundle arg0) {
		connectionProgressDialog.dismiss();
		Toast.makeText(this, "User is connected!", Toast.LENGTH_LONG).show();
		mSignInProgress = STATE_DEFAULT;

		Person curPerson = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
		if (curPerson != null) {

			String personId = curPerson.getId();
			String personName = curPerson.getDisplayName();
			String personEmail = Plus.AccountApi
					.getAccountName(mGoogleApiClient);
			String personPhotoUrl = curPerson.getImage().getUrl();

			Bundle data = new Bundle();
			data.putString(OnLoginCallback.LOGIN_TYPE, "google");
			data.putString(OnLoginCallback.GOOGLE_USER_ID, personId);
			data.putString(OnLoginCallback.GOOGLE_NAME, personName);
			data.putString(OnLoginCallback.GOOGLE_EMAIL, personEmail);
			data.putString(OnLoginCallback.GOOGLE_USER_PHOTO, personPhotoUrl);
			data.putString(OnLoginCallback.TEST_GRAPH_USER,
					new Gson().toJson(curPerson));

			// mOnLoginCallback.onLogin(data);
			Intent intent = new Intent();
			intent.putExtras(data);
			setResult(RESULT_OK, intent);
			finish();
		} else {
			MyLog.d(MultiLoginFragment.class,
					"Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) "
							+ curPerson);
			mSignInProgress = STATE_SIGN_IN;
			mGoogleApiClient.connect();
		}
	}

	@Override
	public void onConnectionSuspended(int arg0) {
		mSignInProgress = STATE_SIGN_IN;
		mGoogleApiClient.connect();
	}

	@Override
	protected void onActivityResult(int requestCode, int responseCode,
			Intent data) {
		if (requestCode == RC_SIGN_IN) {
			connectionProgressDialog.dismiss();
			if (responseCode != RESULT_OK) {
				// mSignInClicked = false;
				mSignInProgress = STATE_DEFAULT;
				Toast.makeText(this, "Google Login Error! Try Again.",
						Toast.LENGTH_SHORT).show();
				finish();
			} else {
				mSignInProgress = STATE_SIGN_IN;
			}

			if (!mGoogleApiClient.isConnecting()) {
				mGoogleApiClient.connect();
			}
		}
	}

}
