package com.wetark.main.helper.rocketChat;

import com.wetark.main.helper.rocketChat.payload.request.RCLoginUser;
import com.wetark.main.helper.rocketChat.payload.request.RCNewEvent;
import com.wetark.main.helper.rocketChat.payload.request.RCRegisterUser;
import com.wetark.main.helper.rocketChat.payload.response.RCEventResponse;
import com.wetark.main.helper.rocketChat.payload.response.RCLoginResponse;
import com.wetark.main.helper.rocketChat.payload.response.RCRegResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

@Service
public class RocketChatHelper {
    private final WebClient webClient;

    @Value("${rocket.chat.username}")
    private String username;

    @Value("${rocket.chat.password}")
    private String password;

    public RocketChatHelper(@Value("https://chat.wetark.in")String baseUrl) {
        this.webClient = WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(HttpClient.newConnection().compress(true)))
                .baseUrl(baseUrl)
                .build();
    }

    public RCRegResponse CreateUser(RCRegisterUser user){
        Mono<RCRegResponse> res = webClient.post().uri("/api/v1/users.register")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(Mono.just(user), RCRegisterUser.class)
                .exchange()
                .flatMap( clientResponse -> {
                    return clientResponse.bodyToMono( RCRegResponse.class );
                });
        return res.block();
    }

    public RCLoginResponse LoginUser(RCLoginUser user){
        RCLoginResponse response = webClient.post().uri("/api/v1/login")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(Mono.just(user), RCLoginUser.class)
                .exchange()
                .flatMap( clientResponse -> {
                    //Error handling
                    if ( clientResponse.statusCode().isError() ) {
                        return clientResponse.createException().flatMap( Mono::error );
                    }
                    return clientResponse.bodyToMono( RCLoginResponse.class );
                }).block();
        return response;
    }

    public RCEventResponse CreateEvent(String eventName){
        RCLoginResponse rcLoginResponse = LoginUser(new RCLoginUser(username, password));
        return webClient.post().uri("/api/v1/channels.create")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .header("X-Auth-Token", rcLoginResponse.getData().getAuthToken())
                .header("X-User-Id", rcLoginResponse.getData().getUserId())
                .body(Mono.just(RCNewEvent.builder().name(eventName).build()), RCNewEvent.class)
                .exchange()
                .flatMap( clientResponse -> {
                    return clientResponse.bodyToMono( RCEventResponse.class );
                }).block();
    }
}
