package com.vitalityactive.va.snv.onboarding.interactor;

import com.vitalityactive.va.snv.dto.GetPotentialPointsAndEventsCompletedPointsDto;

public interface ScreeningsAndVaccinationsInteractor {
    void fetchData();
    GetPotentialPointsAndEventsCompletedPointsDto getResponseData();

    enum GetPotentialPointsAndEventsCompletedPointsResult {
        CONNECTION_ERROR,
        GENERIC_ERROR,
        SUCCESSFUL
    }
}
