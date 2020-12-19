package com.serene.protochat.socket;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.serene.protochat.dao.MessageDao;


public class CustomWebSocketHandler extends TextWebSocketHandler{

	private Map<Integer, List<WebSocketSession>> sessions = new HashMap<Integer,List<WebSocketSession>>();
	
	@Autowired
	MessageDao messageDao;
	
	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		
		Map<String, Object> attributes = session.getAttributes();
		
		int user_Id = (int) attributes.get("user_Id");
		
		this.addSession(user_Id, session);
	}
	
	@Override
	public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
		
		Map<String, Object> attributes = session.getAttributes();
		
		int user_Id = (int) attributes.get("user_Id");
		
		com.serene.protochat.model.Message messageModel = 
				new ObjectMapper().readValue((String) message.getPayload(),com.serene.protochat.model.Message.class);
		
		messageModel.setFromUser_Id(user_Id);
		
		messageModel.setTimestamp(System.currentTimeMillis());
		
		messageDao.insert(messageModel);
		
		TextMessage textMessage = new TextMessage(new ObjectMapper().writeValueAsString(messageModel));
		
		for(Map.Entry<Integer,List<WebSocketSession>> each : sessions.entrySet()) {
			
			if(each.getKey().equals(messageModel.getToUser_Id()) 
					|| each.getKey().equals(messageModel.getFromUser_Id())) {
				

				List<WebSocketSession> sessionList = each.getValue();
				
				for(WebSocketSession sessionEntry: sessionList ) {
					
					sessionEntry.sendMessage(textMessage);
				}
			}
			
		}
	}
	
	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		
		Map<String, Object> attributes = session.getAttributes();
		
		int user_Id = (int) attributes.get("user_Id");
		
		this.removeSession(user_Id, session);
	}
	
	
	private void addSession(int user_Id,WebSocketSession session) {
		
		List<WebSocketSession> sessionList = sessions.get(user_Id);
		
		if(sessionList == null) {
			
			sessionList = new ArrayList<WebSocketSession>();
			
			sessionList.add(session);
			
			sessions.put(user_Id, sessionList);
			
		} else {
			
			sessionList.add(session);
			
		}
		
	}
	
	private void removeSession(int user_Id,WebSocketSession session) {
		
		List<WebSocketSession> sessionList = sessions.get(user_Id);
		
		sessionList.remove(session);
		
		if(sessionList.isEmpty()) {
			
			sessions.remove(user_Id);
		}
		
	}
	
	
}
