package com.vitalityactive.va.vitalitystatus.earningpoints;

import com.vitalityactive.va.wellnessdevices.dto.PointsConditionsDto;

import java.util.List;

class StatusPointsConditionItem {
    private final List<PointsConditionsDto> pointsConditionsDto;
    private int potentialPoints;

    StatusPointsConditionItem(int potentialPoints, List<PointsConditionsDto> pointsConditionsDto) {
        this.potentialPoints = potentialPoints;
        this.pointsConditionsDto = pointsConditionsDto;
    }

    public int getPotentialPoints() {
        return potentialPoints;
    }

    public List<PointsConditionsDto> getConditions() {
        return pointsConditionsDto;
    }
}
