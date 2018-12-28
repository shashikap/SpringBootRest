package com.bokks.micro.springbootrestapi.service;


import java.util.List;

import com.bokks.micro.springbootrestapi.model.User;

public interface UserService {
	
	User findByUsername(String username);
	
	User findByName(String name);
	
	void saveUser(User user);
	
	void updateUser(User user);
	
	void deleteUserByUsername(String username);

	List<User> findAllUsers();
	
	void deleteAllUsers();
	
	boolean isUserExist(User user);
	
}
