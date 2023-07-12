package com.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.security.model.User;
import com.security.model.UserDetailsImpl;
import com.security.repository.UserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	
	@Autowired
	private UserRepository repository;

	@Override
	public UserDetails loadUserByUsername(String username) 
			throws UsernameNotFoundException 
	{
		//loading model class user object
		User user = repository.findByUsername(username)
				.orElseThrow(()->new UsernameNotFoundException("User not exist" + username));
		//converting into Spring Security User object
		return UserDetailsImpl.build(user);
	}

}
