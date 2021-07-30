package com.lifeplus.lifeplus.model;

import java.util.List;

public class ReportRoutine {

    private ActivityRoutine activityRoutine;
    private List<ReportExercise> reportExercises;

    public ReportRoutine() { }

    public ReportRoutine(ActivityRoutine activityRoutine) {
        this.activityRoutine = activityRoutine;
    }

    public ActivityRoutine getActivityRoutine() {
        return activityRoutine;
    }

    public void setActivityRoutine(ActivityRoutine activityRoutine) {
        this.activityRoutine = activityRoutine;
    }

    public List<ReportExercise> getReportExercises() {
        return reportExercises;
    }

    public void setReportExercises(List<ReportExercise> reportExercises) {
        this.reportExercises = reportExercises;
    }
}
