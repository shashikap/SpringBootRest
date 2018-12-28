package com.bokks.micro.springbootrestapi.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import com.bokks.micro.springbootrestapi.model.User;
import com.bokks.micro.springbootrestapi.model.UserRoles;
import org.springframework.stereotype.Service;


@Service("userService")
public class UserServiceImpl implements UserService{
	
	private static final AtomicLong counter = new AtomicLong();
	
	private static List<User> users;
	
	static{
		users= populateDummyUsers();
	}

	public List<User> findAllUsers() {
		return users;
	}
	
	public User findByUsername(String username) {
		for(User user : users){
			if(user.getUsername().equals(username)){
				return user;
			}
		}
		return null;
	}
	
	public User findByName(String name) {
		for(User user : users){
			if(user.getName().equalsIgnoreCase(name)){
				return user;
			}
		}
		return null;
	}
	
	public void saveUser(User user) {
		users.add(user);
	}

	public void updateUser(User user) {
		int index = users.indexOf(user);
		users.set(index, user);
	}

	public void deleteUserByUsername(String username) {
		
		for (Iterator<User> iterator = users.iterator(); iterator.hasNext(); ) {
		    User user = iterator.next();
		    if (user.getUsername() == username) {
		        iterator.remove();
		    }
		}
	}

	public boolean isUserExist(User user) {
		return findByName(user.getName())!=null;
	}
	
	public void deleteAllUsers(){
		users.clear();
	}

	private static List<User> populateDummyUsers(){
		List<User> users = new ArrayList<User>();
		users.add(new User("admin","Sam",30, 70000,"admin123", UserRoles.ADMIN));
		users.add(new User("user1","Tom",40, 50000,"admin123", UserRoles.ROLE1));
		users.add(new User("user2","Jerome",45, 30000,"admin123", UserRoles.ROLE2));
		users.add(new User("user3","Silvia",50, 40000,"admin123", UserRoles.ROLE3));
		return users;
	}

}
