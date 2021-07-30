package com.lifeplus.lifeplus.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "weight_history")
public class WeightEntry implements Identifiable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    private int id;

    @OneToOne
    private Patient patient;

    @Column
    private LocalDateTime date;

    @Column
    private float weight;

    public WeightEntry() {
    }

    public WeightEntry(Patient patient, LocalDateTime date, float weight) {
        this.patient = patient;
        this.date = date;
        this.weight = weight;
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

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    @Override
    public String toString() {
        return "{patient: " +
                patient +
                ", date: " +
                date.toString() +
                ", weight: "
                + weight
                + "}";
    }
}
