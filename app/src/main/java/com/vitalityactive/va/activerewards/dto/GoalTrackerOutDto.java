package com.vitalityactive.va.activerewards.dto;

import android.os.Parcel;
import android.os.Parcelable;

import com.vitalityactive.va.networking.model.goalandprogress.GetGoalProgressAndDetailsResponse;
import com.vitalityactive.va.persistence.models.goalandprogress.GoalTrackerOut;
import com.vitalityactive.va.persistence.models.goalandprogress.ObjectiveTrackersGoal;

import java.util.ArrayList;
import java.util.List;

public class GoalTrackerOutDto implements Parcelable {
    private List<ObjectiveTrackersGoalDto> objectiveTrackers;
    private int completedObjectives;
    private String effectiveFrom;
    private String effectiveTo;
    private String goalCode;
    private int goalKey;
    private String goalName;
    private String goalTrackerStatusCode;
    private int goalTrackerStatusKey;
    private String goalTrackerStatusName;
    private String monitorUntil;
    private long partyId;
    private int percentageCompleted;
    private String statusChangedOn;
    private int totalObjectives;

    public GoalTrackerOutDto(GetGoalProgressAndDetailsResponse.GoalTrackerOut src){
        this.objectiveTrackers = new ArrayList<>();
        if(src.objectiveTrackers != null && !src.objectiveTrackers.isEmpty()) {
            for (GetGoalProgressAndDetailsResponse.ObjectiveTrackersGoal objectiveTrackers : src.objectiveTrackers) {
                this.objectiveTrackers.add(new ObjectiveTrackersGoalDto(objectiveTrackers));
            }
        }
        this.completedObjectives = src.completedObjectives;
        this.effectiveFrom = src.effectiveFrom;
        this.effectiveTo = src.effectiveTo;
        this.goalCode = src.goalCode;
        this.goalKey = src.goalKey;
        this.goalName = src.goalName;
        this.goalTrackerStatusCode = src.goalTrackerStatusCode;
        this.goalTrackerStatusKey = src.goalTrackerStatusKey;
        this.goalTrackerStatusName = src.goalTrackerStatusName;
        this.monitorUntil = src.monitorUntil;
        this.partyId = src.partyId;
        this.percentageCompleted = src.percentageCompleted;
        this.statusChangedOn = src.statusChangedOn;
        this.totalObjectives = src.totalObjectives;
    }

    public GoalTrackerOutDto(GoalTrackerOut src){
        this.objectiveTrackers = new ArrayList<>();
        if(src.getObjectiveTrackers() != null && !src.getObjectiveTrackers().isEmpty()) {
            for (ObjectiveTrackersGoal objectiveTrackers : src.getObjectiveTrackers()) {
                this.objectiveTrackers.add(new ObjectiveTrackersGoalDto(objectiveTrackers));
            }
        }
        this.completedObjectives = src.getCompletedObjectives();
        this.effectiveFrom = src.getEffectiveFrom();
        this.effectiveTo = src.getEffectiveTo();
        this.goalCode = src.getGoalCode();
        this.goalKey = src.getGoalKey();
        this.goalName = src.getGoalName();
        this.goalTrackerStatusCode = src.getGoalTrackerStatusCode();
        this.goalTrackerStatusKey = src.getGoalTrackerStatusKey();
        this.goalTrackerStatusName = src.getGoalTrackerStatusName();
        this.monitorUntil = src.getMonitorUntil();
        this.partyId = src.getPartyId();
        this.percentageCompleted = src.getPercentageCompleted();
        this.statusChangedOn = src.getStatusChangedOn();
        this.totalObjectives = src.getTotalObjectives();
    }

    public List<ObjectiveTrackersGoalDto> getObjectiveTrackers() {
        return objectiveTrackers;
    }

    public int getCompletedObjectives() {
        return completedObjectives;
    }

    public String getEffectiveFrom() {
        return effectiveFrom;
    }

    public String getEffectiveTo() {
        return effectiveTo;
    }

    public String getGoalCode() {
        return goalCode;
    }

    public int getGoalKey() {
        return goalKey;
    }

    public String getGoalName() {
        return goalName;
    }

    public String getGoalTrackerStatusCode() {
        return goalTrackerStatusCode;
    }

    public int getGoalTrackerStatusKey() {
        return goalTrackerStatusKey;
    }

    public String getGoalTrackerStatusName() {
        return goalTrackerStatusName;
    }

    public String getMonitorUntil() {
        return monitorUntil;
    }

    public long getPartyId() {
        return partyId;
    }

    public int getPercentageCompleted() {
        return percentageCompleted;
    }

    public String getStatusChangedOn() {
        return statusChangedOn;
    }

    public int getTotalObjectives() {
        return totalObjectives;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.objectiveTrackers);
        dest.writeInt(this.completedObjectives);
        dest.writeString(this.effectiveFrom);
        dest.writeString(this.effectiveTo);
        dest.writeString(this.goalCode);
        dest.writeInt(this.goalKey);
        dest.writeString(this.goalName);
        dest.writeString(this.goalTrackerStatusCode);
        dest.writeInt(this.goalTrackerStatusKey);
        dest.writeString(this.goalTrackerStatusName);
        dest.writeString(this.monitorUntil);
        dest.writeLong(this.partyId);
        dest.writeInt(this.percentageCompleted);
        dest.writeString(this.statusChangedOn);
        dest.writeInt(this.totalObjectives);
    }

    protected GoalTrackerOutDto(Parcel in) {
        this.objectiveTrackers = in.createTypedArrayList(ObjectiveTrackersGoalDto.CREATOR);
        this.completedObjectives = in.readInt();
        this.effectiveFrom = in.readString();
        this.effectiveTo = in.readString();
        this.goalCode = in.readString();
        this.goalKey = in.readInt();
        this.goalName = in.readString();
        this.goalTrackerStatusCode = in.readString();
        this.goalTrackerStatusKey = in.readInt();
        this.goalTrackerStatusName = in.readString();
        this.monitorUntil = in.readString();
        this.partyId = in.readLong();
        this.percentageCompleted = in.readInt();
        this.statusChangedOn = in.readString();
        this.totalObjectives = in.readInt();
    }

    public static final Parcelable.Creator<GoalTrackerOutDto> CREATOR = new Parcelable.Creator<GoalTrackerOutDto>() {
        @Override
        public GoalTrackerOutDto createFromParcel(Parcel source) {
            return new GoalTrackerOutDto(source);
        }

        @Override
        public GoalTrackerOutDto[] newArray(int size) {
            return new GoalTrackerOutDto[size];
        }
    };
}
