package com.lifeplus.lifeplus.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "activity_routine")
public class ActivityRoutine implements Identifiable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    private int id;

    @ManyToOne
    private Patient patient;

    @Column
    @Min(1)
    @Max(10)
    private Integer patientBorg;

    @Column(length = 1024)
    private String notes;

    @Column(length = 1024)
    private String incidents;

    @Column
    private LocalDateTime date;

    @Column
    private String name;

    @Column(length = 1024)
    private String description;

    @Column
    private int frequency;

    @Column
    private RoutineFrequencyType frequencyType;

    @Column
    @Min(1)
    @Max(10)
    private int borg;

    @OneToMany(fetch = FetchType.EAGER)
    private List<ActivityExercise> exercises;

    public ActivityRoutine() {
    }

    public ActivityRoutine(Routine routine, Patient patient, @Min(1) @Max(10) Integer patientBorg, String notes, String incidents, LocalDateTime date, List<ActivityExercise> exercises) {
        this.patient = patient;
        this.patientBorg = patientBorg;
        this.notes = notes;
        this.incidents = incidents;
        this.date = date;
        this.exercises = exercises;
        this.name = routine.getName();
        this.description = routine.getDescription();
        this.frequency = routine.getFrequency();
        this.frequencyType = routine.getFrequencyType();
        this.borg = routine.getBorg();
    }

    public ActivityRoutine(Routine routine, Patient patient, @Min(1) @Max(10) Integer patientBorg, String notes, String incidents, LocalDateTime date) {
        this.patient = patient;
        this.patientBorg = patientBorg;
        this.notes = notes;
        this.incidents = incidents;
        this.date = date;
        this.name = routine.getName();
        this.description = routine.getDescription();
        this.frequency = routine.getFrequency();
        this.frequencyType = routine.getFrequencyType();
        this.borg = routine.getBorg();
    }

    public ActivityRoutine(Routine routine, Patient patient, LocalDateTime date) {
        this.patient = patient;
        this.date = date;
        this.name = routine.getName();
        this.description = routine.getDescription();
        this.frequency = routine.getFrequency();
        this.frequencyType = routine.getFrequencyType();
        this.borg = routine.getBorg();
    }

    @Override
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @JsonIgnore
    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Integer getPatientBorg() {
        return patientBorg;
    }

    public void setPatientBorg(Integer patientBorg) {
        this.patientBorg = patientBorg;
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

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public List<ActivityExercise> getExercises() {
        return exercises;
    }

    public void setExercises(List<ActivityExercise> exercises) {
        this.exercises = exercises;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public RoutineFrequencyType getFrequencyType() {
        return frequencyType;
    }

    public void setFrequencyType(RoutineFrequencyType frequencyType) {
        this.frequencyType = frequencyType;
    }

    public int getBorg() {
        return borg;
    }

    public void setBorg(int borg) {
        this.borg = borg;
    }
}
