package com.vitalityactive.va.snv.dto;

import android.support.annotation.NonNull;

import com.vitalityactive.va.persistence.models.screeningsandvaccinations.GetPotentialPointsAndEventsCompletedPointsEvent;
import com.vitalityactive.va.persistence.models.screeningsandvaccinations.EventType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kerry.e.lawagan on 11/27/2017.
 */

public class EventTypeDto implements Comparable<EventTypeDto> {
    private String categoryCode;
    private int categoryKey;
    private String categoryName;
    private List<EventDto> event;
    private String reasonCode;
    private int reasonKey;
    private String reasonName;
    private int totalEarnedPoints;
    private int totalPotentialPoints;
    private String typeCode;
    private int typeKey;
    private String typeName;

    public EventTypeDto() {
    }

    public EventTypeDto(EventType eventType) {
        this.categoryCode = eventType.getCategoryCode();
        this.categoryKey = eventType.getCategoryKey();
        this.categoryName = eventType.getCategoryName();
        this.reasonCode = eventType.getReasonCode();
        this.reasonKey = eventType.getReasonKey();
        this.reasonName = eventType.getReasonName();
        this.totalEarnedPoints = eventType.getTotalEarnedPoints();
        this.totalPotentialPoints = eventType.getTotalPotentialPoints();
        this.typeCode = eventType.getTypeCode();
        this.typeKey = eventType.getTypeKey();
        this.typeName = eventType.getTypeName();

        event = new ArrayList<EventDto>();
        for(GetPotentialPointsAndEventsCompletedPointsEvent e: eventType.getEvent()) {
            event.add(new EventDto(e));
        }
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

    public List<EventDto> getEvent() {
        return event;
    }

    public void setEvent(List<EventDto> event) {
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

    @Override
    public int compareTo(@NonNull EventTypeDto eventTypeDto) {
        return this.typeName.compareTo(eventTypeDto.getTypeName());
    }
}
