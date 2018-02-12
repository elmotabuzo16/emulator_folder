package com.vitalityactive.va.myhealth.vitalityage;


import com.vitalityactive.va.DeviceSpecificPreferences;
import com.vitalityactive.va.myhealth.MyHealthBaseTest;
import com.vitalityactive.va.myhealth.entity.VitalityAge;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class VitalityAgePresenterImplTest extends MyHealthBaseTest {

    @Mock
    VitalityAgePresenter.UserInterface userInterface;
    @Mock
    VitalityAgeInteractor mockVitalityAgeInteractor;
    @Mock
    VitalityAgePresenter mockVitalityAgePresenter;
    @Mock
    DeviceSpecificPreferences deviceSpecificPreferences;

    VitalityAgePresenterImpl vitalityAgePresenterImpl = null;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        VitalityAge vitalityAge = mock(VitalityAge.class);
        when(mockVitalityAgePresenter.getPersistedVitalityAge()).thenReturn(vitalityAge);
        when(mockVitalityAgeInteractor.getPersistedVitalityAge()).thenReturn(vitalityAge);
        vitalityAgePresenterImpl = new VitalityAgePresenterImpl(mockVitalityAgeInteractor, deviceSpecificPreferences);
        vitalityAgePresenterImpl.setUserInterface(userInterface);
    }

    @Test
    public void should_show_vitality_age() {
        vitalityAgePresenterImpl.loadVitalityAge();
        verify(mockVitalityAgeInteractor, times(1)).getVitalityAgeHealthAttribute();
    }

    @Test
    public void set_has_shown_vitality_age() {
        vitalityAgePresenterImpl.setHasShownVitalityAge();
        verify(deviceSpecificPreferences, times(1)).setCurrentUserHasSeenVitalityAge();
    }

    @Test
    public void vitality_age_presenter_in_VHR__mode() {
        when(vitalityAgePresenterImpl.getPersistedVitalityAge()).thenReturn(new VitalityAge.Builder()
                .age("23")
                .effectiveType(1)
                .feedbackTitle("Test Title")
                .feedbackContent("Test content")
                .build());
        when(mockVitalityAgeInteractor.getVitalityAgeHealthAttribute()).thenReturn(createHealthAttributeResponse());
        vitalityAgePresenterImpl.setVitalityAgeDisplayMode(VitalityAgeActivity.VHR_MODE);
        vitalityAgePresenterImpl.loadVitalityAge();
        verify(userInterface, times(1)).hideLoadingIndicator();
        verify(userInterface, times(1)).initialize(any(VitalityAge.class));
    }

    @Test
    public void vitality_age_VHC_done_VHR_pending() {
        when(vitalityAgePresenterImpl.getPersistedVitalityAge()).thenReturn(new VitalityAge.Builder()
                .age("23")
                .effectiveType(1)
                .feedbackTitle("Test Title")
                .feedbackContent("Test content")
                .build());
        vitalityAgePresenterImpl.setVitalityAgeDisplayMode(VitalityAgeActivity.VHC__DONE_VHR_PENDING_MODE);
        vitalityAgePresenterImpl.onUserInterfaceAppeared();
        verify(userInterface, times(1)).initialize(any(VitalityAge.class));
    }
}
