package com.vitalityactive.va.dto;

import com.vitalityactive.va.persistence.Model;
import com.vitalityactive.va.persistence.models.vhc.HealthAttributeFeedback;
import com.vitalityactive.va.vhc.service.HealthAttributeResponse;

import io.realm.RealmList;
import io.realm.RealmObject;

public class HealthAttributeReading extends RealmObject implements Model {
    private String healthAttributeTypeCode;
    private int healthAttributeTypeKey;
    private String healthAttributeTypeName;

    private String unitOfMeasure;
    private String measuredOn;
    private String value;
    private String eventId;

    private RealmList<HealthAttributeFeedback> healthAttributeFeedbacks;

    public HealthAttributeReading() {

    }

    public HealthAttributeReading(HealthAttributeResponse.HealthAttributeReading healthAttributeReading, String eventId) {
        healthAttributeTypeCode = healthAttributeReading.healthAttributeTypeCode;
        unitOfMeasure = healthAttributeReading.unitOfMeasure;
        measuredOn = healthAttributeReading.measuredOn;
        healthAttributeTypeKey = healthAttributeReading.healthAttributeTypeKey;
        healthAttributeTypeName = healthAttributeReading.healthAttributeTypeName;
        value = String.valueOf(healthAttributeReading.value);
        this.eventId = eventId;

        healthAttributeFeedbacks = new RealmList<>();
        if (healthAttributeReading.healthAttributeFeedbacks != null) {
            for (HealthAttributeResponse.HealthAttributeFeedback healthAttributeFeedback : healthAttributeReading.healthAttributeFeedbacks) {
                healthAttributeFeedbacks.add(new HealthAttributeFeedback(healthAttributeFeedback));
            }
        }
    }

    public RealmList<HealthAttributeFeedback> getFeedbacks() {
        return healthAttributeFeedbacks;
    }

    public String getMeasuredOn() {
        return measuredOn;
    }

    public String getValue() {
        return value;
    }

    public String getUnitOfMeasureTypeKey() {
        return unitOfMeasure;
    }

    public String getEventId() {
        return eventId;
    }

    public int getHealthAttributeTypeKey() {
        return healthAttributeTypeKey;
    }
}
