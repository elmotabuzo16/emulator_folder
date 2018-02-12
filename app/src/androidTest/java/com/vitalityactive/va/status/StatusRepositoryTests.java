package com.vitalityactive.va.status;

import android.support.test.runner.AndroidJUnit4;

import com.vitalityactive.va.RepositoryTestBase;
import com.vitalityactive.va.constants.StatusType;
import com.vitalityactive.va.vitalitystatus.repository.VitalityStatusRepository;
import com.vitalityactive.va.vitalitystatus.repository.VitalityStatusRepositoryImpl;
import com.vitalityactive.va.home.service.HomeScreenCardStatusResponse;
import com.vitalityactive.va.testutilities.ModelVerifier;
import com.vitalityactive.va.testutilities.annotations.RepositoryTests;
import com.vitalityactive.va.vitalitystatus.VitalityStatus;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
@RepositoryTests
public class StatusRepositoryTests extends RepositoryTestBase {
    private VitalityStatusRepository repository;

    public void setUp() throws IOException {
        super.setUp();
        repository = new VitalityStatusRepositoryImpl(dataStore);
    }

    @Test
    public void home_screen_response_is_persisted_correctly() throws Exception {
        repository.persistVitalityStatusResponse(getResponse("Homescreen_successful.json"));

        new ModelVerifier<>(VitalityStatus.class, getRealm(), 1, new ModelVerifier.Verifier<VitalityStatus>() {
            @Override
            public void verifyModel(VitalityStatus vitalityStatus, int index) throws Exception {
                assertEquals(270, vitalityStatus.getTotalPoints());
                assertEquals(StatusType._BLUE, vitalityStatus.getCurrentStatusKey());
                assertEquals(5, vitalityStatus.getCurrentStatusKey());
                assertEquals(10, vitalityStatus.getPointsToMaintainStatus());
                assertEquals(StatusType._BRONZE, vitalityStatus.getNextStatusKey());
            }
        });
    }

    @Test
    public void home_screen_persists_overwrites_old_items() throws IOException {
        // after one model is in the repository
        repository.persistVitalityStatusResponse(getResponse("Homescreen_successful.json"));
        assertEquals(270, repository.getVitalityStatus().getTotalPoints());

        // when updated with no models, then there are no items in the repository (old items are removed/replaced)
        repository.persistVitalityStatusResponse(getResponse("Homescreen_successful_no_sections.json"));
        assertEquals(0, repository.getVitalityStatus().getTotalPoints());
    }

    private HomeScreenCardStatusResponse getResponse(String path) throws IOException {
        return getResponse(HomeScreenCardStatusResponse.class, path);
    }
}
