package com.bokks.micro.springbootrestapi.model;

import org.apache.commons.codec.binary.Base64;

public class Token {

    private long timestamp;

    private String token;

    private String username;

    public Token(long timestamp,String token,String username){
        this.token = token;
        this.timestamp = timestamp;
        this.username = username;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        String encodedToken = timestamp + "|" + token+ "|" + username;
        byte[] encodedBytes = Base64.encodeBase64(encodedToken.getBytes());

        return new String(encodedBytes);
    }


}
