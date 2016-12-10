package edu.utboy.biteit.db;

import idv.andylin.tw.andyandroidlibs.utils.MyLog;

import java.lang.reflect.Field;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseHelper extends SQLiteOpenHelper implements DbConstants {

	private static final int DATABASE_VERSION = 3;
	private static DataBaseHelper collectionDbOpenHelper;

	private String sqlStoreInfo = "CREATE TABLE " + TABLE_STORE_INFO + "("
			+ STORE_ID + " INTEGER UNIQUE, " 
			+ GOOGLE_STORE_ID + " TEXT, "
			+ NAME + " TEXT, " 
			+ ADDRESS + " TEXT, " 
			+ PHONE + " TEXT, "
			+ LONGITUDE + " REAL, " 
			+ LATITUDE + " REAL, " 
			+ FIRST_LEVEL + " TEXT, " 
			+ THIRD_LEVEL + " TEXT, " 
			+ RATING_CLEAN + " REAL, "
			+ RATING_SERVICE + " REAL, " 
			+ RATING_ATMOS + " REAL, "
			+ RATING_FLAVOR + " REAL, " 
			+ RATING_UNKNOW + " REAL, "
			+ IS_BLOCKED + " INTEGER, " 
			+ IS_FAVOR + " INTEGER, " 
			+ TAGS + " TEXT );";

	private String sqlPhotoComment = "create table " + TABLE_PHOTO_COMMENT + "(" 
			+ STORE_ID + " INTEGER, " 
			+ USER_ID + " INTEGER, " 
			+ URI + " TEXT UNIQUE, " 
			+ COMMENT + " TEXT );";

	private String sqlFriend = "create table " + TABLE_FRIENDS + "(" 
			+ _ID+ " INTEGER PRIMARY KEY, " 
			+ USERNAME + " TEXT UNIQUE, " 
			+ NAME + " TEXT, " 
			+ URI + " TEXT, " 
			+ EMAIL + " TEXT, " 
			+ IS_BLOCKED + " INTEGER, " 
			+ IS_FOCUSED + " INTEGER );";

	private String sqlFeedbackPoint = "create table " + TABLE_FEEDBACK_POINT + "(" 
			+ NEW_STORE + " INTEGER, " 
			+ FILL_EMPTY + " INTEGER, "
			+ REPORT_ERROR + " INTEGER, " 
			+ RATING_FEEDBACK + " INTEGER, "
			+ PHOTO_FEEDBACK + " INTEGER, " 
			+ COMMENT_FEEDBACK + " INTEGER );";

	private final String[] SQL_STRING = { sqlPhotoComment, sqlStoreInfo,
			sqlFriend, sqlFeedbackPoint };

	private DataBaseHelper(Context ctx) {
		super(ctx, ctx.getPackageName(), null, DATABASE_VERSION);
		MyLog.i(DataBaseHelper.class, "db new helper: " + ctx.getPackageName());
	}

	public static DataBaseHelper getDbHelper(Context context) {
		if (collectionDbOpenHelper == null) {
			synchronized (DataBaseHelper.class) {
				if (collectionDbOpenHelper == null) {
					collectionDbOpenHelper = new DataBaseHelper(context);
				}
			}
		}
		return collectionDbOpenHelper;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

		for (String sql : SQL_STRING) {
			db.execSQL(sql);
			MyLog.i(DataBaseHelper.class, "db create: " + sql);
		}

	}

	public void reCreateAll(SQLiteDatabase db) throws IllegalAccessException,
			IllegalArgumentException {
		for (Field field : DbConstants.class.getDeclaredFields()) {
			if (field
					.isAnnotationPresent(idv.andylin.tw.andyandroidlibs.DbConstants.Table.class)) {
				String sql = "DROP TABLE IF EXISTS "
						+ field.get(field.getName());
				db.execSQL(sql);
			}
		}
		onCreate(db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		MyLog.i(DataBaseHelper.class, "upgarde, old: " + oldVersion + ", new: "
				+ newVersion);
		if (newVersion == 3) {
			String sql = "DROP TABLE IF EXISTS " + TABLE_PHOTO_COMMENT;
			db.execSQL(sql);
			MyLog.i(DataBaseHelper.class, "db update :" + sql);
		}
		db.execSQL(sqlPhotoComment);
		MyLog.i(DataBaseHelper.class, "db create: " + sqlPhotoComment);
	}

}