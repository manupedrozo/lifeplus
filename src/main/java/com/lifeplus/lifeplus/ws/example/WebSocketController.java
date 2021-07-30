package com.lifeplus.lifeplus.ws.example;

import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;

/**
 * @author Manuel Pedrozo
 */
@Controller
public class WebSocketController {

    @MessageMapping("/message")
    @SendToUser("/queue/reply")
    public Message greeting(Message message, SimpMessageHeaderAccessor headerAccessor) {
        String sessionId = headerAccessor.getSessionAttributes().get("sessionId").toString();
        System.out.println("Message '" + message.getText() + "' received from session: " + sessionId);
        //Thread.sleep(1000); // simulated delay
        return new Message("Back", message.getFrom() + " : " + message.getText(), LocalDateTime.now());
    }

    @MessageExceptionHandler
    @SendToUser("/queue/errors")
    public String handleException(Throwable exception) {
        return exception.getMessage();
    }
}
