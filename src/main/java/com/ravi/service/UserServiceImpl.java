package com.ravi.service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.ravi.entity.User;
import com.ravi.repo.UserRepository;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Override
	public Integer saveUser(User user) {

		// Encode password before saving into DB.
		user.setUserPassword(bCryptPasswordEncoder.encode(user.getUserPassword()));
		return userRepo.save(user).getUserId();
	}

	@Override
	public Optional<User> findByUserName(String userName) {
		return userRepo.findByUserName(userName);
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<User> opt = userRepo.findByUserName(username);

		org.springframework.security.core.userdetails.User springUser = null;

		if (opt.isEmpty()) {
			throw new UsernameNotFoundException("User with username: " + username + " not found");
		} else {
			User user = opt.get(); // retrieving user from DB
			Set<String> roles = user.getUserRole();
			Set<GrantedAuthority> ga = new HashSet<>();
			for (String role : roles) {
				ga.add(new SimpleGrantedAuthority(role));
			}

			springUser = new org.springframework.security.core.userdetails.User(username, user.getUserPassword(), ga);
		}

		return springUser;
	}

}
