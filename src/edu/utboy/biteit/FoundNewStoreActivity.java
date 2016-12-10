package edu.utboy.biteit;

import idv.andylin.tw.andyandroidlibs.utils.MyLog;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.nostra13.universalimageloader.core.ImageLoader;

import edu.utboy.biteit.asynctasks.FoundNewStoreRequest;
import edu.utboy.biteit.asynctasks.VolleyTaskManager;
import edu.utboy.biteit.models.FeedbackPoint;
import edu.utboy.biteit.models.NewStore;
import edu.utboy.biteit.models.PhotoComment;
import edu.utboy.biteit.models.StoreInfo;
import edu.utboy.biteit.models.dao.FeedbackPointDAO;
import edu.utboy.biteit.models.dao.PhotoCommentDAO;
import edu.utboy.biteit.models.dao.StoreInfoDAO;
import edu.utboy.biteit.ui.MapChoseLocationActivity;
import edu.utboy.biteit.ui.StoreInfoActivity;
import edu.utboy.biteit.ui.pages.PhotoCommentFragment;
import edu.utboy.biteit.utils.AuthUser;

public class FoundNewStoreActivity extends FragmentActivity {

	public static final String FOUND_NEW = "found_new";
	public static final String REPORT_ERROR = "report_error";

	public static final String CURRENT_TYPE = "current_type";
	public static final String CURRENT_STORE_ID = "current_store_id";
	public static final String CURRENT_STORE_INFO = "current_store_info";
	public static final String CURRENT_PHOTOCOMMENTS = "current_photocomments";

	public static final String CURRENT_NEW_STORE = "current_new_store";
	public static final String CURRENT_PHOTOS = "current_photos";
	public static final String ORI_NAME = "ori_store_name";
	public static final String ORI_ADDRESSE = "ori_store_address";
	public static final String ORI_PHONE = "ori_store_phone";

	public static final int TAKE_PICTURE = 0;
	public static final int FIND_LOCATION = 1;

	private StoreInfoDAO storeInfoDAO;
	private PhotoCommentDAO photoCommentDAO;

	private StoreInfo storeInfo;
	private List<PhotoComment> photoComments;
	private NewStore newStore;
	private List<NewStore.Photo> photos;

	private List<PhotoFragment> mPhotoFragments;
	private List<Boolean> checkedList;
	private ViewPager photoPager;
	private MyAdapter adapter;
	private EditText storeNameEdit;
	private EditText storeAddressEdit;
	private EditText storePhoneEdit;
	private ImageView nameWarningView;
	private ImageView addressWarningView;
	private ImageView phoneWarningView;

	private Button reportNotExistBtn;
	private Button applyBtn;

	private String storeNameOri = "";
	private String storeAddressOri = "";
	private String storePhoneOri = "";

