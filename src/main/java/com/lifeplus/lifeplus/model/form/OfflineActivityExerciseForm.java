package com.lifeplus.lifeplus.model.form;

import javax.validation.constraints.NotNull;
import java.util.List;

public class OfflineActivityExerciseForm {

    @NotNull
    private int assignedExercise;
    private Integer duration;
    private Integer repetitions;
    private Integer sets;
    private List<OfflineSensorDataForm> sensorData;

    public OfflineActivityExerciseForm() {
    }

    public OfflineActivityExerciseForm(@NotNull int assignedExercise, Integer duration, Integer repetitions, Integer sets) {
        this.assignedExercise = assignedExercise;
        this.duration = duration;
        this.repetitions = repetitions;
        this.sets = sets;
    }

    public int getAssignedExercise() {
        return assignedExercise;
    }

    public void setAssignedExercise(int assignedExercise) {
        this.assignedExercise = assignedExercise;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Integer getRepetitions() {
        return repetitions;
    }

    public void setRepetitions(Integer repetitions) {
        this.repetitions = repetitions;
    }

    public Integer getSets() {
        return sets;
    }

    public void setSets(Integer sets) {
        this.sets = sets;
    }

    public List<OfflineSensorDataForm> getSensorData() {
        return sensorData;
    }

    public void setSensorData(List<OfflineSensorDataForm> sensorData) {
        this.sensorData = sensorData;
    }
}
