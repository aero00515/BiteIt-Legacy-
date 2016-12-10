package edu.utboy.biteit.ui.pages;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import edu.utboy.biteit.R;

public class StoreInfoPicFragment extends Fragment {

	public final static String STORE_INFO_PIC_TAG = "store_info_pic_tag";
	public final static String STORE_INFO_PIC_URI = "store_info_pic_uri";
	public final static String STORE_INFO_PIC_COMMENT = "store_info_pic_comment";
	private DisplayImageOptions mDisplayImageOptions;
	private ImageSize imageSize;
	private ImageView storeInfoPic;
	private View storeInfoPicView;
	private String uri;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle arg = getArguments();
		mDisplayImageOptions = new DisplayImageOptions.Builder()
				.showImageForEmptyUri(R.drawable.ic_launcher)
				.showImageOnFail(R.drawable.ic_launcher)
				.showImageOnLoading(R.drawable.ic_launcher).cacheInMemory(true)
				.cacheOnDisk(true).build();
		imageSize = new ImageSize((int) getResources().getDimension(
				R.dimen.store_info_pic_min_height), (int) getResources()
				.getDimension(R.dimen.store_info_pic_min_height));
		uri = arg.getString(STORE_INFO_PIC_URI);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		storeInfoPicView = inflater.inflate(R.layout.fragment_store_info_pic,
				container, false);
		storeInfoPic = (ImageView) storeInfoPicView
				.findViewById(R.id.store_info_pic);
		ImageLoader.getInstance().loadImage(uri, imageSize,
				mDisplayImageOptions, new ImageLoadingListener() {

					@Override
					public void onLoadingStarted(String imageUri, View view) {
					}

					@Override
					public void onLoadingFailed(String imageUri, View view,
							FailReason failReason) {
					}

					@Override
					public void onLoadingComplete(String imageUri, View view,
							Bitmap loadedImage) {
						storeInfoPic.setImageBitmap(loadedImage);
					}

					@Override
					public void onLoadingCancelled(String imageUri, View view) {
					}
				});
		return storeInfoPicView;
	}

}
