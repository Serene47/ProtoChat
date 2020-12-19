package com.serene.protochat.model;

public class Contact {

	private int user_Id;
	private String displayName;
	private String displayPicture;
	
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
	
	public String getDisplayPicture() {
		return displayPicture;
	}

	public void setDisplayPicture(String displayPicture) {
		this.displayPicture = displayPicture;
	}
	
	public String getDisplayPictureUrl() {
		return  this.displayPicture  != null ? 
				"display-pictures/" + this.user_Id + "/" + this.displayPicture  :
				"display-pictures/default/default-user.png";
	}
	
	public String getDisplayPictureThumbnailUrl() {
		return  this.displayPicture  != null ? 
				"display-pictures/" + this.user_Id + "/thumbnails/" + this.displayPicture  :
				"display-pictures/default/thumbnails/default-user.png";
	}
	
	
}
