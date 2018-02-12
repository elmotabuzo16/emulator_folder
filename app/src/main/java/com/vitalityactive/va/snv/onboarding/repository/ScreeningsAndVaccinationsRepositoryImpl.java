package com.vitalityactive.va.snv.onboarding.repository;

import com.vitalityactive.va.networking.parsing.Persister;
import com.vitalityactive.va.persistence.DataStore;
import com.vitalityactive.va.persistence.models.screeningsandvaccinations.GetPotentialPointsAndEventsCompletedPointsFeedback;
import com.vitalityactive.va.snv.dto.GetPotentialPointsAndEventsCompletedPointsDto;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kerry.e.lawagan on 11/24/2017.
 */

public class ScreeningsAndVaccinationsRepositoryImpl implements ScreeningsAndVaccinationsRepositoy {
    private final DataStore dataStore;
    private final Persister persister;

    public ScreeningsAndVaccinationsRepositoryImpl(DataStore dataStore) {
        this.dataStore = dataStore;
        persister = new Persister(dataStore);
    }

    @Override
    public boolean persistGetPotentialPointsAndEventsCompletedPointsResponse(GetPotentialPointsAndEventsCompletedPointsFeedback response) {
        return persister.addModel(response);
    }

    @Override
    public List<GetPotentialPointsAndEventsCompletedPointsDto> retrieveGetPotentialPointsAndEventsCompletedPointsFeedback() {
        return dataStore.getModels(GetPotentialPointsAndEventsCompletedPointsFeedback.class,
                new DataStore.ModelListMapper<GetPotentialPointsAndEventsCompletedPointsFeedback, GetPotentialPointsAndEventsCompletedPointsDto>() {
                    @Override
                    public List<GetPotentialPointsAndEventsCompletedPointsDto> mapModels(List<GetPotentialPointsAndEventsCompletedPointsFeedback> models) {
                        List<GetPotentialPointsAndEventsCompletedPointsDto> mappedModels = new ArrayList<>();
                        for (GetPotentialPointsAndEventsCompletedPointsFeedback model : models) {
                            mappedModels.add(new GetPotentialPointsAndEventsCompletedPointsDto(model));
                        }
                        return mappedModels;
                    }
                });
    }
}
