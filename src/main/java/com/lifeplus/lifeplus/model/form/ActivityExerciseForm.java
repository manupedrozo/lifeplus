package com.lifeplus.lifeplus.model.form;

import javax.validation.constraints.NotNull;

public class ActivityExerciseForm {

    @NotNull
    private int assignedExercise;
    private Integer duration;
    private Integer repetitions;
    private Integer sets;

    public ActivityExerciseForm() {
    }

    public ActivityExerciseForm(@NotNull int assignedExercise, Integer duration, Integer repetitions, Integer sets) {
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

}
