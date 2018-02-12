package com.vitalityactive.va.persistence.models.wellnessdevices;

import com.vitalityactive.va.persistence.Model;
import com.vitalityactive.va.wellnessdevices.pointsmonitor.service.EventType;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class WellnessDevicesPotentialPoints extends RealmObject implements Model {
    private int potentialPointsValue;
    private String typeCode;
    @PrimaryKey
    private int typeKey;
    private String typeName;
    private RealmList<WellnessDevicesPointsEntryDetails> pointsEntryDetails;

    public WellnessDevicesPotentialPoints(){}

    public WellnessDevicesPotentialPoints(EventType eventType){
        this.potentialPointsValue = eventType.potentialPointsValue;
        this.typeCode = eventType.typeCode;
        this.typeKey = eventType.typeKey;
        this.typeName = eventType.typeName;
        this.pointsEntryDetails = new RealmList<>();
        if(eventType.pointsEntryDetails != null) {
            for (EventType.PointsEntryDetails pointsEntryDetails : eventType.pointsEntryDetails) {
                this.pointsEntryDetails.add(new WellnessDevicesPointsEntryDetails(pointsEntryDetails));
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

    public RealmList<WellnessDevicesPointsEntryDetails> getPointsEntryDetails() {
        return pointsEntryDetails;
    }
}
