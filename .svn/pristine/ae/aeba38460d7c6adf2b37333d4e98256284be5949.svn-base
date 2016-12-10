package edu.utboy.biteit.models;

import android.os.Bundle;

import com.google.gson.Gson;

public class StoreInfo {

	public final static String STORE_ID = "store_info_store_id";
	public final static String GOOGLE_STORE_ID = "store_info_google_store_id";
	public final static String NAME = "store_info_name";
	public final static String ADDRESS = "store_info_address";
	public final static String PHONE = "store_info_phone";
	public final static String LONGITUDE = "store_info_longitude";
	public final static String LATITUDE = "store_info_latitude";
	public final static String FIRST_LEVEL = "store_info_first_level";
	public final static String THIRD_LEVEL = "store_info_third_level";
	public final static String RATING_CLEAN = "store_info_rating_clean";
	public final static String RATING_SERVICE = "store_info_rating_service";
	public final static String RATING_ATMOS = "store_info_rating_atmos";
	public final static String RATING_FLAVOR = "store_info_rating_flavor";
	public final static String RATING_UNKNOW = "store_info_rating_unknow";
	public final static String IS_FAVOR = "store_info_is_favor";
	public final static String IS_BLOCKED = "store_info_is_blocked";
	public final static String TAGS = "store_info_tags";

	private long storeId;
	private String googleStoreId;
	private String name;
	private String address;
	private String phone;
	private double longitude;
	private double latitude;
	private String firstLevel;
	private String thirdLevel;
	private float ratingClean;
	private float ratingService;
	private float ratingAtmos;
	private float ratingFlavor;
	private float ratingUnkonw;
	private boolean isFavor;
	private boolean isBlocked;
	private String[] tags;

	public StoreInfo(Bundle data) {
		this.storeId = data.getLong(STORE_ID);
		this.googleStoreId = data.getString(GOOGLE_STORE_ID);
		this.name = data.getString(NAME);
		this.address = data.getString(ADDRESS);
		this.phone = data.getString(PHONE);
		this.longitude = data.getDouble(LONGITUDE);
		this.latitude = data.getDouble(LATITUDE);
		this.firstLevel = data.getString(FIRST_LEVEL);
		this.thirdLevel = data.getString(THIRD_LEVEL);
		this.ratingClean = data.getFloat(RATING_CLEAN);
		this.ratingService = data.getFloat(RATING_SERVICE);
		this.ratingAtmos = data.getFloat(RATING_ATMOS);
		this.ratingFlavor = data.getFloat(RATING_FLAVOR);
		this.ratingUnkonw = data.getFloat(RATING_UNKNOW);
		this.isFavor = data.getBoolean(IS_FAVOR);
		this.isBlocked = data.getBoolean(IS_BLOCKED);
		this.tags = data.getStringArray(TAGS);
	}

	public StoreInfo(long storeId, String googleStoreId, String name,
			String address, String phone, double longitude, double latitude,
			String firstLevel, String thirdLevel, float ratingClean,
			float ratingService, float ratingAtmos, float ratingFlavor,
			float ratingUnknow, boolean isFavor, boolean isBlocked,
			String[] tags) {
		this.storeId = storeId;
		this.googleStoreId = googleStoreId;
		this.name = name;
		this.address = address;
		this.phone = phone;
		this.longitude = longitude;
		this.latitude = latitude;
		this.firstLevel = firstLevel;
		this.thirdLevel = thirdLevel;
		this.ratingClean = ratingClean;
		this.ratingService = ratingService;
		this.ratingAtmos = ratingAtmos;
		this.ratingFlavor = ratingFlavor;
		this.ratingUnkonw = ratingUnknow;
		this.isFavor = isFavor;
		this.isBlocked = isBlocked;
		this.tags = tags;
	}

	public long getStoreId() {
		return storeId;
	}

	public void setStoreId(long storeId) {
		this.storeId = storeId;
	}

	public String getGoogleStoreId() {
		return googleStoreId;
	}

	public void setGoogleStoreId(String googleStoreId) {
		this.googleStoreId = googleStoreId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public String getFirstLevel() {
		return firstLevel;
	}

	public void setFirstLevel(String firstLevel) {
		this.firstLevel = firstLevel;
	}

	public String getThirdLevel() {
		return thirdLevel;
	}

	public void setThirdLevel(String thirdLevel) {
		this.thirdLevel = thirdLevel;
	}

	public float getRatingClean() {
		return ratingClean;
	}

	public void setRatingClean(float ratingClean) {
		this.ratingClean = ratingClean;
	}

	public float getRatingService() {
		return ratingService;
	}

	public void setRatingService(float ratingService) {
		this.ratingService = ratingService;
	}

	public float getRatingAtmos() {
		return ratingAtmos;
	}

	public void setRatingAtmos(float ratingAtmos) {
		this.ratingAtmos = ratingAtmos;
	}

	public float getRatingFlavor() {
		return ratingFlavor;
	}

	public void setRatingFavor(float ratingFlavor) {
		this.ratingFlavor = ratingFlavor;
	}

	public float getRatingUnkonw() {
		return ratingUnkonw;
	}

	public void setRatingUnkonw(float ratingUnkonw) {
		this.ratingUnkonw = ratingUnkonw;
	}

	public String[] getTags() {
		return tags;
	}

	public void setTags(String[] tags) {
		this.tags = tags;
	}

	public boolean isBlocked() {
		return isBlocked;
	}

	public void setBlocked(boolean isBlocked) {
		this.isBlocked = isBlocked;
	}

	public boolean isFavor() {
		return isFavor;
	}

	public void setFavor(boolean isFavor) {
		this.isFavor = isFavor;
	}

	public Bundle save() {
		Bundle data = new Bundle();
		data.putLong(STORE_ID, getStoreId());
		data.putString(GOOGLE_STORE_ID, getGoogleStoreId());
		data.putString(NAME, getName());
		data.putString(ADDRESS, getAddress());
		data.putString(PHONE, getPhone());
		data.putDouble(LONGITUDE, getLongitude());
		data.putDouble(LATITUDE, getLatitude());
		data.putString(FIRST_LEVEL, getFirstLevel());
		data.putString(THIRD_LEVEL, getThirdLevel());
		data.putFloat(RATING_CLEAN, getRatingClean());
		data.putFloat(RATING_SERVICE, getRatingService());
		data.putFloat(RATING_ATMOS, getRatingAtmos());
		data.putFloat(RATING_FLAVOR, getRatingFlavor());
		data.putFloat(RATING_UNKNOW, getRatingUnkonw());
		data.putBoolean(IS_BLOCKED, isBlocked());
		data.putBoolean(IS_FAVOR, isFavor());
		data.putStringArray(TAGS, getTags());
		return data;
	}

	@Override
	public String toString() {
		return new Gson().toJson(StoreInfo.this);
	}
}
