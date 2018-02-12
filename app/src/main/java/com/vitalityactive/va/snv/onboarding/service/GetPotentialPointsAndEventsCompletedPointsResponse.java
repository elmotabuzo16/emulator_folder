package com.vitalityactive.va.snv.onboarding.service;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by kerry.e.lawagan on 11/24/2017.
 */

public class GetPotentialPointsAndEventsCompletedPointsResponse {
    @SerializedName("eventType")
    private List<EventType> eventType;
    @SerializedName("warnings")
    private List<Warning> warnings;

    public List<EventType> getEventType() {
        return eventType;
    }

    public void setEventType(List<EventType> eventType) {
        this.eventType = eventType;
    }

    public List<Warning> getWarnings() {
        return warnings;
    }

    public void setWarnings(List<Warning> warnings) {
        this.warnings = warnings;
    }

    public static class Warning {
        @SerializedName("code")
        private String code;
        @SerializedName("context")
        private String context;
        @SerializedName("key")
        private int key;
        @SerializedName("name")
        private String name;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getContext() {
            return context;
        }

        public void setContext(String context) {
            this.context = context;
        }

        public int getKey() {
            return key;
        }

        public void setKey(int key) {
            this.key = key;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public static class EventType {
        @SerializedName("categoryCode")
        private String categoryCode;
        @SerializedName("categoryKey")
        private int categoryKey;
        @SerializedName("categoryName")
        private String categoryName;
        @SerializedName("event")
        private List<Event> event;
        @SerializedName("reasonCode")
        private String reasonCode;
        @SerializedName("reasonKey")
        private int reasonKey;
        @SerializedName("reasonName")
        private String reasonName;
        @SerializedName("totalEarnedPoints")
        private int totalEarnedPoints;
        @SerializedName("totalPotentialPoints")
        private int totalPotentialPoints;
        @SerializedName("typeCode")
        private String typeCode;
        @SerializedName("typeKey")
        private int typeKey;
        @SerializedName("typeName")
        private String typeName;

        public String getCategoryCode() {
            return categoryCode;
        }

        public void setCategoryCode(String categoryCode) {
            this.categoryCode = categoryCode;
        }

        public int getCategoryKey() {
            return categoryKey;
        }

        public void setCategoryKey(int categoryKey) {
            this.categoryKey = categoryKey;
        }

        public String getCategoryName() {
            return categoryName;
        }

        public void setCategoryName(String categoryName) {
            this.categoryName = categoryName;
        }

        public List<Event> getEvent() {
            return event;
        }

        public void setEvent(List<Event> event) {
            this.event = event;
        }

        public String getReasonCode() {
            return reasonCode;
        }

        public void setReasonCode(String reasonCode) {
            this.reasonCode = reasonCode;
        }

        public int getReasonKey() {
            return reasonKey;
        }

        public void setReasonKey(int reasonKey) {
            this.reasonKey = reasonKey;
        }

        public String getReasonName() {
            return reasonName;
        }

        public void setReasonName(String reasonName) {
            this.reasonName = reasonName;
        }

        public int getTotalEarnedPoints() {
            return totalEarnedPoints;
        }

        public void setTotalEarnedPoints(int totalEarnedPoints) {
            this.totalEarnedPoints = totalEarnedPoints;
        }

        public int getTotalPotentialPoints() {
            return totalPotentialPoints;
        }

        public void setTotalPotentialPoints(int totalPotentialPoints) {
            this.totalPotentialPoints = totalPotentialPoints;
        }

        public String getTypeCode() {
            return typeCode;
        }

        public void setTypeCode(String typeCode) {
            this.typeCode = typeCode;
        }

        public int getTypeKey() {
            return typeKey;
        }

        public void setTypeKey(int typeKey) {
            this.typeKey = typeKey;
        }

        public String getTypeName() {
            return typeName;
        }

        public void setTypeName(String typeName) {
            this.typeName = typeName;
        }
    }

    public static class Event {
        @SerializedName("eventDateTime")
        private String eventDateTime;
        @SerializedName("eventId")
        private int eventId;
        @SerializedName("eventMetaDataType")
        private List<EventMetaDataType> eventMetaDataType;
        @SerializedName("eventSource")
        private EventSource eventSource;
        @SerializedName("pointsEntries")
        private List<PointsEntry> pointsEntries;

        public String getEventDateTime() {
            return eventDateTime;
        }

        public void setEventDateTime(String eventDateTime) {
            this.eventDateTime = eventDateTime;
        }

        public int getEventId() {
            return eventId;
        }

        public void setEventId(int eventId) {
            this.eventId = eventId;
        }

        public List<EventMetaDataType> getEventMetaDataType() {
            return eventMetaDataType;
        }

        public void setEventMetaDataType(List<EventMetaDataType> eventMetaDataType) {
            this.eventMetaDataType = eventMetaDataType;
        }

        public EventSource getEventSource() {
            return eventSource;
        }

        public void setEventSource(EventSource eventSource) {
            this.eventSource = eventSource;
        }

        public List<PointsEntry> getPointsEntries() {
            return pointsEntries;
        }

        public void setPointsEntries(List<PointsEntry> pointsEntries) {
            this.pointsEntries = pointsEntries;
        }
    }

    public static class EventMetaDataType {
        @SerializedName("code")
        private String code;
        @SerializedName("key")
        private int key;
        @SerializedName("name")
        private String name;
        @SerializedName("note")
        private String note;
        @SerializedName("unitOfMeasure")
        private String unitOfMeasure;
        @SerializedName("value")
        private String value;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public int getKey() {
            return key;
        }

        public void setKey(int key) {
            this.key = key;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getNote() {
            return note;
        }

        public void setNote(String note) {
            this.note = note;
        }

        public String getUnitOfMeasure() {
            return unitOfMeasure;
        }

        public void setUnitOfMeasure(String unitOfMeasure) {
            this.unitOfMeasure = unitOfMeasure;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

    public static class EventSource {
        @SerializedName("eventSourceCode")
        private String eventSourceCode;
        @SerializedName("eventSourcekey")
        private int eventSourcekey;
        @SerializedName("eventSourceName")
        private String eventSourceName;
        @SerializedName("note")
        private String note;

        public String getEventSourceCode() {
            return eventSourceCode;
        }

        public void setEventSourceCode(String eventSourceCode) {
            this.eventSourceCode = eventSourceCode;
        }

        public int getEventSourcekey() {
            return eventSourcekey;
        }

        public void setEventSourcekey(int eventSourcekey) {
            this.eventSourcekey = eventSourcekey;
        }

        public String getEventSourceName() {
            return eventSourceName;
        }

        public void setEventSourceName(String eventSourceName) {
            this.eventSourceName = eventSourceName;
        }

        public String getNote() {
            return note;
        }

        public void setNote(String note) {
            this.note = note;
        }
    }

    public static class PointsEntry {
        @SerializedName("categoryCode")
        private String categoryCode;
        @SerializedName("categorykey")
        private int categorykey;
        @SerializedName("categoryName")
        private String categoryName;
        @SerializedName("earnedValue")
        private int earnedValue;
        @SerializedName("id")
        private int id;
        @SerializedName("potentialValue")
        private int potentialValue;
        @SerializedName("preLimitValue")
        private int preLimitValue;
        @SerializedName("reason")
        private List<Reason> reason;
        @SerializedName("statusChangeDate")
        private String statusChangeDate;
        @SerializedName("statusTypeCode")
        private String statusTypeCode;
        @SerializedName("statusTypeKey")
        private int statusTypeKey;
        @SerializedName("statusTypeName")
        private String statusTypeName;
        @SerializedName("typeCode")
        private String typeCode;
        @SerializedName("typeKey")
        private int typeKey;
        @SerializedName("typeName")
        private String typeName;

        public String getCategoryCode() {
            return categoryCode;
        }

        public void setCategoryCode(String categoryCode) {
            this.categoryCode = categoryCode;
        }

        public int getCategorykey() {
            return categorykey;
        }

        public void setCategorykey(int categorykey) {
            this.categorykey = categorykey;
        }

        public String getCategoryName() {
            return categoryName;
        }

        public void setCategoryName(String categoryName) {
            this.categoryName = categoryName;
        }

        public int getEarnedValue() {
            return earnedValue;
        }

        public void setEarnedValue(int earnedValue) {
            this.earnedValue = earnedValue;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getPotentialValue() {
            return potentialValue;
        }

        public void setPotentialValue(int potentialValue) {
            this.potentialValue = potentialValue;
        }

        public int getPreLimitValue() {
            return preLimitValue;
        }

        public void setPreLimitValue(int preLimitValue) {
            this.preLimitValue = preLimitValue;
        }

        public List<Reason> getReason() {
            return reason;
        }

        public void setReason(List<Reason> reason) {
            this.reason = reason;
        }

        public String getStatusChangeDate() {
            return statusChangeDate;
        }

        public void setStatusChangeDate(String statusChangeDate) {
            this.statusChangeDate = statusChangeDate;
        }

        public String getStatusTypeCode() {
            return statusTypeCode;
        }

        public void setStatusTypeCode(String statusTypeCode) {
            this.statusTypeCode = statusTypeCode;
        }

        public int getStatusTypeKey() {
            return statusTypeKey;
        }

        public void setStatusTypeKey(int statusTypeKey) {
            this.statusTypeKey = statusTypeKey;
        }

        public String getStatusTypeName() {
            return statusTypeName;
        }

        public void setStatusTypeName(String statusTypeName) {
            this.statusTypeName = statusTypeName;
        }

        public String getTypeCode() {
            return typeCode;
        }

        public void setTypeCode(String typeCode) {
            this.typeCode = typeCode;
        }

        public int getTypeKey() {
            return typeKey;
        }

        public void setTypeKey(int typeKey) {
            this.typeKey = typeKey;
        }

        public String getTypeName() {
            return typeName;
        }

        public void setTypeName(String typeName) {
            this.typeName = typeName;
        }
    }

    public static class Reason {
        @SerializedName("categoryCode")
        private String categoryCode;
        @SerializedName("categoryKey")
        private int categoryKey;
        @SerializedName("categoryName")
        private String categoryName;
        @SerializedName("reasonCode")
        private String reasonCode;
        @SerializedName("reasonKey")
        private int reasonKey;
        @SerializedName("reasonName")
        private String reasonName;

        public String getCategoryCode() {
            return categoryCode;
        }

        public void setCategoryCode(String categoryCode) {
            this.categoryCode = categoryCode;
        }

        public int getCategoryKey() {
            return categoryKey;
        }

        public void setCategoryKey(int categoryKey) {
            this.categoryKey = categoryKey;
        }

        public String getCategoryName() {
            return categoryName;
        }

        public void setCategoryName(String categoryName) {
            this.categoryName = categoryName;
        }

        public String getReasonCode() {
            return reasonCode;
        }

        public void setReasonCode(String reasonCode) {
            this.reasonCode = reasonCode;
        }

        public int getReasonKey() {
            return reasonKey;
        }

        public void setReasonKey(int reasonKey) {
            this.reasonKey = reasonKey;
        }

        public String getReasonName() {
            return reasonName;
        }

        public void setReasonName(String reasonName) {
            this.reasonName = reasonName;
        }
    }
}
