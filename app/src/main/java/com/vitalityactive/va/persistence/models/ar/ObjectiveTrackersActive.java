package com.vitalityactive.va.persistence.models.ar;

import com.vitalityactive.va.networking.model.ar.ActivateServiceResponse;
import com.vitalityactive.va.persistence.Model;

import io.realm.RealmObject;

/**
 * Model for ActivateActiveRewards Response
 */
public class ObjectiveTrackersActive extends RealmObject implements Model {
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

    public ObjectiveTrackersActive() {}

    public ObjectiveTrackersActive(ActivateServiceResponse.ObjectiveTrackersActive objectiveTrackers) {
        this.effectiveFrom = objectiveTrackers.effectiveFrom;
        this.effectiveTo = objectiveTrackers.effectiveTo;
        this.monitorUntil = objectiveTrackers.monitorUntil;
        this.percentageCompleted = objectiveTrackers.percentageCompleted;
        this.pointsAchieved = objectiveTrackers.pointsAchieved;
        this.pointsTarget = objectiveTrackers.pointsTarget;
        this.eventCountAchieved = objectiveTrackers.eventCountAchieved;
        this.eventCountTarget = objectiveTrackers.eventCountTarget;
        this.eventOutcomeAchieved = objectiveTrackers.eventOutcomeAchieved;
        this.eventOutcomeTarget = objectiveTrackers.eventOutcomeTarget;
        this.objectiveTypeCode = objectiveTrackers.objectiveTypeCode;
        this.objectiveTypeKey = objectiveTrackers.objectiveTypeKey;
        this.objectiveTypeName = objectiveTrackers.objectiveTypeName;
        this.statusCode = objectiveTrackers.statusCode;
        this.statusDate = objectiveTrackers.statusDate;
        this.statusName = objectiveTrackers.statusName;
        this.statusKey = objectiveTrackers.statusKey;
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
