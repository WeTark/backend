package com.wetark.main.payload.response.user;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Setter
@Getter
public class UserLoginResponse {
    private Boolean isUserExist;
    private String message;
    private String sessionId;
}
