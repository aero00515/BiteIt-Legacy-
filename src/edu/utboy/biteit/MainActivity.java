package edu.utboy.biteit;

import idv.andylin.tw.andyandroidlibs.HandlerCallBack;
import idv.andylin.tw.andyandroidlibs.utils.MyLog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.location.Location;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.VisibleRegion;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import edu.utboy.biteit.asynctasks.PhotoCommentRequest;
import edu.utboy.biteit.asynctasks.StoreInfoRequest;
import edu.utboy.biteit.asynctasks.VolleyTaskManager;
import edu.utboy.biteit.asynctasks.requestqueues.PhotoCommentRequestQueue;
import edu.utboy.biteit.asynctasks.requestqueues.StoreInfoRequestQueue;
import edu.utboy.biteit.models.Friend;
import edu.utboy.biteit.models.MarkerItem;
import edu.utboy.biteit.models.PhotoComment;
import edu.utboy.biteit.models.StoreFriends;
import edu.utboy.biteit.models.StoreInfo;
import edu.utboy.biteit.models.StorePhotoComments;
import edu.utboy.biteit.models.dao.FeedbackPointDAO;
import edu.utboy.biteit.models.dao.FriendDAO;
import edu.utboy.biteit.models.dao.PhotoCommentDAO;
import edu.utboy.biteit.models.dao.StoreInfoDAO;
import edu.utboy.biteit.ui.FriendsInfoActivity;
import edu.utboy.biteit.ui.StoreInfoActivity;
import edu.utboy.biteit.ui.adapters.FragmentViewPagerAdapter;
import edu.utboy.biteit.ui.pages.SlideshowFragment;
import edu.utboy.biteit.ui.pages.SlideshowFragment.OnSlideshowImgClicked;
import edu.utboy.biteit.utils.ConnectionDetector;
import edu.utboy.biteit.utils.MapInfo;

