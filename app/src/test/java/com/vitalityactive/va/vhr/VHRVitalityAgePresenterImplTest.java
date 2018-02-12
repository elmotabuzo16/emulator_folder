package com.vitalityactive.va.vhr;

import com.vitalityactive.va.DeviceSpecificPreferences;
import com.vitalityactive.va.events.EventDispatcher;
import com.vitalityactive.va.myhealth.service.HealthAttributeInformationServiceClient;
import com.vitalityactive.va.myhealth.vitalityage.VitalityAgeInteractor;
import com.vitalityactive.va.vhr.landing.VHRVitalityAgePresenterImpl;
import com.vitalityactive.va.myhealth.shared.VitalityAgeConstants;
import com.vitalityactive.va.myhealth.dto.VitalityAgeRepository;
import com.vitalityactive.va.myhealth.entity.VitalityAge;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

public class VHRVitalityAgePresenterImplTest {

    @Mock
    VitalityAgeInteractor mockVitalityAgeRepository;
    @Mock
    DeviceSpecificPreferences mockDeviceSpecificPreferences;

    VHRVitalityAgePresenterImpl vHRVitalityAgePresenterImpl;
    @Mock
    EventDispatcher mockEventDispatcher;
    @Mock
    HealthAttributeInformationServiceClient mockAttributeInformationServiceClient;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        vHRVitalityAgePresenterImpl = new VHRVitalityAgePresenterImpl(mockEventDispatcher,mockAttributeInformationServiceClient,mockVitalityAgeRepository, mockDeviceSpecificPreferences);
    }

    @Test
    public void does_not_launch_if_not_returning_from_form_completion() {
        when(mockVitalityAgeRepository.getPersistedVitalityAge()).thenReturn(new VitalityAge.Builder()
                .age("43")
                .effectiveType(2)
                .feedbackTitle("Looking Great!")
                .feedbackContent("Your Vitality Age is 5 years older than your actual age. Keep your VHC upto date to maintain the most accurate Vitality age.")
                .build());
        vHRVitalityAgePresenterImpl.setReturnfromFormSubmission(false);
        when(mockDeviceSpecificPreferences.hasCurrentUserSeenVitalityAge()).thenReturn(false);
        assertFalse(vHRVitalityAgePresenterImpl.shouldShowVitalityAge());
    }

    @Test
    public void should_launch_if_returning_from_form_completion() {
        when(mockVitalityAgeRepository.getPersistedVitalityAge()).thenReturn(new VitalityAge.Builder()
                .age("43")
                .effectiveType(VitalityAgeConstants.VA_ABOVE)
                .feedbackTitle("Looking Great!")
                .feedbackContent("Your Vitality Age is 5 years older than your actual age. Keep your VHC upto date to maintain the most accurate Vitality age.")
                .build());
        vHRVitalityAgePresenterImpl.setReturnfromFormSubmission(true);
        when(mockDeviceSpecificPreferences.hasCurrentUserSeenVitalityAge()).thenReturn(false);
        when(mockVitalityAgeRepository.getPersistedVitalityAge()).thenReturn(new VitalityAge.Builder()
                .age("23")
                .effectiveType(VitalityAgeConstants.VA_ABOVE)
                .feedbackTitle("Test Title")
                .feedbackContent("Test content")
                .build());
        assertTrue(vHRVitalityAgePresenterImpl.shouldShowVitalityAge());
    }

    @Test
    public void should_not_launch_if_not_enough_data(){
        when(mockVitalityAgeRepository.getPersistedVitalityAge()).thenReturn(new VitalityAge.Builder()
                .age("43")
                .effectiveType(VitalityAgeConstants.VA_ABOVE)
                .feedbackTitle("Looking Great!")
                .feedbackContent("Your Vitality Age is 5 years older than your actual age. Keep your VHC upto date to maintain the most accurate Vitality age.")
                .build());
        vHRVitalityAgePresenterImpl.setReturnfromFormSubmission(true);
        when(mockDeviceSpecificPreferences.hasCurrentUserSeenVitalityAge()).thenReturn(false);
        when(mockVitalityAgeRepository.getPersistedVitalityAge()).thenReturn(new VitalityAge.Builder()
                .age("23")
                .effectiveType(VitalityAgeConstants.VA_NOT_ENOUGH_DATA)
                .feedbackTitle("Test Title")
                .feedbackContent("Test content")
                .build());
        assertTrue(vHRVitalityAgePresenterImpl.shouldShowVitalityAge());
        when(mockVitalityAgeRepository.getPersistedVitalityAge()).thenReturn(new VitalityAge.Builder()
                .age("23")
                .effectiveType(100)
                .feedbackTitle("Test Title")
                .feedbackContent("Test content")
                .build());
        assertTrue(vHRVitalityAgePresenterImpl.shouldShowVitalityAge());
    }


}
