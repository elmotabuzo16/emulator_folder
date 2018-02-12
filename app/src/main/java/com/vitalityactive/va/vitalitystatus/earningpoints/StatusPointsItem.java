package com.vitalityactive.va.vitalitystatus.earningpoints;

import com.vitalityactive.va.wellnessdevices.dto.PointsDetailsDto;

import java.util.ArrayList;
import java.util.List;

public class StatusPointsItem {
    private final int potentialPoints;
    private int pointsEntryType;
    private List<PointsDetailsDto> pointsDetailsDtos;

    public StatusPointsItem(int potentialPoints, Integer pointsEntryType, List<PointsDetailsDto> pointsDetailsDtos) {
        this.potentialPoints = potentialPoints;
        this.pointsEntryType = pointsEntryType;
        this.pointsDetailsDtos = pointsDetailsDtos;
    }

    public StatusPointsItem(Integer potentialPoints, int pointsEntryType) {
        this(potentialPoints, pointsEntryType, new ArrayList<PointsDetailsDto>());
    }

    public int getPotentialPoints() {
        return potentialPoints;
    }

    int getPointsEntryType() {
        return pointsEntryType;
    }

    List<PointsDetailsDto> getPointsDetailsDtos() {
        return pointsDetailsDtos;
    }
}
