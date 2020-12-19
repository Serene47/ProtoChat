package com.serene.protochat.event;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import com.serene.protochat.model.User;
import com.serene.protochat.service.UserVerificationTokenService;

@Component
public class UserVerficationMailEventListener implements ApplicationListener<UserVerficationMailEvent>{

	@Autowired
	JavaMailSender mailSender;
	
	@Autowired
	UserVerificationTokenService userVerificationTokenService;
	
	@Value("${spring.mail.username}")
	private String FROM_EMAIL;
	
	private final int EXPIRY_LIMIT = 24 * 60 * 60 * 1000;
	
	private final String APP_URL = "localhost:8080";
	
	@Override
	public void onApplicationEvent(UserVerficationMailEvent event) {
		
		String  token = UUID.randomUUID().toString();
		
		long expiry = System.currentTimeMillis() + EXPIRY_LIMIT;
		
		User user = event.getUser();
		
		String email = user.getUsername();
		
		int user_Id = user.getUser_Id();
		
		String type = event.getType();
		
		userVerificationTokenService.insert(user_Id, token, expiry, type);
		
		sendMail(type, token, email);
		
	}
	
	private void sendMail(String type, String token, String email) {
		
		String subject,message;
		
		if(type == "emailverification") {
			
			subject = "Registration Confirmation";
			
			message = "To continue your registration click the link below \r\n" + 
					"http://" + APP_URL + "/registrationConfirm?token=" + token;
			
			
		} else {
			
			subject = "Reset Password";
			
			message = "To reset your password click the link below \r\n" + 
					"http://" + APP_URL + "/resetPassword?token=" + token;
			
		}
		
		SimpleMailMessage mailMessage = new SimpleMailMessage();
		
		mailMessage.setTo(email);
		mailMessage.setFrom(FROM_EMAIL);
		mailMessage.setSubject(subject);
		mailMessage.setText(message);
		
		mailSender.send(mailMessage);
		
		
	}
	
	

}
