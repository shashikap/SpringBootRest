package com.bokks.micro.springbootrestapi.controller;

import java.util.List;

import com.bokks.micro.springbootrestapi.filters.AuthenticationFilter;
import com.bokks.micro.springbootrestapi.filters.Secured;
import com.bokks.micro.springbootrestapi.model.User;
import com.bokks.micro.springbootrestapi.model.UserRoles;
import com.bokks.micro.springbootrestapi.service.UserService;
import com.bokks.micro.springbootrestapi.util.CustomErrorType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/expose")
public class RestApiController {

    public static final Logger logger = LoggerFactory.getLogger(RestApiController.class);

    @Autowired
    UserService userService; //Service which will do all data retrieval/manipulation work

    // -------------------Retrieve All Users---------------------------------------------

    @Secured(authorizedBy = {UserRoles.ADMIN,UserRoles.ROLE1}) // Annotation will allow Role based authorization fot the APIs
    @RequestMapping(value = "/version", method = RequestMethod.GET)
    public ResponseEntity<String> getVersion() {
        logger.info("API version details : SpringBootRestAPI Verion 1.0");
        return new ResponseEntity<String>("SpringBootRestAPI Verion 1.0", HttpStatus.OK);
    }

    @Secured(authorizedBy = UserRoles.ADMIN)
    @RequestMapping(value = "/user/", method = RequestMethod.GET)
    public ResponseEntity<List<User>> listAllUsers() {
        List<User> users = userService.findAllUsers();
        if (users.isEmpty()) {
            logger.info("No Users found");
            return new ResponseEntity(HttpStatus.NO_CONTENT);
            // You many decide to return HttpStatus.NOT_FOUND
        }
        return new ResponseEntity<List<User>>(users, HttpStatus.OK);
    }


    // -------------------Retrieve Single User------------------------------------------

    @RequestMapping(value = "/user/{username}", method = RequestMethod.GET)
    public ResponseEntity<?> getUser(@PathVariable("username") String username) {
        logger.info("Fetching User with username {}", username);
        User user = userService.findByUsername(username);
        if (user == null) {
            logger.error("User with username {} not found.", username);
            return new ResponseEntity(new CustomErrorType("User with username " + username
                    + " not found"), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<User>(user, HttpStatus.OK);
    }

    // -------------------Create a User-------------------------------------------

    @RequestMapping(value = "/user/", method = RequestMethod.POST)
    public ResponseEntity<?> createUser(@RequestBody User user, UriComponentsBuilder ucBuilder) {
        logger.info("Creating User : {}", user);

        if (userService.isUserExist(user)) {
            logger.error("Unable to create. A User with name {} already exist", user.getName());
            return new ResponseEntity(new CustomErrorType("Unable to create. A User with name " +
                    user.getName() + " already exist."), HttpStatus.CONFLICT);
        }
        userService.saveUser(user);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/api/user/{id}").buildAndExpand(user.getUsername()).toUri());
        return new ResponseEntity<String>(headers, HttpStatus.CREATED);
    }

    // ------------------- Update a User ------------------------------------------------

    @RequestMapping(value = "/user/{username}", method = RequestMethod.PUT)
    public ResponseEntity<?> updateUser(@PathVariable("username") String username, @RequestBody User user) {
        logger.info("Updating User with username {}", username);

        User currentUser = userService.findByUsername(username);

        if (currentUser == null) {
            logger.error("Unable to update. User with username {} not found.", username);
            return new ResponseEntity(new CustomErrorType("Unable to upate. User with username " + username + " not found."),
                    HttpStatus.NOT_FOUND);
        }

        currentUser.setName(user.getName());
        currentUser.setAge(user.getAge());
        currentUser.setSalary(user.getSalary());

        userService.updateUser(currentUser);
        return new ResponseEntity<User>(currentUser, HttpStatus.OK);
    }

    // ------------------- Delete a User-----------------------------------------

    @RequestMapping(value = "/user/{username}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteUser(@PathVariable("username") String username) {
        logger.info("Fetching & Deleting User with username {}", username);

        User user = userService.findByUsername(username);
        if (user == null) {
            logger.error("Unable to delete. User with username {} not found.", username);
            return new ResponseEntity(new CustomErrorType("Unable to delete. User with username " + username + " not found."),
                    HttpStatus.NOT_FOUND);
        }
        userService.deleteUserByUsername(username);
        return new ResponseEntity<User>(HttpStatus.NO_CONTENT);
    }

    // ------------------- Delete All Users-----------------------------

    @RequestMapping(value = "/user/", method = RequestMethod.DELETE)
    public ResponseEntity<User> deleteAllUsers() {
        logger.info("Deleting All Users");

        userService.deleteAllUsers();
        return new ResponseEntity<User>(HttpStatus.NO_CONTENT);
    }

}