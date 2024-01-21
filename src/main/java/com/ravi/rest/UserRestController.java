package com.ravi.rest;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ravi.entity.User;
import com.ravi.request.UserRequest;
import com.ravi.response.UserResponse;
import com.ravi.service.IUserService;
import com.ravi.utils.JwtUtils;

@RestController
@RequestMapping("/user")
public class UserRestController {

	@Autowired
	private IUserService service;

	@Autowired
	private JwtUtils utils;

	@Autowired
	private AuthenticationManager authenticationManager;

	// User registration
	@PostMapping(value = "/save", consumes = "application/json", produces = "plain/text")
	public ResponseEntity<String> saveUser(@RequestBody User user) {
		Integer id = service.saveUser(user);
		String msg = "User saved with id " + id;
		return new ResponseEntity<String>(msg, HttpStatus.CREATED);
	}

	// User login validation and generating token.
	@PostMapping(value = "/login", consumes = "application/json", produces = "application/json")
	public ResponseEntity<UserResponse> validateUser(@RequestBody UserRequest request) {

		// Validate userName and Password from DB
		authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(request.getUserName(), request.getPassword()));

		String token = utils.generateTocken(request.getUserName());
		UserResponse response = new UserResponse(token, "Success, Token Generated!!");
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	// After login
	@PostMapping(value = "/welcome", produces = "plain/text")
	public ResponseEntity<String> accessData(Principal principal){
		return new ResponseEntity<String>("Hello user :: "+principal.getName(),HttpStatus.OK);
	}

}
