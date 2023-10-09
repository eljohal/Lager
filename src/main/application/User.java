package main.application;

public class User {
	public static int id;
	public static String username;
	
	
	
	public int getUserID() {
		return id;
	}
	
	public static void setUserName(String user) {
		username = user;
	}
	
	public static void setUserID(int user) {
		id = user;
	}
	
	
	public String getUserName() {
		return username;
	}
}
