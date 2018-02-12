package com.vitalityactive.va.snv.dto;

import com.vitalityactive.va.persistence.models.screeningsandvaccinations.GetPotentialPointsAndEventsCompletedPointsEvent;

import java.util.List;

/**
 * Created by kerry.e.lawagan on 11/27/2017.
 */

public class EventDto {
    private String eventDateTime;
    private int eventId;
    private List<EventMetaDataTypeDto> eventMetaDataType;
    private EventSourceDto eventSource;
    private List<PointsEntryDto> pointsEntries;

    public EventDto() {
    }

    public EventDto(GetPotentialPointsAndEventsCompletedPointsEvent event) {
        eventDateTime = event.getEventDateTime();
        eventId = event.getEventId();
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

    public List<EventMetaDataTypeDto> getEventMetaDataType() {
        return eventMetaDataType;
    }

    public void setEventMetaDataType(List<EventMetaDataTypeDto> eventMetaDataType) {
        this.eventMetaDataType = eventMetaDataType;
    }

    public EventSourceDto getEventSource() {
        return eventSource;
    }

    public void setEventSource(EventSourceDto eventSource) {
        this.eventSource = eventSource;
    }

    public List<PointsEntryDto> getPointsEntries() {
        return pointsEntries;
    }

    public void setPointsEntries(List<PointsEntryDto> pointsEntries) {
        this.pointsEntries = pointsEntries;
    }
}
