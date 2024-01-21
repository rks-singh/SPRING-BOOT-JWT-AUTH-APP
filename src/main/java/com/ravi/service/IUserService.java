package com.ravi.service;

import java.util.Optional;

import com.ravi.entity.User;

public interface IUserService {

	public Integer saveUser(User user);

	Optional<User> findByUserName(String userName);
}
