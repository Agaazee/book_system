package com.book.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.book.model.LoginRequest;
import com.book.model.LoginResponse;
import com.book.model.User;
import com.book.repository.UserRepository;
import com.book.security.util.JwtUtil;
import com.book.service.EmailValidator;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api")
public class AdminController {

	JwtUtil jwtUtil;
	UserRepository userRepository;
	EmailValidator emailValidator;
	BCryptPasswordEncoder bCryptPasswordEncoder;
	AuthenticationManager authenticationManager;

	public AdminController(JwtUtil jwtUtil, UserRepository userRepository, EmailValidator emailValidator,
			BCryptPasswordEncoder bCryptPasswordEncoder, AuthenticationManager authenticationManager) {
		this.jwtUtil = jwtUtil;
		this.userRepository = userRepository;
		this.emailValidator = emailValidator;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
		this.authenticationManager = authenticationManager;
	}

	private final Set<String> setTokenBlacklist = new HashSet<>();

	@PostMapping("/admin/login")
	public ResponseEntity<?> userLogin(@RequestBody LoginRequest loginRequest) throws Exception {
		try {

			if (!EmailValidator.isValidEmail(loginRequest.getUsername())) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid email address");
			}

			Authentication authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

			if (authentication.isAuthenticated()) {
				String token = jwtUtil.generateToken(loginRequest.getUsername());
				return ResponseEntity.ok(new LoginResponse(token));
			} else {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User Not Found!");
			}
		} catch (AuthenticationException e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
		}
	}

	@PostMapping("/admin/add")
	public ResponseEntity<?> addUserByAdmin(@RequestBody User user) {

		if (!EmailValidator.isValidEmail(user.getUsername())) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid email address");
		}

		User checkUserEntry = userRepository.findByUsername(user.getUsername());
		if (checkUserEntry != null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User Already Exists");
		}
		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		userRepository.save(user);
		return ResponseEntity.status(HttpStatus.CREATED).body("User Added Successfully");
	}

	@GetMapping("/admin/allUsers")
	public ResponseEntity<?> getAllUsers() {
		try {
			List<User> users = userRepository.findAll();
			return ResponseEntity.ok(users);
		} catch (AuthenticationException authenticationException) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
		}
	}

	@PostMapping("/admin/logout")
	public ResponseEntity<String> logout(@RequestHeader("Authorization") String token) throws Exception {
		setTokenBlacklist.add(token.replace("Bearer ", ""));
		return ResponseEntity.status(HttpStatus.OK).body("Logged out successfully. Token is now invalidated.");
	}

	public boolean isTokenBlacklisted(String token) {
		return setTokenBlacklist.contains(token);
	}

}
