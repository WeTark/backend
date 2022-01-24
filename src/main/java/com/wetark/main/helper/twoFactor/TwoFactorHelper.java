package com.wetark.main.helper.twoFactor;

import com.wetark.main.helper.rocketChat.payload.request.RCRegisterUser;
import com.wetark.main.helper.rocketChat.payload.response.RCRegResponse;
import com.wetark.main.helper.twoFactor.payload.TwoFactorSendOtpResponse;
import com.wetark.main.helper.twoFactor.payload.TwoFactorVerifyOtpResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

@Service
public class TwoFactorHelper {
    private final WebClient webClient;

    public TwoFactorHelper(@Value("https://2factor.in/API/V1/${2factor.api.key}")String baseUrl) {
        this.webClient = WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(HttpClient.newConnection().compress(true)))
                .baseUrl(baseUrl)
                .build();
    }

    public TwoFactorSendOtpResponse sendOTP(String phoneNo){
        return webClient.get().uri("/SMS/"+phoneNo+"/AUTOGEN/Template2")
                .retrieve()
                .bodyToMono(TwoFactorSendOtpResponse.class)
                .block();
    }

    public TwoFactorVerifyOtpResponse verifyOTP(String sessionId, String otp){
        return webClient.get().uri("/SMS/VERIFY/"+sessionId+"/"+otp)
                .retrieve()
                .bodyToMono(TwoFactorVerifyOtpResponse.class)
                .block();
    }
}
