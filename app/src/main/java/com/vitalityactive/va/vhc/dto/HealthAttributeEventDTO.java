package com.vitalityactive.va.vhc.dto;

import android.support.annotation.NonNull;

import com.vitalityactive.va.dto.HealthAttributeEvent;
import com.vitalityactive.va.dto.HealthAttributePointsEntry;
import com.vitalityactive.va.dto.HealthAttributePointsEntryReason;
import com.vitalityactive.va.dto.HealthAttributeReading;
import com.vitalityactive.va.persistence.models.vhc.EventMetaData;
import com.vitalityactive.va.persistence.models.vhc.EventSource;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmList;

class HealthAttributeEventDTO {
    private int eventTypeTypeKey;
    private List<EventMetaDataDTO> eventMetaDataDTOs = new ArrayList<>();
    private int eventSourceKey;
    private List<HealthAttributeReadingDTO> attributeReadings = new ArrayList<>();
    private long date;
    private String eventSourceName;

    public HealthAttributeEventDTO(HealthAttributeEvent event) {
        date = event.getEventDateTime();
        eventTypeTypeKey = event.getEventTypeTypeKey();

        EventSource eventSource = event.getEventSource();
        if (eventSource != null) {
            eventSourceName = eventSource.getEventSourceName();
            eventSourceKey = eventSource.getEventSourceKey();
        }

        RealmList<HealthAttributePointsEntry> pointsEntries = event.getPointsEntries();
        if (pointsEntries != null && pointsEntries.size() != 0) {
            HealthAttributePointsEntry pointsEntry = pointsEntries.first();

            RealmList<HealthAttributePointsEntryReason> reasons = pointsEntry.getReasons();
            if (reasons != null && reasons.size() != 0) {
                HealthAttributePointsEntryReason reason = reasons.first();
            }
        }

        RealmList<HealthAttributeReading> attributeReadings = event.getAttributeReadings();
        if (attributeReadings != null && attributeReadings.size() > 0) {
            for (HealthAttributeReading reading : attributeReadings) {
                this.attributeReadings.add(new HealthAttributeReadingDTO(reading));
            }
        }

        RealmList<EventMetaData> realmEventMetaData = event.getMetaData();
        if (realmEventMetaData != null && realmEventMetaData.size() > 0) {
            for (EventMetaData eventMetaData : realmEventMetaData) {
                this.eventMetaDataDTOs.add(new EventMetaDataDTO(eventMetaData));
            }
        }
    }

    public HealthAttributeEventDTO() {

    }

    @NonNull
    public List<HealthAttributeReadingDTO> getAttributeReadings() {
        return attributeReadings;
    }

    public long getDate() {
        return date;
    }

    public String getEventSourceName() {
        return eventSourceName;
    }

    public int getEventSourceKey() {
        return eventSourceKey;
    }

    public List<EventMetaDataDTO> getEventMetaDataDTOs() {
        return eventMetaDataDTOs;
    }
}
