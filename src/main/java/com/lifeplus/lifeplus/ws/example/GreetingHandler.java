package com.lifeplus.lifeplus.ws.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.time.LocalDateTime;

public class GreetingHandler extends TextWebSocketHandler {

    private ObjectMapper objectMapper;

    public GreetingHandler() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) {
        try {
            Message m = objectMapper.readValue(message.asBytes(), Message.class);
            Message responseMessage = new Message("Back", m.getFrom() + " : " + m.getText(), LocalDateTime.now());
            TextMessage response = new TextMessage(objectMapper.writeValueAsBytes(responseMessage));
            session.sendMessage(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
