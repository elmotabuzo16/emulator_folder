package com.vitalityactive.va.snv.onboarding.interactor;

import com.vitalityactive.va.snv.confirmandsubmit.repository.SNVItemRepository;
import com.vitalityactive.va.snv.dto.GetPotentialPointsAndEventsCompletedPointsDto;
import com.vitalityactive.va.snv.onboarding.repository.ScreeningsAndVaccinationsRepositoy;
import com.vitalityactive.va.snv.onboarding.service.GetPotentialPointsAndEventsCompletedPointsServiceClient;

import java.util.List;


public class ScreeningsAndVaccinationsInteractorImpl implements ScreeningsAndVaccinationsInteractor {
    private GetPotentialPointsAndEventsCompletedPointsServiceClient serviceClient;
    private ScreeningsAndVaccinationsRepositoy repository;

    public  ScreeningsAndVaccinationsInteractorImpl(GetPotentialPointsAndEventsCompletedPointsServiceClient serviceClient,
                                                    ScreeningsAndVaccinationsRepositoy repository) {
        this.serviceClient = serviceClient;
        this.repository = repository;
    }

    public void fetchData() {
        serviceClient.invokeApi();
    }

    public GetPotentialPointsAndEventsCompletedPointsDto getResponseData() {
        List<GetPotentialPointsAndEventsCompletedPointsDto> response = repository.retrieveGetPotentialPointsAndEventsCompletedPointsFeedback();
        if (response==null || response.isEmpty()) {
            return null;
        }
        return response.get(0);
    }
}
