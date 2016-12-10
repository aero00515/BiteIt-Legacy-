package edu.utboy.biteit.utils;

public class AuthUser {
	private static String name = "";
	private static int id;
	private static String email = "";
	private static String picUrl = "";

	public static String getName() {
		return name;
	}

	public static void setName(String name) {
		AuthUser.name = name;
	}

	public static int getId() {
		return id;
	}

	public static void setId(int id) {
		AuthUser.id = id;
	}

	public static String getEmail() {
		return email;
	}

	public static void setEmail(String email) {
		AuthUser.email = email;
	}

	public static String getPicUrl() {
		return picUrl;
	}

	public static void setPicUrl(String picUrl) {
		AuthUser.picUrl = picUrl;
	}

	public static void clean() {
		id = 0;
		name = "";
		email = "";
		picUrl = "";
	}

}
