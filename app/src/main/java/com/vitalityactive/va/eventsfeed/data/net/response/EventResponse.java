package com.vitalityactive.va.eventsfeed.data.net.response;

/**
 * Created by jayellos on 11/17/17.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class EventResponse {

    @SerializedName("associatedEvents")
    @Expose
    private List<AssociatedEvent> associatedEvents = null;
    @SerializedName("dateLogged")
    @Expose
    private String dateLogged;
    @SerializedName("eventDateTime")
    @Expose
    private String eventDateTime;
    @SerializedName("eventExternalReference")
    @Expose
    private List<EventExternalReference> eventExternalReference = null;
    @SerializedName("eventMetaDataOuts")
    @Expose
    private List<EventMetaDataOut> eventMetaDataOuts = null;
    @SerializedName("eventSourceCode")
    @Expose
    private String eventSourceCode;
    @SerializedName("eventSourceKey")
    @Expose
    private Integer eventSourceKey;
    @SerializedName("eventSourceName")
    @Expose
    private String eventSourceName;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("partyId")
    @Expose
    private Integer partyId;
    @SerializedName("reportedBy")
    @Expose
    private Integer reportedBy;
    @SerializedName("typeCode")
    @Expose
    private String typeCode;
    @SerializedName("typeKey")
    @Expose
    private Integer typeKey;
    @SerializedName("typeName")
    @Expose
    private String typeName;

    @SerializedName("categoryCode")
    @Expose
    private String categoryCode;
    @SerializedName("categoryKey")
    @Expose
    private int categoryKey;
    @SerializedName("categoryName")
    @Expose
    private String categoryName;



    public List<AssociatedEvent> getAssociatedEvents() {
        return associatedEvents;
    }

    public void setAssociatedEvents(List<AssociatedEvent> associatedEvents) {
        this.associatedEvents = associatedEvents;
    }

    public String getDateLogged() {
        return dateLogged;
    }

    public void setDateLogged(String dateLogged) {
        this.dateLogged = dateLogged;
    }

    public String getEventDateTime() {
        return eventDateTime;
    }

    public void setEventDateTime(String eventDateTime) {
        this.eventDateTime = eventDateTime;
    }

    public List<EventExternalReference> getEventExternalReference() {
        return eventExternalReference;
    }

    public void setEventExternalReference(List<EventExternalReference> eventExternalReference) {
        this.eventExternalReference = eventExternalReference;
    }

    public List<EventMetaDataOut> getEventMetaDataOuts() {
        return eventMetaDataOuts;
    }

    public void setEventMetaDataOuts(List<EventMetaDataOut> eventMetaDataOuts) {
        this.eventMetaDataOuts = eventMetaDataOuts;
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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPartyId() {
        return partyId;
    }

    public void setPartyId(Integer partyId) {
        this.partyId = partyId;
    }

    public Integer getReportedBy() {
        return reportedBy;
    }

    public void setReportedBy(Integer reportedBy) {
        this.reportedBy = reportedBy;
    }

    public String getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

    public Integer getTypeKey() {
        return typeKey;
    }

    public void setTypeKey(Integer typeKey) {
        this.typeKey = typeKey;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public int getCategoryKey() {
        return categoryKey;
    }

    public void setCategoryKey(int categoryKey) {
        this.categoryKey = categoryKey;
    }
}
