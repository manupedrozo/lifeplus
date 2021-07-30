package com.lifeplus.lifeplus.model.form;

import java.util.List;

/**
 * @author Manuel Pedrozo
 */
public class RoutineDivisionForm {

    private Integer id; // For updates only

    private String name;

    private List<AssignedExerciseForm> assignedExercises;

    public RoutineDivisionForm() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<AssignedExerciseForm> getAssignedExercises() {
        return assignedExercises;
    }

    public void setAssignedExercises(List<AssignedExerciseForm> assignedExercises) {
        this.assignedExercises = assignedExercises;
    }
}
