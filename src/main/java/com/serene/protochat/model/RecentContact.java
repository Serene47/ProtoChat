package com.serene.protochat.model;

public class RecentContact {

	private int user_Id;
	private int chatRoom_Id;
	private String displayName;
	private int inCommingUnread;
	private String displayPicture;
	
	
	public int getUser_Id() {
		return user_Id;
	}

	public void setUser_Id(int user_Id) {
		this.user_Id = user_Id;
	}

	public int getChatRoom_Id() {
		return chatRoom_Id;
	}

	public void setChatRoom_Id(int chatRoom_Id) {
		this.chatRoom_Id = chatRoom_Id;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public int getInCommingUnread() {
		return inCommingUnread;
	}

	public void setInCommingUnread(int inCommingUnread) {
		this.inCommingUnread = inCommingUnread;
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
