package com.vitalityactive.va.persistence.models.wellnessdevices;

import com.vitalityactive.va.persistence.Model;
import com.vitalityactive.va.wellnessdevices.pointsmonitor.service.EventType;

import io.realm.RealmList;
import io.realm.RealmObject;

public class WellnessDevicesPointsEntryDetails extends RealmObject implements Model {
    private RealmList<WellnessDevicesPointsConditions> conditions;
    private int potentialPoints;

    public WellnessDevicesPointsEntryDetails() {
    }

    public WellnessDevicesPointsEntryDetails(EventType.PointsEntryDetails src) {
        if(src != null) {
            this.conditions = new RealmList<>();
            for(EventType.Conditions conditions : src.conditions){
                this.conditions.add(new WellnessDevicesPointsConditions(conditions));
            }
            this.potentialPoints = src.potentialPoints;
        }
    }

    public RealmList<WellnessDevicesPointsConditions> getConditions() {
        return conditions;
    }

    public int getPotentialPoints() {
        return potentialPoints;
    }
}
