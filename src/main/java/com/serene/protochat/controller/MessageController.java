package com.serene.protochat.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.serene.protochat.model.Message;
import com.serene.protochat.security.CustomPrincipal;
import com.serene.protochat.service.MessageService;

@RestController
@RequestMapping("/messages")
public class MessageController {

	@Autowired
	MessageService messageService;
	
	@GetMapping("/{chatRoom_Id}")
	public List<Message> selectAll(@PathVariable int chatRoom_Id,Authentication authentication){
		
		CustomPrincipal principal = (CustomPrincipal)authentication.getPrincipal();
		
		int user_Id = principal.getUser_Id();
		
		return messageService.selectAll(chatRoom_Id, user_Id);
	}
	
	@PostMapping(value="/markMessagesRead")
	public void markMessagesRead(@RequestBody String messageIdList,Authentication authentication) {
		
		CustomPrincipal principal = (CustomPrincipal)authentication.getPrincipal();
		
		int user_Id = principal.getUser_Id();
		
		messageService.markMessagesRead(messageIdList,user_Id);
		
	}
	
}
