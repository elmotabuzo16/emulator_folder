package com.vitalityactive.va.eventsfeed.data.net.request;

import com.google.gson.annotations.SerializedName;

/**
 * Created by jayellos on 11/17/17.
 */


public class EventsFeedRequest {

    @SerializedName("effectivePeriod")
    private EffectivePeriod effectivePeriod;
    @SerializedName("eventTypeFilters")
    private EventTypeFilter[] eventTypeFilters = null;
    @SerializedName("eventTypes")
    private EventType[] eventTypes = null;


    public EventsFeedRequest(){
    }

    public EventsFeedRequest(EffectivePeriod effectivePeriod, EventTypeFilter[] eventTypeFilters, EventType[] eventTypes) {
        this.effectivePeriod = effectivePeriod;
        this.eventTypeFilters = eventTypeFilters;
        this.eventTypes = eventTypes;
    }

    public EffectivePeriod getEffectivePeriod() {
        return effectivePeriod;
    }

    public void setEffectivePeriod(EffectivePeriod effectivePeriod) {
        this.effectivePeriod = effectivePeriod;
    }

    public EventTypeFilter[] getEventTypeFilters() {
        return eventTypeFilters;
    }

    public void setEventTypeFilters(EventTypeFilter[] eventTypeFilters) {
        this.eventTypeFilters = eventTypeFilters;
    }

    public EventType[] getEventTypes() {
        return eventTypes;
    }

    public void setEventTypes(EventType[] eventTypes) {
        this.eventTypes = eventTypes;
    }

}
