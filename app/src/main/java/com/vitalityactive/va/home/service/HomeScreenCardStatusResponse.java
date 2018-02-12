package com.vitalityactive.va.home.service;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class HomeScreenCardStatusResponse {
    @SerializedName("daysLeftInMembershipPeriod")
    public DaysLeftInMembershipPeriod daysLeftInMembershipPeriod;

    public static class DaysLeftInMembershipPeriod {
        @SerializedName("daysRemaining")
        public int daysRemaining;
    }

    @SerializedName("currentStatus")
    public VitalityStatus vitalityStatus;

    public static class VitalityStatus {
        @SerializedName("highestVitalityStatusCode")
        public String highestVitalityStatusCode;
        @SerializedName("highestVitalityStatusKey")
        public int highestVitalityStatusKey;
        @SerializedName("highestVitalityStatusName")
        public String highestVitalityStatusName;
        @SerializedName("lowestVitalityStatusCode")
        public String lowestVitalityStatusCode;
        @SerializedName("lowestVitalityStatusKey")
        public int lowestVitalityStatusKey;
        @SerializedName("lowestVitalityStatusName")
        public String lowestVitalityStatusName;
        @SerializedName("nextVitalityStatusCode")
        public String nextVitalityStatusCode;
        @SerializedName("nextVitalityStatusKey")
        public int nextVitalityStatusKey;
        @SerializedName("nextVitalityStatusName")
        public String nextVitalityStatusName;
        @SerializedName("overallVitalityStatusCode")
        public String overallVitalityStatusCode;
        @SerializedName("overallVitalityStatusKey")
        public int overallVitalityStatusKey;
        @SerializedName("overallVitalityStatusName")
        public String overallVitalityStatusName;
        @SerializedName("pointsStatusCode")
        public String pointsStatusCode;
        @SerializedName("pointsStatusKey")
        public int pointsStatusKey;
        @SerializedName("pointsStatusName")
        public String pointsStatusName;
        @SerializedName("pointsToMaintainStatus")
        public int pointsToMaintainStatus;
        @SerializedName("totalPoints")
        public int totalPoints;
        @SerializedName("carryOverStatusKey")
        public int carryOverStatusKey;
        @SerializedName("carryOverStatusCode")
        public String carryOverStatusCode;
        @SerializedName("carryOverStatusName")
        public String carryOverStatusName;
    }

    @SerializedName("pointsToNextStatuses")
    public List<LevelStatus> availableStatuses;

    public static class LevelStatus {
        @SerializedName("pointsNeeded")
        public int pointsNeeded;
        @SerializedName("sortOrder")
        public int sortOrder;
        @SerializedName("statusCode")
        public String statusCode;
        @SerializedName("statusKey")
        public int statusKey;
        @SerializedName("statusName")
        public String statusName;
        @SerializedName("statusPoints")
        public int statusPoints;
    }

    @SerializedName("sections")
    public List<Section> sections;

    public static class Section {
        @SerializedName("typeKey")
        public String typeKey;
        @SerializedName("cards")
        public List<Card> cards;
    }

    public static class Card {
        @SerializedName("amountCompleted")
        public long amountCompleted;
        @SerializedName("typeKey")
        public String typeKey;
        @SerializedName("priority")
        public int priority;
        @SerializedName("cardMetadatas")
        public List<Metadata> cardMetadatas;
        @SerializedName("statusTypeKey")
        public Integer statusTypeKey;
        @SerializedName("total")
        public String total;
        @SerializedName("cardItems")
        public List<CardItem> cardItems;
        @SerializedName("validTo")
        public String validTo;
    }

    public static class Metadata {
        @SerializedName("typeKey")
        public Integer typeKey;
        @SerializedName("value")
        public String value;
    }

    public static class CardItem {
        @SerializedName("typeCode")
        public String typeCode;
        @SerializedName("typeKey")
        public Integer typeKey;
        @SerializedName("typeName")
        public String typeName;
        @SerializedName("cardItemMetadatas")
        public List<Metadata> cardItemMetadatas;
        @SerializedName("statusTypeCode")
        public String statusTypeCode;
        @SerializedName("statusTypeName")
        public String statusTypeName;
        @SerializedName("statusTypeKey")
        public Integer statusTypeKey;
        @SerializedName("validFrom")
        public String validFrom;
        @SerializedName("validTo")
        public String validTo;
    }
}
