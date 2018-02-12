package com.vitalityactive.va.vitalitystatus.earningpoints;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class PointsInformationDTO {
    private int potentialPoints;
    private boolean hasFeatures;
    private boolean pointsLimitReached;
    private int key;
    @NonNull
    private String pointsEarningFlag = "";
    @NonNull
    private String name = "";

    public PointsInformationDTO(@Nullable String name,
                                int potentialPoints,
                                boolean hasFeatures,
                                boolean pointsLimitReached,
                                int key,
                                @NonNull String pointsEarningFlag) {
        this.name = name == null ? "" : name;
        this.potentialPoints = potentialPoints;
        this.hasFeatures = hasFeatures;
        this.pointsLimitReached = pointsLimitReached;
        this.key = key;
        this.pointsEarningFlag = pointsEarningFlag;
    }

    public PointsInformationDTO() {

    }

    public PointsInformationDTO(String name, int potentialPoints, boolean hasFeatures, int key, String pointsEarningFlag) {
        this(name, potentialPoints, hasFeatures, false, key, pointsEarningFlag);
    }

    public boolean hasFeatures() {
        return hasFeatures;
    }

    public boolean isPointsLimitReached() {
        return pointsLimitReached;
    }

    public int getKey() {
        return key;
    }

    public int getPotentialPoints() {
        return potentialPoints;
    }

    @NonNull
    public String getPointsEarningFlag() {
        return pointsEarningFlag;
    }

    @NonNull
    public String getName() {
        return name;
    }
}
