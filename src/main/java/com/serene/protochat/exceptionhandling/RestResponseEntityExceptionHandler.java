package com.serene.protochat.exceptionhandling;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.serene.protochat.customexception.ForgotPasswordUserNotFoundException;
import com.serene.protochat.customexception.InvalidOldPasswordException;
import com.serene.protochat.customexception.UserAlreadyExistException;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler{

	@ExceptionHandler(UserAlreadyExistException.class)
	protected ResponseEntity<Object> handleException(UserAlreadyExistException ex,WebRequest request ){
		
		String body = "Someone previously registered using this email.";
		
		HttpHeaders headers = new HttpHeaders();
		
		headers.add("statusText", body);
		
		return handleExceptionInternal(ex,body,headers,HttpStatus.CONFLICT,request);
	}
	
	@ExceptionHandler(InvalidOldPasswordException.class)
	protected ResponseEntity<Object> handleException(InvalidOldPasswordException ex,WebRequest request ){
		
		String body = "Old Password does not match.";
		
		HttpHeaders headers = new HttpHeaders();
		
		headers.add("statusText", body);
		
		return handleExceptionInternal(ex,body,headers,HttpStatus.FORBIDDEN,request);
	}
	
	
	@ExceptionHandler(ForgotPasswordUserNotFoundException.class)
	protected ResponseEntity<Object> handleException(ForgotPasswordUserNotFoundException ex,WebRequest request ){
		
		String body = "No user is registeredwith this email.";
		
		HttpHeaders headers = new HttpHeaders();
		
		headers.add("statusText", body);
		
		return handleExceptionInternal(ex,body,headers,HttpStatus.UNAUTHORIZED,request);
	}
	
}
