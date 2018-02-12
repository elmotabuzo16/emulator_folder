package com.vitalityactive.va.myhealth.dto;


import com.vitalityactive.va.persistence.models.myhealth.Event;

public class EventDTO {

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

    public EventDTO(Long applicableTo, String dateLogged, String effectiveDateTime, Integer eventId, String eventSourceTypeCode, Integer eventSourceTypeKey, String eventSourceTypeName, Long reportedBy, String typeCode, Integer typeKey, String typeName) {
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

    public EventDTO(Event event) {
        this.applicableTo = event.getApplicableTo();
        this.dateLogged = event.getDateLogged();
        this.effectiveDateTime = event.getEffectiveDateTime();
        this.eventId = event.getEventId();
        this.eventSourceTypeCode = event.getEventSourceTypeCode();
        this.eventSourceTypeKey = event.getEventSourceTypeKey();
        this.eventSourceTypeName = event.getEventSourceTypeName();
        this.reportedBy = event.getReportedBy();
        this.typeCode = event.getTypeCode();
        this.typeKey = event.getTypeKey();
        this.typeName = event.getTypeName();
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
