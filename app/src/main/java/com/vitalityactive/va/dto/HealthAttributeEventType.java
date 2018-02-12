package com.vitalityactive.va.dto;

import android.support.annotation.NonNull;

import com.vitalityactive.va.persistence.Model;
import com.vitalityactive.va.persistence.models.vhc.HealthAttribute;
import com.vitalityactive.va.vhc.service.HealthAttributeResponse;

import io.realm.RealmList;
import io.realm.RealmObject;

public class HealthAttributeEventType extends RealmObject implements Model {

    private int reasonKey;
    private String reasonName;
    private String typeCode;
    private String typeName;

    private int totalPotentialPoints;
    private int totalEarnedPoints;

    private int categoryKey;
    private String categoryCode;
    private String categoryName;

    private int typeKey;

    private RealmList<HealthAttribute> healthAttributes;
    private RealmList<HealthAttributeEvent> events;
    private int source;

    public HealthAttributeEventType() {
    }

    public HealthAttributeEventType(HealthAttributeResponse.EventType eventType) {
        typeKey = eventType.typeKey;
        typeCode = eventType.typeCode;
        typeName = eventType.typeName;
        totalPotentialPoints = eventType.totalPotentialPoints;
        totalEarnedPoints = eventType.totalEarnedPoints;
        categoryKey = eventType.categoryKey;
        categoryCode = eventType.categoryCode;
        categoryName = eventType.categoryName;
        reasonName = eventType.reasonName;
        reasonKey = eventType.reasonKey;

        events = new RealmList<>();
        if (eventType.events != null) {
            for (HealthAttributeResponse.Event event : eventType.events) {
                events.add(new HealthAttributeEvent(event, eventType.typeKey));
            }
        }

        healthAttributes = new RealmList<>();
        if (eventType.healthAttributes != null) {
            for (HealthAttributeResponse.HealthAttribute healthAttribute : eventType.healthAttributes) {
                healthAttributes.add(new HealthAttribute(healthAttribute));
            }
        }
    }

    public RealmList<HealthAttribute> getHealthAttributes() {
        return healthAttributes;
    }

    public RealmList<HealthAttributeEvent> getEvents() {
        return events;
    }

    public int getTotalEarnedPoints() {
        return totalEarnedPoints;
    }

    public int getTotalPotentialPoints() {
        return totalPotentialPoints;
    }

    public int getTypeKey() {
        return typeKey;
    }

    public String getTypeName() {
        return typeName;
    }

    public int getSource() {
        return source;
    }

    @NonNull
    public String getReasonName() {
        return reasonName == null ? "" : reasonName;
    }

    public int getReasonKey() {
        return reasonKey;
    }
}
