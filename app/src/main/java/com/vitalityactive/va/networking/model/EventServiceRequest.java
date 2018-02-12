package com.vitalityactive.va.networking.model;

import com.google.gson.annotations.SerializedName;
import com.vitalityactive.va.constants.EventSource;

public class EventServiceRequest {
    @SerializedName("eventCreation")
    public Event[] event;

    public EventServiceRequest(Event[] event) {
        this.event = event;
    }

    public static class Event {
        public String eventDateTime;
        public String dateLogged;
        public long eventSourceKey;
        public long typeKey;
        public long partyId;

        // not all fields mapped (ex eventMetaData, eventExternalReferenceIns)

        public Event(long typeKey, String dateString, long partyId) {
            this.typeKey = typeKey;
            this.partyId = partyId;
            eventDateTime = dateString;
            dateLogged = dateString;
            eventSourceKey = EventSource._MOBILEAPP;
        }
    }
}
