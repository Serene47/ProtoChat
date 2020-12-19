package com.serene.protochat.event;

import org.springframework.context.ApplicationEvent;

import com.serene.protochat.model.User;

public class UserVerficationMailEvent extends ApplicationEvent{

	private User user;
	private String type;
	
	
	public UserVerficationMailEvent(Object source,User user, String type) {
		
		super(source);
	
		this.user = user;
		this.type = type;
	
	}

	public User getUser() {
		return user;
	}

	public String getType() {
		return type;
	}
	
	
}
