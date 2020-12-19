package com.serene.protochat.model;


public class Message {

	private int message_Id;
	private int fromUser_Id;
	private int toUser_Id;
	private String content;
	private long timestamp;
	private int chatRoom_Id;
	private boolean readStatus;
	
	
	public int getMessage_Id() {
		return message_Id;
	}
	public void setMessage_Id(int message_Id) {
		this.message_Id = message_Id;
	}
	public int getFromUser_Id() {
		return fromUser_Id;
	}
	public void setFromUser_Id(int fromUser_Id) {
		this.fromUser_Id = fromUser_Id;
	}
	public int getToUser_Id() {
		return toUser_Id;
	}
	public void setToUser_Id(int toUser_Id) {
		this.toUser_Id = toUser_Id;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public long getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
	public int getChatRoom_Id() {
		return chatRoom_Id;
	}
	public void setChatRoom_Id(int chatRoom_Id) {
		this.chatRoom_Id = chatRoom_Id;
	}
	public boolean isReadStatus() {
		return readStatus;
	}
	public void setReadStatus(boolean readStatus) {
		this.readStatus = readStatus;
	}
	
	
}
