package com.lifeplus.lifeplus.model;

import java.util.List;

public class ReportExercise {

    private int activityExerciseId;
    private List<SensorData> data;

    public ReportExercise() {
    }

    public int getActivityExerciseId() {
        return activityExerciseId;
    }

    public void setActivityExerciseId(int activityExerciseId) {
        this.activityExerciseId = activityExerciseId;
    }

    public List<SensorData> getData() {
        return data;
    }

    public void setData(List<SensorData> data) {
        this.data = data;
    }
}
