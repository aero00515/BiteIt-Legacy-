package edu.utboy.biteit.ui.adapters;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import edu.utboy.biteit.R;
import edu.utboy.biteit.asynctasks.VolleyTaskManager;
import edu.utboy.biteit.models.PhotoComment;
import edu.utboy.biteit.models.StoreInfo;
import edu.utboy.biteit.utils.AnimateImageView;

public class StoreListAdapter extends BaseAdapter {

	private Activity activity;
	private List<StoreLists> storeInfos;
	private List<StoreLists> curStoreInfos;

	public StoreListAdapter(Activity activity, List<StoreLists> storeInfos) {
		curStoreInfos = new ArrayList<StoreLists>();
		this.activity = activity;
		this.storeInfos = storeInfos;
		filter(true);

	}

	@Override
	public int getCount() {
		return curStoreInfos.size();
	}

	@Override
	public Object getItem(int position) {
		return curStoreInfos.get(position);
	}

	@Override
	public long getItemId(int position) {
		return curStoreInfos.get(position).getStoreInfo().getStoreId();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = activity.getLayoutInflater().inflate(
					R.layout.store_list_style, parent, false);
		}
		StoreLists storeList = curStoreInfos.get(position);
		TextView storeListTextView = (TextView) convertView
				.findViewById(R.id.store_list_style_store_name);
		AnimateImageView storeListBG = (AnimateImageView) convertView
				.findViewById(R.id.store_list_store_background);
		storeListTextView.setText(storeList.getStoreInfo().getName());
		List<PhotoComment> photoComments = storeList.getPhotoComments();
		List<String> uris = new ArrayList<String>(photoComments.size());
		for (PhotoComment pc : photoComments) {
			uris.add(VolleyTaskManager.getInstance().getMediaRoot()
					+ pc.getUri());
		}
		storeListBG.setUILDisplayer(new FadeInBitmapDisplayer(700));
		storeListBG.setUpUris(uris);

		if (storeList.getStoreInfo().isBlocked()) {
			// TODO add blocked view
		} else {
			// TODO remove blocked view
		}
		return convertView;
	}

	public void filter(boolean isLovedPage) {
		curStoreInfos.clear();
		for (StoreLists storeLists : storeInfos) {
			if (isLovedPage) {
				if (storeLists.getStoreInfo().isFavor()) {
					curStoreInfos.add(storeLists);
				}
			} else {
				if (storeLists.getStoreInfo().isBlocked()) {
					curStoreInfos.add(storeLists);
				}
			}
		}
		notifyDataSetChanged();
	}

	public class StoreLists {
		private StoreInfo storeInfo;
		private List<PhotoComment> photoComments;

		public StoreLists(StoreInfo storeInfo, List<PhotoComment> photoComments) {
			this.storeInfo = storeInfo;
			this.photoComments = photoComments;
		}

		public StoreInfo getStoreInfo() {
			return storeInfo;
		}

		public void setStoreInfo(StoreInfo storeInfo) {
			this.storeInfo = storeInfo;
		}

		public List<PhotoComment> getPhotoComments() {
			return photoComments;
		}

		public void setPhotoComments(List<PhotoComment> photoComments) {
			this.photoComments = photoComments;
		}
	}

}
