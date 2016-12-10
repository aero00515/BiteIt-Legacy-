package edu.utboy.biteit.models.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import edu.utboy.biteit.db.DataBaseHelper;
import edu.utboy.biteit.models.FeedbackPoint;

public class FeedbackPointDAO extends BaseDAO<FeedbackPoint> {

	public FeedbackPointDAO(Context ctx) {
		super(ctx);
	}

	/**
	 * @return the row ID of the newly inserted row, or -1 if an error occurred
	 */
	@Override
	public long save(FeedbackPoint feedbackPoint) {
		if (get().size() > 0) {
			return 0;
		}
		ContentValues values = new ContentValues();
		values.put(DataBaseHelper.NEW_STORE, feedbackPoint.getNewStore());
		values.put(DataBaseHelper.FILL_EMPTY, feedbackPoint.getFillEmpty());
		values.put(DataBaseHelper.REPORT_ERROR, feedbackPoint.getReportError());
		values.put(DataBaseHelper.RATING_FEEDBACK,
				feedbackPoint.getRatingFeedback());
		values.put(DataBaseHelper.PHOTO_FEEDBACK,
				feedbackPoint.getPhotoFeedback());
		values.put(DataBaseHelper.COMMENT_FEEDBACK,
				feedbackPoint.getCommentFeedback());
		return database.insert(DataBaseHelper.TABLE_FEEDBACK_POINT, null,
				values);
	}

	@Deprecated
	public List<FeedbackPoint> getAllFeedbackPoints() {
		List<FeedbackPoint> feedbackPoints = new ArrayList<FeedbackPoint>();

		Cursor cursor = database.query(DataBaseHelper.TABLE_FEEDBACK_POINT,
				columns, null, null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			FeedbackPoint feedbackPoint = cursorTo(cursor);
			feedbackPoints.add(feedbackPoint);
			cursor.moveToNext();
		}
		// make sure to close the cursor
		cursor.close();
		return feedbackPoints;
	}

	@Override
	protected void setAllColumns() {
		columns = new String[] { DataBaseHelper.NEW_STORE,
				DataBaseHelper.FILL_EMPTY, DataBaseHelper.REPORT_ERROR,
				DataBaseHelper.RATING_FEEDBACK, DataBaseHelper.PHOTO_FEEDBACK,
				DataBaseHelper.COMMENT_FEEDBACK };

	}

	@Override
	protected FeedbackPoint cursorTo(Cursor cursor) {
		int newStore = cursor.getInt(cursor
				.getColumnIndex(DataBaseHelper.NEW_STORE));
		int fillEmpty = cursor.getInt(cursor
				.getColumnIndex(DataBaseHelper.FILL_EMPTY));
		int reportError = cursor.getInt(cursor
				.getColumnIndex(DataBaseHelper.REPORT_ERROR));
		int ratingFeedback = cursor.getInt(cursor
				.getColumnIndex(DataBaseHelper.RATING_FEEDBACK));
		int photoFeedback = cursor.getInt(cursor
				.getColumnIndex(DataBaseHelper.PHOTO_FEEDBACK));
		int commentFeedback = cursor.getInt(cursor
				.getColumnIndex(DataBaseHelper.COMMENT_FEEDBACK));

		FeedbackPoint feedbackPoint = new FeedbackPoint(newStore, fillEmpty,
				reportError, ratingFeedback, photoFeedback, commentFeedback);
		return feedbackPoint;
	}

	@Override
	public List<Long> save(List<FeedbackPoint> objects) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void update(FeedbackPoint feedback) {
		ContentValues values = new ContentValues();
		values.put(DataBaseHelper.NEW_STORE, feedback.getNewStore());
		values.put(DataBaseHelper.REPORT_ERROR, feedback.getReportError());
		values.put(DataBaseHelper.FILL_EMPTY, feedback.getFillEmpty());
		values.put(DataBaseHelper.RATING_FEEDBACK, feedback.getRatingFeedback());
		values.put(DataBaseHelper.PHOTO_FEEDBACK, feedback.getPhotoFeedback());
		values.put(DataBaseHelper.COMMENT_FEEDBACK,
				feedback.getCommentFeedback());
		database.update(DataBaseHelper.TABLE_FEEDBACK_POINT, values, null, null);

	}

	@Override
	@Deprecated
	public boolean delete(FeedbackPoint object) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	@Deprecated
	public boolean delete(List<FeedbackPoint> objects) {
		return false;
	}

	@Override
	public List<FeedbackPoint> get() {
		List<FeedbackPoint> feedbackPoints = new ArrayList<FeedbackPoint>();

		Cursor cursor = database.query(DataBaseHelper.TABLE_FEEDBACK_POINT,
				columns, null, null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			FeedbackPoint feedbackPoint = cursorTo(cursor);
			feedbackPoints.add(feedbackPoint);
			cursor.moveToNext();
		}
		// make sure to close the cursor
		cursor.close();
		return feedbackPoints;
	}

	@Override
	@Deprecated
	public FeedbackPoint get(long id) {
		List<FeedbackPoint> feedbackPoints = new ArrayList<FeedbackPoint>();

		Cursor cursor = database.query(DataBaseHelper.TABLE_FEEDBACK_POINT,
				columns, DataBaseHelper._ID, null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			FeedbackPoint feedbackPoint = cursorTo(cursor);
			feedbackPoints.add(feedbackPoint);
			cursor.moveToNext();
		}
		// make sure to close the cursor
		cursor.close();
		return feedbackPoints.get(0);
	}

}
