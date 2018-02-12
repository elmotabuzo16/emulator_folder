package com.vitalityactive.va.activerewards.history;

import com.vitalityactive.va.activerewards.baseevents.GoalProgressAndDetailsLoadedEvent;
import com.vitalityactive.va.networking.RequestResult;
import com.vitalityactive.va.networking.model.goalandprogress.GetGoalProgressAndDetailsResponse;

public class GoalsAndProgressLoadedHistoryEvent extends GoalProgressAndDetailsLoadedEvent {
    public GoalsAndProgressLoadedHistoryEvent(RequestResult requestResult, String effectiveFrom, String effectiveTo) {
        super(requestResult, effectiveFrom, effectiveTo);
    }

    public GoalsAndProgressLoadedHistoryEvent(RequestResult requestResult, GetGoalProgressAndDetailsResponse responseBody, String effectiveFrom, String effectiveTo) {
        super(requestResult, responseBody, effectiveFrom, effectiveTo);
    }
}
