package com.vitalityactive.va.networking.model.ar;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ActivateServiceResponse {
    @SerializedName("goalTracker")
    public GoalTracker goalTracker;

    public static class GoalTracker {
        @SerializedName("objectiveTrackers")
        public List<ObjectiveTrackersActive> objectiveTrackers;
        @SerializedName("completedObjectives")
        public int completedObjectives;
        @SerializedName("goalTypeCode")
        public String goalTypeCode;
        @SerializedName("goalTypeKey")
        public int goalTypeKey;
        @SerializedName("goalTypeName")
        public String goalTypeName;
        @SerializedName("monitorUntil")
        public String monitorUntil;
        @SerializedName("percentageCompleted")
        public int percentageCompleted;
        @SerializedName("statusDate")
        public String statusDate;
        @SerializedName("statusTypeCode")
        public String statusTypeCode;
        @SerializedName("statusTypeKey")
        public int statusTypeKey;
        @SerializedName("statusTypeName")
        public String statusTypeName;
        @SerializedName("totalObjectives")
        public int totalObjectives;
        @SerializedName("validFrom")
        public String validFrom;
        @SerializedName("validTo")
        public String validTo;
    }

    public static class ObjectiveTrackersActive {
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
        @SerializedName("objectiveTypeCode")
        public String objectiveTypeCode;
        @SerializedName("objectiveTypeKey")
        public int objectiveTypeKey;
        @SerializedName("objectiveTypeName")
        public String objectiveTypeName;
        @SerializedName("statusCode")
        public String statusCode;
        @SerializedName("statusDate")
        public String statusDate;
        @SerializedName("statusName")
        public String statusName;
        @SerializedName("statusKey")
        public int statusKey;
    }
}
