package com.vitalityactive.va.snv.onboarding.presenter;

import com.vitalityactive.va.snv.onboarding.interactor.ScreeningsAndVaccinationsInteractor;

/**
 * Created by kerry.e.lawagan on 11/26/2017.
 */

public class GetPotentialPointsAndEventsCompletedPointFailedEvent {
    ScreeningsAndVaccinationsInteractor.GetPotentialPointsAndEventsCompletedPointsResult result;

    public GetPotentialPointsAndEventsCompletedPointFailedEvent(ScreeningsAndVaccinationsInteractor.GetPotentialPointsAndEventsCompletedPointsResult result) {
        this.result = result;
    }

}
