package com.vitalityactive.va.activerewards.dto;

import android.os.Parcel;
import android.os.Parcelable;

import com.vitalityactive.va.networking.model.goalandprogress.GetGoalProgressAndDetailsResponse;
import com.vitalityactive.va.persistence.models.goalandprogress.GoalProgressAndDetailsOutbound;
import com.vitalityactive.va.persistence.models.goalandprogress.ObjectivePointsEntries;
import com.vitalityactive.va.persistence.models.goalandprogress.ObjectiveTrackersGoal;

import java.util.ArrayList;
import java.util.List;

public class ObjectiveTrackersGoalDto implements Parcelable {
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
    private List<GoalProgressAndDetailsOutboundDto> events;
    private String objectiveCode;
    private String objectiveKey;
    private String objectiveName;
    private List<ObjectivePointsEntriesDto> objectivePointsEntries;
    private String statusChangedOn;

    public ObjectiveTrackersGoalDto(ObjectiveTrackersGoal src){
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
        this.statusCode = src.getStatusCode();
        this.statusDate = src.getStatusDate();
        this.statusName = src.getStatusName();
        this.statusKey = src.getStatusKey();
        this.events = new ArrayList<>();
        if(src.getEvents() != null && !src.getEvents().isEmpty()) {
            for (GoalProgressAndDetailsOutbound goalProgressAndDetailsOutbound : src.getEvents()) {
                this.events.add(new GoalProgressAndDetailsOutboundDto(goalProgressAndDetailsOutbound));
            }
        }
        this.objectiveCode = src.getObjectiveCode();
        this.objectiveKey = src.getObjectiveKey();
        this.objectiveName = src.getObjectiveName();
        this.objectivePointsEntries = new ArrayList<>();
        if(src.getObjectivePointsEntries() != null && !src.getObjectivePointsEntries().isEmpty()) {
            for (ObjectivePointsEntries objectivePointsEntries : src.getObjectivePointsEntries()) {
                this.objectivePointsEntries.add(new ObjectivePointsEntriesDto(objectivePointsEntries));
            }
        }
        this.statusChangedOn = src.getStatusChangedOn();
    }

    public ObjectiveTrackersGoalDto(GetGoalProgressAndDetailsResponse.ObjectiveTrackersGoal src){
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
        this.statusCode = src.statusCode;
        this.statusDate = src.statusDate;
        this.statusName = src.statusName;
        this.statusKey = src.statusKey;
        this.events = new ArrayList<>();
        if(src.events != null && !src.events.isEmpty()) {
            for (com.vitalityactive.va.networking.model.goalandprogress.GoalProgressAndDetailsOutbound goalProgressAndDetailsOutbound : src.events) {
                this.events.add(new GoalProgressAndDetailsOutboundDto(goalProgressAndDetailsOutbound));
            }
        }
        this.objectiveCode = src.objectiveCode;
        this.objectiveKey = src.objectiveKey;
        this.objectiveName = src.objectiveName;
        this.objectivePointsEntries = new ArrayList<>();
        for(com.vitalityactive.va.networking.model.goalandprogress.ObjectivePointsEntries objectivePointsEntries : src.objectivePointsEntries){
            this.objectivePointsEntries.add(new ObjectivePointsEntriesDto(objectivePointsEntries));
        }
        this.statusChangedOn = src.statusChangedOn;
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

    public List<GoalProgressAndDetailsOutboundDto> getEvents() {
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

    public List<ObjectivePointsEntriesDto> getObjectivePointsEntries() {
        return objectivePointsEntries;
    }

    public String getStatusChangedOn() {
        return statusChangedOn;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.effectiveFrom);
        dest.writeString(this.effectiveTo);
        dest.writeString(this.monitorUntil);
        dest.writeInt(this.percentageCompleted);
        dest.writeInt(this.pointsAchieved);
        dest.writeInt(this.pointsTarget);
        dest.writeInt(this.eventCountAchieved);
        dest.writeInt(this.eventCountTarget);
        dest.writeByte(this.eventOutcomeAchieved ? (byte) 1 : (byte) 0);
        dest.writeInt(this.eventOutcomeTarget);
        dest.writeString(this.statusCode);
        dest.writeString(this.statusDate);
        dest.writeString(this.statusName);
        dest.writeInt(this.statusKey);
        dest.writeTypedList(this.events);
        dest.writeString(this.objectiveCode);
        dest.writeString(this.objectiveKey);
        dest.writeString(this.objectiveName);
        dest.writeTypedList(this.objectivePointsEntries);
        dest.writeString(this.statusChangedOn);
    }

    protected ObjectiveTrackersGoalDto(Parcel in) {
        this.effectiveFrom = in.readString();
        this.effectiveTo = in.readString();
        this.monitorUntil = in.readString();
        this.percentageCompleted = in.readInt();
        this.pointsAchieved = in.readInt();
        this.pointsTarget = in.readInt();
        this.eventCountAchieved = in.readInt();
        this.eventCountTarget = in.readInt();
        this.eventOutcomeAchieved = in.readByte() != 0;
        this.eventOutcomeTarget = in.readInt();
        this.statusCode = in.readString();
        this.statusDate = in.readString();
        this.statusName = in.readString();
        this.statusKey = in.readInt();
        this.events = in.createTypedArrayList(GoalProgressAndDetailsOutboundDto.CREATOR);
        this.objectiveCode = in.readString();
        this.objectiveKey = in.readString();
        this.objectiveName = in.readString();
        this.objectivePointsEntries = in.createTypedArrayList(ObjectivePointsEntriesDto.CREATOR);
        this.statusChangedOn = in.readString();
    }

    public static final Parcelable.Creator<ObjectiveTrackersGoalDto> CREATOR = new Parcelable.Creator<ObjectiveTrackersGoalDto>() {
        @Override
        public ObjectiveTrackersGoalDto createFromParcel(Parcel source) {
            return new ObjectiveTrackersGoalDto(source);
        }

        @Override
        public ObjectiveTrackersGoalDto[] newArray(int size) {
            return new ObjectiveTrackersGoalDto[size];
        }
    };
}
