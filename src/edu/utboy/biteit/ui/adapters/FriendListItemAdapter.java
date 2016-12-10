package edu.utboy.biteit.ui.adapters;

import java.util.List;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nhaarman.listviewanimations.itemmanipulation.expandablelistitem.ExpandableListItemAdapter;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import edu.utboy.biteit.R;
import edu.utboy.biteit.models.Friend;
import edu.utboy.biteit.models.dao.FriendDAO;

public class FriendListItemAdapter extends ExpandableListItemAdapter<Friend> {

	private final Activity activity;
	private DisplayImageOptions options;
	private FriendDAO friendDAO;
	private List<Friend> allFriends;

	/**
	 * Creates a new ExpandableListItemAdapter with the specified list, or an
	 * empty list if items == null.
	 */
	public FriendListItemAdapter(Activity activity, FriendDAO friendDAO) {
		super(activity, R.layout.activity_expandablelistitem_card,
				R.id.activity_expandablelistitem_card_title,
				R.id.activity_expandablelistitem_card_content);
		this.activity = activity;
		options = new DisplayImageOptions.Builder().cacheOnDisk(true).build();

		this.friendDAO = friendDAO;
		allFriends = friendDAO.get();
		filter(true);
	}

	@Override
	public View getTitleView(final int position, View convertView,
			final ViewGroup parent) {
		if (convertView == null) {
			convertView = activity.getLayoutInflater().inflate(
					R.layout.friend_title, parent, false);
		}

		final TextView nameText = (TextView) convertView
				.findViewById(R.id.activity_expandablelistitem_profile_name);
		ImageView profilePhoto = (ImageView) convertView
				.findViewById(R.id.activity_expandablelistitem_profile_photo);
		final Friend friend = getItem(position);
		nameText.setText(friend.getName());
		ImageLoader.getInstance().displayImage(friend.getPhoto(), profilePhoto,
				options);
		ImageView isBlockedImageView = (ImageView) convertView
				.findViewById(R.id.activity_expandablelistitem_is_blocked);
		ImageView isFocusedImageView = (ImageView) convertView
				.findViewById(R.id.activity_expandablelistitem_is_focused);

		isBlockedImageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (friend.isBlocked()) {
					friend.setBlocked(false);
				} else {
					friend.setBlocked(true);
				}
				new Thread() {
					public void run() {
						updateFriend(friend);
					};
				}.start();
				notifyDataSetChanged();
			}
		});

		isFocusedImageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (friend.isFocued()) {
					friend.setFocued(false);
				} else {
					friend.setFocued(true);
				}
				new Thread() {
					public void run() {
						updateFriend(friend);
					};
				}.start();
				notifyDataSetChanged();
			}
		});

		if (friend.isBlocked()) {
			isBlockedImageView.setVisibility(View.VISIBLE);
			isBlockedImageView
					.setBackgroundResource(R.drawable.close_background);
			isFocusedImageView.setVisibility(View.GONE);
			isFocusedImageView.setBackgroundResource(0);
		} else if (friend.isFocued()) {
			isBlockedImageView.setVisibility(View.GONE);
			isBlockedImageView.setBackgroundResource(0);
			isFocusedImageView.setVisibility(View.VISIBLE);
			isFocusedImageView
					.setBackgroundResource(R.drawable.apply_background);
		} else {
			isBlockedImageView.setVisibility(View.VISIBLE);
			isBlockedImageView.setBackgroundResource(0);
			isFocusedImageView.setVisibility(View.VISIBLE);
			isFocusedImageView.setBackgroundResource(0);
		}
		return convertView;
	}

	@Override
	public View getContentView(final int position, final View convertView,
			final ViewGroup parent) {
		ImageView imageView = (ImageView) convertView;
		if (imageView == null) {
			imageView = new ImageView(activity);
			imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
		}

		return imageView;
	}

	public void filter(boolean isLovedPage) {
		clear();
		for (Friend friend : allFriends) {
			if (isLovedPage) {
				if (!friend.isBlocked()) {
					if (friend.isFocued()) {
						add(0, friend);
					} else {
						add(friend);
					}
				}
			} else {
				if (friend.isBlocked()) {
					add(friend);
				}
			}
		}
		notifyDataSetChanged();
	}

	private synchronized void updateFriend(Friend friend) {
		friendDAO.update(friend);
	}
}