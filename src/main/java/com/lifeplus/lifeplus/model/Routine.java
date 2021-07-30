package com.lifeplus.lifeplus.model;

import com.lifeplus.lifeplus.model.form.RoutineForm;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "routine")
public class Routine implements Identifiable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    private int id;

    @Column
    private String name;

    @Column(length = 1024)
    private String description;

    @Column
    private int frequency;

    @Column
    private RoutineFrequencyType frequencyType;

    @Column
    @Min(0)
    @Max(10)
    private int borg;

    @OneToMany(cascade = CascadeType.REMOVE)
    @OrderBy("number")
    private List<RoutineDivision> divisions;

    public Routine() {
        this.divisions = new ArrayList<>();
    }

    public Routine(String name, String description, int frequency, RoutineFrequencyType frequencyType, @Min(0) @Max(10) int borg, List<RoutineDivision> divisions) {
        this.name = name;
        this.description = description;
        this.frequency = frequency;
        this.frequencyType = frequencyType;
        this.borg = borg;
        this.divisions = divisions;
    }

    public Routine(String name, String description, int frequency, RoutineFrequencyType frequencyType, @Min(0) @Max(10) int borg) {
        this.name = name;
        this.description = description;
        this.frequency = frequency;
        this.frequencyType = frequencyType;
        this.divisions = new ArrayList<>();
        this.borg = borg;
    }

    public Routine(RoutineForm form) {
        this.name = form.getName();
        this.description = form.getDescription();
        this.frequency = form.getFrequency();
        this.frequencyType = form.getFrequencyType();
        this.borg = form.getBorg();
    }

    @Override
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public List<RoutineDivision> getDivisions() {
        return divisions;
    }

    public void setDivisions(List<RoutineDivision> divisions) {
        this.divisions = divisions;
    }

    public static Routine copy(Routine r) {
        return new Routine(
                r.getName(),
                r.getDescription(),
                r.getFrequency(),
                r.getFrequencyType(),
                r.getBorg()
        );
    }
}
