package com.vitalityactive.va.persistence.models.myhealth;

import com.vitalityactive.va.networking.model.HealthAttributeInformationResponse;
import com.vitalityactive.va.persistence.Model;

import io.realm.RealmObject;

public class Event extends RealmObject implements Model {
    Long applicableTo;
    String dateLogged;
    String effectiveDateTime;
    Integer eventId;
    String eventSourceTypeCode;
    Integer eventSourceTypeKey;
    String eventSourceTypeName;
    Long reportedBy;
    String typeCode;
    Integer typeKey;
    String typeName;

    public Event() {
    }

    public Event(HealthAttributeInformationResponse.Event event) {
        this.applicableTo = event.applicableTo;
        this.dateLogged = event.dateLogged;
        this.effectiveDateTime = event.effectiveDateTime;
        this.eventId = event.eventId;
        this.eventSourceTypeCode = event.eventSourceTypeCode;
        this.eventSourceTypeKey = event.eventSourceTypeKey;
        this.eventSourceTypeName = event.eventSourceTypeName;
        this.reportedBy = event.reportedBy;
        this.typeCode = event.typeCode;
        this.typeKey = event.typeKey;
        this.typeName = event.typeName;
    }

    public Event(Long applicableTo, String dateLogged, String effectiveDateTime, Integer eventId, String eventSourceTypeCode, Integer eventSourceTypeKey, String eventSourceTypeName, Long reportedBy, String typeCode, Integer typeKey, String typeName) {
        this.applicableTo = applicableTo;
        this.dateLogged = dateLogged;
        this.effectiveDateTime = effectiveDateTime;
        this.eventId = eventId;
        this.eventSourceTypeCode = eventSourceTypeCode;
        this.eventSourceTypeKey = eventSourceTypeKey;
        this.eventSourceTypeName = eventSourceTypeName;
        this.reportedBy = reportedBy;
        this.typeCode = typeCode;
        this.typeKey = typeKey;
        this.typeName = typeName;
    }

    public Long getApplicableTo() {
        return applicableTo;
    }

    public String getDateLogged() {
        return dateLogged;
    }

    public String getEffectiveDateTime() {
        return effectiveDateTime;
    }

    public Integer getEventId() {
        return eventId;
    }

    public String getEventSourceTypeCode() {
        return eventSourceTypeCode;
    }

    public Integer getEventSourceTypeKey() {
        return eventSourceTypeKey;
    }

    public String getEventSourceTypeName() {
        return eventSourceTypeName;
    }

    public Long getReportedBy() {
        return reportedBy;
    }

    public String getTypeCode() {
        return typeCode;
    }

    public Integer getTypeKey() {
        return typeKey;
    }

    public String getTypeName() {
        return typeName;
    }
}