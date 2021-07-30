package com.lifeplus.lifeplus.model.Dto;

import com.lifeplus.lifeplus.model.Identifiable;

import java.time.LocalDateTime;

public class ActivityRoutineBasicDto implements Identifiable {

    private int id;

    private LocalDateTime date;

    private String name;

    public ActivityRoutineBasicDto() {
    }

    public ActivityRoutineBasicDto(int id, LocalDateTime date, String name) {
        this.id = id;
        this.date = date;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
