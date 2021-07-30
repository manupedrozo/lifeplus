package com.lifeplus.lifeplus.model;

public class DashboardMobile {

    private Integer points;
    private Integer targetPoints;
    private Integer time;
    private Integer ppm;

    public DashboardMobile() {

    }

    public DashboardMobile(Integer points, Integer targetPoints, Integer time, Integer ppm) {
        this.points = points;
        this.targetPoints = targetPoints;
        this.time = time;
        this.ppm = ppm;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

    public Integer getPpm() {
        return ppm;
    }

    public void setPpm(Integer ppm) {
        this.ppm = ppm;
    }

    public Integer getTargetPoints() {
        return targetPoints;
    }

    public void setTargetPoints(Integer targetPoints) {
        this.targetPoints = targetPoints;
    }
}
