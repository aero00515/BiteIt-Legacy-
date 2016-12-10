package edu.utboy.biteit.ui;

import idv.andylin.tw.andyandroidlibs.utils.MyLog;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.app.IntentService;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import edu.utboy.biteit.R;

public class MapChoseLocationActivity extends FragmentActivity {

	public static final String NOW_LATITUDE = "current_latitude";
	public static final String NOW_LONGITUDE = "current_longitude";
	public static final String NOW_ADDRESS = "current_address";
	public static final String NOW_FIRST = "current_first_level";
	public static final String NOW_THIRD = "current_third_level";
	public static final String NOW_ZOOM = "now_zoom";

	private static GoogleMap googleMap;
	private static Marker current;
	private static Geocoder gc;
	private static String address = "";
	private static String firstLevel = "";
	private static String thirdLevel = "";
	private static double currentLat;
	private static double currentLng;
	private Button confirm;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_map_chooser);
		confirm = (Button) findViewById(R.id.activity_map_chooser_confirm);

		googleMap = ((SupportMapFragment) getSupportFragmentManager()
				.findFragmentById(R.id.activity_map_chooser_support_map))
				.getMap();

		googleMap.getUiSettings().setMyLocationButtonEnabled(true);
		googleMap.setMyLocationEnabled(true);
		googleMap.getUiSettings().setZoomGesturesEnabled(true);
		gc = new Geocoder(getApplicationContext(), Locale.TRADITIONAL_CHINESE);
		if (arg0 != null && !arg0.isEmpty()) {
			MyLog.d(MapChoseLocationActivity.class, "saved value");
			currentLat = arg0.getDouble(NOW_LATITUDE, 0);
			currentLng = arg0.getDouble(NOW_LONGITUDE, 0);
			address = arg0.getString(NOW_ADDRESS, "");
			firstLevel = arg0.getString(NOW_FIRST, "");
			thirdLevel = arg0.getString(NOW_THIRD, "");
			LatLng target = new LatLng(currentLat, currentLng);
			current = googleMap.addMarker(new MarkerOptions().position(target));
			changeMapLocation(target, arg0.getFloat(NOW_ZOOM, 18));
		} else if (getIntent() != null && getIntent().getExtras() != null) {
			MyLog.d(MapChoseLocationActivity.class, "has value");
			Bundle data = getIntent().getExtras();
			currentLat = data.getDouble(NOW_LATITUDE, 0);
			currentLng = data.getDouble(NOW_LONGITUDE, 0);
			LatLng target = new LatLng(currentLat, currentLng);
			current = googleMap.addMarker(new MarkerOptions().position(target));
			changeMapLocation(target);
		} else {
			MyLog.d(MapChoseLocationActivity.class, "start service");
			Intent locationIntent = new Intent();
			locationIntent.setClass(getApplicationContext(),
					LocationService.class);
			startService(locationIntent);
		}
		confirm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent resultIntent = new Intent();
				Bundle data = new Bundle();
				data.putDouble(NOW_LATITUDE, currentLat);
				data.putDouble(NOW_LONGITUDE, currentLng);
				data.putString(NOW_ADDRESS, address);
				data.putString(NOW_FIRST, firstLevel);
				data.putString(NOW_THIRD, thirdLevel);
				resultIntent.putExtras(data);
				setResult(RESULT_OK, resultIntent);
				finish();
			}
		});
		googleMap.setOnMapClickListener(mapClickListener);
	}

	private static void setUp(LatLng currentLatLng) {
		currentLat = currentLatLng.latitude;
		currentLng = currentLatLng.longitude;
		if (current == null) {
			current = googleMap.addMarker(new MarkerOptions()
					.position(currentLatLng));
		} else {
			current.setPosition(currentLatLng);
		}
		changeMapLocation(currentLatLng);
		getLocationAddress(currentLatLng);
	}

	private static void changeMapLocation(LatLng target, float zoom) {
		CameraPosition cameraPosition = new CameraPosition.Builder()
				.target(target).zoom(zoom).build();

		googleMap.animateCamera(CameraUpdateFactory
				.newCameraPosition(cameraPosition));
	}

	private static void changeMapLocation(LatLng target) {
		float zoom = googleMap.getCameraPosition().zoom;
		if (zoom < 18) {
			zoom = 18;
		}
		changeMapLocation(target, zoom);
	}

	private static void getLocationAddress(LatLng latlna) {
		Double longitude = latlna.longitude;
		Double latitude = latlna.latitude;

		try {
			List<Address> lstAddress = gc.getFromLocation(latitude, longitude,
					1);
			address = lstAddress.get(0).getAddressLine(0);
			firstLevel = lstAddress.get(0).getAdminArea();
			thirdLevel = lstAddress.get(0).getLocality();
			MyLog.d(MapChoseLocationActivity.class, "Latitude: " + latitude);
			MyLog.d(MapChoseLocationActivity.class, "Longitude: " + longitude);
			MyLog.d(MapChoseLocationActivity.class, "Address: " + address);
			MyLog.d(MapChoseLocationActivity.class, "firstLevel: " + firstLevel);
			MyLog.d(MapChoseLocationActivity.class, "thirdLevel: " + thirdLevel);
		} catch (IOException e) {
			if (e.getMessage().contains("Unable to parse response from server")) {
				MyLog.w(MapChoseLocationActivity.class, e.getMessage());
			}
		}

	}

	private OnMapClickListener mapClickListener = new OnMapClickListener() {

		@Override
		public void onMapClick(LatLng currentLatLng) {
			setUp(currentLatLng);
		}
	};

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putString(NOW_ADDRESS, address);
		outState.putString(NOW_FIRST, firstLevel);
		outState.putString(NOW_THIRD, thirdLevel);
		outState.putDouble(NOW_LATITUDE, currentLat);
		outState.putDouble(NOW_LONGITUDE, currentLng);
		outState.putFloat(NOW_ZOOM, googleMap.getCameraPosition().zoom);
		super.onSaveInstanceState(outState);
	};

	public static class LocationService extends IntentService implements
			GooglePlayServicesClient.ConnectionCallbacks,
			GooglePlayServicesClient.OnConnectionFailedListener {
		private LocationClient mLocationClient;

		public LocationService() {
			super("LocationService");
		}

		private LatLng getLocation() {
			Location location = mLocationClient.getLastLocation();
			MyLog.d(MapChoseLocationActivity.class, "Finding location...");
			while (location == null) {
				location = mLocationClient.getLastLocation();
			}
			MyLog.d(MapChoseLocationActivity.class, "... Get location");
			Double longitude = location.getLongitude();
			Double latitude = location.getLatitude();

			return new LatLng(latitude, longitude);
		}

		@Override
		public void onConnectionFailed(ConnectionResult arg0) {
			if (arg0.hasResolution()) {
				MyLog.e(MapChoseLocationActivity.class,
						"error" + arg0.getErrorCode());
			}

		}

		@Override
		public void onConnected(Bundle arg0) {
			MyLog.d(MapChoseLocationActivity.class, "Connected");
			LatLng currentLatLng = getLocation();
			setUp(currentLatLng);
		}

		@Override
		public void onDisconnected() {
		}

		@Override
		protected void onHandleIntent(Intent intent) {
			mLocationClient = new LocationClient(this, this, this);
			mLocationClient.connect();
			MyLog.d(MapChoseLocationActivity.class, "start connect");
		}

	}

}
