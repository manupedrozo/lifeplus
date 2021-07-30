package com.lifeplus.lifeplus.model.form;

import com.lifeplus.lifeplus.model.RoutineFrequencyType;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;

public class RoutineForm {

    private String name;

    private String description;

    private int frequency;

    private RoutineFrequencyType frequencyType;

    @Min(0)
    @Max(10)
    private int borg;

    private List<RoutineDivisionForm> divisions;

    public RoutineForm() {
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

    public List<RoutineDivisionForm> getDivisions() {
        return divisions;
    }

    public void setDivisions(List<RoutineDivisionForm> divisions) {
        this.divisions = divisions;
    }
}
