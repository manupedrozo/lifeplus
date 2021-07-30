package com.lifeplus.lifeplus.model.form;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

public class ActivityRoutineForm {

    @NotNull
    private int patient;

    private LocalDateTime date;

    @NotNull
    private int routine;

    @NotNull
    private List<ActivityExerciseForm> activityExercises;

    public ActivityRoutineForm() {
    }

    public ActivityRoutineForm(@NotNull int patient, LocalDateTime date, @NotNull int routine, @NotNull List<ActivityExerciseForm> activityExercises) {
        this.patient = patient;
        this.date = date;
        this.routine = routine;
        this.activityExercises = activityExercises;
    }

    public int getPatient() {
        return patient;
    }

    public void setPatient(int patient) {
        this.patient = patient;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public int getRoutine() {
        return routine;
    }

    public void setRoutine(int routine) {
        this.routine = routine;
    }

    public List<ActivityExerciseForm> getActivityExercises() {
        return activityExercises;
    }

    public void setActivityExercises(List<ActivityExerciseForm> activityExercises) {
        this.activityExercises = activityExercises;
    }
}
