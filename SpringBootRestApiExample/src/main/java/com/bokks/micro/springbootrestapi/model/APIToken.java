package com.bokks.micro.springbootrestapi.model;

public class APIToken {

    String apiToken;

    public APIToken(String apiToken) {
        this.apiToken= apiToken;
    }

    public String getApiToken() {
        return apiToken;
    }

    public void setApiToken(String apiToken) {
        this.apiToken = apiToken;
    }
}
