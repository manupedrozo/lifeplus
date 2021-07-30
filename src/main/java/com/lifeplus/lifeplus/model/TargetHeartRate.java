package com.lifeplus.lifeplus.model;

public class TargetHeartRate {
    private int min;
    private int med;
    private int max;

    public TargetHeartRate() {
    }

    public TargetHeartRate(int min, int med, int max) {
        this.min = min;
        this.med = med;
        this.max = max;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public int getMed() {
        return med;
    }

    public void setMed(int med) {
        this.med = med;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    @Override
    public String toString() {
        return "TargetHeartRate{" +
                "min=" + min +
                ", med=" + med +
                ", max=" + max +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        TargetHeartRate t = (TargetHeartRate) o;
        return t.min == this.min && t.med == this.med && t.max == this.max;
    }
}
