package com.vitalityactive.va.activerewards.baseevents;

import com.vitalityactive.va.events.BaseEventWithResponse;
import com.vitalityactive.va.networking.RequestResult;
import com.vitalityactive.va.networking.model.goalandprogress.GetGoalProgressAndDetailsResponse;

public class GoalProgressAndDetailsLoadedEvent extends BaseEventWithResponse<GetGoalProgressAndDetailsResponse> {
    private final String effectiveFrom;
    private final String effectiveTo;

    public GoalProgressAndDetailsLoadedEvent(RequestResult requestResult,
                                             String effectiveFrom,
                                             String effectiveTo) {
        super(requestResult);
        this.effectiveFrom = effectiveFrom;
        this.effectiveTo = effectiveTo;
    }

    public GoalProgressAndDetailsLoadedEvent(RequestResult requestResult,
                                             GetGoalProgressAndDetailsResponse responseBody,
                                             String effectiveFrom,
                                             String effectiveTo) {
        super(requestResult, responseBody);
        this.effectiveFrom = effectiveFrom;
        this.effectiveTo = effectiveTo;
    }

    public GoalProgressAndDetailsLoadedEvent(RequestResult requestResult) {
        this(requestResult, "", "");
    }

    public GoalProgressAndDetailsLoadedEvent(RequestResult requestResult, GetGoalProgressAndDetailsResponse responseBody) {
        this(requestResult, responseBody, "", "");
    }

    public String getEffectiveFrom() {
        return effectiveFrom;
    }

    public String getEffectiveTo() {
        return effectiveTo;
    }
}
