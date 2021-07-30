package com.lifeplus.lifeplus.controller.ws.raw;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.lifeplus.lifeplus.model.form.SensorDataForm;
import com.lifeplus.lifeplus.service.ActivityRoutineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;

@Component
public class SensorDataHandler extends TextWebSocketHandler {

    private ActivityRoutineService activityRoutineService;
    private ObjectMapper objectMapper;

    @Autowired
    public SensorDataHandler(ActivityRoutineService activityRoutineService) {
        this.activityRoutineService = activityRoutineService;
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) {
        try {
            SensorDataForm sensorDataForm = objectMapper.readValue(message.asBytes(), SensorDataForm.class);
            activityRoutineService.save(sensorDataForm);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
