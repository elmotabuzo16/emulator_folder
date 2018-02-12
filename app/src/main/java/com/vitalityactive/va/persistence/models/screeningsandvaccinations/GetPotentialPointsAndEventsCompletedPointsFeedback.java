package com.vitalityactive.va.persistence.models.screeningsandvaccinations;

import com.vitalityactive.va.persistence.Model;
import com.vitalityactive.va.snv.onboarding.service.GetPotentialPointsAndEventsCompletedPointsResponse;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by kerry.e.lawagan on 11/24/2017.
 */

public class GetPotentialPointsAndEventsCompletedPointsFeedback extends RealmObject implements Model {
    private RealmList<EventType> eventTypes;

    public GetPotentialPointsAndEventsCompletedPointsFeedback() {
    }

    public GetPotentialPointsAndEventsCompletedPointsFeedback(GetPotentialPointsAndEventsCompletedPointsResponse response) {
        eventTypes = new RealmList<EventType>();
        for (GetPotentialPointsAndEventsCompletedPointsResponse.EventType eventType: response.getEventType()) {
            eventTypes.add(new EventType(eventType));
        }
    }

    public RealmList<EventType> getEventTypes() {
        return eventTypes;
    }

    public void setEventTypes(RealmList<EventType> eventTypes) {
        this.eventTypes = eventTypes;
    }
}
