package com.vitalityactive.va.persistence.models.screeningsandvaccinations;

import io.realm.RealmObject;

import com.vitalityactive.va.persistence.Model;
import com.vitalityactive.va.snv.onboarding.service.GetPotentialPointsAndEventsCompletedPointsResponse;

/**
 * Created by kerry.e.lawagan on 11/29/2017.
 */

public class GetPotentialPointsAndEventsCompletedPointsEvent extends RealmObject implements Model {
    private String eventDateTime;
    private int eventId;

    public GetPotentialPointsAndEventsCompletedPointsEvent(){
    }

    public GetPotentialPointsAndEventsCompletedPointsEvent(GetPotentialPointsAndEventsCompletedPointsResponse.Event event) {
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
}
