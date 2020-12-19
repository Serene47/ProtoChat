package com.serene.protochat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.serene.protochat.model.UserVerificationToken;
import com.serene.protochat.security.AuthenticationFacade;
import com.serene.protochat.service.ContactsService;
import com.serene.protochat.service.UserService;
import com.serene.protochat.service.UserVerificationTokenService;

@Controller
public class ApplicationViewsController {

	@Autowired
	ContactsService contactsService;
	
	@Autowired
	AuthenticationFacade authentication;
	
	@Autowired
	UserVerificationTokenService userVerificationTokenService;
	
	@Autowired
	UserService userService;
	
	@Autowired
	ApplicationEventPublisher eventPublisher;
	
	@RequestMapping("/login")
	public String getLogin() {
		return "login";
	}
	
	
	@RequestMapping("/")
	public ModelAndView getIndex(ModelAndView modelAndView) {
		
		int user_Id = authentication.getPrincipal().getUser_Id();
		
		modelAndView.addObject("allContacts", contactsService.selectAllContacts(user_Id));
		modelAndView.addObject("recentContacts", contactsService.selectRecentContacts(user_Id));
		
		modelAndView.setViewName("index");
		
		return modelAndView;
		
	}
	
	
	@RequestMapping("/registrationConfirm")
	public String registrationConfirm(@RequestParam("token") String token, Model model, RedirectAttributes redirect) {
		
		
		UserVerificationToken userVerificationToken = 
				userVerificationTokenService.loadDetailsByToken(token,"emailverification");
		
		long currentTime = System.currentTimeMillis();
		
		if(userVerificationToken == null) {
			
			model.addAttribute("error", "We cannot confirm your identity");
			
			return "baduser";
			
			
		} else if(userVerificationToken.getExpiry() <= currentTime) {
			
			model.addAttribute("error", "The url is expired");
			
			return "bauser";
			
		} else {
			
			userService.enableUser(userVerificationToken.getUser_Id());
			
			userVerificationTokenService.delete(userVerificationToken.getUserVerificationToken_Id());
			
			redirect.addFlashAttribute("registrationsuccess", "You have successfully registered with ProtoChat ! Please Login");
			
			return "redirect:login";
			
		}
		
	}
	
	@RequestMapping("/forgotPassword")
	public String getForgotPassword() {
		
		return "forgotpassword";
		
	}
	
	
	@RequestMapping("/resetPassword")
	public String getResetPassword(@RequestParam("token") String token,Model model) {
		
		
		UserVerificationToken userVerificationToken = 
				userVerificationTokenService.loadDetailsByToken(token,"forgotpassword");
		
		long currentTime = System.currentTimeMillis();
		
		if(userVerificationToken == null) {
			
			model.addAttribute("error", "We cannot confirm your identity");
			
			return "baduser";
			
			
		} else if(userVerificationToken.getExpiry() <= currentTime) {
			
			model.addAttribute("error", "The url is expired");
			
			return "baduser";
			
		} else {
			
			model.addAttribute("token", token);
			
			return "resetpassword";
			
		}
		
	}
	
	
	@RequestMapping(value = "/performResetPassword", method = RequestMethod.POST)
	public String performResetPassword(@RequestParam("token") String token, @RequestParam("password") String password, Model model, RedirectAttributes redirect) {
		
		
		UserVerificationToken userVerificationToken = 
				userVerificationTokenService.loadDetailsByToken(token,"forgotpassword");
		
		long currentTime = System.currentTimeMillis();
		
		if(userVerificationToken == null) {
			
			model.addAttribute("error", "We cannot confirm your identity");
			
			return "baduser";
			
			
		} else if(userVerificationToken.getExpiry() <= currentTime) {
			
			model.addAttribute("error", "The url is expired");
			
			return "baduser";
			
		} else {
			
			userService.resetPassword(userVerificationToken.getUser_Id(), password);
			
			userVerificationTokenService.delete(userVerificationToken.getUserVerificationToken_Id());
			
			redirect.addFlashAttribute("passwordResetSuccess", "Your password is reset! Please login.");
			
			return "redirect:login";
			
		}
		
	}
	
	
	
	
	
	
	
}
