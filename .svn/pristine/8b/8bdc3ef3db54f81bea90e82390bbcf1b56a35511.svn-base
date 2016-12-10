package edu.utboy.biteit.models;

import android.os.Bundle;

import com.google.gson.Gson;

public class FeedbackPoint {

	public static final String NEW_STORE = "new_store";
	public static final String FILL_EMPTY = "fill_empty";
	public static final String REPORT_ERROR = "report_error";
	public static final String RATING_FEEDBACK = "rating_feedback";
	public static final String PHOTO_FEEDBACK = "photo_feedback";
	public static final String COMMENT_FEEDBACK = "comment_feedback";
	
	private int newStore;
	private int fillEmpty;
	private int reportError;
	private int ratingFeedback;
	private int photoFeedback;
	private int commentFeedback;

	public FeedbackPoint(Bundle data) {
		this.newStore = data.getInt(NEW_STORE);
		this.fillEmpty = data.getInt(FILL_EMPTY);
		this.reportError = data.getInt(REPORT_ERROR);
		this.ratingFeedback = data.getInt(RATING_FEEDBACK);
		this.photoFeedback = data.getInt(PHOTO_FEEDBACK);
		this.commentFeedback = data.getInt(COMMENT_FEEDBACK);
	}

	public FeedbackPoint(int newStore, int fillEmpty, int reportError,
			int ratingFeedback, int photoFeedback, int commentFeedback) {
		this.newStore = newStore;
		this.fillEmpty = fillEmpty;
		this.reportError = reportError;
		this.ratingFeedback = ratingFeedback;
		this.photoFeedback = photoFeedback;
		this.commentFeedback = commentFeedback;
	}
	

	public int getNewStore() {
		return newStore;
	}

	public void setNewStore(int newStore) {
		this.newStore = newStore;
	}

	public int getFillEmpty() {
		return fillEmpty;
	}

	public void setFillEmpty(int fillEmpty) {
		this.fillEmpty = fillEmpty;
	}

	public int getReportError() {
		return reportError;
	}

	public void setReportError(int reportError) {
		this.reportError = reportError;
	}

	public int getRatingFeedback() {
		return ratingFeedback;
	}

	public void setRatingFeedback(int ratingFeedback) {
		this.ratingFeedback = ratingFeedback;
	}

	public int getPhotoFeedback() {
		return photoFeedback;
	}

	public void setPhotoFeedback(int photoFeedback) {
		this.photoFeedback = photoFeedback;
	}

	public int getCommentFeedback() {
		return commentFeedback;
	}

	public void setCommentFeedback(int commentFeedback) {
		this.commentFeedback = commentFeedback;
	}

	public Bundle save() {
		Bundle data = new Bundle();
		data.putInt(NEW_STORE, getNewStore());
		data.putInt(FILL_EMPTY, getFillEmpty());
		data.putInt(REPORT_ERROR, getReportError());
		data.putInt(RATING_FEEDBACK, getRatingFeedback());
		data.putInt(PHOTO_FEEDBACK, getPhotoFeedback());
		data.putInt(COMMENT_FEEDBACK, getCommentFeedback());
		
		return data;
	}

	@Override
	public String toString() {
		return new Gson().toJson(FeedbackPoint.this);
	}
}
