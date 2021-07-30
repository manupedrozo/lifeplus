package com.lifeplus.lifeplus.model;

import javax.persistence.*;

@Entity
@Table(name = "assigned_exercise")
public class AssignedExercise implements Identifiable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    private int id;

    @ManyToOne
    private Exercise exercise;

    @Column
    private int number;

    @Column
    private Integer duration;

    @Column
    private Integer repetitions;

    @Column
    private Integer sets;

    @Column
    private boolean active = true;

    public AssignedExercise() {
    }

    public AssignedExercise(Exercise exercise, int number, Integer duration, Integer repetitions, Integer sets) {
        this.exercise = exercise;
        this.number = number;
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

    public Exercise getExercise() {
        return exercise;
    }

    public void setExercise(Exercise exercise) {
        this.exercise = exercise;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
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

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public static AssignedExercise copy(AssignedExercise ae) {
        return new AssignedExercise(
                ae.getExercise(),
                ae.getNumber(),
                ae.getDuration(),
                ae.getRepetitions(),
                ae.getSets()
        );
    }
}
