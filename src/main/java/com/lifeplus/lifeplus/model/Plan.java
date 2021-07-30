package com.lifeplus.lifeplus.model;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "plan",
        uniqueConstraints =
        @UniqueConstraint(columnNames = {"patient_id"})
)
public class Plan implements Identifiable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    private int id;

    @OneToMany(cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
    private List<Routine> routines;

    @OneToOne
    private Patient patient;

    public Plan() {
    }

    public Plan(Patient patient) {
        this.patient = patient;
    }

    public Plan(List<Routine> routines, Patient patient) {
        this.routines = routines;
        this.patient = patient;
    }

    @Override
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Routine> getRoutines() {
        return routines;
    }

    public void setRoutines(List<Routine> routines) {
        this.routines = routines;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }
}
