package com.serene.protochat.model;

public class UserVerificationToken {

	private int userVerificationToken_Id;
	private int user_Id;
	private String token;
	private long expiry;
	private String type;
	
	public UserVerificationToken() {
		super();
	}
	
	public UserVerificationToken(int user_Id, String token, long expiry, String type) {
		super();
		this.user_Id = user_Id;
		this.token = token;
		this.expiry = expiry;
		this.type = type;
	}
	public int getUserVerificationToken_Id() {
		return userVerificationToken_Id;
	}
	public void setUserVerificationToken_Id(int userVerificationToken_Id) {
		this.userVerificationToken_Id = userVerificationToken_Id;
	}
	public int getUser_Id() {
		return user_Id;
	}
	public void setUser_Id(int user_Id) {
		this.user_Id = user_Id;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public long getExpiry() {
		return expiry;
	}
	public void setExpiry(long expiry) {
		this.expiry = expiry;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
}
