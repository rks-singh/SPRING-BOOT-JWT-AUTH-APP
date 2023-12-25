package com.ravi.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ravi.entity.User;
import com.ravi.request.UserRequest;
import com.ravi.response.UserResponse;
import com.ravi.service.UserService;
import com.ravi.util.JWTUtil;

@RestController
public class UserRestController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private JWTUtil jwtUtil;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@PostMapping(value = "/saveUser", consumes = "application/json", produces = "text/plain")
	public ResponseEntity<String> saveUser(@RequestBody User user){
		Integer id = userService.saveUser(user);
		String message = "User with id "+id+" saved successfully!";
		return new ResponseEntity<>(message, HttpStatus.OK);
	}
	
	@PostMapping(value = "/loginUser", consumes = "application/json", produces = "application/json")
	public ResponseEntity<UserResponse> login(@RequestBody UserRequest request){
		
		//Validate userName/password with DB
		// It is required in case of Stateless Authentication.
		authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(
						request.getUserName(), request.getUserPassword())
				);
		
		String tocken = jwtUtil.generateToken(request.getUserName());
		
		return new ResponseEntity<UserResponse>(new UserResponse(tocken,"Tocken generated successfully!"), HttpStatus.OK);
	}
	
	@PostMapping("/getData")
	public ResponseEntity<String> testAfterLogin(Principal principal){
		String message = "You are accessing data after valid login. You are : "+principal.getName();
		return new ResponseEntity<String>(message, HttpStatus.OK);
	}
}
