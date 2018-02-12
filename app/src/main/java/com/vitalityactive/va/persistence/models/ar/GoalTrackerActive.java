package com.vitalityactive.va.persistence.models.ar;

import com.vitalityactive.va.networking.model.ar.ActivateServiceResponse;
import com.vitalityactive.va.persistence.Model;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class GoalTrackerActive extends RealmObject implements Model {
    private RealmList<ObjectiveTrackersActive> objectiveTrackers;
    private int completedObjectives;
    private String goalTypeCode;
    @PrimaryKey
    private int goalTypeKey;
    private String goalTypeName;
    private String monitorUntil;
    private int percentageCompleted;
    private String statusDate;
    private String statusTypeCode;
    private int statusTypeKey;
    private String statusTypeName;
    private int totalObjectives;
    private String validFrom;
    private String validTo;

    public GoalTrackerActive() {}

    public GoalTrackerActive(ActivateServiceResponse.GoalTracker src){
        this.objectiveTrackers = new RealmList<>();
        // removed to not override proper values obtained with general GET request after activation
//        for(ActivateServiceResponse.ObjectiveTrackersActive objectiveTrackers : src.objectiveTrackers){
//            this.objectiveTrackers.add(new ObjectiveTrackersActive(objectiveTrackers));
//        }

        this.completedObjectives = src.completedObjectives;
        this.goalTypeCode = src.goalTypeCode;
        this.goalTypeKey = src.goalTypeKey;
        this.goalTypeName = src.goalTypeName;
        this.monitorUntil = src.monitorUntil;
        this.percentageCompleted = src.percentageCompleted;
        this.statusDate = src.statusDate;
        this.statusTypeCode = src.statusTypeCode;
        this.statusTypeKey = src.statusTypeKey;
        this.statusTypeName = src.statusTypeName;
        this.totalObjectives = src.totalObjectives;
        this.validFrom = src.validFrom;
        this.validTo = src.validTo;
    }

    public RealmList<ObjectiveTrackersActive> getObjectiveTrackers() {
        return objectiveTrackers;
    }

    public int getCompletedObjectives() {
        return completedObjectives;
    }

    public String getGoalTypeCode() {
        return goalTypeCode;
    }

    public int getGoalTypeKey() {
        return goalTypeKey;
    }

    public String getGoalTypeName() {
        return goalTypeName;
    }

    public String getMonitorUntil() {
        return monitorUntil;
    }

    public int getPercentageCompleted() {
        return percentageCompleted;
    }

    public String getStatusDate() {
        return statusDate;
    }

    public String getStatusTypeCode() {
        return statusTypeCode;
    }

    public int getStatusTypeKey() {
        return statusTypeKey;
    }

    public String getStatusTypeName() {
        return statusTypeName;
    }

    public int getTotalObjectives() {
        return totalObjectives;
    }

    public String getValidFrom() {
        return validFrom;
    }

    public String getValidTo() {
        return validTo;
    }
}