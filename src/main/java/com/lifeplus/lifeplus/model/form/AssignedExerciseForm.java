package com.lifeplus.lifeplus.model.form;

import com.lifeplus.lifeplus.model.AssignedExercise;

public class AssignedExerciseForm {

    private int exercise;

    private Integer duration;

    private Integer repetitions;

    private Integer sets;

    public AssignedExerciseForm() {
    }

    public int getExercise() {
        return exercise;
    }

    public void setExercise(int exercise) {
        this.exercise = exercise;
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

    public boolean equals(AssignedExercise assignedExercise) {

        return exercise == assignedExercise.getExercise().getId() &&
                ((duration == null && assignedExercise.getDuration() == null) ||
                        (duration != null && assignedExercise.getDuration() != null && duration.intValue() == assignedExercise.getDuration().intValue())) &&
                ((repetitions == null && assignedExercise.getRepetitions() == null) ||
                        (repetitions != null && assignedExercise.getRepetitions() != null && repetitions.intValue() == assignedExercise.getRepetitions().intValue())) &&
                ((sets == null && assignedExercise.getSets() == null) ||
                        (sets != null && assignedExercise.getSets() != null && sets.intValue() == assignedExercise.getSets().intValue()));
    }
}
