package com.vitalityactive.va.eventsfeed.data.net.response;

/**
 * Created by jayellos on 11/17/17.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AssociatedEvent{

    @SerializedName("associationTypeCode")
    @Expose
    private String associationTypeCode;
    @SerializedName("associationTypeKey")
    @Expose
    private Integer associationTypeKey;
    @SerializedName("associationTypeName")
    @Expose
    private String associationTypeName;
    @SerializedName("dateLogged")
    @Expose
    private String dateLogged;
    @SerializedName("dateTimeAssociated")
    @Expose
    private String dateTimeAssociated;
    @SerializedName("eventDateTime")
    @Expose
    private String eventDateTime;
    @SerializedName("eventId")
    @Expose
    private Integer eventId;
    @SerializedName("eventSourceCode")
    @Expose
    private String eventSourceCode;
    @SerializedName("eventSourceKey")
    @Expose
    private Integer eventSourceKey;
    @SerializedName("eventSourceName")
    @Expose
    private String eventSourceName;
    @SerializedName("eventTypeCode")
    @Expose
    private String eventTypeCode;
    @SerializedName("eventTypeKey")
    @Expose
    private Integer eventTypeKey;
    @SerializedName("eventTypeName")
    @Expose
    private String eventTypeName;

    public String getAssociationTypeCode() {
        return associationTypeCode;
    }

    public void setAssociationTypeCode(String associationTypeCode) {
        this.associationTypeCode = associationTypeCode;
    }

    public Integer getAssociationTypeKey() {
        return associationTypeKey;
    }

    public void setAssociationTypeKey(Integer associationTypeKey) {
        this.associationTypeKey = associationTypeKey;
    }

    public String getAssociationTypeName() {
        return associationTypeName;
    }

    public void setAssociationTypeName(String associationTypeName) {
        this.associationTypeName = associationTypeName;
    }

    public String getDateLogged() {
        return dateLogged;
    }

    public void setDateLogged(String dateLogged) {
        this.dateLogged = dateLogged;
    }

    public String getDateTimeAssociated() {
        return dateTimeAssociated;
    }

    public void setDateTimeAssociated(String dateTimeAssociated) {
        this.dateTimeAssociated = dateTimeAssociated;
    }

    public String getEventDateTime() {
        return eventDateTime;
    }

    public void setEventDateTime(String eventDateTime) {
        this.eventDateTime = eventDateTime;
    }

    public Integer getEventId() {
        return eventId;
    }

    public void setEventId(Integer eventId) {
        this.eventId = eventId;
    }

    public String getEventSourceCode() {
        return eventSourceCode;
    }

    public void setEventSourceCode(String eventSourceCode) {
        this.eventSourceCode = eventSourceCode;
    }

    public Integer getEventSourceKey() {
        return eventSourceKey;
    }

    public void setEventSourceKey(Integer eventSourceKey) {
        this.eventSourceKey = eventSourceKey;
    }

    public String getEventSourceName() {
        return eventSourceName;
    }

    public void setEventSourceName(String eventSourceName) {
        this.eventSourceName = eventSourceName;
    }

    public String getEventTypeCode() {
        return eventTypeCode;
    }

    public void setEventTypeCode(String eventTypeCode) {
        this.eventTypeCode = eventTypeCode;
    }

    public Integer getEventTypeKey() {
        return eventTypeKey;
    }

    public void setEventTypeKey(Integer eventTypeKey) {
        this.eventTypeKey = eventTypeKey;
    }

    public String getEventTypeName() {
        return eventTypeName;
    }

    public void setEventTypeName(String eventTypeName) {
        this.eventTypeName = eventTypeName;
    }

}
