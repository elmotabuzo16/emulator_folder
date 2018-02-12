package com.vitalityactive.va.snv.dto;

import android.support.annotation.NonNull;
import android.util.Log;

import com.vitalityactive.va.persistence.models.screeningsandvaccinations.ScreeningVaccinationAssociatedEvents;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by dharel.h.rosell on 12/1/2017.
 */

public class HistoryDetailDto implements Comparable<HistoryDetailDto> {

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

    public HistoryDetailDto() {
    }

    public HistoryDetailDto(ScreeningVaccinationAssociatedEvents model) {
        this.associationTypeCode = model.getAssociationTypeCode();
        this.associationTypeKey = model.getAssociationTypeKey();
        this.associationTypeName = model.getAssociationTypeName();
        this.categoryCode = model.getCategoryCode();
        this.categoryKey = model.getCategoryKey();
        this.categoryName = model.getCategoryName();
        this.dateLogged = model.getDateLogged();
        this.dateTimeAssociated = model.getDateTimeAssociated();
        this.eventDateTime = model.getEventDateTime();
        this.eventId = model.getEventId();
        this.eventSourceCode = model.eventSourceCode;
        this.eventSourceKey = model.getEventSourceKey();
        this.eventSourceName = model.eventSourceName;
        this.eventTypeCode = model.getEventTypeCode();
        this.eventTypeKey = model.getEventTypeKey();
        this.eventTypeName = model.getEventTypeName();
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
        return getDateMessage(eventDateTime);
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

    public String getDateMessage(){
        return transforDetailedDate(getDateMessage(this.eventDateTime));
    }

    public String getDateString(){
        return getDateMessage(this.eventDateTime);
    }

    public String transforDetailedDate(String detailedDate) {
        StringBuilder dateStringBuilder = new StringBuilder("Tested on ");
        dateStringBuilder.append(detailedDate);
        return dateStringBuilder.toString();
    }

    public String getDateMessage(String stringDate) {
        String dateTime = "";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Log.d("TEST DATE TIME", "TRY");
            Date dateTrue = format.parse(stringDate);
            Log.d("TEST STRING DATE1", dateTrue.toString());
            format = new SimpleDateFormat("EEE, d MMM yyyy");
            dateTime = format.format(dateTrue);
            Log.d("TEST STRING DATE2", dateTime.toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return dateTime;
    }

    @Override
    public int compareTo(@NonNull HistoryDetailDto o) {
        if (getEventDateTime() == null || o.getEventDateTime() == null)
            return 0;
        return getEventDateTime().compareTo(o.getEventDateTime());
    }
}
