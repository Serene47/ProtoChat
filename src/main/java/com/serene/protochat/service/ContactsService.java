package com.serene.protochat.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.serene.protochat.dao.ContactsDao;
import com.serene.protochat.model.Contact;
import com.serene.protochat.model.RecentContact;

@Service
public class ContactsService {

	@Autowired
	private ContactsDao contactsDao;
	
	public List<RecentContact> selectRecentContacts(int currentuser_Id){
		return contactsDao.selectRecentContacts(currentuser_Id);
	}
	
	public List<Contact> selectAllContacts(int currentuser_Id) {
		return contactsDao.selectAllContacts(currentuser_Id);
	}
	
}
