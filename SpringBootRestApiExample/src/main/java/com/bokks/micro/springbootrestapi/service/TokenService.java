package com.bokks.micro.springbootrestapi.service;

import com.bokks.micro.springbootrestapi.model.Token;

public interface TokenService {

    Token findByToken(String token);

    String createToken(String username);

    void updateToken();

    boolean isTokenValid(String tokenString);

}
