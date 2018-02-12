package com.vitalityactive.va.persistence.models.screeningsandvaccinations;

import com.vitalityactive.va.home.service.EventByPartyResponse;
import com.vitalityactive.va.persistence.Model;
import com.vitalityactive.va.utilities.date.Date;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by dharel.h.rosell on 12/8/2017.
 */

public class ScreeningVaccinationEventByPartyOutbound extends RealmObject implements Model {

    public RealmList<ScreeningVaccinationAssociatedEvents> associatedEvents;
    public String categoryCode;
    public int categoryKey;
    public String categoryName;
    public String dateLogged;
    public String eventDateTime;
    public RealmList<ScreeningVaccinationEventExternalReference> eventExternalReference;
    public RealmList<ScreeningVaccinationMetadata> eventMetaDataOuts;
    public String eventSourceCode;
    public int eventSourceKey;
    public String eventSourceName;
    public int id;
    public long partyId;
    public long reportedBy;
    public String typeCode;
    public int typeKey;
    public String typeName;
    public Long dateConverted;
//    public String dateConverted;

    public ScreeningVaccinationEventByPartyOutbound() {
    }

    public ScreeningVaccinationEventByPartyOutbound(EventByPartyResponse.EventByPartyOutbound eventByPartyOutbound) {

        this.associatedEvents = new RealmList<ScreeningVaccinationAssociatedEvents>();
        this.eventExternalReference = new RealmList<ScreeningVaccinationEventExternalReference>();
        this.eventMetaDataOuts = new RealmList<ScreeningVaccinationMetadata>();

        if(eventByPartyOutbound.getAssociatedEvents() != null) {
            for (EventByPartyResponse.AssociatedEvent associatedEvent : eventByPartyOutbound.getAssociatedEvents()) {
                this.associatedEvents.add(new ScreeningVaccinationAssociatedEvents(associatedEvent));
            }
        }

        for(EventByPartyResponse.EventExternalReference eventExternalReference: eventByPartyOutbound.getEventExternalReference()) {
            this.eventExternalReference.add(new ScreeningVaccinationEventExternalReference(eventExternalReference));
        }

        for(EventByPartyResponse.EventMetaData eventMetaData: eventByPartyOutbound.getEventMetaDataOuts()){
            this.eventMetaDataOuts.add(new ScreeningVaccinationMetadata(eventMetaData));
        }

        this.categoryCode = eventByPartyOutbound.getCategoryCode();
        this.categoryKey = eventByPartyOutbound.getCategoryKey();
        this.categoryName = eventByPartyOutbound.getCategoryName();
        this.dateLogged = eventByPartyOutbound.getDateLogged();
        this.eventDateTime = eventByPartyOutbound.getEventDateTime();
        this.eventSourceCode = eventByPartyOutbound.getEventSourceCode();
        this.eventSourceKey = eventByPartyOutbound.getEventSourceKey();
        this.eventSourceName = eventByPartyOutbound.getEventSourceName();
        this.id = eventByPartyOutbound.getId();
        this.partyId = eventByPartyOutbound.getPartyId();
        this.reportedBy = eventByPartyOutbound.getReportedBy();
        this.typeCode = eventByPartyOutbound.getTypeCode();
        this.typeKey = eventByPartyOutbound.getTypeKey();
        this.typeName = eventByPartyOutbound.getTypeName();
        this.dateConverted = new Date(eventByPartyOutbound.getEventDateTime()).getMillisecondsSinceEpoch();
//        this.dateConverted = eventByPartyOutbound.getEventDateTime();

    }

    public RealmList<ScreeningVaccinationAssociatedEvents> getAssociatedEvents() {
        return associatedEvents;
    }

    public void setAssociatedEvents(RealmList<ScreeningVaccinationAssociatedEvents> associatedEvents) {
        this.associatedEvents = associatedEvents;
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

    public String getEventDateTime() {
        return eventDateTime;
    }

    public void setEventDateTime(String eventDateTime) {
        this.eventDateTime = eventDateTime;
    }

    public RealmList<ScreeningVaccinationEventExternalReference> getEventExternalReference() {
        return eventExternalReference;
    }

    public void setEventExternalReference(RealmList<ScreeningVaccinationEventExternalReference> eventExternalReference) {
        this.eventExternalReference = eventExternalReference;
    }

    public RealmList<ScreeningVaccinationMetadata> getEventMetaDataOuts() {
        return eventMetaDataOuts;
    }

    public void setEventMetaDataOuts(RealmList<ScreeningVaccinationMetadata> eventMetaDataOuts) {
        this.eventMetaDataOuts = eventMetaDataOuts;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getPartyId() {
        return partyId;
    }

    public void setPartyId(long partyId) {
        this.partyId = partyId;
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

    public Long getDateConverted() {
        return dateConverted;
    }

    public void setDateConverted(Long dateConverted) {
        this.dateConverted = dateConverted;
    }
}
