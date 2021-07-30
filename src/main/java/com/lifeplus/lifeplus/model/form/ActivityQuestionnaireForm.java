package com.lifeplus.lifeplus.model.form;

import javax.persistence.Column;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

/**
 * @author Manuel Pedrozo
 */
public class ActivityQuestionnaireForm {

    private int activityRoutine;

    @Min(1)
    @Max(10)
    private int borg;

    @Column(length = 1024)
    private String notes;

    @Column(length = 1024)
    private String incidents;


    public ActivityQuestionnaireForm() {
    }

    public int getActivityRoutine() {
        return activityRoutine;
    }

    public void setActivityRoutine(int activityRoutine) {
        this.activityRoutine = activityRoutine;
    }

    public int getBorg() {
        return borg;
    }

    public void setBorg(int borg) {
        this.borg = borg;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getIncidents() {
        return incidents;
    }

    public void setIncidents(String incidents) {
        this.incidents = incidents;
    }
}
