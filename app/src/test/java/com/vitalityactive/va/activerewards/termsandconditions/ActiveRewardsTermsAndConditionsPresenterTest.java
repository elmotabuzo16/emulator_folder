package com.vitalityactive.va.activerewards.termsandconditions;

import com.vitalityactive.va.SameThreadScheduler;
import com.vitalityactive.va.activerewards.ActivationErrorType;
import com.vitalityactive.va.activerewards.ActiveRewardsActivator;
import com.vitalityactive.va.events.EventDispatcher;
import com.vitalityactive.va.networking.RequestResult;
import com.vitalityactive.va.termsandconditions.TermsAndConditionsAgreeRequestCompletedEvent;
import com.vitalityactive.va.termsandconditions.TermsAndConditionsConsenter;
import com.vitalityactive.va.termsandconditions.TermsAndConditionsInteractor;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ActiveRewardsTermsAndConditionsPresenterTest {
    private ActiveRewardsTermsAndConditionsPresenterImpl presenter;

    @Mock
    private ActiveRewardsTermsAndConditionsUserInterface mockUserInterface;

    @Mock
    private ActiveRewardsActivator mockActivator;

    @Mock
    private TermsAndConditionsInteractor mockInteractor;

    @Mock
    private TermsAndConditionsConsenter mockConsenter;

    private EventDispatcher eventDispatcher;
    private SameThreadScheduler scheduler;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        eventDispatcher = new EventDispatcher();
        scheduler = new SameThreadScheduler();
        presenter = new ActiveRewardsTermsAndConditionsPresenterImpl(scheduler, mockActivator, mockInteractor, eventDispatcher, mockConsenter);
        presenter.setUserInterface(mockUserInterface);

        presenter.onUserInterfaceAppeared();
    }

    @Test
    public void user_interface_is_not_navigated_after_agree_request_completes() {
        eventDispatcher.dispatchEvent(new TermsAndConditionsAgreeRequestCompletedEvent(RequestResult.SUCCESSFUL));

        verify(mockUserInterface, never()).navigateAfterTermsAndConditionsAccepted();
    }

    @Test
    public void user_interface_is_navigated_after_successful_activation_request() {
        eventDispatcher.dispatchEvent(new ActiveRewardsActivator.ActivationSucceededEvent());

        verify(mockUserInterface).navigateAfterTermsAndConditionsAccepted();
        assertTrue(scheduler.invoked);
    }

    @Test
    public void agree_button_is_enabled_after_activation_request_failed() {
        eventDispatcher.dispatchEvent(new ActiveRewardsActivator.ActivationFailedEvent());

        verify(mockUserInterface).enableAgreeButton();
        assertTrue(scheduler.invoked);
    }

    @Test
    public void user_interface_is_not_navigated_if_back_is_pressed_while_activation_request_is_in_progress() {
        when(mockActivator.isActivateRequestInProgress()).thenReturn(true);

        presenter.onBackPressed();

        verify(mockUserInterface, never()).navigateAfterTermsAndConditionsDeclined();
    }

    @Test
    public void user_interface_is_not_navigated_if_back_is_pressed_while_terms_request_is_in_progress() {
        when(mockConsenter.isRequestInProgress()).thenReturn(true);

        presenter.onBackPressed();

        verify(mockUserInterface, never()).navigateAfterTermsAndConditionsDeclined();
    }

    @Test
    public void generic_activation_error_message_is_shown_when_activation_request_fails_with_a_generic_error() {
        eventDispatcher.dispatchEvent(new ActiveRewardsActivator.ActivationFailedEvent(ActivationErrorType.GENERIC));

        verify(mockUserInterface).showGenericActivationErrorMessage();
        assertTrue(scheduler.invoked);
    }

    @Test
    public void connection_activation_error_message_is_shown_when_activation_request_fails_with_a_connection_error() {
        eventDispatcher.dispatchEvent(new ActiveRewardsActivator.ActivationFailedEvent(ActivationErrorType.CONNECTION));

        verify(mockUserInterface).showConnectionActivationErrorMessage();
        assertTrue(scheduler.invoked);
    }

}
