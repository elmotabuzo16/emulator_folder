package com.vitalityactive.va.dto;

public class PointsEntryCategoryDTO {
    private final int typeKey;
    private final String title;

    public PointsEntryCategoryDTO(int typeKey, String title) {
        this.typeKey = typeKey;
        this.title = title;
    }

    public int getTypeKey() {
        return typeKey;
    }

    public String getTitle() {
        return title;
    }

    public boolean isAll() {
        return typeKey == 999;
    }

    public boolean isOther() {
        return typeKey == -1;
    }

    public boolean isAssessment() {
        return typeKey == 5;
    }

    public boolean isScreening() {
        return typeKey == 6;
    }

    public boolean isFitness() {
        return typeKey == 3;
    }
}
