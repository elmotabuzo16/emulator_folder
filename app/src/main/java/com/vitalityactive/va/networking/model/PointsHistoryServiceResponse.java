package com.vitalityactive.va.networking.model;

import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PointsHistoryServiceResponse {
    public List<PointsAccount> pointsAccounts;

    public static class PointsAccount {
        @NonNull
        public String effectiveFrom = "";
        @NonNull
        public String effectiveTo = "";
        public int carryOverPoints;
        public int pointsTotal;
        public List<PointsEntry> pointsEntries;
    }

    public static class PointsEntry {
        @NonNull
        public String eventId = "";
        @NonNull
        public String typeKey = "";
        public List<Reason> reasons;
        public List<Metadata> metadatas;
        @NonNull
        public String typeCode = "";
        public String typeName = "";
        public int earnedValue;
        public int prelimitValue;
        @NonNull
        public String id = "";
        public List<ProgressTrackerEvent> progressTrackerEvents;
        public List<GoalPointsTracker> goalPointsTrackers;
        @NonNull
        public String effectiveDate = "";
        @SerializedName("categoryKey")
        public Integer category;
        @SerializedName("categoryName")
        public String categoryName = "";
    }

    public static class Reason {
        @NonNull
        @SerializedName("reasonName")
        public String reason = "";
        @NonNull
        public String categoryKey = "";
        @NonNull
        public String categoryCode = "";
        @SerializedName("categoryName")
        public String categoryName = "";
    }

    public static class Metadata {
        @NonNull
        public String typeKey = "";
        @NonNull
        public String unitOfMeasure = "";
        @NonNull
        public String value = "";
        @NonNull
        public String typeCode = "";
        @NonNull
        public String typeName = "";
    }

    public static class ProgressTrackerEvent {
        @NonNull
        public String goalTypeCode = "";
        @NonNull
        public String goalTypeKey = "";
        @NonNull
        public String goalTypeName = "";
        @NonNull
        public String points = "";
    }

    public static class GoalPointsTracker {
        public int pointsContributing;
    }
}
