package edu.utboy.biteit.ui.pages;

import idv.andylin.tw.andyandroidlibs.utils.MyLog;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import edu.utboy.biteit.R;
import edu.utboy.biteit.models.PhotoComment;
import edu.utboy.biteit.ui.StoreInfoActivity;
import edu.utboy.biteit.ui.StoreInfoTakePictureActivity;
import edu.utboy.biteit.utils.AuthUser;

public class PhotoCommentFragment extends Fragment {

	private final static int REQUEST_IMAGE_CAPTURE = 1;

	private View rootView;
	private ImageButton takePhotoButton;
	private ListView photoListView;
	private PhotoListViewAdapter photoListViewAdapter;
	private File storageDir;

	private List<PhotoComment> photos;
	private long storeId;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		storageDir = new File(
				Environment
						.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
				getString(R.string.app_name));
		photoListViewAdapter = new PhotoListViewAdapter();
		photos = new ArrayList<PhotoComment>();

		// TODO: after store db has created, bundle must have store id inside
		if (savedInstanceState == null) {
			savedInstanceState = getArguments();
		}
		storeId = savedInstanceState.getLong(StoreInfoActivity.STORE_ID_TAG, 0);
		String[] photoString = savedInstanceState
				.getStringArray(StoreInfoTakePictureActivity.STORE_INFO_FEEDBACK_PHOTO_COMMENT);
		if (photoString != null) {
			for (String photo : photoString) {
				try {
					JSONObject photoJson = new JSONObject(photo);
					photos.add(new PhotoComment(photoJson
							.getLong(PhotoComment.STORE_ID), photoJson
							.getLong(PhotoComment.USER_ID), Uri.parse(photoJson
							.getString(PhotoComment.URI)), photoJson
							.getString(PhotoComment.COMMENT)));
					photoListViewAdapter.notifyDataSetChanged();
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}

	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		outState.putLong(StoreInfoActivity.STORE_ID_TAG, storeId);
		List<PhotoComment> photoComments = photos;
		String[] photos = new String[photoComments.size()];
		for (int i = 0; i < photoComments.size(); i++) {
			PhotoComment photoComment = photoComments.get(i);
			try {
				photos[i] = photoComment.saveAsJson().toString();
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		outState.putStringArray(
				StoreInfoTakePictureActivity.STORE_INFO_FEEDBACK_PHOTO_COMMENT,
				photos);
		super.onSaveInstanceState(outState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_photo_comment, container,
				false);
		takePhotoButton = (ImageButton) rootView
				.findViewById(R.id.activity_store_info_feedback_add_photo_btn);
		photoListView = (ListView) rootView
				.findViewById(R.id.activity_store_info_feedback_added_photo_listview);

		takePhotoButton.setOnClickListener(takePhotoListener);
		photoListView.setAdapter(photoListViewAdapter);

		return rootView;
	}

	private OnClickListener takePhotoListener = new OnClickListener() {

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
				if (takePictureIntent.resolveActivity(getActivity()
						.getPackageManager()) != null) {
					takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
							Uri.fromFile(photoFile));
					startActivityForResult(takePictureIntent,
							REQUEST_IMAGE_CAPTURE);
				}
			}
		}
	};

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

	String imageFileName;

	public List<PhotoComment> getPhotoComments() {
		return photos;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case REQUEST_IMAGE_CAPTURE:
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

					Bitmap imageBitmap = rotateBitmap(
							BitmapFactory.decodeFile(tmpPath, bmOptions),
							orientation);
					// Bundle extras = data.getExtras();
					// Bitmap imageBitmap = (Bitmap) extras.get("data");
					ByteArrayOutputStream bytes = new ByteArrayOutputStream();
					imageBitmap
							.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
					String path = Images.Media.insertImage(getActivity()
							.getContentResolver(), imageBitmap,
							"rev" + System.currentTimeMillis(), null);
					MyLog.d(PhotoCommentFragment.class, "rev data at: " + path);
					uri = Uri.parse(path);
					// uri = (Uri) data.getExtras().get("data");
					// }

					PhotoComment photoComment = new PhotoComment(storeId,
							AuthUser.getId(), uri, "");
					photos.add(photoComment);
					photoListViewAdapter.notifyDataSetChanged();
				} catch (IOException e) {
					e.printStackTrace();
				}
				// MyLog.d(getClass(), photoComment.toString());
			} else if (resultCode == Activity.RESULT_CANCELED) {
				// User cancelled the image capture
				MyLog.d(PhotoCommentFragment.class, "Result Canceled "
						+ imageFileName);
			} else {
				// Image capture failed, advise user
			}
			break;

		default:
			break;
		}
	}

	private class PhotoListViewAdapter extends BaseAdapter {
		DisplayImageOptions dio;

		public PhotoListViewAdapter() {
			dio = new DisplayImageOptions.Builder().considerExifParams(true)
					.build();
		}

		@Override
		public int getCount() {
			return photos.size();
		}

		@Override
		public Object getItem(int position) {
			return photos.get(position);
		}

		@Override
		public long getItemId(int position) {
			return photos.get(position).getStoreId();
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final PhotoComment photoComment = photos.get(position);
			if (convertView == null) {
				convertView = getActivity().getLayoutInflater().inflate(
						R.layout.added_photo_listview_style, parent, false);
			}
			ImageView photoImageView = (ImageView) convertView
					.findViewById(R.id.added_photo_imageview);
			final EditText commentTextView = (EditText) convertView
					.findViewById(R.id.added_comment_textview);
			// TODO: adjust the problem of orientation,
			// portrait will be landscape
			commentTextView.addTextChangedListener(new TextWatcher() {

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
					MyLog.d(PhotoCommentFragment.class,
							"afterTextChanged " + s.toString());
					photoComment.setComment(s.toString());
				}
			});

			commentTextView.setText(photoComment.getComment());
			ImageLoader.getInstance().displayImage(
					photoComment.getUri().toString(), photoImageView, dio);
			return convertView;
		}
	}

	public static Bitmap rotateBitmap(Bitmap bitmap, int orientation) {

		Matrix matrix = new Matrix();
		switch (orientation) {
		case ExifInterface.ORIENTATION_NORMAL:
			return bitmap;
		case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
			matrix.setScale(-1, 1);
			break;
		case ExifInterface.ORIENTATION_ROTATE_180:
			matrix.setRotate(180);
			break;
		case ExifInterface.ORIENTATION_FLIP_VERTICAL:
			matrix.setRotate(180);
			matrix.postScale(-1, 1);
			break;
		case ExifInterface.ORIENTATION_TRANSPOSE:
			matrix.setRotate(90);
			matrix.postScale(-1, 1);
			break;
		case ExifInterface.ORIENTATION_ROTATE_90:
			matrix.setRotate(90);
			break;
		case ExifInterface.ORIENTATION_TRANSVERSE:
			matrix.setRotate(-90);
			matrix.postScale(-1, 1);
			break;
		case ExifInterface.ORIENTATION_ROTATE_270:
			matrix.setRotate(-90);
			break;
		default:
			return bitmap;
		}
		try {
			Bitmap bmRotated = Bitmap.createBitmap(bitmap, 0, 0,
					bitmap.getWidth(), bitmap.getHeight(), matrix, true);
			bitmap.recycle();
			return bmRotated;
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
			return null;
		}
	}

}
