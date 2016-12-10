package edu.utboy.biteit.utils;

import java.util.List;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.BitmapDisplayer;

import edu.utboy.biteit.R;

public class AnimateImageView extends ImageView {

	private final int NO_PIC = R.drawable.ic_launcher;
	private long duration = 3000;
	private List<Integer> ids;
	private List<String> uris;
	private boolean isIds;
	private boolean isUris;
	private ImageLoader imageLoader;
	private DisplayImageOptions.Builder builder;
	private DisplayImageOptions options;
	private Handler handler;
	private int counter;

	public AnimateImageView(Context context) {
		super(context);
		init(context);
	}

	public AnimateImageView(Context context, AttributeSet arg1) {
		super(context, arg1);
		init(context);
	}

	public AnimateImageView(Context context, AttributeSet arg1, int arg2) {
		super(context, arg1, arg2);
		init(context);
	}

	private void init(Context context) {
		ImageLoaderConfiguration configuration = new ImageLoaderConfiguration.Builder(
				context).build();
		ImageLoader.getInstance().init(configuration);
		imageLoader = ImageLoader.getInstance();
		builder = new DisplayImageOptions.Builder().cacheOnDisk(true)
				.showImageForEmptyUri(NO_PIC);
		options = builder.cacheOnDisk(true).build();
		// .displayer(new FadeInBitmapDisplayer(700))
		handler = new Handler();
		counter = 0;
	}

	Runnable run = new Runnable() {

		@Override
		public void run() {
			if (isIds) {
				if (ids.size() > 0) {
					if (ids.size() > 1) {
						int pos = (counter % ids.size());
						AnimateImageView.this.setImageResource(ids.get(pos));
					} else {
						AnimateImageView.this.setImageResource(ids.get(0));
					}
				} else {
					AnimateImageView.this.setImageResource(NO_PIC);
				}
			} else if (isUris) {
				if (uris.size() > 0) {
					if (uris.size() > 1) {
						int pos = (counter % uris.size());
						imageLoader.displayImage(uris.get(pos),
								AnimateImageView.this, options);
					} else {
						imageLoader.displayImage(uris.get(0),
								AnimateImageView.this, options);
					}
				} else {
					AnimateImageView.this.setImageResource(NO_PIC);
				}
			} else {
				// Shouldn't happen
				return;
			}
			counter++;
			handler.postDelayed(run, duration);
		}
	};

	// Set up for image source
	public void setUpIds(List<Integer> ids) {
		isIds = true;
		isUris = false;
		this.ids = ids;
	}

	// Set up for image source
	public void setUpUris(List<String> uris) {
		isUris = true;
		isIds = false;
		this.uris = uris;
	}

	public long getDuration() {
		return duration;
	}

	public void setUILOptions(DisplayImageOptions options) {
		this.options = options;
	}

	public void setUILDisplayer(BitmapDisplayer bitmapDisplayer) {
		options = builder.displayer(bitmapDisplayer).build();
	}

	public void setDuration(long duration) {
		this.duration = duration;
	}

	@Override
	protected void onAttachedToWindow() {
		handler.postDelayed(run, duration);
		super.onAttachedToWindow();
	}

	@Override
	protected void onDetachedFromWindow() {
		handler.removeCallbacks(run);
		super.onDetachedFromWindow();
	}

}
