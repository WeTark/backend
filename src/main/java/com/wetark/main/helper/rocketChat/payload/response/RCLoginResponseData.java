package com.wetark.main.helper.rocketChat.payload.response;

public class RCLoginResponseData {
    private String userId;
    private String authToken;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
}
