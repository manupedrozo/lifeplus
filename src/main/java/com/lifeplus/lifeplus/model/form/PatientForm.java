package com.lifeplus.lifeplus.model.form;

import com.lifeplus.lifeplus.model.Dto.WeightUpdateFrequency;
import com.lifeplus.lifeplus.model.Patient;

import java.time.LocalDate;

public class PatientForm {

    private LocalDate birthDate;
    private int height;
    private float targetWeight;
    private WeightUpdateFrequency weightUpdateFrequency;

    public PatientForm() {
    }

    public PatientForm(LocalDate birthDate, int height, float targetWeight, WeightUpdateFrequency weightUpdateFrequency) {
        this.birthDate = birthDate;
        this.height = height;
        this.targetWeight = targetWeight;
        this.weightUpdateFrequency = weightUpdateFrequency;
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

    public WeightUpdateFrequency getWeightUpdateFrequency() {
        return weightUpdateFrequency;
    }

    public void setWeightUpdateFrequency(WeightUpdateFrequency weightUpdateFrequency) {
        this.weightUpdateFrequency = weightUpdateFrequency;
    }

    public void patch(Patient entity) {
        entity.setBirthDate(this.birthDate);
        entity.setHeight(this.height);
        entity.setTargetWeight(this.targetWeight);
        entity.setEnumWeightUpdateFrequency(this.weightUpdateFrequency);
    }
}
