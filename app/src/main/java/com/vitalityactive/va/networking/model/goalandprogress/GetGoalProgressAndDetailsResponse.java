package com.vitalityactive.va.networking.model.goalandprogress;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetGoalProgressAndDetailsResponse {
    @SerializedName("getGoalProgressAndDetailsResponse")
    public GoalProgressAndDetailsResponse getGoalProgressAndDetailsResponse;
    @SerializedName("pointsEntry")
    public ObjectivePointsEntries pointsEntry;

    public static class GoalProgressAndDetailsResponse {
        @SerializedName("goalTrackerOuts")
        public List<GoalTrackerOut> goalTrackerOuts;
    }

    public static class GoalTrackerOut {
        @SerializedName("objectiveTrackers")
        public List<ObjectiveTrackersGoal> objectiveTrackers;
        @SerializedName("completedObjectives")
        public int completedObjectives;
        @SerializedName("effectiveFrom")
        public String effectiveFrom;
        @SerializedName("effectiveTo")
        public String effectiveTo;
        @SerializedName("goalCode")
        public String goalCode;
        @SerializedName("goalKey")
        public int goalKey;
        @SerializedName("goalName")
        public String goalName;
        @SerializedName("goalTrackerStatusCode")
        public String goalTrackerStatusCode;
        @SerializedName("goalTrackerStatusKey")
        public int goalTrackerStatusKey;
        @SerializedName("goalTrackerStatusName")
        public String goalTrackerStatusName;
        @SerializedName("monitorUntil")
        public String monitorUntil;
        @SerializedName("partyId")
        public long partyId;
        @SerializedName("percentageCompleted")
        public int percentageCompleted;
        @SerializedName("statusChangedOn")
        public String statusChangedOn;
        @SerializedName("totalObjectives")
        public int totalObjectives;
        @SerializedName("id")
        public int id;
    }

    public static class ObjectiveTrackersGoal {
        @SerializedName("effectiveFrom")
        public String effectiveFrom;
        @SerializedName("effectiveTo")
        public String effectiveTo;
        @SerializedName("monitorUntil")
        public String monitorUntil;
        @SerializedName("percentageCompleted")
        public int percentageCompleted;
        @SerializedName("pointsAchieved")
        public int pointsAchieved;
        @SerializedName("pointsTarget")
        public int pointsTarget;
        @SerializedName("eventCountAchieved")
        public int eventCountAchieved;
        @SerializedName("eventCountTarget")
        public int eventCountTarget;
        @SerializedName("eventOutcomeAchieved")
        public boolean eventOutcomeAchieved;
        @SerializedName("eventOutcomeTarget")
        public int eventOutcomeTarget;
        @SerializedName("statusCode")
        public String statusCode;
        @SerializedName("statusDate")
        public String statusDate;
        @SerializedName("statusName")
        public String statusName;
        @SerializedName("statusKey")
        public int statusKey;
        @SerializedName("events")
        public List<GoalProgressAndDetailsOutbound> events;
        @SerializedName("objectiveCode")
        public String objectiveCode;
        @SerializedName("objectiveKey")
        public String objectiveKey;
        @SerializedName("objectiveName")
        public String objectiveName;
        @SerializedName("objectivePointsEntries")
        public List<ObjectivePointsEntries> objectivePointsEntries;
        @SerializedName("statusChangedOn")
        public String statusChangedOn;
    }

}