	private File storageDir;
	private String imageFileName;
	private String currentType = "";
	private long storeId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_foundnewstore);
		storageDir = new File(
				Environment
						.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
				getString(R.string.app_name));
		storeInfoDAO = new StoreInfoDAO(getApplicationContext());
		photoCommentDAO = new PhotoCommentDAO(getApplicationContext());
		storeInfoDAO.open();
		photoCommentDAO.open();

		checkedList = new ArrayList<Boolean>();
		mPhotoFragments = new ArrayList<FoundNewStoreActivity.PhotoFragment>();
		adapter = new MyAdapter(getSupportFragmentManager(), mPhotoFragments);
		photos = new ArrayList<NewStore.Photo>();
		newStore = new NewStore("", "", "", 0, 0, "", "", photos);

		photoPager = (ViewPager) findViewById(R.id.foundnewstore_pager);
		storeNameEdit = (EditText) findViewById(R.id.foundnewstore_name);
		storeAddressEdit = (EditText) findViewById(R.id.foundnewstore_address);
		storePhoneEdit = (EditText) findViewById(R.id.foundnewstore_tel);

		nameWarningView = (ImageView) findViewById(R.id.foundnewstore_name_change_warning_view);
		addressWarningView = (ImageView) findViewById(R.id.foundnewstore_address_change_warning_view);
		phoneWarningView = (ImageView) findViewById(R.id.foundnewstore_phone_change_warning_view);

		ImageButton addressMapBtn = (ImageButton) findViewById(R.id.foundnewstore_address_map);

		reportNotExistBtn = (Button) findViewById(R.id.foundnewstore_store_not_exists_btn);
		applyBtn = (Button) findViewById(R.id.foundnewstore_store_apply);

		storeNameEdit.addTextChangedListener(nameWatcher);
		storeAddressEdit.addTextChangedListener(addressWatcher);
		storePhoneEdit.addTextChangedListener(phoneWatcher);

		addressMapBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent mapChooseIntent = new Intent();
				mapChooseIntent.setClass(FoundNewStoreActivity.this,
						MapChoseLocationActivity.class);
				startActivityForResult(mapChooseIntent, FIND_LOCATION);
			}
		});

		reportNotExistBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					reportNotExists();
				} catch (JSONException e) {
					e.printStackTrace();
				}
				finish();
			}
		});
		applyBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (isValid()) {
					try {
						apply();
					} catch (JSONException e) {
						e.printStackTrace();
					}
					finish();
				}
			}
		});

		ImageButton addPhoto = (ImageButton) findViewById(R.id.foundnewstore_addphoto_btn);
		TextView hint = (TextView) findViewById(R.id.foundnewstore_check_hint);

		Intent intent = getIntent();
		if (intent.getAction().equals(FOUND_NEW)) {
			currentType = FOUND_NEW;
			hint.setVisibility(View.GONE);
			reportNotExistBtn.setVisibility(View.GONE);
			addPhoto.setVisibility(View.VISIBLE);
			addPhoto.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					File photoFile = null;
					try {
						photoFile = createImageFile();
					} catch (IOException ex) {
						// Error occurred while creating the File
					}

					if (photoFile != null) {
						Intent takePictureIntent = new Intent(
								MediaStore.ACTION_IMAGE_CAPTURE);
						if (takePictureIntent
								.resolveActivity(getPackageManager()) != null) {
							takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
									Uri.fromFile(photoFile));
							startActivityForResult(takePictureIntent,
									TAKE_PICTURE);
						}
					}
				}

				private File createImageFile() throws IOException {
					// Create an image file name
					String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
							Locale.getDefault()).format(new Date());
					imageFileName = "IMG_" + timeStamp;
					if (!storageDir.exists()) {
						if (!storageDir.mkdirs()) {
							MyLog.d(PhotoCommentFragment.class,
									"failed to create directory");
							return null;
						}
					}
					File image = new File(storageDir.getPath() + File.separator
							+ imageFileName + ".jpg");
					// File.createTempFile(imageFileName, /* prefix */
					// ".jpg", /* suffix */
					// storageDir /* directory */
					// );

					// Save a file: path for use with ACTION_VIEW intents
					MyLog.d(PhotoCommentFragment.class, imageFileName);
					return image;
				}
			});
		} else if (intent.getAction().equals(REPORT_ERROR)) {
			currentType = REPORT_ERROR;
			hint.setVisibility(View.VISIBLE);
			reportNotExistBtn.setVisibility(View.VISIBLE);
			storeId = intent.getLongExtra(StoreInfoActivity.STORE_ID_TAG, 0);
			if (storeId == 0) {
				// TODO Fatal error, no store to report error
				MyLog.e(FoundNewStoreActivity.class,
						"Fatal error, no store to report error");
			} else {
				storeInfo = storeInfoDAO.get(storeId);
				photoComments = photoCommentDAO.getStorePhotoComments(storeId);

				storeNameOri = storeInfo.getName();
				storeAddressOri = storeInfo.getAddress();
				storePhoneOri = storeInfo.getPhone();

				newStore.setName(storeNameOri);
				newStore.setAddress(storeAddressOri);
				newStore.setPhone(storePhoneOri);
				newStore.setLatitude(storeInfo.getLatitude());
				newStore.setLongitude(storeInfo.getLongitude());
				newStore.setFirstLevel(storeInfo.getFirstLevel());
				newStore.setThirdLevel(storeInfo.getThirdLevel());

				storeNameEdit.setText(storeNameOri);
				storeAddressEdit.setText(storeAddressOri);
				storePhoneEdit.setText(storePhoneOri);

				for (PhotoComment photoComment : photoComments) {
					String uri = VolleyTaskManager.getInstance().getMediaRoot()
							+ photoComment.getUri();
					NewStore.Photo photo = newStore.new Photo(uri,
							photoComment.getComment(), 0);
					photos.add(photo);
					addFragment(photo);
				}
				addPhoto.setVisibility(View.GONE);
			}
		} else {
			// TODO error entry
		}

		photoPager.setAdapter(adapter);
	}

	private boolean isValid() {
		boolean isValid = true;
		String name = storeNameEdit.getText().toString();
		String address = storeAddressEdit.getText().toString();
		String phone = storePhoneEdit.getText().toString();
		if (!name.isEmpty()) {
			if (!address.isEmpty()) {
				if (storeNameOri.equals(name)) {
					if (storeAddressOri.equals(address)) {
						if (storePhoneOri.equals(phone)) {
							// if info are all the same
							// check the photo
							for (boolean isChecked : checkedList) {
								if (isChecked) {
									return true;
								}
							}
							Toast.makeText(
									getApplicationContext(),
									getString(R.string.foundnewstore_no_data_changed_error),
									Toast.LENGTH_SHORT).show();
							return false;
						}
					}
				}
			} else {
				// address empty
				Toast.makeText(getApplicationContext(),
						getString(R.string.foundnewstore_address_empty_error),
						Toast.LENGTH_SHORT).show();
				return false;
			}
		} else {
			// name empty
			Toast.makeText(getApplicationContext(),
					getString(R.string.foundnewstore_name_empty_error),
					Toast.LENGTH_SHORT).show();
			return false;
		}

		return isValid;
	}

	private void reportNotExists() throws JSONException {
		// upload current store id and which user upload
		VolleyTaskManager.getInstance().init(getAssets());
		JSONObject json = new JSONObject();
		json.put("store_id", storeId);
		json.put("user_id", AuthUser.getId());
		FoundNewStoreRequest.getInstance().notExists(this, json,
				new Response.Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						MyLog.d(FoundNewStoreActivity.class,
								"" + response.toString());
					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						MyLog.e(FoundNewStoreActivity.class, new String(
								error.networkResponse.data));
					}
				});
	}

	private void apply() throws JSONException {
		// upload Photo and NewStore
		VolleyTaskManager.getInstance().init(getAssets());
		JSONObject newStoreJson = FoundNewStoreRequest.getInstance()
				.createNewStoreJson(newStore);
		JSONArray photosJson = FoundNewStoreRequest.getInstance()
				.createPhotosJsonArray(newStore.getPhotoUris());
		MyLog.d(FoundNewStoreActivity.class, "array: " + photosJson.toString());
		if (currentType.equals(FOUND_NEW)) {

			// Update the feedback point by service
			Intent intentFbpUpdate = new Intent();
			intentFbpUpdate.setClass(getApplicationContext(),
					FeedbackUpdateService.class);
			intentFbpUpdate.setAction(FeedbackUpdateService.UPDATE);
			FeedbackPointDAO fpDAO = new FeedbackPointDAO(
					getApplicationContext());
			fpDAO.open();
			FeedbackPoint fbp = fpDAO.get().get(0);
			fbp.setNewStore(fbp.getNewStore() + 1);
			intentFbpUpdate.putExtras(fbp.save());
			startService(intentFbpUpdate);

			FoundNewStoreRequest.getInstance().foundNewStore(
					getApplicationContext(), newStoreJson, photosJson,
					new Response.Listener<JSONObject>() {

						@Override
						public void onResponse(JSONObject response) {
							MyLog.d(FoundNewStoreActivity.class,
									"" + response.toString());
						}
					}, new Response.ErrorListener() {

						@Override
						public void onErrorResponse(VolleyError error) {
							MyLog.e(FoundNewStoreActivity.class, new String(
									error.networkResponse.data));
						}
					});
		} else if (currentType.equals(REPORT_ERROR)) {

			// Update the feedback point by service
			Intent intentFbpUpdate = new Intent();
			intentFbpUpdate.setClass(getApplicationContext(),
					FeedbackUpdateService.class);
			intentFbpUpdate.setAction(FeedbackUpdateService.UPDATE);
			FeedbackPointDAO fpDAO = new FeedbackPointDAO(
					getApplicationContext());
			fpDAO.open();
			FeedbackPoint fbp = fpDAO.get().get(0);
			if (storeInfo.getPhone() != null && newStore.getPhone() != null
					&& storeInfo.getPhone().isEmpty()
					&& !newStore.getPhone().isEmpty()) {
				fbp.setFillEmpty(fbp.getFillEmpty() + 1);
			}
			fbp.setReportError(fbp.getReportError() + 1);
			intentFbpUpdate.putExtras(fbp.save());
			startService(intentFbpUpdate);

			FoundNewStoreRequest.getInstance().reportError(
					getApplicationContext(), storeId, newStoreJson, photosJson,
					new Response.Listener<JSONObject>() {

						@Override
						public void onResponse(JSONObject response) {
							MyLog.d(FoundNewStoreActivity.class,
									"" + response.toString());
						}
					}, new Response.ErrorListener() {

						@Override
						public void onErrorResponse(VolleyError error) {
							MyLog.e(FoundNewStoreActivity.class, new String(
									error.networkResponse.data));
						}
					});
		} else {
			MyLog.e(FoundNewStoreActivity.class,
					"Fatal Error should never happen");
		}

	}

	private void addFragment(final NewStore.Photo photo) {
		PhotoFragment photoFragment = new PhotoFragment(photo,
				new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						int pos = photos.indexOf(photo);
						checkedList.set(pos, isChecked);
					}
				});
		checkedList.add(false);
		mPhotoFragments.add(photoFragment);
		adapter.notifyDataSetChanged();
	}

	private TextWatcher nameWatcher = new TextWatcher() {

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			nameWarningView
					.setVisibility(s.toString().equals(storeNameOri) ? View.INVISIBLE
							: View.VISIBLE);
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}

		@Override
		public void afterTextChanged(Editable s) {
			newStore.setName(s.toString());
		}
	};

	private TextWatcher addressWatcher = new TextWatcher() {

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			MyLog.d(FoundNewStoreActivity.class, "" + s.toString());
			addressWarningView.setVisibility(s.toString().equals(
					storeAddressOri) ? View.INVISIBLE : View.VISIBLE);
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}

		@Override
		public void afterTextChanged(Editable s) {
			newStore.setAddress(s.toString());
		}
	};

	private TextWatcher phoneWatcher = new TextWatcher() {

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			phoneWarningView
					.setVisibility(s.toString().equals(storePhoneOri) ? View.INVISIBLE
							: View.VISIBLE);
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}

		@Override
		public void afterTextChanged(Editable s) {
			newStore.setPhone(s.toString());
		}
	};

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putLong(CURRENT_STORE_ID, storeId);
		outState.putString(CURRENT_TYPE, currentType);
		outState.putString(ORI_NAME, storeNameOri);
		outState.putString(ORI_ADDRESSE, storeAddressOri);
		outState.putString(ORI_PHONE, storePhoneOri);
		super.onSaveInstanceState(outState);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case TAKE_PICTURE:
			if (resultCode == Activity.RESULT_OK) {
				// MyLog.d(PhotoCommentFragment.class,
				// "data at: " + data.getData());
				try {
					Uri uri = null;
					// if (uri == null) {
					String tmpPath = storageDir.getPath() + File.separator
							+ imageFileName + ".jpg";

					ExifInterface exif = new ExifInterface(tmpPath);
					int orientation = exif.getAttributeInt(
							ExifInterface.TAG_ORIENTATION,
							ExifInterface.ORIENTATION_UNDEFINED);

					BitmapFactory.Options bmOptions = new BitmapFactory.Options();
					bmOptions.inJustDecodeBounds = true;
					BitmapFactory.decodeFile(tmpPath, bmOptions);
					int photoW = bmOptions.outWidth;
					int photoH = bmOptions.outHeight;

					// float whRatio = (float) photoW / (float) photoH;

					// Determine how much to scale down the image
					int scaleFactor = Math
							.max(photoW
									/ (int) (getResources()
											.getDimension(R.dimen.store_info_pic_min_height)),
									photoH
											/ (int) getResources()
													.getDimension(
															R.dimen.store_info_pic_min_height));

					// Decode the image file into a Bitmap sized to fill the
					// View
					bmOptions.inJustDecodeBounds = false;
					bmOptions.inSampleSize = scaleFactor;
					bmOptions.inPurgeable = true;

					Bitmap imageBitmap = PhotoCommentFragment.rotateBitmap(
							BitmapFactory.decodeFile(tmpPath, bmOptions),
							orientation);
					// Bundle extras = data.getExtras();
					// Bitmap imageBitmap = (Bitmap) extras.get("data");
					ByteArrayOutputStream bytes = new ByteArrayOutputStream();
					imageBitmap
							.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
					String path = Images.Media.insertImage(
							getContentResolver(), imageBitmap,
							"rev" + System.currentTimeMillis(), null);
					MyLog.d(PhotoCommentFragment.class, "rev data at: " + path);
					uri = Uri.parse(path);

					MyLog.d(FoundNewStoreActivity.class,
							"uri at: " + uri.toString());
					NewStore.Photo photo = newStore.new Photo(uri.toString(),
							"", 0);
					photos.add(photo);
					newStore.setPhotoUris(photos);
					addFragment(photo);
					// MyLog.d(getClass(), photoComment.toString());
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else if (resultCode == Activity.RESULT_CANCELED) {
				// User cancelled the image capture
			} else {
				// Image capture failed, advise user
			}
			break;
		case FIND_LOCATION:
			if (resultCode == Activity.RESULT_OK) {
				Bundle locationInfo = data.getExtras();
				if (locationInfo
						.getString(MapChoseLocationActivity.NOW_ADDRESS) != null
						&& !locationInfo.getString(
								MapChoseLocationActivity.NOW_ADDRESS).isEmpty()) {
					storeAddressEdit.setText(locationInfo
							.getString(MapChoseLocationActivity.NOW_ADDRESS));
				}
				newStore.setAddress(locationInfo
						.getString(MapChoseLocationActivity.NOW_ADDRESS));
				newStore.setLatitude(locationInfo.getDouble(
						MapChoseLocationActivity.NOW_LATITUDE, 0));
				newStore.setLongitude(locationInfo.getDouble(
						MapChoseLocationActivity.NOW_LONGITUDE, 0));
				newStore.setFirstLevel(locationInfo
						.getString(MapChoseLocationActivity.NOW_FIRST));
				newStore.setThirdLevel(locationInfo
						.getString(MapChoseLocationActivity.NOW_THIRD));
				MyLog.d(FoundNewStoreActivity.class, newStore.toString());
			} else if (resultCode == Activity.RESULT_CANCELED) {
			} else {
			}
			break;
		}
	}

	private class MyAdapter extends FragmentStatePagerAdapter {

		private List<PhotoFragment> photoFragments;

		public MyAdapter(FragmentManager fm, List<PhotoFragment> photoFragments) {
			super(fm);
			MyAdapter.this.photoFragments = photoFragments;
		}

		@Override
		public Fragment getItem(int arg0) {
			return MyAdapter.this.photoFragments.get(arg0);
		}

		@Override
		public int getCount() {
			return MyAdapter.this.photoFragments.size();
		}

		@Override
		public float getPageWidth(int position) {
			return getCount() > 1 ? 1f : 0.9f;
		}

	}

	private class PhotoFragment extends Fragment {

		private View rootView;
		private ImageView photoImageView;
		private EditText commentEditText;
		private CheckBox errorPicCheck;
		private NewStore.Photo photo;
		private OnCheckedChangeListener listener;

		public PhotoFragment(NewStore.Photo photo,
				OnCheckedChangeListener listener) {
			PhotoFragment.this.photo = photo;
			this.listener = listener;
		}

		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {

			rootView = inflater.inflate(R.layout.fragment_new_store_photo,
					container, false);
			photoImageView = (ImageView) rootView
					.findViewById(R.id.fragment_new_store_photo_imageview);
			commentEditText = (EditText) rootView
					.findViewById(R.id.fragment_new_store_photo_comment_edit);
			errorPicCheck = (CheckBox) rootView
					.findViewById(R.id.fragment_new_store_check_error);

			if (currentType.equals(REPORT_ERROR)) {
				errorPicCheck.setVisibility(View.VISIBLE);
				commentEditText.setText(photo.getComment());
				commentEditText.setInputType(InputType.TYPE_NULL);
			} else {
				errorPicCheck.setVisibility(View.GONE);
				commentEditText.addTextChangedListener(new TextWatcher() {

					@Override
					public void onTextChanged(CharSequence s, int start,
							int before, int count) {
					}

					@Override
					public void beforeTextChanged(CharSequence s, int start,
							int count, int after) {
					}

					@Override
					public void afterTextChanged(Editable s) {
						photo.setComment(s.toString());
					}
				});
			}

			errorPicCheck.setOnCheckedChangeListener(listener);
			ImageLoader.getInstance().displayImage(photo.getUri(),
					photoImageView);

			return rootView;
		}
	}

}
