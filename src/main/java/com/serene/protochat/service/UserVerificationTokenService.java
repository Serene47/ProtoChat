package com.serene.protochat.service;

import org.springframework.stereotype.Service;

import com.serene.protochat.dao.UserVerificationTokenDao;
import com.serene.protochat.model.UserVerificationToken;

@Service
public class UserVerificationTokenService {

	UserVerificationTokenDao dao;
	
	public UserVerificationTokenService(UserVerificationTokenDao dao) {
		
		this.dao = dao;
		
	}
	
	public int insert(int user_Id, String token, long expiry,String type ) {
		
		UserVerificationToken emailVerificationToken = 
				new UserVerificationToken(user_Id, token, expiry, type);
		
		return dao.insert(emailVerificationToken);
	}
	
	public void delete(int emailVerificationToken_Id) {
		dao.delete(emailVerificationToken_Id);
	}
	
	public UserVerificationToken loadDetailsByToken(String token, String type) {
		return dao.loadDetailsByToken(token,type);
	}
	
}
