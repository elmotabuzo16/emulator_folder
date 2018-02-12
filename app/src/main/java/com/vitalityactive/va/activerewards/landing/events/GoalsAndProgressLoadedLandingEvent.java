package com.vitalityactive.va.activerewards.landing.events;

import com.vitalityactive.va.activerewards.baseevents.GoalProgressAndDetailsLoadedEvent;
import com.vitalityactive.va.networking.RequestResult;

public class GoalsAndProgressLoadedLandingEvent extends GoalProgressAndDetailsLoadedEvent {
    public GoalsAndProgressLoadedLandingEvent(RequestResult requestResult) {
        super(requestResult);
    }
}
