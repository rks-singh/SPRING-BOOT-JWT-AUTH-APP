package com.ravi.service;

import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.ravi.entity.User;
import com.ravi.repo.UserRepository;

@Service
public class UserServiceImpl implements IUserService, UserDetailsService {

	@Autowired
	private UserRepository repo;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Override
	public Integer saveUser(User user) {
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		user = repo.save(user);
		return user.getId();
	}

	@Override
	public Optional<User> findByUserName(String userName) {
		return repo.findByUserName(userName);
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<User> result = findByUserName(username);
		if(result.isEmpty()) {
			throw new UsernameNotFoundException("User not exist.");
		}else {
			User user = result.get();
			return new org.springframework.security.core.userdetails.User(
					user.getUserName(),
					user.getPassword(), 
					user.getRoles().stream()
								.map(role -> new SimpleGrantedAuthority(role))
								.collect(Collectors.toList()));
		}
	}

}
