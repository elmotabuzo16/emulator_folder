package com.vitalityactive.va.activerewards.dto;

import com.vitalityactive.va.networking.model.ar.ActivateServiceResponse;
import com.vitalityactive.va.persistence.models.ar.GoalTrackerActive;
import com.vitalityactive.va.persistence.models.ar.ObjectiveTrackersActive;

import java.util.ArrayList;
import java.util.List;

public class GoalTrackerActiveDto {
    private List<ObjectiveTrackersActiveDto> objectiveTrackers;
    private int completedObjectives;
    private String goalTypeCode;
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

    public GoalTrackerActiveDto(ActivateServiceResponse.GoalTracker src){
        this.objectiveTrackers = new ArrayList<>();
        if(src.objectiveTrackers != null && !src.objectiveTrackers.isEmpty()) {
            for (ActivateServiceResponse.ObjectiveTrackersActive objectiveTrackers : src.objectiveTrackers) {
                this.objectiveTrackers.add(new ObjectiveTrackersActiveDto(objectiveTrackers));
            }
        }
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

    public GoalTrackerActiveDto(GoalTrackerActive src){
        this.objectiveTrackers = new ArrayList<>();
        if(src.getObjectiveTrackers() != null && !src.getObjectiveTrackers().isEmpty()) {
            for (ObjectiveTrackersActive objectiveTrackers : src.getObjectiveTrackers()) {
                this.objectiveTrackers.add(new ObjectiveTrackersActiveDto(objectiveTrackers));
            }
        }
        this.completedObjectives = src.getCompletedObjectives();
        this.goalTypeCode = src.getGoalTypeCode();
        this.goalTypeKey = src.getGoalTypeKey();
        this.goalTypeName = src.getGoalTypeName();
        this.monitorUntil = src.getMonitorUntil();
        this.percentageCompleted = src.getPercentageCompleted();
        this.statusDate = src.getStatusDate();
        this.statusTypeCode = src.getStatusTypeCode();
        this.statusTypeKey = src.getStatusTypeKey();
        this.statusTypeName = src.getStatusTypeName();
        this.totalObjectives = src.getTotalObjectives();
        this.validFrom = src.getValidFrom();
        this.validTo = src.getValidTo();
    }

    public List<ObjectiveTrackersActiveDto> getObjectiveTrackers() {
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
