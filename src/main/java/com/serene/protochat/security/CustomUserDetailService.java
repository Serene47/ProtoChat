package com.serene.protochat.security;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.serene.protochat.dao.UserDao;

@Service("userDetailsService")
public class CustomUserDetailService implements UserDetailsService{

	@Autowired
	private UserDao userDao;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		com.serene.protochat.model.User user = userDao.findUserByUsername(username);
		
		if(user == null)
			throw new UsernameNotFoundException(username + "not found");
		
		return buildUserForAuthentication(user);
		
	}
	
	
	private User buildUserForAuthentication(com.serene.protochat.model.User user) {
		
	    boolean accountNonExpired = true;
	    boolean credentialsNonExpired = true;
	    boolean accountNonLocked = true;
	    
	    CustomPrincipal principal =  new CustomPrincipal(user.getUsername(), user.getPassword(),
	    		user.isEnabled(), accountNonExpired, credentialsNonExpired, accountNonLocked, buildUserAuthority());
	    
	    principal.setUser_Id(user.getUser_Id());
	    principal.setDisplayName(user.getDisplayName());
	    principal.setDisplayPicture(user.getDisplayPicture());
	    
	    return principal;
		
	}
	
	private List<GrantedAuthority> buildUserAuthority(){
		return new ArrayList<GrantedAuthority>();
	}

}
