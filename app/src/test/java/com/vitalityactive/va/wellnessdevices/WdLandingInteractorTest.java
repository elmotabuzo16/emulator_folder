package com.vitalityactive.va.wellnessdevices;

import com.vitalityactive.va.BaseTest;
import com.vitalityactive.va.events.EventDispatcher;
import com.vitalityactive.va.wellnessdevices.landing.WellnessDevicesLandingInteractor;
import com.vitalityactive.va.wellnessdevices.landing.service.WellnessDevicesServiceClient;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mock;

import java.io.IOException;

public class WdLandingInteractorTest extends BaseTest {
    private WellnessDevicesLandingInteractor interactor;

    @Mock
    WellnessDevicesServiceClient mockServiceClient;
    @Mock
    EventDispatcher mockEventDispatcher;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        // TODO update
//        mockEventDispatcher = new EventDispatcher();
//        interactor = new WellnessDevicesLandingInteractorImpl(mockServiceClient, mockEventDispatcher);
    }

    @Test
    @Ignore
    public void wd_landing_screen_get_list() throws IOException {
        // TODO implement
    }
}
