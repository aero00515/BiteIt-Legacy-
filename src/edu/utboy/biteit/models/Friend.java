package edu.utboy.biteit.models;

public class Friend {
	private long id;
	private String username;
	private String name;
	private String photo;
	private String email;
	private boolean isBlocked;
	private boolean isFocused;

	/**
	 * @param id
	 *            Is the auto incresement id
	 * @param username
	 *            Is for stdId right now
	 * @param storeId
	 *            Which store they leave the message
	 * @param name
	 *            Friend's name
	 * @param photo
	 *            Friend's photo
	 * @param email
	 *            Friend's email
	 * @param isFocused
	 *            User have focused this friend
	 */
	public Friend(long id, String username, String name, String photo,
			String email, boolean isBlocked, boolean isFocused) {
		this.id = id;
		this.username = username;
		this.name = name;
		this.photo = photo;
		this.email = email;
		this.isBlocked = isBlocked;
		this.isFocused = isFocused;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public boolean isBlocked() {
		return isBlocked;
	}

	public void setBlocked(boolean isBlocked) {
		this.isBlocked = isBlocked;
	}

	public boolean isFocued() {
		return isFocused;
	}

	public void setFocued(boolean isFocused) {
		this.isFocused = isFocused;
	}
}
