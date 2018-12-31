package com.bokks.micro.springbootrestapi.controller;

import com.bokks.micro.springbootrestapi.model.APIToken;
import com.bokks.micro.springbootrestapi.model.Credentials;
import com.bokks.micro.springbootrestapi.model.User;
import com.bokks.micro.springbootrestapi.service.TokenService;
import com.bokks.micro.springbootrestapi.service.UserService;
import com.bokks.micro.springbootrestapi.util.CustomErrorType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/authenticate")
public class LoginController {

    public static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    UserService userService; //Service which will do all data retrieval/manipulation work

    @Autowired
    TokenService tokenService;

    /**
     * This is the login api it requires input as json
     * {
     *   "username": "admin",
     *   "password": "admin123"
     * }
     * @param credentials
     * @return
     */

    @RequestMapping(value = "/login", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
    @ResponseBody
    public ResponseEntity<?> loginUser(@RequestBody Credentials credentials) {

        try {

            String username = credentials.getUsername();
            String password = credentials.getPassword();

            // Authenticate the user using the credentials provided
            authenticate(username, password);

            // Issue a token for the user
            String token = issueToken(username);

            // Return the token on the response
            return  new ResponseEntity<APIToken>(new APIToken(token), HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.FORBIDDEN);
        }
    }

    private void authenticate(String username, String password) throws Exception {
        // Authenticate against a database, LDAP, file or whatever
        // Throw an Exception if the credentials are invalid
        // In this case authentiating using hardcoded values

        logger.info("Fetching User with username {}", username);
        User user = userService.findByUsername(username);
        if (user == null) {
            logger.error("User with username {} not found.", username);
            throw new Exception("No user found for the user name : " + username);
        }

        if(user.getPassword().equals(password)){
            logger.info("User with username {}  found. Going to issue the token", username);
        }else{
            throw new Exception("User credentials are invalid for user name: " + username);
        }


    }

    private String issueToken(String username) {
        // Issue a token (can be a random String persisted to a database or a JWT token)
        // The issued token must be associated to a user
        // Return the issued token
        logger.info("Creating Token for username {}", username);
        return  tokenService.createToken(username);

    }
}
