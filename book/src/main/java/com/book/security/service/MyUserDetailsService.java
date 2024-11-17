package com.book.security.service;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.book.repository.UserRepository;

@Service
public class MyUserDetailsService implements UserDetailsService {
	
	private UserRepository userRepository;
	
	public MyUserDetailsService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    	
    	com.book.model.User user = userRepository.findByUsername(username);
    	
        if (user.getUsername().equals(username)) {
            return User.withUsername(username)
                       .password(user.getPassword())
                       .roles("ADMIN")
                       .build();
        }
        throw new UsernameNotFoundException("User not found");
    }
}
