package com.serene.protochat.model;

public class User {

	private int user_Id;
	private String displayName;
	private String username;
	private boolean enabled;
	private String password;
	private String displayPicture;
	
	public User() {
		super();
	}
	public User(String displayName, String username, String password) {
		super();
		this.displayName = displayName;
		this.username = username;
		this.password = password;
	}
	public int getUser_Id() {
		return user_Id;
	}
	public void setUser_Id(int user_Id) {
		this.user_Id = user_Id;
	}
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public boolean isEnabled() {
		return enabled;
	}
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getDisplayPicture() {
		return displayPicture;
	}
	public void setDisplayPicture(String displayPicture) {
		this.displayPicture = displayPicture;
	}
	
	
	
}
