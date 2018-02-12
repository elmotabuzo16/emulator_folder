package com.vitalityactive.va.eventsfeed.data.dto;

import com.vitalityactive.va.persistence.models.EventFeed;
import com.vitalityactive.va.persistence.models.Metadata;
import com.vitalityactive.va.utilities.date.Date;

import io.realm.RealmList;

public class EventDTO {
    private RealmList<Metadata> eventExternalReference;
    private RealmList<Metadata> eventMetaDataOuts;
    private int id;
    private Date dateLogged;
    private Date eventDateTime;
    private String eventSourceCode;
    private int eventSourceKey;
    private long reportedBy;
    private String typeCode;
    private int typeKey;
    private String typeName;
    private String categoryCode;
    private int categoryKey;
    private String categoryName;


    public EventDTO(EventFeed event) {
        id = event.getId();
        dateLogged = new Date(event.getDateLogged());
        eventDateTime = new Date(event.getEventDateTime());
        eventSourceCode = event.getEventSourceCode();
        eventSourceKey = event.getEventSourceKey();
        reportedBy = event.getReportedBy();
        typeCode = event.getTypeCode();
        typeKey = event.getTypeKey();
        typeName = event.getTypeName();
        categoryCode = event.getCategoryCode();
        categoryKey = event.getCategoryKey();
        categoryName = event.getCategoryName();

        eventExternalReference = event.getEventExternalReference();
        eventMetaDataOuts = event.getEventMetaDataOuts();
    }

    public RealmList<Metadata> getEventExternalReference() {
        return eventExternalReference;
    }

    public void setEventExternalReference(RealmList<Metadata> eventExternalReference) {
        this.eventExternalReference = eventExternalReference;
    }

    public RealmList<Metadata> getEventMetaDataOuts() {
        return eventMetaDataOuts;
    }

    public void setEventMetaDataOuts(RealmList<Metadata> eventMetaDataOuts) {
        this.eventMetaDataOuts = eventMetaDataOuts;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDateLogged() {
        return dateLogged;
    }

    public void setDateLogged(Date dateLogged) {
        this.dateLogged = dateLogged;
    }

    public Date getEventDateTime() {
        return eventDateTime;
    }

    public void setEventDateTime(Date eventDateTime) {
        this.eventDateTime = eventDateTime;
    }

    public String getEventSourceCode() {
        return eventSourceCode;
    }

    public void setEventSourceCode(String eventSourceCode) {
        this.eventSourceCode = eventSourceCode;
    }

    public int getEventSourceKey() {
        return eventSourceKey;
    }

    public void setEventSourceKey(int eventSourceKey) {
        this.eventSourceKey = eventSourceKey;
    }

    public long getReportedBy() {
        return reportedBy;
    }

    public void setReportedBy(long reportedBy) {
        this.reportedBy = reportedBy;
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
