package com.lifeplus.lifeplus.ws;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.server.HandshakeInterceptor;

/**
 * @author Manuel Pedrozo
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig
        implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/queue/");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        HandshakeInterceptor handshakeInterceptor = new HttpHandshakeInterceptor();
        registry.addEndpoint("/ws")
                .setAllowedOrigins("*")
                .addInterceptors(handshakeInterceptor)
                .withSockJS();
        registry.addEndpoint("/ws")
                .setAllowedOrigins("*")
                .addInterceptors(handshakeInterceptor);
    }
}
