package com.vitalityactive.va.persistence.models.screeningsandvaccinations;


import com.vitalityactive.va.home.service.EventByPartyResponse;
import com.vitalityactive.va.persistence.Model;
import com.vitalityactive.va.snv.history.service.ScreeningAndVaccinationHistoryResponse;
import com.vitalityactive.va.utilities.date.Date;

import java.util.List;

import io.realm.RealmObject;

/**
 * Created by dharel.h.rosell on 11/30/2017.
 */

public class ScreeningVaccinationAssociatedEvents extends RealmObject implements Model {

    public String associationTypeCode;
    public int associationTypeKey;
    public String associationTypeName;
    public String categoryCode;
    public int categoryKey;
    public String categoryName;
    public String dateLogged;
    public String dateTimeAssociated;
    public String eventDateTime;
    public int eventId;
    public String eventSourceCode;
    public int eventSourceKey;
    public String eventSourceName;
    public String eventTypeCode;
    public int eventTypeKey;
    public String eventTypeName;
    public Long dateConverted;

    public ScreeningVaccinationAssociatedEvents() {
    }

    public ScreeningVaccinationAssociatedEvents(EventByPartyResponse.AssociatedEvent responseHistory) {
        this.associationTypeCode = responseHistory.getAssociationTypeCode();
        this.associationTypeKey = responseHistory.getAssociationTypeKey();
        this.associationTypeName = responseHistory.getAssociationTypeName();
        this.categoryCode = responseHistory.getCategoryCode();
        this.categoryKey = responseHistory.getCategoryKey();
        this.categoryName = responseHistory.getCategoryName();
        this.dateLogged = responseHistory.getDateLogged();
        this.dateTimeAssociated = responseHistory.getDateTimeAssociated();
        this.eventDateTime = responseHistory.getEventDateTime();
        this.eventId = responseHistory.getEventId();
        this.eventSourceCode = responseHistory.getEventSourceCode();
        this.eventSourceKey = responseHistory.getEventSourceKey();
        this.eventSourceName = responseHistory.getEventSourceName();
        this.eventTypeCode = responseHistory.getEventTypeCode();
        this.eventTypeKey = responseHistory.getEventTypeKey();
        this.eventTypeName = responseHistory.getEventTypeName();
        this.dateConverted = new Date(responseHistory.getEventDateTime()).getMillisecondsSinceEpoch();
    }

    public String getAssociationTypeCode() {
        return associationTypeCode;
    }

    public void setAssociationTypeCode(String associationTypeCode) {
        this.associationTypeCode = associationTypeCode;
    }

    public int getAssociationTypeKey() {
        return associationTypeKey;
    }

    public void setAssociationTypeKey(int associationTypeKey) {
        this.associationTypeKey = associationTypeKey;
    }

    public String getAssociationTypeName() {
        return associationTypeName;
    }

    public void setAssociationTypeName(String associationTypeName) {
        this.associationTypeName = associationTypeName;
    }

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

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
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

    public int getEventTypeKey() {
        return eventTypeKey;
    }

    public void setEventTypeKey(int eventTypeKey) {
        this.eventTypeKey = eventTypeKey;
    }

    public String getEventTypeName() {
        return eventTypeName;
    }

    public void setEventTypeName(String eventTypeName) {
        this.eventTypeName = eventTypeName;
    }

    public Long getDateConverted() {
        return dateConverted;
    }

    public void setDateConverted(Long dateConverted) {
        this.dateConverted = dateConverted;
    }
}
