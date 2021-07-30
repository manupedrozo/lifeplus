package com.lifeplus.lifeplus.model;

import java.time.LocalDateTime;
import java.util.List;

public class Report {

    private LocalDateTime from;
    private LocalDateTime to;
    private int patientId;
    private List<ReportRoutine> reportRoutines;

    public Report() {
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

    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public List<ReportRoutine> getReportRoutines() {
        return reportRoutines;
    }

    public void setReportRoutines(List<ReportRoutine> reportRoutines) {
        this.reportRoutines = reportRoutines;
    }
}
