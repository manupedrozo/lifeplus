package com.lifeplus.lifeplus.model;

import javax.persistence.*;

@Entity
@Table(name = "activity_exercise")
public class ActivityExercise implements Identifiable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    private int id;

    @ManyToOne
    private AssignedExercise assignedExercise;

    @Column
    private Integer duration;

    @Column
    private Integer repetitions;

    @Column
    private Integer sets;

    public ActivityExercise() {
    }

    public ActivityExercise(AssignedExercise assignedExercise, Integer duration, Integer repetitions, Integer sets) {
        this.assignedExercise = assignedExercise;
        this.duration = duration;
        this.repetitions = repetitions;
        this.sets = sets;
    }

    @Override
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public AssignedExercise getAssignedExercise() {
        return assignedExercise;
    }

    public void setAssignedExercise(AssignedExercise assignedExercise) {
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
