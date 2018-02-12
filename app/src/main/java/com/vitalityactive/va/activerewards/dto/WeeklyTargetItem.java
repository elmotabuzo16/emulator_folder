package com.vitalityactive.va.activerewards.dto;

public class WeeklyTargetItem {
    private String range;
    private int pointsTarget;
    private String pointsAchieved;
    private int initialPoints;

    public String getRange() {
        return range;
    }

    public void setRange(String range) {
        this.range = range;
    }

    public int getPointsTarget() {
        return pointsTarget;
    }

    public void setPointsTarget(int pointsTarget) {
        this.pointsTarget = pointsTarget;
    }

    public String getPointsAchieved() {
        return pointsAchieved;
    }

    public void setPointsAchieved(String pointsAchieved) {
        this.pointsAchieved = pointsAchieved;
    }

    public void setInitialPoints(int initialPoints) {
        this.initialPoints = initialPoints;
    }

    public int getInitialPoints() {
        return initialPoints;
    }
}
