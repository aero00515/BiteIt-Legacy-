package edu.utboy.biteit.models;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

public class MarkerItem {

	private double lat;
	private double lng;
	private boolean isInBounds;
	private Marker marker;
	private String name;
	private StoreInfo info;
	private long storeId;

	public MarkerItem(double lat, double lng, String name, long storeId,
			StoreInfo info) {
		this.lat = lat;
		this.lng = lng;
		this.name = name;
		this.storeId = storeId;
		this.info = info;
		isInBounds = false;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLng() {
		return lng;
	}

	public void setLng(double lng) {
		this.lng = lng;
	}

	public boolean isInBounds() {
		return isInBounds;
	}

	public void setInBounds(boolean isInBounds) {
		this.isInBounds = isInBounds;
	}

	public LatLng getLatlng() {
		return new LatLng(lat, lng);
	}

	public Marker getMarker() {
		return marker;
	}

	public void setMarker(Marker marker) {
		this.marker = marker;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getStoreId() {
		return storeId;
	}

	public void setStoreId(long storeId) {
		this.storeId = storeId;
	}

	public StoreInfo getInfo() {
		return info;
	}

	public void setInfo(StoreInfo info) {
		this.info = info;
	}
}
