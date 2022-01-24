package com.wetark.main.helper.twoFactor.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TwoFactorVerifyOtpResponse {
    @JsonProperty("Status")
    private String Status;
    @JsonProperty("Details")
    private String Details;
}
