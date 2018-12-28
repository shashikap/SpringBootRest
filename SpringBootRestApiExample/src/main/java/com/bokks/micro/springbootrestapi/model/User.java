package com.bokks.micro.springbootrestapi.model;

public class User {

	private String username;
	
	private String name;
	
	private int age;
	
	private double salary;

	private String password;

	private UserRoles userRole;

	public User(){
		username ="";
	}
	
	public User(String username, String name, int age, double salary, String password, UserRoles role ){
		this.username = username;
		this.name = name;
		this.age = age;
		this.salary = salary;
		this.password = password;
		this.userRole = role;
	}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public double getSalary() {
		return salary;
	}

	public void setSalary(double salary) {
		this.salary = salary;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public UserRoles getUserRole() {
		return userRole;
	}

	public void setUserRole(UserRoles userRole) {
		this.userRole = userRole;
	}

//	@Override
//	public int hashCode() {
//		final int prime = 31;
//		int result = 1;
//		result = prime * result + (int) (username ^ (username >>> 32));
//		return result;
//	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (username != other.username)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "User [username=" + username + ", name=" + name + ", age=" + age
				+ ", salary=" + salary + "]";
	}


}
