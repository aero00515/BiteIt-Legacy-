package edu.utboy.biteit;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import edu.utboy.biteit.asynctasks.FeedbackPointRequest;
import edu.utboy.biteit.models.FeedbackPoint;
import edu.utboy.biteit.models.dao.FeedbackPointDAO;
import edu.utboy.biteit.utils.AuthUser;
import idv.andylin.tw.andyandroidlibs.utils.MyLog;
import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;

public class FeedbackUpdateService extends IntentService {

	public static final String UPDATE = "";

	public FeedbackUpdateService() {
		super("FeedbackUpdateService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		if (intent.getAction().equals(UPDATE)) {
			try {
				updateToServer(intent.getExtras());
				updateToSqlite(intent.getExtras());
			} catch (JSONException e) {
				e.printStackTrace();
			}
		} else {
			// TODO Impossible entry
			MyLog.e(FeedbackUpdateService.class, "Impossible entry");
		}
	}

	private void updateToServer(Bundle data) throws JSONException {
		JSONObject feedJsonRequest = FeedbackPointRequest.getInstance()
				.createFeedbackPointRequestBody(AuthUser.getId(),
						data.getInt(FeedbackPoint.NEW_STORE),
						data.getInt(FeedbackPoint.FILL_EMPTY),
						data.getInt(FeedbackPoint.REPORT_ERROR),
						data.getInt(FeedbackPoint.RATING_FEEDBACK),
						data.getInt(FeedbackPoint.PHOTO_FEEDBACK),
						data.getInt(FeedbackPoint.COMMENT_FEEDBACK));
		FeedbackPointRequest.getInstance().updateFeedbackPoint(
				getApplicationContext(), AuthUser.getId(), feedJsonRequest,
				new Response.Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						MyLog.d(FeedbackUpdateService.class,
								"" + response.toString());
					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						MyLog.e(FeedbackUpdateService.class,
								"" + error.getMessage());
					}
				});
	}

	private void updateToSqlite(Bundle data) {
		FeedbackPointDAO fpDAO = new FeedbackPointDAO(getApplicationContext());
		fpDAO.open();
		fpDAO.update(new FeedbackPoint(data));
	}

}
