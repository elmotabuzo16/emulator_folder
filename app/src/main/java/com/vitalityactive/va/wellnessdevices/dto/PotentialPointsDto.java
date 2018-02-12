package com.vitalityactive.va.wellnessdevices.dto;

import com.vitalityactive.va.persistence.DataStore;
import com.vitalityactive.va.persistence.models.wellnessdevices.WellnessDevicesPointsEntryDetails;
import com.vitalityactive.va.persistence.models.wellnessdevices.WellnessDevicesPotentialPoints;
import com.vitalityactive.va.wellnessdevices.pointsmonitor.service.EventType;

import java.util.ArrayList;
import java.util.List;

public class PotentialPointsDto {
    private int potentialPointsValue;
    private String typeCode;
    private int typeKey;
    private String typeName;
    private List<PointsDetailsDto> pointsEntryDetails;

    public PotentialPointsDto(EventType eventType){
        this.potentialPointsValue = eventType.potentialPointsValue;
        this.typeCode = eventType.typeCode;
        this.typeKey = eventType.typeKey;
        this.typeName = eventType.typeName;
        this.pointsEntryDetails = new ArrayList<>();
        if(eventType.pointsEntryDetails != null) {
            for (EventType.PointsEntryDetails pointsEntryDetails : eventType.pointsEntryDetails) {
                this.pointsEntryDetails.add(new PointsDetailsDto(pointsEntryDetails));
            }
        }
    }

    public PotentialPointsDto(WellnessDevicesPotentialPoints eventType){
        this.potentialPointsValue = eventType.getPotentialPointsValue();
        this.typeCode = eventType.getTypeCode();
        this.typeKey = eventType.getTypeKey();
        this.typeName = eventType.getTypeName();
        this.pointsEntryDetails = new ArrayList<>();
        if(eventType.getPointsEntryDetails() != null) {
            for (WellnessDevicesPointsEntryDetails pointsEntryDetails : eventType.getPointsEntryDetails()) {
                this.pointsEntryDetails.add(new PointsDetailsDto(pointsEntryDetails));
            }
        }
    }

    public int getPotentialPointsValue() {
        return potentialPointsValue;
    }

    public String getTypeCode() {
        return typeCode;
    }

    public int getTypeKey() {
        return typeKey;
    }

    public String getTypeName() {
        return typeName;
    }

    public List<PointsDetailsDto> getPointsEntryDetails() {
        return pointsEntryDetails;
    }

    public static class Mapper implements DataStore.ModelMapper<WellnessDevicesPotentialPoints, PotentialPointsDto> {
        @Override
        public PotentialPointsDto mapModel(WellnessDevicesPotentialPoints potentialPoints) {
            return new PotentialPointsDto(potentialPoints);
        }
    }
}
