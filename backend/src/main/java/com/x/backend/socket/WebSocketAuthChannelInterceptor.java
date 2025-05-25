package com.x.backend.socket;

import com.x.backend.services.token.TokenAuthenticationService;
import org.springframework.lang.NonNull;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        if (accessor != null && StompCommand.CONNECT.equals(accessor.getCommand())) {
            List<String> authHeaders = accessor.getNativeHeader("Authorization");

            if (authHeaders != null && !authHeaders.isEmpty()) {
                String token = authHeaders.get(0).replace("Bearer ", "");
                Authentication authentication = tokenAuthenticationService.authenticateAccessToken(token);
                accessor.setUser(authentication);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        return message;
    }

}
