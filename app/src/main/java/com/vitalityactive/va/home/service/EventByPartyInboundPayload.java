package com.vitalityactive.va.home.service;

import com.google.gson.annotations.SerializedName;

public class EventByPartyInboundPayload {
    @SerializedName("eventTypes")
    public EventType[] eventTypes;

    public EventByPartyInboundPayload(int key) {
        this.eventTypes = new EventType[] { new EventType(key) };
    }

    public static class EventType {
        @SerializedName("typeKey")
        public int key;

        public EventType(int key) {
            this.key = key;
        }
    }
}
