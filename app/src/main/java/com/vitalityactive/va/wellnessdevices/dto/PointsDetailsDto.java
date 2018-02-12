package com.vitalityactive.va.wellnessdevices.dto;

import com.vitalityactive.va.persistence.DataStore;
import com.vitalityactive.va.persistence.models.wellnessdevices.WellnessDevicesPointsConditions;
import com.vitalityactive.va.persistence.models.wellnessdevices.WellnessDevicesPointsEntryDetails;
import com.vitalityactive.va.vitalitystatus.repository.ConditionsModel;
import com.vitalityactive.va.vitalitystatus.repository.PotentialPointsModel;
import com.vitalityactive.va.wellnessdevices.pointsmonitor.service.EventType;

import java.util.ArrayList;
import java.util.List;

public class PointsDetailsDto {
    private List<PointsConditionsDto> conditions;
    private int potentialPoints;

    public PointsDetailsDto(EventType.PointsEntryDetails src) {
        if(src != null) {
            this.conditions = new ArrayList<>();
            for(EventType.Conditions conditions : src.conditions){
                this.conditions.add(new PointsConditionsDto(conditions));
            }
            this.potentialPoints = src.potentialPoints;
        }
    }

    public PointsDetailsDto(WellnessDevicesPointsEntryDetails src) {
        if(src != null) {
            this.conditions = new ArrayList<>();
            for(WellnessDevicesPointsConditions conditions : src.getConditions()){
                this.conditions.add(new PointsConditionsDto(conditions));
            }
            this.potentialPoints = src.getPotentialPoints();
        }
    }

    public PointsDetailsDto(PotentialPointsModel src) {
        if(src != null) {
            this.conditions = new ArrayList<>();
            for(ConditionsModel conditions : src.getConditions()){
                this.conditions.add(new PointsConditionsDto(conditions));
            }
            this.potentialPoints = src.getPotentialPoints();
        }
    }

    public List<PointsConditionsDto> getConditions() {
        return conditions;
    }

    public int getPotentialPoints() {
        return potentialPoints;
    }

    public static class Mapper implements DataStore.ModelMapper<WellnessDevicesPointsEntryDetails, PointsDetailsDto> {
        @Override
        public PointsDetailsDto mapModel(WellnessDevicesPointsEntryDetails details) {
            return new PointsDetailsDto(details);
        }
    }
}