public class MainActivity extends ActionBarActivity implements
		GooglePlayServicesClient.ConnectionCallbacks,
		GooglePlayServicesClient.OnConnectionFailedListener {

	public static final String STORE_MODE_TAG = "store_mode_tag";
	public static final String FRIEND_MODE_TAG = "friend_mode_tag";
	private static final int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
	private static final int GET_CURRENT_LOCATION = 6;
	private static final int GET_STOREFRIENDS_FINISH = 7;
	private static final int SHOW_OPEN_WIFI = 8;
	public static final int GET_ADMIN_AREA = 9;
	private static final double MOVE_OFFSET = 1.3;

	private GoogleMap gMap;
	private CameraPosition cameraPosition;
	private LatLng curMyLocation;
	private Point p1;
	private FragmentViewPagerAdapter slideshowPicAdapter;
	private ViewPager pager;
	private LocationClient mLocClient;
	private StoreInfoDAO storeInfoDao;
	private PhotoCommentDAO photoCommentDao;
	private FeedbackPointDAO feedbackPointDAO;
	private FriendDAO friendDao;
	private MapInfo mapInfo;
	private List<Fragment> slideshowPicFragments;
	private List<MarkerItem> markerItems;
	private List<StoreInfo> storeInfos;
	private List<Friend> friends;
	private List<StoreFriends> storeFriendsList;
	private List<Long> friendIds;
	private Map<String, StorePhotoComments> storePhotoComments;
	private boolean isInternetPresent = false;
	private boolean isFirstClickSlideshow = true;
	private boolean isOverMoveOffset = false;
	private boolean isFirstAddMarker = true;
	private int preClickedMarker;
	private int lastStoreInfoSize;
	private String adminArea = "";
	private String locality = "";
	private String latLngString;
	private String curMode = STORE_MODE_TAG;

	/**
	 * Fragment managing the behaviors, interactions and presentation of the
	 * navigation drawer.
	 */
	private NavigationDrawerFragment mNavigationDrawerFragment;

	/**
	 * Used to store the last screen title. For use in
	 * {@link #restoreActionBar()}.
	 */
	private CharSequence mTitle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		slideshowPicFragments = new ArrayList<Fragment>();
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				this).build();
		ImageLoader.getInstance().init(config);
		ConnectionDetector cd = new ConnectionDetector(getApplicationContext());
		slideshowPicAdapter = new FragmentViewPagerAdapter(
				getSupportFragmentManager(), slideshowPicFragments);
		markerItems = new ArrayList<MarkerItem>();
		storeInfos = new ArrayList<StoreInfo>();
		storePhotoComments = new HashMap<String, StorePhotoComments>();
		mLocClient = new LocationClient(this, this, this);
		friendIds = new ArrayList<Long>();
		storeFriendsList = new ArrayList<StoreFriends>();
		storeInfoDao = new StoreInfoDAO(MainActivity.this);
		photoCommentDao = new PhotoCommentDAO(MainActivity.this);
		feedbackPointDAO = new FeedbackPointDAO(getApplicationContext());
		friendDao = new FriendDAO(this);
		mapInfo = new MapInfo(handler);
		VolleyTaskManager.getInstance().init(getAssets());
		storeInfoDao.open();
		photoCommentDao.open();
		friendDao.open();
		friends = friendDao.get();
		for (Friend f : friends) {
			friendIds.add(f.getId());
		}
		mNavigationDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager()
				.findFragmentById(R.id.navigation_drawer);
		pager = (ViewPager) findViewById(R.id.pager);
		mNavigationDrawerFragment.setUp(R.id.navigation_drawer,
				(DrawerLayout) findViewById(R.id.drawer_layout));
		mTitle = getTitle();

		// Set up the drawer.

		pager.setAdapter(slideshowPicAdapter);
		pager.setPageMargin(5);
		// If hardware acceleration is enabled, you should also remove
		// clipping on the pager for its children.
		pager.setClipChildren(false);

		isInternetPresent = cd.isConnectingToInternet();
		if (!isInternetPresent) {
			Toast.makeText(this, getString(R.string.open_wifi),
					Toast.LENGTH_LONG).show();
		} else {
			// Check google play service available
			int status = GooglePlayServicesUtil
					.isGooglePlayServicesAvailable(this);
			if (status != ConnectionResult.SUCCESS) {
				GooglePlayServicesUtil.getErrorDialog(status, this,
						CONNECTION_FAILURE_RESOLUTION_REQUEST).show();
			} else {
				mLocClient.connect();
				initGoogleMap();
			}
		}
	}

	private void initGoogleMap() {
		gMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
				.getMap();
		gMap.getUiSettings().setMyLocationButtonEnabled(true);
		gMap.setMyLocationEnabled(true);
		gMap.getUiSettings().setRotateGesturesEnabled(false);
		gMap.setOnMarkerClickListener(new OnMarkerClickListener() {
			@Override
			public boolean onMarkerClick(Marker arg0) {
				arg0.showInfoWindow();
				String name = arg0.getTitle();
				for (MarkerItem m : markerItems) {
					if (m.getName().equals(name)) {
						saveStoreInfoDbThread(m.getInfo());
						savePhotoCommentDbThread(storePhotoComments.get(
								String.valueOf(m.getStoreId()))
								.getPhotocomments());
					}
				}
				return false;
			}
		});
		gMap.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {
			@Override
			public void onInfoWindowClick(Marker arg0) {
				String name = arg0.getTitle();
				long storeId = 0;
				for (MarkerItem m : markerItems) {
					if (m.getName().equals(name)) {
						if (storeInfoDao.get(m.getStoreId()) != null) {
							storeId = m.getStoreId();
						}
					}
				}
				Intent intent = new Intent();
				intent.setClass(MainActivity.this, StoreInfoActivity.class);
				intent.putExtra(StoreInfoActivity.STORE_ID_TAG, storeId);
				MainActivity.this.startActivity(intent);
			}
		});
	}

	private OnCameraChangeListener cameraChangeListener = new OnCameraChangeListener() {
		@Override
		public void onCameraChange(CameraPosition position) {
			double lat = position.target.latitude;
			double lng = position.target.longitude;
			if (gMap.getCameraPosition().zoom < 15.8f) {
				gMap.animateCamera(CameraUpdateFactory.zoomTo(15.8f));
			} else {
				if (isOverMoveOffset && isInternetPresent) {
					MyLog.d(MainActivity.class, "Over the moving offset");
					isOverMoveOffset = false;

					StringBuilder latLng = new StringBuilder();
					latLng.append(String.valueOf(lng)).append(",")
							.append(String.valueOf(lat));
					latLngString = latLng.toString();
					adminArea = "";
					locality = "";
					mapInfo.getAdminArea(lat, lng, MainActivity.this);

					VisibleRegion vr = gMap.getProjection().getVisibleRegion();
					double left = vr.latLngBounds.southwest.longitude;
					double top = vr.latLngBounds.northeast.latitude;

					float leftRadius = mapInfo.getRadius(lat, lng, lat, left);
					float topRadius = mapInfo.getRadius(lat, lng, top, lng);
					String curViewRadius = (leftRadius < topRadius) ? String
							.valueOf(leftRadius) : String.valueOf(topRadius);

					isFirstAddMarker = true;
					cancelAllQueue();
					Bundle storePrams = new Bundle();
					try {
						storePrams = StoreInfoRequest.getInstance()
								.getNearbyStoreParams(adminArea, locality,
										latLngString, curViewRadius);
						requestStoreInfo(storePrams);
					} catch (JSONException e) {
						e.printStackTrace();
					}

				}
			}
		}
	};

	private void cancelAllQueue() {
		StoreInfoRequestQueue.getSampleRequestQueue(this).getRequestQueue()
				.cancelAll(new RequestQueue.RequestFilter() {
					@Override
					public boolean apply(Request<?> request) {
						MyLog.d(MainActivity.class,
								"cancel all the storeInfo requests");
						return true;
					}
				});
		PhotoCommentRequestQueue.getSampleRequestQueue(this).getRequestQueue()
				.cancelAll(new RequestQueue.RequestFilter() {
					@Override
					public boolean apply(Request<?> request) {
						MyLog.d(MainActivity.class,
								"cancel all the photoComment requests");
						return true;
					}
				});
	}

	private void requestStoreInfo(Bundle storePrams) {
		StoreInfoRequest.getInstance().storeInfoRequest(this, storePrams,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						List<StoreInfo> infos = StoreInfoRequest.getInstance()
								.getStoreInfoByResponse(response);
						List<StoreInfo> tempInfos = new ArrayList<StoreInfo>();
						List<Long> storeIds = new ArrayList<Long>();
						if ((infos.size() != 0) && !infos.get(0).equals(null)) {
							if (isFirstAddMarker) {
								isFirstAddMarker = false;
								isFirstClickSlideshow = true;
								preClickedMarker = 0;
								cleanOutBoundMarkers();
								lastStoreInfoSize = storeInfos.size();
							}
							for (StoreInfo in : storeInfos) {
								storeIds.add(in.getStoreId());
							}
							for (StoreInfo responseInfo : infos) {
								if (!storeIds.contains(responseInfo
										.getStoreId())) {
									tempInfos.add(responseInfo);
								}
							}
							storeInfos.addAll(tempInfos);
							for (int i = lastStoreInfoSize; i < storeInfos
									.size(); i++) {
								storePhotoComments.put(
										String.valueOf(storeInfos.get(i)
												.getStoreId()),
										new StorePhotoComments(storeInfos
												.get(i),
												new ArrayList<PhotoComment>()));

								Bundle params = new Bundle();
								params = PhotoCommentRequest
										.getInstance()
										.createPhotoCommentBundleParamsByStoreId(
												storeInfos.get(i).getStoreId());
								requestPhotoComment(params, storeInfos.get(i),
										i);
							}
							addMarker(tempInfos);
							initSlideShowFragment();

							if (StoreInfoRequest.getInstance()
									.getStoreInfoNextPage() != null) {
								Bundle storePrams = new Bundle();
								storePrams = StoreInfoRequest
										.getInstance()
										.getNextPrevParams(
												StoreInfoRequest.getInstance()
														.getStoreInfoNextPage());
								requestStoreInfo(storePrams);
							}

							lastStoreInfoSize = storeInfos.size();
						}

					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						if (error != null) {
							MyLog.e(MainActivity.class, error.toString());
						}
					}
				});
	}

	private void requestPhotoComment(Bundle params, final StoreInfo info,
			final int pos) {
		PhotoCommentRequest.getInstance().getPhotoComments(this, params,
				new Response.Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {

						List<PhotoComment> photoComments = PhotoCommentRequest
								.getInstance().getPhotoCommentRequest(response);

						String key = String.valueOf(info.getStoreId());
						if (storePhotoComments.get(key) != null) {
							storePhotoComments.get(key).getPhotocomments()
									.addAll(photoComments);

							if (PhotoCommentRequest.getInstance()
									.getPhotoCommentNextPage() != null) {
								Bundle params = new Bundle();
								params = PhotoCommentRequest
										.getInstance()
										.getNextPrevParams(
												PhotoCommentRequest
														.getInstance()
														.getPhotoCommentNextPage());
								requestPhotoComment(params, info, pos);
							} else {
								setSlideshowPic(curMode, photoComments, pos);
								slideshowPicAdapter.notifyDataSetChanged();
							}
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						if (error != null) {
							MyLog.e(MainActivity.class, "" + error.getMessage());
						}
					}
				});
	}

	private void addMarker(List<StoreInfo> infos) {
		MyLog.d(MainActivity.class, "add Marker");
		List<MarkerItem> ms = new ArrayList<MarkerItem>();
		for (StoreInfo info : infos) {
			ms.add(new MarkerItem(info.getLatitude(), info.getLongitude(), info
					.getName(), info.getStoreId(), info));
		}

		markerItems.addAll(ms);

		for (MarkerItem m : ms) {
			Marker marker = gMap.addMarker(new MarkerOptions()
					.position(m.getLatlng())
					.icon(BitmapDescriptorFactory
							.fromResource(R.drawable.yellow_point)).alpha(1.0f)
					.title(m.getName()));

			m.setMarker(marker);
		}
	}

	private void initSlideShowFragment() {
		for (int i = lastStoreInfoSize; i < storeInfos.size(); i++) {
			Bundle data = new Bundle();
			data.putString(SlideshowFragment.SLIDESHOW_PIC_URI, "");
			data.putInt(SlideshowFragment.SlIDESHOW_PIC_NUMBER, i);
			data.putString(SlideshowFragment.SLIDESHOW_PIC_MODE, curMode);
			if (curMode == STORE_MODE_TAG) {
				data.putString(SlideshowFragment.SLIDESHOW_PIC_NAME, storeInfos
						.get(i).getName());
			}
			Fragment fragment = new SlideshowFragment(onSlideshowImgClicked);
			fragment.setArguments(data);
			slideshowPicFragments.add(fragment);
		}
		slideshowPicAdapter.notifyDataSetChanged();
	}

	private void resetSlideshow() {
		slideshowPicFragments.clear();
		slideshowPicAdapter.notifyDataSetChanged();
		for (int i = 0; i < storeInfos.size(); i++) {
			String uri = "";
			Bundle data = new Bundle();
			String key = String.valueOf(storeInfos.get(0).getStoreId());
			if (curMode == STORE_MODE_TAG) {
				PhotoComment pc = storePhotoComments.get(key)
						.getPhotocomments().get(0);
				if (!pc.getUri().toString().equals("")) {
					uri = VolleyTaskManager.getInstance().getMediaRoot()
							+ pc.getUri();
				}

			} else if (curMode == FRIEND_MODE_TAG) {
				List<Friend> fs = new ArrayList<Friend>();
				for (PhotoComment photoComment : storePhotoComments.get(key)
						.getPhotocomments()) {
					if (friendIds.contains(photoComment.getUserId())) {
						for (Friend f : friends) {
							if (f.getId() == photoComment.getUserId()) {
								fs.add(f);
							}
						}
					}
				}
				if (fs.size() != 0) {
					uri = fs.get(0).getPhoto();
				}
			}
			data.putString(SlideshowFragment.SLIDESHOW_PIC_URI, uri);
			data.putInt(SlideshowFragment.SlIDESHOW_PIC_NUMBER, i);
			data.putString(SlideshowFragment.SLIDESHOW_PIC_MODE, curMode);
			if (curMode == STORE_MODE_TAG) {
				data.putString(SlideshowFragment.SLIDESHOW_PIC_NAME, storeInfos
						.get(i).getName());
			}
			Fragment fragment = new SlideshowFragment(onSlideshowImgClicked);
			fragment.setArguments(data);
			slideshowPicFragments.add(fragment);

		}
		slideshowPicAdapter.notifyDataSetChanged();
	}

	// TODO there is only one picture in every fragment
	private void setSlideshowPic(String modeTag,
			List<PhotoComment> photoComments, int pos) {
		curMode = (modeTag == STORE_MODE_TAG) ? STORE_MODE_TAG
				: FRIEND_MODE_TAG;
		String uri = "";
		Bundle data = new Bundle();
		if (modeTag == STORE_MODE_TAG) {
			if (!photoComments.get(0).getUri().toString().equals("")) {
				uri = VolleyTaskManager.getInstance().getMediaRoot()
						+ photoComments.get(0).getUri();
			}
		} else if (modeTag == FRIEND_MODE_TAG) {
			List<Friend> fs = new ArrayList<Friend>();
			for (PhotoComment photoComment : photoComments) {
				if (friendIds.contains(photoComment.getUserId())) {
					for (Friend f : friends) {
						if (f.getId() == photoComment.getUserId()) {
							fs.add(f);
						}
					}
				}
			}
			if (fs.size() != 0) {
				uri = fs.get(0).getPhoto();
			}
		}
		data.putString(SlideshowFragment.SLIDESHOW_PIC_URI, uri);
		data.putInt(SlideshowFragment.SlIDESHOW_PIC_NUMBER, pos);
		if (curMode == STORE_MODE_TAG) {
			data.putString(SlideshowFragment.SLIDESHOW_PIC_NAME, storeInfos
					.get(pos).getName());
		}
		data.putString(SlideshowFragment.SLIDESHOW_PIC_MODE, modeTag);
		slideshowPicFragments.get(pos).getArguments().putAll(data);
	}

	private void getStoreFriend() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				for (int i = 0; i < storeInfos.size(); i++) {
					List<Friend> fs = new ArrayList<Friend>();
					Set<Long> photofsId = new HashSet<Long>();
					String key = String.valueOf(storeInfos.get(i).getStoreId());
					List<PhotoComment> phoComms = storePhotoComments.get(key)
							.getPhotocomments();
					for (int j = 0; j < phoComms.size(); j++) {
						if (friendIds.contains(phoComms.get(j).getUserId())) {
							photofsId.add(phoComms.get(j).getUserId());
						}
					}

					for (Friend f : friends) {
						if (photofsId.contains(f.getId())) {
							fs.add(f);
						}
					}
					storeFriendsList
							.add(new StoreFriends(storeInfos.get(i), fs));
				}
				sendMsg(GET_STOREFRIENDS_FINISH);
			}
		}) {
		}.start();
	}

	private synchronized void saveStoreInfoDb(StoreInfo info) {
		if (storeInfoDao.get(info.getStoreId()) == null) {
			storeInfoDao.save(info);
		} else {
			storeInfoDao.update(info);
		}
	}

	private synchronized void savePhotoCommentDb(
			List<PhotoComment> photoComments) {
		List<PhotoComment> pcs = photoCommentDao.get();
		List<String> pcsId = new ArrayList<String>();
		for (PhotoComment pc : pcs) {
			pcsId.add(pc.getUri().toString());
		}
		for (PhotoComment photoComment : photoComments) {
			if (pcsId.contains(photoComment.getUri().toString())) {
				photoCommentDao.update(photoComment);
			} else {
				photoCommentDao.save(photoComments);
			}
		}
	}

	private void savePhotoCommentDbThread(final List<PhotoComment> photoComments) {
		new Thread() {
			@Override
			public void run() {
				savePhotoCommentDb(photoComments);
				super.run();
			}
		}.start();
	}

	private void saveStoreInfoDbThread(final StoreInfo info) {
		new Thread() {
			@Override
			public void run() {
				saveStoreInfoDb(info);
				super.run();
			}
		}.start();
	}

	private void cleanOutBoundMarkers() {
		Projection projection = gMap.getProjection();
		LatLngBounds bounds = projection.getVisibleRegion().latLngBounds;
		int preSize = markerItems.size();
		List<MarkerItem> outItems = new ArrayList<MarkerItem>();
		for (MarkerItem m : markerItems) {
			if (!bounds.contains(m.getLatlng())) {
				m.getMarker().remove();
				storeInfos.remove(m.getInfo());
				storePhotoComments.remove(String.valueOf(m.getStoreId()));
				outItems.add(m);
			}
		}
		markerItems.removeAll(outItems);
		if (markerItems.size() != preSize) {
			if (!storePhotoComments
					.get(String.valueOf(storeInfos.get(0).getStoreId()))
					.getPhotocomments().isEmpty()) {
				resetSlideshow();
			}
		}
	}

	public HandlerCallBack handler = new HandlerCallBack() {
		@Override
		public void handleDataMessage(Message msg) {
			switch (msg.what) {
			case GET_CURRENT_LOCATION:
				isOverMoveOffset = true;
				gMap.setOnCameraChangeListener(cameraChangeListener);
				cameraPosition = new CameraPosition.Builder()
						.target(curMyLocation).zoom(16).build();
				gMap.animateCamera(CameraUpdateFactory
						.newCameraPosition(cameraPosition));
				if (mLocClient.isConnected()) {
					mLocClient.disconnect();
				}
				break;
			case GET_STOREFRIENDS_FINISH:
				Bundle data = new Bundle();
				for (int i = 0; i < storeFriendsList.size(); i++) {
					String uri = "";
					if (storeFriendsList.get(i).getFriends().size() != 0) {
						uri = storeFriendsList.get(i).getFriends().get(0)
								.getPhoto();
					}
					data.putString(SlideshowFragment.SLIDESHOW_PIC_URI, uri);
					data.putInt(SlideshowFragment.SlIDESHOW_PIC_NUMBER, i);
					data.putString(SlideshowFragment.SLIDESHOW_PIC_MODE,
							curMode);
					slideshowPicFragments.get(i).getArguments().putAll(data);
				}
				slideshowPicAdapter.notifyDataSetChanged();
				break;
			case SHOW_OPEN_WIFI:
				Toast.makeText(MainActivity.this,
						getString(R.string.getlocation_fail), Toast.LENGTH_LONG)
						.show();
				break;
			case GET_ADMIN_AREA:
				Bundle bundle = msg.getData();
				String[] admins = bundle.getStringArray("admins");
				adminArea = admins[0];
				locality = admins[1];
				break;
			}
		}
	};

	private void sendMsg(int msgTag) {
		Message message = new Message();
		message.what = msgTag;
		handler.dataHandler.sendMessage(message);
	}

	private OnSlideshowImgClicked onSlideshowImgClicked = new OnSlideshowImgClicked() {
		@Override
		public void onImgClicked(int mNum) {
			if ((storeInfos.size() == storePhotoComments.size())
					&& (mNum < storeInfos.size())) {
				if ((preClickedMarker == mNum) && !isFirstClickSlideshow) {
					Intent intent = new Intent();
					if (curMode == STORE_MODE_TAG) {
						intent.setClass(MainActivity.this,
								StoreInfoActivity.class);
					} else if (curMode == FRIEND_MODE_TAG) {
						intent.setClass(MainActivity.this,
								FriendsInfoActivity.class);
					}
					if (storeInfoDao.get(storeInfos.get(mNum).getStoreId()) != null) {
						intent.putExtra(StoreInfoActivity.STORE_ID_TAG,
								storeInfos.get(mNum).getStoreId());
						MainActivity.this.startActivity(intent);
					}

				} else {
					isFirstClickSlideshow = false;
					saveStoreInfoDbThread(storeInfos.get(mNum));
					savePhotoCommentDbThread(storePhotoComments.get(
							String.valueOf(storeInfos.get(mNum).getStoreId()))
							.getPhotocomments());

					markerItems
							.get(preClickedMarker)
							.getMarker()
							.setIcon(
									BitmapDescriptorFactory
											.fromResource(R.drawable.yellow_point));

					markerItems
							.get(mNum)
							.getMarker()
							.setIcon(
									BitmapDescriptorFactory
											.fromResource(R.drawable.red_point));
					markerItems.get(mNum).getMarker().showInfoWindow();

					preClickedMarker = mNum;

				}
			}
		}
	};

	public void onSectionAttached(int number) {
		switch (number) {
		case 1:
			mTitle = getString(R.string.title_section1);
			break;
		case 2:
			mTitle = getString(R.string.title_section2);
			break;
		case 3:
			mTitle = getString(R.string.title_section3);
			break;
		}
	}

	public void restoreActionBar() {
		ActionBar actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setTitle(mTitle);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (!mNavigationDrawerFragment.isDrawerOpen()) {
			// Only show items in the action bar relevant to this screen
			// if the drawer is not showing. Otherwise, let the drawer
			// decide what to show in the action bar.
			getMenuInflater().inflate(R.menu.main, menu);
			restoreActionBar();
			return true;
		}
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		switch (id) {
		case R.id.action_settings:
			return true;
		case R.id.action_new:
			Intent foundNewIntent = new Intent();
			foundNewIntent.setAction(FoundNewStoreActivity.FOUND_NEW);
			foundNewIntent.setClass(getApplicationContext(),
					FoundNewStoreActivity.class);
			startActivity(foundNewIntent);
			return true;
		case R.id.action_switch:
			if (storeInfos.size() == storePhotoComments.size()) {
				if (curMode == STORE_MODE_TAG) {
					curMode = FRIEND_MODE_TAG;
					storeFriendsList.clear();
					getStoreFriend();
				} else if ((curMode == FRIEND_MODE_TAG)) {
					curMode = STORE_MODE_TAG;
					for (int i = 0; i < storeInfos.size(); i++) {
						String key = String.valueOf(storeInfos.get(i)
								.getStoreId());
						try {
							setSlideshowPic(curMode, storePhotoComments
									.get(key).getPhotocomments(), i);
						} catch (Exception e) {
						}
					}
					slideshowPicAdapter.notifyDataSetChanged();
				}
			}
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onDestroy() {
		storeInfoDao.close();
		photoCommentDao.close();
		feedbackPointDAO.close();
		friendDao.close();
		gMap.setOnCameraChangeListener(null);
		gMap.setOnMyLocationChangeListener(null);
		if (mLocClient.isConnected()) {
			mLocClient.disconnect();
		}
		android.os.Process.killProcess(android.os.Process.myPid());
		super.onDestroy();
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		private static final String ARG_SECTION_NUMBER = "section_number";

		/**
		 * Returns a new instance of this fragment for the given section number.
		 */
		public static PlaceholderFragment newInstance(int sectionNumber) {
			PlaceholderFragment fragment = new PlaceholderFragment();
			Bundle args = new Bundle();
			args.putInt(ARG_SECTION_NUMBER, sectionNumber);
			fragment.setArguments(args);
			return fragment;
		}

		public PlaceholderFragment() {
		}

		@Override
		public void onAttach(Activity activity) {
			super.onAttach(activity);
			((MainActivity) activity).onSectionAttached(getArguments().getInt(
					ARG_SECTION_NUMBER));
		}
	}

	@Override
	public void onConnectionFailed(ConnectionResult arg0) {
		if (arg0.hasResolution()) {
			MyLog.e(MainActivity.class, "error" + arg0.getErrorCode());
		}
	}

	@Override
	public void onConnected(Bundle arg0) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				Location loc = mLocClient.getLastLocation();
				int count = 0;
				while (true) {
					count++;
					if (count == 10) {
						curMyLocation = new LatLng(25.036619, 121.514073);
						sendMsg(SHOW_OPEN_WIFI);
						sendMsg(GET_CURRENT_LOCATION);
						break;
					}
					if (loc == null) {
						loc = mLocClient.getLastLocation();
					} else {
						curMyLocation = new LatLng(loc.getLatitude(),
								loc.getLongitude());
						sendMsg(GET_CURRENT_LOCATION);
						break;
					}
				}
			}
		}) {
		}.start();
	}

	@Override
	public void onDisconnected() {
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		if (ev.getAction() == MotionEvent.ACTION_DOWN) {
			isOverMoveOffset = false;
			p1 = new Point((int) ev.getX(), (int) ev.getY());
		}
		if (ev.getAction() == MotionEvent.ACTION_MOVE) {
			double distance = 0;
			distance = (Math.sqrt(Math.pow(p1.x - ev.getX(), 2)
					+ Math.pow(p1.y - ev.getY(), 2)) / 100);
			if (distance >= MOVE_OFFSET) {
				p1.set((int) ev.getX(), (int) ev.getY());
				isOverMoveOffset = true;
			}
		}
		return super.dispatchTouchEvent(ev);
	}

}
