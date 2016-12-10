package edu.utboy.biteit.models;

import java.util.List;

public class DrawerEventDetail {
	private List<User> users;

	public DrawerEventDetail(List<User> users) {
		this.users = users;
	}

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

	public class User {
		private int no;
		private long uID;
		private String name;
		private String awardPicUrl;

		public User(int no, long uID, String name, String awardPicUrl) {
			this.no = no;
			this.uID = uID;
			this.name = name;
			this.awardPicUrl = awardPicUrl;
		}

		public int getNo() {
			return no;
		}

		public void setNo(int no) {
			this.no = no;
		}

		public long getuID() {
			return uID;
		}

		public void setuID(long uID) {
			this.uID = uID;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getAwardPic() {
			return awardPicUrl;
		}

		public void setAwardPic(String awardPic) {
			this.awardPicUrl = awardPic;
		}
	}
}
