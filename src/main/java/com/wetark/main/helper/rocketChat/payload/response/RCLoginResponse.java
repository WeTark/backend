package com.wetark.main.helper.rocketChat.payload.response;

import java.util.HashMap;
import java.util.Map;

public class RCLoginResponse {
    private String status;
    private RCLoginResponseData data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public RCLoginResponseData getData() {
        return data;
    }

    public void setData(RCLoginResponseData data) {
        this.data = data;
    }
}