package com.serene.protochat.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.serene.protochat.dao.MessageDao;
import com.serene.protochat.model.Message;

@Service
public class MessageService {

	@Autowired
	MessageDao messageDao;
	
	public List<Message> selectAll(int chatRoom_Id,int user_Id){
		
		return messageDao.selectAll(chatRoom_Id, user_Id);
	}
	
	public void markMessagesRead(String messageIdList, int user_Id) {
		
		messageDao.markMessagesRead(messageIdList,user_Id);
		
	}
}
