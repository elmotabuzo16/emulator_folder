package com.vitalityactive.va.snv.onboarding.presenter;

import com.vitalityactive.va.snv.onboarding.service.GetPotentialPointsAndEventsCompletedPointsResponse;

/**
 * Created by kerry.e.lawagan on 11/26/2017.
 */

public class GetPotentialPointsAndEventsCompletedPointSuccessEvent {
    GetPotentialPointsAndEventsCompletedPointsResponse response;

    public GetPotentialPointsAndEventsCompletedPointSuccessEvent(GetPotentialPointsAndEventsCompletedPointsResponse response) {
        this.response = response;
    }
}
