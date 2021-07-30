package com.lifeplus.lifeplus.model;

import java.time.LocalDateTime;
import java.util.List;

public class WeightReport {
    private LocalDateTime from;
    private LocalDateTime to;
    private Patient patient;
    private List<WeightEntry> reportEntries;

    public WeightReport() {
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

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public List<WeightEntry> getReportEntries() {
        return reportEntries;
    }

    public void setReportEntries(List<WeightEntry> reportEntries) {
        this.reportEntries = reportEntries;
    }
}
