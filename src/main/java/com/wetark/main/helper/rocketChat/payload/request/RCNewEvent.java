package com.wetark.main.helper.rocketChat.payload.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class RCNewEvent {
    private String name;
}
