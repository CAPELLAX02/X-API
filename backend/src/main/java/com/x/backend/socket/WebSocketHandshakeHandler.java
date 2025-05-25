package com.x.backend.socket;

import com.x.backend.services.token.TokenAuthenticationService;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.security.core.Authentication;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.security.Principal;
import java.util.List;
import java.util.Map;

public class WebSocketHandshakeHandler extends DefaultHandshakeHandler {

    private final TokenAuthenticationService tokenAuthenticationService;

    public WebSocketHandshakeHandler(TokenAuthenticationService tokenAuthenticationService) {
        this.tokenAuthenticationService = tokenAuthenticationService;
    }

    @Override
    protected Principal determineUser(ServerHttpRequest request,
                                      WebSocketHandler wsHandler,
                                      Map<String, Object> attributes) {

        List<String> authHeaders = request.getHeaders().get("Authorization");
        if (authHeaders != null && !authHeaders.isEmpty()) {
            String token = authHeaders.get(0).replace("Bearer ", "");
            Authentication authentication = tokenAuthenticationService.authenticateAccessToken(token);
            if (authentication != null && authentication.isAuthenticated()) {
                return authentication;
            }
        }
        return null;
    }

}
