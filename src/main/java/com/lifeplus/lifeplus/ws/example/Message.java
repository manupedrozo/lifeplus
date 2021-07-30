package com.lifeplus.lifeplus.ws.example;

import java.time.LocalDateTime;

/**
 * @author Manuel Pedrozo
 */
public class Message {
    private String from;
    private String text;
    private LocalDateTime time;

    public Message() { }

    public Message(String from, String text, LocalDateTime time) {
        this.from = from;
        this.text = text;
        this.time = time;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
