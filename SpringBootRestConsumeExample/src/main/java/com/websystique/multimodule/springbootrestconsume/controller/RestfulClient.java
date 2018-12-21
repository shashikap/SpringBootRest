package com.websystique.multimodule.springbootrestconsume.controller;

import com.websystique.multimodule.springbootrestconsume.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service("restfulClient")
public class RestfulClient {
	RestTemplate restTemplate;
	
	public RestfulClient(){
		restTemplate = new RestTemplate();
	}
	
	/**
	 * post entity
	 */
	public void postEntity(){
		System.out.println("Begin /POST request!");
		String postUrl = "http://localhost:8080/post";
//		Customer customer = new Customer(123, "Jack", 23);
//		ResponseEntity<String> postResponse = restTemplate.postForEntity(postUrl, customer, String.class);
//		System.out.println("Response for Post Request: " + postResponse.getBody());
	}
	
	
	/**
	 * get entity
	 */
	public ResponseEntity getUserById(String id){
		System.out.println("Begin /GET request!");
//		String getUrl = "http://localhost:8080/get?id=1&name='Mary'&age=20";
		String getUrl = "http://localhost:8083/expose/user/" +id ;
		ResponseEntity<User> getResponse = restTemplate.getForEntity(getUrl, User.class);
		if(getResponse.getBody() != null){
			System.out.println("Response for Get Request: " + getResponse.getBody().toString());	
		}else{
			System.out.println("Response for Get Request: NULL");
		}

		return getResponse;
	}


	public ResponseEntity getAllUsers(){
		System.out.println("Begin /GET request!");
//		String getUrl = "http://localhost:8080/get?id=1&name='Mary'&age=20";
		String getUrl = "http://localhost:8080/api/user/" ;
		ResponseEntity<User> getResponse = restTemplate.getForEntity(getUrl, User.class);
		if(getResponse.getBody() != null){
			System.out.println("Response for Get Request: " + getResponse.getBody().toString());
		}else{
			System.out.println("Response for Get Request: NULL");
		}

		return getResponse;
	}
	/**
	 * put entity
	 */
	public void putEntity(){
		System.out.println("Begin /PUT request!");
		String putUrl = "http://localhost:8080/put/2";
		//Customer puttCustomer = new Customer("Bush", 23);
		//restTemplate.put(putUrl, puttCustomer);
	}
	
	/**
	 * delete entity
	 */
	public void deleteEntity(){
		System.out.println("Begin /DELETE request!");
		String deleteUl = "http://localhost:8080/delete/1";
		restTemplate.delete(deleteUl);
	}
}
