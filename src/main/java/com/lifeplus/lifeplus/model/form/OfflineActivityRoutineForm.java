package com.lifeplus.lifeplus.model.form;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

public class OfflineActivityRoutineForm {

    @Min(1)
    @Max(10)
    private Integer patientBorg;

    private String notes;

    private String incidents;

    private LocalDateTime date;

    @NotNull
    private int routine;

    @NotNull
    private List<OfflineActivityExerciseForm> activityExercises;

    public OfflineActivityRoutineForm() {
    }

    public OfflineActivityRoutineForm(@Min(1) @Max(10) Integer patientBorg, String notes, String incidents, LocalDateTime date, @NotNull int routine, @NotNull List<OfflineActivityExerciseForm> activityExercises) {
        this.patientBorg = patientBorg;
        this.notes = notes;
        this.incidents = incidents;
        this.date = date;
        this.routine = routine;
        this.activityExercises = activityExercises;
    }

    public Integer getPatientBorg() {
        return patientBorg;
    }

    public void setPatientBorg(Integer patientBorg) {
        this.patientBorg = patientBorg;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getIncidents() {
        return incidents;
    }

    public void setIncidents(String incidents) {
        this.incidents = incidents;
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

    public List<OfflineActivityExerciseForm> getActivityExercises() {
        return activityExercises;
    }

    public void setActivityExercises(List<OfflineActivityExerciseForm> activityExercises) {
        this.activityExercises = activityExercises;
    }
}
