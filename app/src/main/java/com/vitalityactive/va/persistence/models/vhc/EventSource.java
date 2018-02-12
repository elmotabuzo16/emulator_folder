package com.vitalityactive.va.persistence.models.vhc;

import com.vitalityactive.va.persistence.Model;
import com.vitalityactive.va.vhc.service.HealthAttributeResponse;

import io.realm.RealmObject;

public class EventSource extends RealmObject implements Model {
    private String note;

    private String eventSourceName;
    private int eventSourceKey;
    private String eventSourceCode;

    public EventSource() {
    }

    public EventSource(HealthAttributeResponse.EventSource source) {
        eventSourceName = source.eventSourceName;
        eventSourceKey = source.eventSourceKey;
        eventSourceCode = source.eventSourceCode;
        note = source.note;
    }

    public String getEventSourceName() {
        return eventSourceName;
    }

    public int getEventSourceKey() {
        return eventSourceKey;
    }
}
