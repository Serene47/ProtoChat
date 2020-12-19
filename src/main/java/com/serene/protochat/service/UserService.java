package com.serene.protochat.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.serene.protochat.customexception.InvalidOldPasswordException;
import com.serene.protochat.customexception.UserAlreadyExistException;
import com.serene.protochat.dao.UserDao;
import com.serene.protochat.model.User;
import com.serene.protochat.security.CustomPrincipal;

@Service
public class UserService {

	@Autowired
	private UserDao userDao;
	
	@Autowired
	FileUploadService fileUploadService;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	public int insert(User user) throws UserAlreadyExistException {
		
		if(userDao.isUsernameExist(user.getUsername())) {
			throw new UserAlreadyExistException();
		} 
		
		return userDao.insert(user);
	}
	
	public String updateDisplayPicture(CustomPrincipal principal, MultipartFile file) {
		
		String fileName = file.getOriginalFilename();
		
		int user_Id = principal.getUser_Id();
		
		String directoryName = "display-pictures/" + user_Id;
		
		fileUploadService.saveFile(directoryName,fileName, file);
		
		userDao.updateDisplayPicture(user_Id, fileName);
		
		principal.setDisplayPicture(fileName);
		
		return directoryName + "/" + fileName;
		
	}
	
	public void removeDisplayPicture(CustomPrincipal principal) {
		
		String displayPicture = principal.getDisplayPicture();
		
		int user_Id = principal.getUser_Id();
		
		fileUploadService.removeDisplayPicture("display-pictures/" + user_Id, displayPicture);
		
		userDao.updateDisplayPicture(user_Id, null);
		
		principal.setDisplayPicture(null);
		
		
	}
	
	public void updateDisplayName(CustomPrincipal principal,String displayName) {
		
		int user_Id = principal.getUser_Id();
		
		userDao.updateDisplayName(user_Id, displayName);
		
		principal.setDisplayName(displayName);
		
	}
	
	public void updatePassword(String username, String currentPassword,String newPassword) throws InvalidOldPasswordException {
		
		User user = userDao.findUserByUsername(username);
		
		if(!checkOldPasswordMatch(user.getPassword(), currentPassword)) {
			
			throw new InvalidOldPasswordException();
			
		} 

		userDao.updatePassword(user.getUser_Id(), newPassword);
		
		
	}
	
	private boolean checkOldPasswordMatch(String oldPassword, String newPassword) {
		
		return passwordEncoder.matches(newPassword, oldPassword);
		
	}
	
	public void enableUser(int user_Id) {
		
		userDao.enableUser(user_Id);
		
	}
	
	public User loadUserByUsername(String username) {
		
		return userDao.findUserByUsername(username);
		
	}
	
	public void resetPassword(int user_Id,String newPassword) {
		
		userDao.updatePassword(user_Id, newPassword);
		
	}
	
	
}
