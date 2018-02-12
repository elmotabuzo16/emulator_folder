package com.vitalityactive.va.vhc.service;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class HealthAttributeResponse {
    @SerializedName("warnings")
    public List<WarningData> warnings;

    public static class WarningData {
        @SerializedName("code")
        public String code;
        @SerializedName("name")
        public String name;
        @SerializedName("context")
        public String context;
        @SerializedName("feedbackKey")
        public int key;
    }

    @SerializedName("eventType")
    public List<EventType> eventTypes;

    public static class EventType {
        @SerializedName("typeKey")
        public int typeKey;
        @SerializedName("typeCode")
        public String typeCode;
        @SerializedName("typeName")
        public String typeName;
        @SerializedName("totalPotentialPoints")
        public int totalPotentialPoints;
        @SerializedName("totalEarnedPoints")
        public int totalEarnedPoints;
        @SerializedName("categoryKey")
        public int categoryKey;
        @SerializedName("categoryCode")
        public String categoryCode;
        @SerializedName("categoryName")
        public String categoryName;
        @SerializedName("healthAttribute")
        public List<HealthAttribute> healthAttributes;
        @SerializedName("event")
        public List<Event> events;
        @SerializedName("reasonName")
        public String reasonName;
        @SerializedName("reasonKey")
        public int reasonKey;
    }

    public static class HealthAttribute {
        @SerializedName("typekey")
        public int typeKey;
        @SerializedName("typeName")
        public String typeName;
        @SerializedName("typeCode")
        public String typeCode;
        @SerializedName("healthAttributeTypeValidValueses")
        public List<ValidValue> validValues;
    }

    public static class ValidValue {
        @SerializedName("minValue")
        public Float minValue;
        @SerializedName("maxValue")
        public Float maxValue;
        @SerializedName("typeTypeKey")
        public int typeTypeKey;
        @SerializedName("unitofMeasure")
        public String unitOfMeasureId;
        @SerializedName("typeTypeCode")
        public String typeTypeCode;
        @SerializedName("upperLimit")
        public float upperLimit;
        @SerializedName("lowerLimit")
        public float lowerLimit;
        @SerializedName("typeTypeName")
        public String typeTypeName;
        @SerializedName("validValuesList")
        public List<String> validValuesList;
    }

    public static class Event {
        @SerializedName("eventDateTime")
        public String eventDateTime;
        @SerializedName("eventId")
        public int eventId;
        @SerializedName("eventMetaDataType")
        public List<EventMetaData> eventMetaData;
        @SerializedName("healthAttributeReadings")
        public List<HealthAttributeReading> healthAttributeReadings;
        @SerializedName("pointsEntries")
        public List<PointsEntry> pointsEntries;
        @SerializedName("eventSource")
        public EventSource eventSource;
    }

    public static class EventMetaData {
        @SerializedName("note")
        public String note;
        @SerializedName("code")
        public String code;
        @SerializedName("unitOfMeasure")
        public String unitOfMeasure;
        @SerializedName("name")
        public String name;
        @SerializedName("value")
        public String value;
        @SerializedName("feedbackKey")
        public int key;
    }

    public static class HealthAttributeReading {
        @SerializedName("healthAttributeTypeCode")
        public String healthAttributeTypeCode;
        @SerializedName("unitOfMeasure")
        public String unitOfMeasure;
        @SerializedName("measuredOn")
        public String measuredOn;
        @SerializedName("healthAttributeTypeKey")
        public int healthAttributeTypeKey;
        @SerializedName("healthAttributeTypeName")
        public String healthAttributeTypeName;
        @SerializedName("healthAttributeFeedbacks")
        public List<HealthAttributeFeedback> healthAttributeFeedbacks;
        @SerializedName("value")
        public String value;
    }

    public static class HealthAttributeFeedback {
        @SerializedName("feedbackTypeTypeCode")
        public String feedbackTypeTypeCode;
        @SerializedName("feedbackTypeCode")
        public String feedbackTypeCode;
        @SerializedName("feedbackTypeKey")
        public int feedbackTypeKey;
        @SerializedName("feedbackTypeName")
        public String feedbackTypeName;
        @SerializedName("feedbackTypeTypeKey")
        public int feedbackTypeTypeKey;
        @SerializedName("feedbackTypeTypeName")
        public String feedbackTypeTypeName;
    }

    public static class EventSource {
        @SerializedName("note")
        public String note;
        @SerializedName("eventSourceName")
        public String eventSourceName;
        @SerializedName("eventSourceKey")
        public int eventSourceKey;
        @SerializedName("eventSourceCode")
        public String eventSourceCode;
    }

    public static class PointsEntry {
        @SerializedName("reason")
        public List<Reason> reasons;
    }

    public static class Reason {
        @SerializedName("reasonName")
        public String reasonName;
        @SerializedName("reasonKey")
        public int reasonKey;
    }
}
