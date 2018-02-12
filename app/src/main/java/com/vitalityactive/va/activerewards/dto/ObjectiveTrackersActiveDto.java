package com.vitalityactive.va.activerewards.dto;

import com.vitalityactive.va.networking.model.ar.ActivateServiceResponse;
import com.vitalityactive.va.persistence.models.ar.ObjectiveTrackersActive;

public class ObjectiveTrackersActiveDto {
    private String effectiveFrom;
    private String effectiveTo;
    private String monitorUntil;
    private int percentageCompleted;
    private int pointsAchieved;
    private int pointsTarget;
    private int eventCountAchieved;
    private int eventCountTarget;
    private boolean eventOutcomeAchieved;
    private int eventOutcomeTarget;
    private String objectiveTypeCode;
    private int objectiveTypeKey;
    private String objectiveTypeName;
    private String statusCode;
    private String statusDate;
    private String statusName;
    private int statusKey;

    public ObjectiveTrackersActiveDto(ObjectiveTrackersActive src) {
        this.effectiveFrom = src.getEffectiveFrom();
        this.effectiveTo = src.getEffectiveTo();
        this.monitorUntil = src.getMonitorUntil();
        this.percentageCompleted = src.getPercentageCompleted();
        this.pointsAchieved = src.getPointsAchieved();
        this.pointsTarget = src.getPointsTarget();
        this.eventCountAchieved = src.getEventCountAchieved();
        this.eventCountTarget = src.getEventCountTarget();
        this.eventOutcomeAchieved = src.isEventOutcomeAchieved();
        this.eventOutcomeTarget = src.getEventOutcomeTarget();
        this.objectiveTypeCode = src.getObjectiveTypeCode();
        this.objectiveTypeKey = src.getObjectiveTypeKey();
        this.objectiveTypeName = src.getObjectiveTypeName();
        this.statusCode = src.getStatusCode();
        this.statusDate = src.getStatusDate();
        this.statusName = src.getStatusName();
        this.statusKey = src.getStatusKey();
    }

    public ObjectiveTrackersActiveDto(ActivateServiceResponse.ObjectiveTrackersActive src) {
        this.effectiveFrom = src.effectiveFrom;
        this.effectiveTo = src.effectiveTo;
        this.monitorUntil = src.monitorUntil;
        this.percentageCompleted = src.percentageCompleted;
        this.pointsAchieved = src.pointsAchieved;
        this.pointsTarget = src.pointsTarget;
        this.eventCountAchieved = src.eventCountAchieved;
        this.eventCountTarget = src.eventCountTarget;
        this.eventOutcomeAchieved = src.eventOutcomeAchieved;
        this.eventOutcomeTarget = src.eventOutcomeTarget;
        this.objectiveTypeCode = src.objectiveTypeCode;
        this.objectiveTypeKey = src.objectiveTypeKey;
        this.objectiveTypeName = src.objectiveTypeName;
        this.statusCode = src.statusCode;
        this.statusDate = src.statusDate;
        this.statusName = src.statusName;
        this.statusKey = src.statusKey;
    }

    public String getEffectiveFrom() {
        return effectiveFrom;
    }

    public String getEffectiveTo() {
        return effectiveTo;
    }

    public String getMonitorUntil() {
        return monitorUntil;
    }

    public int getPercentageCompleted() {
        return percentageCompleted;
    }

    public int getPointsAchieved() {
        return pointsAchieved;
    }

    public int getPointsTarget() {
        return pointsTarget;
    }

    public int getEventCountAchieved() {
        return eventCountAchieved;
    }

    public int getEventCountTarget() {
        return eventCountTarget;
    }

    public boolean isEventOutcomeAchieved() {
        return eventOutcomeAchieved;
    }

    public int getEventOutcomeTarget() {
        return eventOutcomeTarget;
    }

    public String getObjectiveTypeCode() {
        return objectiveTypeCode;
    }

    public int getObjectiveTypeKey() {
        return objectiveTypeKey;
    }

    public String getObjectiveTypeName() {
        return objectiveTypeName;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public String getStatusDate() {
        return statusDate;
    }

    public String getStatusName() {
        return statusName;
    }

    public int getStatusKey() {
        return statusKey;
    }
}
