package com.vitalityactive.va.vhc.landing;

import com.vitalityactive.va.DeviceSpecificPreferences;
import com.vitalityactive.va.InsurerConfigurationRepository;
import com.vitalityactive.va.Scheduler;
import com.vitalityactive.va.constants.ProductFeatureType;
import com.vitalityactive.va.events.EventDispatcher;
import com.vitalityactive.va.events.EventListener;
import com.vitalityactive.va.networking.RequestFailedEvent;
import com.vitalityactive.va.vhc.HealthAttributeGroup;
import com.vitalityactive.va.vhc.content.VHCHealthAttributeContent;
import com.vitalityactive.va.vhc.dto.HealthAttributeGroupDTO;
import com.vitalityactive.va.vhc.dto.HealthAttributeRepository;
import com.vitalityactive.va.vhc.service.HealthAttributeRequestSuccess;
import com.vitalityactive.va.vhc.service.HealthAttributeServiceClient;
import com.vitalityactive.va.myhealth.service.VitalityAgeServiceClient;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class VHCHealthMeasurementsPresenterTests {
    @Mock
    HealthAttributeServiceClient healthAttributeServiceClient;
    @Mock
    HealthAttributeRepository repository;
    @Mock
    EventDispatcher eventDispatcher;
    @Mock
    Scheduler scheduler;
    @Mock
    VHCHealthMeasurementsPresenter.UserInterface userInterface;
    @Mock
    InsurerConfigurationRepository insurerConfigurationRepository;
    @Mock
    VHCHealthAttributeContent vhcHealthAttributeContent;
    private VHCHealthMeasurementsPresenter presenter;
    @Mock
    VitalityAgeServiceClient vitalityAgeServiceClient;
    @Mock
    DeviceSpecificPreferences deviceSpecificPreferences;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(vhcHealthAttributeContent.getBmiGroupTitle()).thenReturn("Body Mass Index");
        when(vhcHealthAttributeContent.getBloodPressureGroupTitle()).thenReturn("Blood Pressure");
        when(vhcHealthAttributeContent.getGlucoseGroupTitle()).thenReturn("Glucose");
        when(vhcHealthAttributeContent.getWaistCircumferenceGroupTitle()).thenReturn("Waist Circumference");
        when(vhcHealthAttributeContent.getCholesterolGroupTitle()).thenReturn("Cholesterol");
        when(vhcHealthAttributeContent.getHbA1cGroupTitle()).thenReturn("HbA1c");
        presenter = new VHCHealthMeasurementsPresenterImpl(healthAttributeServiceClient, repository, eventDispatcher, scheduler, insurerConfigurationRepository, vitalityAgeServiceClient, deviceSpecificPreferences);
        setupScheduler();
        presenter.setUserInterface(userInterface);
    }

    @SuppressWarnings("unchecked")
    private void givenServiceRequestCompleted() {
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                final EventListener eventListener = invocation.getArgumentAt(1, EventListener.class);
                try {
                    EventListener<HealthAttributeRequestSuccess> listener = (EventListener<HealthAttributeRequestSuccess>) eventListener;
                    listener.onEvent(new HealthAttributeRequestSuccess());
                } catch (ClassCastException ignored) {
                }

                return null;
            }
        }).when(eventDispatcher).addEventListener(any(Class.class), any(EventListener.class));
    }

    @SuppressWarnings("unchecked")
    private void givenServiceRequestFails() {
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                final EventListener eventListener = invocation.getArgumentAt(1, EventListener.class);
                try {
                    EventListener<RequestFailedEvent> listener = (EventListener<RequestFailedEvent>) eventListener;
                    listener.onEvent(new RequestFailedEvent(RequestFailedEvent.Type.CONNECTION_ERROR));
                } catch (ClassCastException ignored) {
                }

                return null;
            }
        }).when(eventDispatcher).addEventListener(any(Class.class), any(EventListener.class));
    }

    @SuppressWarnings("unchecked")
    private void setupScheduler() {
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                Runnable runnable = invocation.getArgumentAt(0, Runnable.class);
                runnable.run();
                return null;
            }
        }).when(scheduler).schedule(any(Runnable.class));
    }

    @Test
    public void when_started_pass_groups_to_user_interface() {
        givenServiceRequestCompleted();

        when(repository.getAllHealthAttributeGroups()).thenReturn(new ArrayList<HealthAttributeGroup>());

        presenter.onUserInterfaceCreated(true);
        presenter.onUserInterfaceAppeared();

        verify(userInterface, times(2)).onHealthAttributesReady(anyListOf(HealthAttributeGroupDTO.class), anyListOf(HealthAttributeGroupDTO.class));
    }

    @Test
    public void when_started_pass_incomplete_to_user_interface() {
        givenServiceRequestCompleted();

        List<HealthAttributeGroupDTO> groups = new ArrayList<>();
        groups.add(new HealthAttributeGroupDTO("Body Mass Index", true, -1, new Date(), ProductFeatureType._VHCBMI, 0, 0, false));
        groups.add(new HealthAttributeGroupDTO("Waist Circumference", true, -1, new Date(), ProductFeatureType._VHCWAISTCIRCUM, 0, 0, false));

        List<Integer> keys = new ArrayList<>();
        keys.add(ProductFeatureType._VHCBMI);
        keys.add(ProductFeatureType._VHCWAISTCIRCUM);

        when(insurerConfigurationRepository.getConfiguredVHCFeatureTypes()).thenReturn(keys);
        when(insurerConfigurationRepository.getCurrentMembershipPeriodStart()).thenReturn(new Date());
        when(insurerConfigurationRepository.getCurrentMembershipPeriodEnd()).thenReturn(new Date());
        when(repository.getHealthAttributeGroups(keys)).thenReturn(groups);

        MockUserInterface userInterface = new MockUserInterface();
        presenter.setUserInterface(userInterface);
        presenter.onUserInterfaceCreated(true);
        presenter.onUserInterfaceAppeared();

        assertTrue(userInterface.called);
        assertTrue("BMI not found in incomplete list", containsGroup(userInterface.incomplete, ProductFeatureType._VHCBMI));
        assertTrue("WAIST_CIRCUMFERENCE not found in incomplete list", containsGroup(userInterface.incomplete, ProductFeatureType._VHCWAISTCIRCUM));
        assertFalse("BMI found in captured list", containsGroup(userInterface.captured, ProductFeatureType._VHCBMI));
        assertFalse("WAIST_CIRCUMFERENCE found in captured list", containsGroup(userInterface.captured, ProductFeatureType._VHCWAISTCIRCUM));
    }

    @Test
    public void when_health_attribute_group_tapped_pending_request_is_cancelled() {
        //request is not completed
        presenter.onCompletedHealthAttributeGroupTapped(new HealthAttributeGroupDTO());

        verify(healthAttributeServiceClient, times(1)).cancelRequest();
    }

    @Test
    public void when_request_not_completed_show_persisted_attributes() {
        //request is not completed
        when(repository.getAllHealthAttributeGroups()).thenReturn(new ArrayList<HealthAttributeGroup>());

        presenter.onUserInterfaceCreated(true);
        presenter.onUserInterfaceAppeared();

        verify(userInterface, times(1)).onHealthAttributesReady(anyListOf(HealthAttributeGroupDTO.class), anyListOf(HealthAttributeGroupDTO.class));
        // but the request is still active (so still loading)
        verify(userInterface, never()).hideLoadingIndicator();
    }

    @Test
    public void when_request_request_error_generic_alert_displayed_and_loading_indicator_hidden() {
        givenServiceRequestFails();

        presenter.onUserInterfaceCreated(true);
        presenter.onUserInterfaceAppeared();

        verify(userInterface, times(1)).showConnectionError();
        verify(userInterface, times(1)).hideLoadingIndicator();
    }

    @Test
    public void launches_vitality_age_on_first_time_vhc_completion(){
        presenter.setReturnfromFormSubmission(true);
        when(deviceSpecificPreferences.hasCurrentUserCompletedVHC()).thenReturn(false);
        presenter.onUserInterfaceAppeared();
        verify(vitalityAgeServiceClient).getVitalityAgeValue();
        verify(deviceSpecificPreferences).setCurrentUserHasCompletedVHC();
    }

    @Test
    public void does_not_launch_vitality_age_if_not_first_time_vhc_completion(){
        presenter.setReturnfromFormSubmission(true);
        when(deviceSpecificPreferences.hasCurrentUserCompletedVHC()).thenReturn(true);
        presenter.onUserInterfaceAppeared();
        verify(vitalityAgeServiceClient,times(0)).getVitalityAgeValue();
    }

    @Test
    public void does_not_launch_vitality_age_if_not_notreturning_from_form_completion(){
        presenter.setReturnfromFormSubmission(false);
        when(deviceSpecificPreferences.hasCurrentUserCompletedVHC()).thenReturn(false);
        presenter.onUserInterfaceAppeared();
        verify(vitalityAgeServiceClient,times(0)).getVitalityAgeValue();
    }

    @Test
    public void when_landing_screen_shown_attributes_only_loaded_first_time() {
        givenServiceRequestCompleted();

        presenter.onUserInterfaceCreated(true);
        presenter.onUserInterfaceAppeared();
        presenter.onUserInterfaceAppeared();
        presenter.onUserInterfaceAppeared();

        verify(healthAttributeServiceClient, times(1)).fetchHealthAttributeEventTypes(any(int[].class));
    }

    private static boolean containsGroup(List<HealthAttributeGroupDTO> groups, int featureType) {
        for (int i = 0; i < groups.size(); i++) {
            if (featureType == groups.get(i).getFeatureTypeKey()) {
                return true;
            }
        }
        return false;
    }

    private class MockUserInterface implements VHCHealthMeasurementsPresenter.UserInterface {
        boolean called = false;
        List<HealthAttributeGroupDTO> incomplete;
        List<HealthAttributeGroupDTO> captured;

        @Override
        public void showLoadingIndicator() {
        }

        @Override
        public void hideLoadingIndicator() {
        }

        @Override
        public void onHealthAttributesReady(List<HealthAttributeGroupDTO> incomplete, List<HealthAttributeGroupDTO> captured) {
            called = true;
            this.incomplete = incomplete;
            this.captured = captured;
        }

        @Override
        public void showHealthAttributeGroupDetails(int featureType) {
        }

        @Override
        public void showGenericError() {
        }

        @Override
        public void showConnectionError() {
        }

        @Override
        public void setUpPullToRefresh() {
        }

        @Override
        public void navigateToVitalityAge() {

        }
    }
}
