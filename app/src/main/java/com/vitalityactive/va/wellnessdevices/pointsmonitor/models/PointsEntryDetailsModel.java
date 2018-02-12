package com.vitalityactive.va.wellnessdevices.pointsmonitor.models;

import com.vitalityactive.va.wellnessdevices.dto.PointsDetailsDto;

public class PointsEntryDetailsModel {
    private final PointsDetailsDto pointsEntryDetails;
    private final boolean isLastItemInList;

    public PointsEntryDetailsModel(PointsDetailsDto pointsEntryDetails,
                            boolean isLastItemInList){
        this.pointsEntryDetails = pointsEntryDetails;
        this.isLastItemInList = isLastItemInList;
    }

    public PointsDetailsDto getPointsEntryDetails() {
        return pointsEntryDetails;
    }

    public boolean isLastItemInList() {
        return isLastItemInList;
    }
}
