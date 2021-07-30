package com.lifeplus.lifeplus.model.form;

public class PlanForm {
    private int patient;

    public PlanForm() {
    }

    public PlanForm(int patient) {
        this.patient = patient;
    }

    public int getPatient() {
        return patient;
    }

    public void setPatient(int patient) {
        this.patient = patient;
    }
}
