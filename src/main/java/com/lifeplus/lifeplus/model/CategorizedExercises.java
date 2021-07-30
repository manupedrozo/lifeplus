package com.lifeplus.lifeplus.model;

import java.util.List;

public class CategorizedExercises {

    private int id;

    private String name;

    private List<Exercise> exercises;

    public CategorizedExercises() {
    }

    public CategorizedExercises(int id, String name, List<Exercise> exercises) {
        this.id = id;
        this.name = name;
        this.exercises = exercises;
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

    public List<Exercise> getExercises() {
        return exercises;
    }

    public void setExercises(List<Exercise> exercises) {
        this.exercises = exercises;
    }
}
