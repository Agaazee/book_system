package com.book.security.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class PasswordValidator {

	private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	public boolean checkIfMatches(String rawPassword, String hashedPassword) {
		return passwordEncoder.matches(rawPassword, hashedPassword);
	}

}
