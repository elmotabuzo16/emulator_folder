package com.vitalityactive.va.eventsfeed.data.net.response;

/**
 * Created by jayellos on 11/17/17.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class EventsFeedResult {
    @SerializedName("event")
    @Expose
    private List<EventResponse> events = null;

    public List<EventResponse> getEventResponses() {
        return events;
    }

    public void setEventResponses(List<EventResponse> eventResponse) {
        this.events = eventResponse;
    }
}
