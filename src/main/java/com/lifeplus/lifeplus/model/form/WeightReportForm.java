package com.lifeplus.lifeplus.model.form;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class WeightReportForm {
    @NotNull
    private LocalDateTime from;
    @NotNull
    private LocalDateTime to;
    @NotNull
    private int patient;

    public WeightReportForm() {
    }

    public LocalDateTime getFrom() {
        return from;
    }

    public void setFrom(LocalDateTime from) {
        this.from = from;
    }

    public LocalDateTime getTo() {
        return to;
    }

    public void setTo(LocalDateTime to) {
        this.to = to;
    }

    public int getPatient() {
        return patient;
    }

    public void setPatient(int patient) {
        this.patient = patient;
    }

    @Override
    public String toString() {
        return "{patient: " + patient + ", \n from: " + from.toString() + ", \n to:" + to.toString() + "}";
    }
}
