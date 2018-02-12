package com.vitalityactive.va.persistence.models.goalandprogress;

import com.vitalityactive.va.networking.model.goalandprogress.GetGoalProgressAndDetailsResponse;
import com.vitalityactive.va.persistence.Model;

import io.realm.RealmList;
import io.realm.RealmObject;

public class ObjectiveTrackersGoal extends RealmObject implements Model {
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
    private String statusCode;
    private String statusDate;
    private String statusName;
    private int statusKey;
    private RealmList<GoalProgressAndDetailsOutbound> events;
    private String objectiveCode;
    private String objectiveKey;
    private String objectiveName;
    private RealmList<ObjectivePointsEntries> objectivePointsEntries;
    private String statusChangedOn;

    public ObjectiveTrackersGoal() {}

    public ObjectiveTrackersGoal(GetGoalProgressAndDetailsResponse.ObjectiveTrackersGoal objectiveTrackers) {
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
        this.statusCode = objectiveTrackers.statusCode;
        this.statusDate = objectiveTrackers.statusDate;
        this.statusName = objectiveTrackers.statusName;
        this.statusKey = objectiveTrackers.statusKey;
        this.events = new RealmList<>();
        if(objectiveTrackers.events != null &&
                !objectiveTrackers.events.isEmpty()) {
            for (com.vitalityactive.va.networking.model.goalandprogress.GoalProgressAndDetailsOutbound item : objectiveTrackers.events) {
                this.events.add(new GoalProgressAndDetailsOutbound(item));
            }
        }
        this.objectiveCode = objectiveTrackers.objectiveCode;
        this.objectiveKey = objectiveTrackers.objectiveKey;
        this.objectiveName = objectiveTrackers.objectiveName;
        this.objectivePointsEntries = new RealmList<>();
        if(objectiveTrackers.objectivePointsEntries != null &&
                !objectiveTrackers.objectivePointsEntries.isEmpty()) {
            for (com.vitalityactive.va.networking.model.goalandprogress.ObjectivePointsEntries item : objectiveTrackers.objectivePointsEntries) {
                this.objectivePointsEntries.add(new ObjectivePointsEntries(item));
            }
        }
        this.statusChangedOn = objectiveTrackers.statusChangedOn;
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

    public RealmList<GoalProgressAndDetailsOutbound> getEvents() {
        return events;
    }

    public String getObjectiveCode() {
        return objectiveCode;
    }

    public String getObjectiveKey() {
        return objectiveKey;
    }

    public String getObjectiveName() {
        return objectiveName;
    }

    public RealmList<ObjectivePointsEntries> getObjectivePointsEntries() {
        return objectivePointsEntries;
    }

    public String getStatusChangedOn() {
        return statusChangedOn;
    }
}
