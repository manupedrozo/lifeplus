package com.lifeplus.lifeplus.model.ws;

import java.util.List;

/**
 * @author Manuel Pedrozo
 */
public class ActivityIds {
    private int activityRoutine;
    private List<Integer> activityExercises;

    public ActivityIds() { }

    public ActivityIds(int activityRoutine, List<Integer> activityExercises) {
        this.activityRoutine = activityRoutine;
        this.activityExercises = activityExercises;
    }

    public int getActivityRoutine() {
        return activityRoutine;
    }

    public void setActivityRoutine(int activityRoutine) {
        this.activityRoutine = activityRoutine;
    }

    public List<Integer> getActivityExercises() {
        return activityExercises;
    }

    public void setActivityExercises(List<Integer> activityExercises) {
        this.activityExercises = activityExercises;
    }
}
