package com.bokks.micro.springbootrestapi.service;

import com.bokks.micro.springbootrestapi.model.Token;
import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Service;

import java.util.*;

@Service("tokenService")
public class TokenServiceImpl implements TokenService {

    private static List<Token> tokens;

    static{
        tokens= populateDummyTokens();
    }

    @Override
    public void updateToken() {

    }

    @Override
    public boolean isTokenValid(String tokenString) {
        for(Token token : tokens){
            if(token.getToken().equals(this.getTokenUUID(tokenString))){
                return true;
            }
        }

        return false;
    }


    public Token findByToken(String tokenString) {
        for(Token token : tokens){
            if(token.getToken().equals(this.getTokenUUID(tokenString))){
                return token;
            }
        }
        return null;
    }

    @Override
    public String createToken(String username) {
        Date date= new Date();
        long time = date.getTime();
        String tokenString = UUID.randomUUID().toString();
        Token newToken = new Token(time,tokenString,username);
        tokens.add(newToken);

        return newToken.toString();
    }

    public void updateToken(String token) {
//        int index = tokens.indexOf(token);
//        tokens.set(index, token);
    }

    private String getTokenUUID(String tokenForUUID){
        byte[] decodedBytes = Base64.decodeBase64(tokenForUUID.getBytes());
        String tokenStringAfterDecode = new String(decodedBytes);
        String[] tokenArray = tokenStringAfterDecode.split("##");

        return tokenArray[1];

    }
    private static List<Token> populateDummyTokens(){
        List<Token> tokens = new ArrayList<Token>();
//        tokens.add(new User(counter.incrementAndGet(),"Sam",30, 70000,"admin123", UserRoles.ADMIN));
//        tokens.add(new User(counter.incrementAndGet(),"Tom",40, 50000,"admin123", UserRoles.ROLE1));
//        tokens.add(new User(counter.incrementAndGet(),"Jerome",45, 30000,"admin123", UserRoles.ROLE2));
//        tokens.add(new User(counter.incrementAndGet(),"Silvia",50, 40000,"admin123", UserRoles.ROLE3));
        return tokens;
    }
}
