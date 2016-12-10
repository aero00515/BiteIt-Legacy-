package edu.utboy.biteit.utils;

public class AuthToken {
	public static String gcmId = "";
	public static String accessToken = "";

	public static void saveGCMId(String gcm_id) {
		gcmId = gcm_id;
	}

	public static String getGCMId() {
		return gcmId;
	}

	public static void saveToken(String token) {
		accessToken = token;
	}

	public static String getAuthToken() {
		return accessToken;
	}

	public static void clean() {
		gcmId = "";
		accessToken = "";
	}
}
