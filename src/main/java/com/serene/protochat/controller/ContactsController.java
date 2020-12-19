package com.serene.protochat.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.serene.protochat.model.RecentContact;
import com.serene.protochat.security.CustomPrincipal;
import com.serene.protochat.service.ContactsService;

@RestController
@RequestMapping("/contacts")
public class ContactsController {

	@Autowired
	ContactsService contactsService;
	
	@GetMapping
	public List<RecentContact> getContacts(Authentication authentication) {
		
		CustomPrincipal principal = (CustomPrincipal)authentication.getPrincipal();
		
		int fromuser_Id = principal.getUser_Id();
		
		return contactsService.selectRecentContacts(fromuser_Id);
	}
	
	
}
