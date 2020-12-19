package com.serene.protochat.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.serene.protochat.customexception.ForgotPasswordUserNotFoundException;
import com.serene.protochat.customexception.InvalidOldPasswordException;
import com.serene.protochat.event.UserVerficationMailEvent;
import com.serene.protochat.model.RegisterPayload;
import com.serene.protochat.model.User;
import com.serene.protochat.security.CustomPrincipal;
import com.serene.protochat.service.UserService;

@RestController
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private ApplicationEventPublisher eventPublisher;

	@GetMapping("/loggedinuserdetails")
	public HashMap<String,Object> getUserDetailsl(Authentication authentication) {
		
		HashMap<String,Object> userDetails = new  HashMap<String,Object>();
		
		CustomPrincipal principal = (CustomPrincipal)authentication.getPrincipal();
		
		userDetails.put("user_Id", principal.getUser_Id());
		
		userDetails.put("displayName", principal.getDisplayName());
		
		return userDetails;
	}
	
	
	@PostMapping("/register" )
	public ResponseEntity<String> registerUser(@RequestBody RegisterPayload userData) throws Exception {
		
		User user = new User(userData.getDisplayName(), userData.getUsername(), userData.getPassword());
		
		int user_Id = userService.insert(user);
		
		user.setUser_Id(user_Id);
		
		eventPublisher.publishEvent(new UserVerficationMailEvent(this,user,"emailverification"));
		
		return ResponseEntity.ok("Verfication mail send. Please confirm your identity.");
	}
	
	@PostMapping("/updateDisplayPicture")
	public ResponseEntity<Map<String,String>> updateDisplayPicture(@RequestParam("image") MultipartFile file,Authentication authentication) {
		
		CustomPrincipal principal = (CustomPrincipal)authentication.getPrincipal();
		
		String url  = userService.updateDisplayPicture(principal, file);
		
		Map<String,String> responseBody = new HashMap<String,String>();
		
		responseBody.put("url", url);
		
		return ResponseEntity.ok(responseBody);
	}
	
	@DeleteMapping("/removeDisplayPicture")
	public ResponseEntity<String> removeDisplayPicture(Authentication authentication) {
		
		CustomPrincipal principal = (CustomPrincipal)authentication.getPrincipal();
		
		userService.removeDisplayPicture(principal);
		
		return ResponseEntity.ok("Display Picture removed");
	}
	
	@PatchMapping("/updateDisplayName")
	public ResponseEntity<String> updateDisplayname(@RequestBody String displayName, Authentication authentication ) {
		
		CustomPrincipal principal = (CustomPrincipal)authentication.getPrincipal();
		
		userService.updateDisplayName(principal, displayName);
		
		return ResponseEntity.ok("Display Name updated");
		
	}
	
	@PatchMapping("/updatePassword")
	public ResponseEntity<String> updatePassword(@RequestParam("currentPassword") String currentPassword,
			@RequestParam("newPassword") String newPassword,
			Authentication authentication ) throws InvalidOldPasswordException {
		
		CustomPrincipal principal = (CustomPrincipal)authentication.getPrincipal();
		
		String username = principal.getUsername();
		
		userService.updatePassword(username, currentPassword, newPassword);
		
		return ResponseEntity.ok("Password updated");
		
	}
	
	@PostMapping("/resetPasswordUserVerify")
	public ResponseEntity<String> resetPasswordUserVerify(@RequestBody String username) throws ForgotPasswordUserNotFoundException{
		
		User user = userService.loadUserByUsername(username);
		
		if(user == null) {
			
			throw new ForgotPasswordUserNotFoundException();
			
		} 
			
		eventPublisher.publishEvent(new UserVerficationMailEvent(this, user, "forgotpassword"));
		
		return ResponseEntity.ok("Verfication mail send");
	}
	
	
}
