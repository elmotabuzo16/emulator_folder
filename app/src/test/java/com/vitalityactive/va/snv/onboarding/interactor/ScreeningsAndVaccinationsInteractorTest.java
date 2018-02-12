package com.vitalityactive.va.snv.onboarding.interactor;

import com.vitalityactive.va.snv.onboarding.repository.ScreeningsAndVaccinationsRepositoy;
import com.vitalityactive.va.snv.onboarding.service.GetPotentialPointsAndEventsCompletedPointsServiceClient;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.verify;

/**
 * Created by kerry.e.lawagan on 1/16/2018.
 */

public class ScreeningsAndVaccinationsInteractorTest {

    private ScreeningsAndVaccinationsInteractor interactor;

    @Mock
    private GetPotentialPointsAndEventsCompletedPointsServiceClient serviceClient;
    @Mock
    private ScreeningsAndVaccinationsRepositoy snvRepository;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        interactor = new ScreeningsAndVaccinationsInteractorImpl(serviceClient, snvRepository);
    }

    @Test
    public void should_fetch_data() {
        interactor.fetchData();
        verify(serviceClient).invokeApi();
    }

    @Test
    public void should_get_response_data() {
        interactor.getResponseData();
        verify(snvRepository).retrieveGetPotentialPointsAndEventsCompletedPointsFeedback();
    }
}
