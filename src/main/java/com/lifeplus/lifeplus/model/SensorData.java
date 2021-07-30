package com.lifeplus.lifeplus.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author Manuel Pedrozo
 */
@Entity
@Table(name = "sensor_data")
public class SensorData implements Identifiable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    private int id;

    @ManyToOne
    private ActivityExercise activityExercise;

    @Column
    private int bpm;

    @Column
    private LocalDateTime date;

    public SensorData() {
    }

    public SensorData(ActivityExercise activityExercise, int bpm, LocalDateTime date) {
        this.activityExercise = activityExercise;
        this.bpm = bpm;
        this.date = date;
    }

    @Override
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @JsonIgnore
    public ActivityExercise getActivityExercise() {
        return activityExercise;
    }

    public void setActivityExercise(ActivityExercise activityExercise) {
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
