package com.vitalityactive.va.snv.onboarding.repository;

import com.vitalityactive.va.persistence.models.screeningsandvaccinations.GetPotentialPointsAndEventsCompletedPointsFeedback;
import com.vitalityactive.va.snv.dto.GetPotentialPointsAndEventsCompletedPointsDto;

import java.util.List;

/**
 * Created by kerry.e.lawagan on 11/24/2017.
 */

public interface ScreeningsAndVaccinationsRepositoy {
    boolean persistGetPotentialPointsAndEventsCompletedPointsResponse(GetPotentialPointsAndEventsCompletedPointsFeedback response);
    List<GetPotentialPointsAndEventsCompletedPointsDto> retrieveGetPotentialPointsAndEventsCompletedPointsFeedback();

}
