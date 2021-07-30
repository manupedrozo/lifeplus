package com.lifeplus.lifeplus.model.form;


import java.time.LocalDateTime;

/**
 * @author Manuel Pedrozo
 */
public class SensorDataForm {
    private int activityExercise;
    private int bpm;
    private LocalDateTime date;

    public int getActivityExercise() {
        return activityExercise;
    }

    public void setActivityExercise(int activityExercise) {
        this.activityExercise = activityExercise;
    }

    public int getBpm() {
        return bpm;
    }

    public void setBpm(int bpm) {
        this.bpm = bpm;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }
}
