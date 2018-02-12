package com.vitalityactive.va.myhealth.landing;


import com.vitalityactive.va.events.EventDispatcher;
import com.vitalityactive.va.myhealth.MyHealthBaseTest;
import com.vitalityactive.va.myhealth.entity.VitalityAge;
import com.vitalityactive.va.myhealth.service.HealthAttributeInformationServiceClient;
import com.vitalityactive.va.networking.RequestResult;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MyHealthPresenterImplTest extends MyHealthBaseTest {

    @Mock
    EventDispatcher mockEventDispatcher;
    @Mock
    HealthAttributeInformationServiceClient healthAttributeInformationServiceClient;
    @Mock
    MyHealthLandingInteractor mockMyHealthInteractor;
    @Mock
    MyHealthLandingPresenter.UserInterface mockUserInterface;
    @Mock
    MyHealthLandingPresenterImpl myHealthPresenterImpl;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        myHealthPresenterImpl = new MyHealthLandingPresenterImpl(mockEventDispatcher, healthAttributeInformationServiceClient, mockMyHealthInteractor);
    }

    @Test
    public void can_process_health_attribute_feedback() {
        when(healthAttributeInformationServiceClient.getHealthAtttributeTipsFeedbackResult()).thenReturn(RequestResult.SUCCESSFUL);
        when(mockMyHealthInteractor.getVitalityAgeHealthAttribute()).thenReturn(createHealthAttributeResponse());
        myHealthPresenterImpl.setUserInterface(mockUserInterface);
        myHealthPresenterImpl.onHealthAttributeFeedbackEventReceived();
        verify(mockUserInterface).loadVitalityAge(any(VitalityAge.class));
    }

    @Test
    public void can_handle_failed_health_attribute_feedback() {
        when(healthAttributeInformationServiceClient.getHealthAtttributeTipsFeedbackResult()).thenReturn(RequestResult.GENERIC_ERROR);
        when(mockMyHealthInteractor.getVitalityAgeHealthAttribute()).thenReturn(createHealthAttributeResponse());
        myHealthPresenterImpl.setUserInterface(mockUserInterface);
        myHealthPresenterImpl.onHealthAttributeFeedbackEventReceived();
        verify(mockUserInterface).loadVitalityAge(any(VitalityAge.class));
    }

}
