package com.vitalityactive.va.vitalitystatus.levels;

public class LevelStatusDTO {
    private String name;
    private int key;
    private int pointsNeeded;
    private int pointsThreshold;
    private int smallIconResourceId;
    private int largeIconResourceId;
    private int sortOrder;

    public LevelStatusDTO(String name, int key, int pointsNeeded, int pointsThreshold, int smallIconResourceId, int largeIconResourceId, int sortOrder) {
        this.name = name;
        this.key = key;
        this.pointsNeeded = pointsNeeded;
        this.pointsThreshold = pointsThreshold;
        this.smallIconResourceId = smallIconResourceId;
        this.largeIconResourceId = largeIconResourceId;
        this.sortOrder = sortOrder;
    }

    public LevelStatusDTO() {

    }

    public int getKey() {
        return key;
    }

    public String getName() {
        return name;
    }

    public int getPointsNeeded() {
        return pointsNeeded;
    }

    public int getPointsThreshold() {
        return pointsThreshold;
    }

    public int getSortOrder() {
        return sortOrder;
    }

    public int getSmallIconResourceId() {
        return smallIconResourceId;
    }

    public int getLargeIconResourceId() {
        return largeIconResourceId;
    }
}
