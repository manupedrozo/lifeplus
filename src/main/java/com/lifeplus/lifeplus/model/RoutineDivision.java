package com.lifeplus.lifeplus.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Manuel Pedrozo
 */
@Entity
@Table(name = "routine_division")
public class RoutineDivision {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    private int id;

    @Column
    private String name;

    @Column
    private int number;

    @OrderBy("number")
    @OneToMany(fetch = FetchType.EAGER)
    private List<AssignedExercise> assignedExercises;

    public RoutineDivision() {
    }

    public RoutineDivision(String name, int number) {
        this.name = name;
        this.number = number;
        this.assignedExercises = new ArrayList<>();
    }

    public RoutineDivision(String name, int number, List<AssignedExercise> assignedExercises) {
        this.name = name;
        this.number = number;
        this.assignedExercises = assignedExercises;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public List<AssignedExercise> getAssignedExercises() {
        return assignedExercises;
    }

    public void setAssignedExercises(List<AssignedExercise> assignedExercises) {
        this.assignedExercises = assignedExercises;
    }

    public static RoutineDivision copy(RoutineDivision rd) {
        return new RoutineDivision(rd.getName(), rd.getNumber());
    }
}
