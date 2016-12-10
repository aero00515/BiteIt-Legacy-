package edu.utboy.biteit.db;

public interface DbConstants extends idv.andylin.tw.andyandroidlibs.DbConstants {

	@Table
	public static final String TABLE_PHOTO_COMMENT = "table_photo_comment";

	@Table
	public static final String TABLE_STORE_INFO = "table_store_info";

	@Table
	public static final String TABLE_FRIENDS = "table_friends";

	@Table
	public static final String TABLE_FEEDBACK_POINT = "table_feedback_point";

	public static final String STORE_ID = "store_id";
	public static final String USER_ID = "user_id";
	public static final String URI = "uri";
	public static final String COMMENT = "comment";

	public static final String GOOGLE_STORE_ID = "google_store_id";
	public static final String NAME = "name";
	public static final String ADDRESS = "address";
	public static final String PHONE = "phone";
	public static final String LONGITUDE = "longitude";
	public static final String LATITUDE = "latitude";
	public static final String FIRST_LEVEL = "first_level";
	public static final String THIRD_LEVEL = "third_level";
	public static final String RATING_CLEAN = "ratingClean";
	public static final String RATING_SERVICE = "ratingService";
	public static final String RATING_ATMOS = "ratingAtmos";
	public static final String RATING_FLAVOR = "ratingFlavor";
	public static final String RATING_UNKNOW = "ratingUnknow";
	public static final String TAGS = "tags";

	public static final String NEW_STORE = "new_store";
	public static final String FILL_EMPTY = "fill_empty";
	public static final String REPORT_ERROR = "report_error";
	public static final String RATING_FEEDBACK = "rating_feedback";
	public static final String PHOTO_FEEDBACK = "photo_feedback";
	public static final String COMMENT_FEEDBACK = "comment_feedback";

	public static final String IS_FAVOR = "is_favor";
	public static final String IS_BLOCKED = "is_blocked";

	public static final String USERNAME = "username";
	public static final String EMAIL = "email";
	public static final String IS_FOCUSED = "is_focused";

}