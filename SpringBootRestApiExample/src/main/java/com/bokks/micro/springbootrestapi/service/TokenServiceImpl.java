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

    /**
     * Check the token is in the system
     * @param tokenString
     * @return based on availability return boolena value
     */
    @Override
    public boolean isTokenValid(String tokenString) {
        for(Token token : tokens){
            if(token.getToken().equals(this.getTokenUUID(tokenString))){
                return true;
            }
        }

        return false;
    }


    /***
     * Find the token using token string send in the request
     * find the token using unique UUID
     * @param tokenString
     * @return the Token object
     */
    public Token findByToken(String tokenString) {
        for(Token token : tokens){
            if(token.getToken().equals(this.getTokenUUID(tokenString))){
                return token;
            }
        }
        return null;
    }

    /**
     * Generate token based on the timestamp random UUID and username
     * @param username
     * @return decoded Token string format will be timestamp##UUID##username
     */
    @Override
    public String createToken(String username) {
        // Token unqiuely identified using UUID else can use the decoded token string as Unique identifier
        Date date= new Date();
        long time = date.getTime();
        String tokenString = UUID.randomUUID().toString();
        Token newToken = new Token(time,tokenString,username);
        tokens.add(newToken);

        return newToken.toString();
    }


    /**
     * Split the string and get the token UUID in the token send in the request
     * @param tokenForUUID
     * @return UUID String
     */
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
