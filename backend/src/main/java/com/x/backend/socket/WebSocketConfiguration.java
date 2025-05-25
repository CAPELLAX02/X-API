package com.x.backend.socket;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * Configures the WebSocket messaging infrastructure for STOMP-based communication.
 *
 * <p>This class registers the main WebSocket endpoint, sets up message broker routing,
 * and applies authentication logic via a custom {@link WebSocketAuthChannelInterceptor}.
 * It enables support for both application-specific and user-targeted message destinations.
 * </p>
 *
 * <p>Key components:</p>
 * <ul>
 *     <li><b>STOMP endpoint:</b> exposed at <code>/ws</code> with SockJS fallback</li>
 *     <li><b>App prefix:</b> messages from client are routed to <code>/app/**</code> methods</li>
 *     <li><b>User destinations:</b> messages from server to a specific user are sent via <code>/user/**</code></li>
 * </ul>
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfiguration implements WebSocketMessageBrokerConfigurer {

    private final WebSocketAuthChannelInterceptor authChannelInterceptor;

    public WebSocketConfiguration(WebSocketAuthChannelInterceptor authChannelInterceptor) {
        this.authChannelInterceptor = authChannelInterceptor;
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*")
                .withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // Client -> Server
        registry.setApplicationDestinationPrefixes("/app");
        // Server -> Client
        registry.enableSimpleBroker("/user", "/topic");
        // User-specific
        registry.setUserDestinationPrefix("/user");
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(authChannelInterceptor);
    }

}
