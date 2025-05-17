package com.x.backend.config;

import com.x.backend.services.token.JwtService;
import com.x.backend.services.token.TokenAuthenticationService;
import org.springframework.lang.NonNull;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class WebSocketAuthChannelInterceptor implements ChannelInterceptor {

    private final TokenAuthenticationService tokenAuthenticationService;

    public WebSocketAuthChannelInterceptor(TokenAuthenticationService tokenAuthenticationService) {
        this.tokenAuthenticationService = tokenAuthenticationService;
    }

    @Override
    public Message<?> preSend(@NonNull Message<?> message,
                              @NonNull MessageChannel channel
    ) {
        StompHeaderAccessor stompHeaderAccessor = StompHeaderAccessor.wrap(message);

        if (StompCommand.CONNECT == stompHeaderAccessor.getCommand()) {
            List<String> authHeaders = stompHeaderAccessor.getNativeHeader("Authorization");

            if (authHeaders != null && !authHeaders.isEmpty()) {
                String accessToken = authHeaders.get(0).replace("Bearer ", "");
                Authentication authentication = tokenAuthenticationService.authenticateAccessToken(accessToken);
                stompHeaderAccessor.setUser(authentication);
            }
        }

        return message;
    }


}
