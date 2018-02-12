package com.vitalityactive.va.snv.onboarding.repository;

import android.support.test.filters.SmallTest;
import android.support.test.runner.AndroidJUnit4;

import com.vitalityactive.va.RepositoryTestBase;
import com.vitalityactive.va.persistence.models.screeningsandvaccinations.GetPotentialPointsAndEventsCompletedPointsFeedback;
import com.vitalityactive.va.snv.dto.GetPotentialPointsAndEventsCompletedPointsDto;
import com.vitalityactive.va.snv.onboarding.service.GetPotentialPointsAndEventsCompletedPointsResponse;
import com.vitalityactive.va.testutilities.VitalityActive;
import com.vitalityactive.va.testutilities.annotations.RepositoryTests;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.List;

/**
 * Created by kerry.e.lawagan on 11/30/2017.
 */
@RunWith(AndroidJUnit4.class)
@SmallTest
@RepositoryTests
public class ScreeningsAndVaccinationsRepositoyTest extends RepositoryTestBase {
    private ScreeningsAndVaccinationsRepositoy repository;

    @Override
    public void setUp() throws IOException {
        super.setUp();
        repository = new ScreeningsAndVaccinationsRepositoryImpl(dataStore);
    }

    @Test
    public void repository_test() throws IOException {
        persistSNVLandingResponse("snv/get_potential_points_and_events_completed_points.json");
        assertModelCount(GetPotentialPointsAndEventsCompletedPointsFeedback.class, 1);

        List<GetPotentialPointsAndEventsCompletedPointsDto> persistedResponse = repository.retrieveGetPotentialPointsAndEventsCompletedPointsFeedback();
        Assert.assertEquals(persistedResponse.size(), 1);
    }

    private void persistSNVLandingResponse(String path) throws java.io.IOException {
        GetPotentialPointsAndEventsCompletedPointsResponse response = getResponse(GetPotentialPointsAndEventsCompletedPointsResponse.class, path);
        GetPotentialPointsAndEventsCompletedPointsFeedback responseFeedack = new GetPotentialPointsAndEventsCompletedPointsFeedback(response);
        repository.persistGetPotentialPointsAndEventsCompletedPointsResponse(responseFeedack);
    }
}
