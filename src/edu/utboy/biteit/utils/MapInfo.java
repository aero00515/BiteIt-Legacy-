package edu.utboy.biteit.utils;

import idv.andylin.tw.andyandroidlibs.HandlerCallBack;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Message;
import edu.utboy.biteit.MainActivity;

public class MapInfo {

	private HandlerCallBack handler;

	public MapInfo(HandlerCallBack handler) {
		this.handler = handler;
	}

	public void getAdminArea(final double lat, final double lng,
			final Context ctx) {
		new Thread() {
			@Override
			public void run() {
				String[] admins = new String[2];

				Geocoder mLocation = new Geocoder(ctx, Locale.getDefault());

				if (mLocation != null && Geocoder.isPresent()) {
					try {
						List<Address> components = mLocation.getFromLocation(
								lat, lng, 1);
						if (!components.isEmpty() && components != null) {
							admins[0] = components.get(0).getAdminArea();
							admins[1] = components.get(0).getLocality();
						} else {
							admins[0] = "";
							admins[1] = "";
						}
						Message message = new Message();
						message.what = MainActivity.GET_ADMIN_AREA;
						Bundle bundle = new Bundle();
						bundle.putStringArray("admins", admins);
						message.setData(bundle);
						handler.dataHandler.sendMessage(message);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				super.run();
			}
		}.start();
	}

	public float getRadius(double centerLat, double centerLng,
			double nearLeftLat, double nearLeftLng) {
		float radius;

		Location centerLoc = new Location("");
		centerLoc.setLatitude(centerLat);
		centerLoc.setLongitude(centerLng);

		Location nearleftLoc = new Location("");
		nearleftLoc.setLatitude(nearLeftLat);
		nearleftLoc.setLongitude(nearLeftLng);

		radius = centerLoc.distanceTo(nearleftLoc);

		return radius;
	}
}
