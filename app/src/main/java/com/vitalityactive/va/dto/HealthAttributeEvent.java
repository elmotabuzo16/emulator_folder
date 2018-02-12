package com.vitalityactive.va.dto;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.vitalityactive.va.persistence.Model;
import com.vitalityactive.va.persistence.models.vhc.EventMetaData;
import com.vitalityactive.va.persistence.models.vhc.EventSource;
import com.vitalityactive.va.utilities.date.Date;
import com.vitalityactive.va.vhc.service.HealthAttributeResponse;

import io.realm.RealmList;
import io.realm.RealmObject;

public class HealthAttributeEvent extends RealmObject implements Model {
    private long eventDateTime;

    private String eventId;

    private RealmList<EventMetaData> metadata;

    private RealmList<HealthAttributeReading> healthAttributeReadings;

    private RealmList<HealthAttributePointsEntry> pointsEntries;

    private EventSource eventSource;

    private int eventTypeTypeKey;

    public HealthAttributeEvent() {
    }

    public HealthAttributeEvent(HealthAttributeResponse.Event event, int eventTypeTypeKey) {
        eventDateTime = new Date(event.eventDateTime).getMillisecondsSinceEpoch();
        eventId = String.valueOf(event.eventId);
        this.eventTypeTypeKey = eventTypeTypeKey;

        if (event.eventSource != null)
            eventSource = new EventSource(event.eventSource);

        metadata = new RealmList<>();
        if (event.eventMetaData != null) {
            for (HealthAttributeResponse.EventMetaData eventMetaData : event.eventMetaData) {
                metadata.add(new EventMetaData(eventMetaData));
            }
        }

        healthAttributeReadings = new RealmList<>();
        if (event.healthAttributeReadings != null) {
            for (HealthAttributeResponse.HealthAttributeReading healthAttributeReading : event.healthAttributeReadings) {
                healthAttributeReadings.add(new HealthAttributeReading(healthAttributeReading, eventId));
            }
        }

        pointsEntries = new RealmList<>();
        if (event.pointsEntries != null) {
            for (HealthAttributeResponse.PointsEntry pointsEntry : event.pointsEntries) {
                pointsEntries.add(new HealthAttributePointsEntry(pointsEntry));
            }
        }
    }

    @NonNull
    public RealmList<HealthAttributeReading> getAttributeReadings() {
        return healthAttributeReadings;
    }

    public RealmList<EventMetaData> getMetaData() {
        return metadata;
    }

    public EventSource getEventSource() {
        return eventSource;
    }

    public RealmList<HealthAttributePointsEntry> getPointsEntries() {
        return pointsEntries;
    }

    public long getEventDateTime() {
        return eventDateTime;
    }

    public int getEventTypeTypeKey() {
        return eventTypeTypeKey;
    }
}
