package com.lifeplus.lifeplus.ws;

import com.lifeplus.lifeplus.controller.ws.raw.SensorDataHandler;
import com.lifeplus.lifeplus.ws.example.GreetingHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketRawConfig implements WebSocketConfigurer {

    @Autowired
    SensorDataHandler sensorDataHandler;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new GreetingHandler(), "/greeting").setAllowedOrigins("*");
        registry.addHandler(sensorDataHandler, "/activity").setAllowedOrigins("*");
    }
}
