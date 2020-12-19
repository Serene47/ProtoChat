package com.serene.protochat.security;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SuppressWarnings("deprecation")
@Configuration
@EnableWebSecurity
public class ApplicationSecurityConfig extends WebSecurityConfigurerAdapter{

	@Autowired
	private DataSource dataSource;
	
	@Autowired
	private CustomUserDetailService userDetailsService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http
			.csrf().disable()
			.authorizeRequests()
		    	.antMatchers("/performLogin","/css/login.css","/css/common-noauth.css","/js/login.js",
		    			"/login","/user/register","/registrationConfirm", "/forgotPassword", "/user/resetPasswordUserVerify",
		    			"/resetPassword", "/performResetPassword","/js/forgotpassword.js" ,"/js/resetpassword.js","/js/validation.js",
		    			"/css/loader.css","/js/loader.js").permitAll()
			.anyRequest().authenticated()
			.and()
			.formLogin()
			    .loginPage("/login")
			    .loginProcessingUrl("/performLogin")
				.defaultSuccessUrl("/", true)
			.and()
				.rememberMe();
	}
	
	

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {

		auth.userDetailsService(userDetailsService)
			.passwordEncoder(passwordEncoder);
		/*
		auth.
			jdbcAuthentication()
			.dataSource(dataSource)
			.usersByUsernameQuery("select username,password,enabled from Users where username = ?" )
			.authoritiesByUsernameQuery("select username,'USER' from Users where username = ?"); */
	} 
	
	@Override
	@Bean
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	
}