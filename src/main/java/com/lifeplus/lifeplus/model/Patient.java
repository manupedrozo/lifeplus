package com.lifeplus.lifeplus.model;

import com.lifeplus.lifeplus.model.Dto.WeightUpdateFrequency;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "patient")
public class Patient implements Identifiable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    private int id;

    @OneToOne
    private User user;

    @Column
    private LocalDate birthDate;

    @Column
    private int height;

    @Column
    private float targetWeight;

    @Column
    private float weight;

    @Column
    private int weightUpdateFrequency; // in days

    @Column
    private LocalDateTime weightLastUpdated;

    @OneToOne
    private Team team;

    public Patient() {
    }

    public Patient(User user, LocalDate birthDate, int height, float targetWeight, float weight, int weightUpdateFrequency, LocalDateTime weightLastUpdated, Team team) {
        this.user = user;
        this.birthDate = birthDate;
        this.height = height;
        this.targetWeight = targetWeight;
        this.weight = weight;
        this.weightUpdateFrequency = weightUpdateFrequency;
        this.weightLastUpdated = weightLastUpdated;
        this.team = team;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public float getTargetWeight() {
        return targetWeight;
    }

    public void setTargetWeight(float targetWeight) {
        this.targetWeight = targetWeight;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public int getWeightUpdateFrequency() {
        return weightUpdateFrequency;
    }

    public void setWeightUpdateFrequency(int weightUpdateFrequency) {
        this.weightUpdateFrequency = weightUpdateFrequency;
    }

    public WeightUpdateFrequency getEnumWeightUpdateFrequency() {
        return WeightUpdateFrequency.toWeightUpdateFrequency(this.weightUpdateFrequency);
    }

    public void setEnumWeightUpdateFrequency(WeightUpdateFrequency weightUpdateFrequency) {
        this.weightUpdateFrequency = WeightUpdateFrequency.toDays(weightUpdateFrequency);
    }

    public LocalDateTime getWeightLastUpdated() {
        return weightLastUpdated;
    }

    public void setWeightLastUpdated(LocalDateTime weightLastUpdated) {
        this.weightLastUpdated = weightLastUpdated;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }
}
