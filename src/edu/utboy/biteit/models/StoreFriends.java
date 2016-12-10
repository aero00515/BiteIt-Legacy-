package edu.utboy.biteit.models;

import java.util.List;

public class StoreFriends {

	private StoreInfo storeInfo;
	private List<Friend> friends;

	public StoreFriends(StoreInfo storeinfo, List<Friend> friends) {
		this.storeInfo = storeinfo;
		this.friends = friends;
	}

	public StoreInfo getStoreInfo() {
		return storeInfo;
	}

	public void setStoreInfo(StoreInfo storeInfo) {
		this.storeInfo = storeInfo;
	}

	public List<Friend> getFriends() {
		return friends;
	}

	public void setFriends(List<Friend> friends) {
		this.friends = friends;
	}
}
