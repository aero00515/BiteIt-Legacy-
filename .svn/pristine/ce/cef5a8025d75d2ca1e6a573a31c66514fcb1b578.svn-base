package edu.utboy.biteit.ui.adapters;

import java.util.List;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import edu.utboy.biteit.R;
import edu.utboy.biteit.models.DrawerEventDetail;
import edu.utboy.biteit.models.DrawerEventDetail.User;
import edu.utboy.biteit.models.DrawerItem;
import edu.utboy.biteit.utils.AuthUser;

public class DrawerListAdapter extends BaseExpandableListAdapter {

	Activity activity;
	List<DrawerItem> mDrawerItems;
	DrawerEventDetail mDrawerEventDetail;

	public DrawerListAdapter(Activity activity, List<DrawerItem> drawerItems,
			DrawerEventDetail mDrawerEventDetail) {
		this.activity = activity;
		this.mDrawerItems = drawerItems;
		this.mDrawerEventDetail = mDrawerEventDetail;
	}

	@Override
	public int getGroupCount() {
		return mDrawerItems.size();
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return groupPosition == 0 ? 1 : 0;
	}

	@Override
	public Object getGroup(int groupPosition) {
		return mDrawerItems.get(groupPosition);
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return groupPosition == 0 ? mDrawerEventDetail : null;
	}

	@Override
	public long getGroupId(int groupPosition) {
		return 0;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return 0;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = activity.getLayoutInflater().inflate(
					R.layout.drawer_list, parent, false);
		}
		TextView title = (TextView) convertView
				.findViewById(R.id.drawer_list_title);
		DrawerItem currentItem = mDrawerItems.get(groupPosition);
		title.setText(currentItem.getTitle());
		title.setCompoundDrawablesWithIntrinsicBounds(currentItem.getViewId(),
				0, 0, 0);
		return convertView;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = activity.getLayoutInflater().inflate(
					R.layout.drawer_event_detail, parent, false);
		}
		TextView first = (TextView) convertView
				.findViewById(R.id.drawer_event_detail_first);
		TextView firstView = (TextView) convertView
				.findViewById(R.id.drawer_event_detail_first_imageview);
		TextView second = (TextView) convertView
				.findViewById(R.id.drawer_event_detail_second);
		TextView secondView = (TextView) convertView
				.findViewById(R.id.drawer_event_detail_second_imageview);
		TextView third = (TextView) convertView
				.findViewById(R.id.drawer_event_detail_third);
		TextView thirdView = (TextView) convertView
				.findViewById(R.id.drawer_event_detail_third_imageview);
		TextView maybeFourth = (TextView) convertView
				.findViewById(R.id.drawer_event_detail_maybe_you);
		TextView maybeFourthView = (TextView) convertView
				.findViewById(R.id.drawer_event_detail_maybe_you_imageview);
		ImageView crown = (ImageView) convertView
				.findViewById(R.id.drawer_event_detail_crown);
		List<User> users = mDrawerEventDetail.getUsers();
		firstView.setText("1");
		secondView.setText("2");
		thirdView.setText("3");
		firstView.setVisibility(View.GONE);
		secondView.setVisibility(View.GONE);
		thirdView.setVisibility(View.GONE);
		first.setVisibility(View.GONE);
		second.setVisibility(View.GONE);
		third.setVisibility(View.GONE);

		if (users.size() > 3) {
			// user is not in top 3, show
			maybeFourth.setVisibility(View.VISIBLE);
			maybeFourthView.setVisibility(View.VISIBLE);
		} else {
			maybeFourth.setVisibility(View.GONE);
			maybeFourthView.setVisibility(View.GONE);
		}

		for (int i = 0; i < users.size(); i++) {
			User user = users.get(i);
			String name = user.getName();
			if (i == 0) {
				crown.setVisibility(View.VISIBLE);
				firstView.setVisibility(View.VISIBLE);
				first.setVisibility(View.VISIBLE);
				first.setText(name);
			} else if (i == 1) {
				secondView.setVisibility(View.VISIBLE);
				second.setVisibility(View.VISIBLE);
				second.setText(name);
			} else if (i == 2) {
				thirdView.setVisibility(View.VISIBLE);
				third.setVisibility(View.VISIBLE);
				third.setText(name);
			} else {
				if (name.equals(AuthUser.getName())) {
					int top = 0;
					if (user.getNo() > 4) {
						top = R.drawable.abc_ic_menu_moreoverflow_normal_holo_light;
					}
					maybeFourth.setCompoundDrawablesWithIntrinsicBounds(0, top,
							R.drawable.abc_ic_voice_search_api_holo_light, 0);
					maybeFourth.setText(name);
					maybeFourthView.setText(String.valueOf(user.getNo()));
					maybeFourth.setVisibility(View.VISIBLE);
					maybeFourthView.setVisibility(View.VISIBLE);
					break;
				} else {
					maybeFourth.setVisibility(View.GONE);
					maybeFourthView.setVisibility(View.GONE);
				}
			}
		}
		return convertView;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return groupPosition == 0 ? true : false;
	}

}
