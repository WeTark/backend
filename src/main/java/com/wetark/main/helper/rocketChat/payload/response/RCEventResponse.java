package com.wetark.main.helper.rocketChat.payload.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RCEventResponse {
    private Boolean success;
    private RCEventChannelData channel;
}

