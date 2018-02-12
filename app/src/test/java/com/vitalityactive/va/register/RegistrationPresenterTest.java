package com.vitalityactive.va.register;

import com.vitalityactive.va.SameThreadScheduler;
import com.vitalityactive.va.Scheduler;
import com.vitalityactive.va.events.EventDispatcher;
import com.vitalityactive.va.register.interactor.RegistrationInteractor;
import com.vitalityactive.va.register.view.RegistrationUserInterface;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

public class RegistrationPresenterTest {
    @Mock
    private RegistrationUserInterface mockUserInterface;

    @Mock
    private RegistrationInteractor mockInteractor;

    private RegistrationPresenter presenter;
    private EventDispatcher eventDispatcher;
    private SameThreadScheduler scheduler;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        when(mockInteractor.canRegister()).thenReturn(true);
        when(mockInteractor.getRegistrationRequestResult()).thenReturn(RegistrationInteractor.RegistrationRequestResult.NONE);
        eventDispatcher = new EventDispatcher();
        scheduler = new SameThreadScheduler();
        presenter = new RegistrationPresenterImpl(eventDispatcher, mockInteractor, scheduler);
        presenter.setUserInterface(mockUserInterface);
    }

    @Test
    public void loading_indicator_is_shown_if_registering_when_user_interface_appears() {
        when(mockInteractor.isRegistering()).thenReturn(true);

        presenter.onUserInterfaceAppeared();

        verify(mockUserInterface).showLoadingIndicator();
    }

    @Test
    public void loading_indicator_is_hidden_if_not_registering_when_user_interface_appears() {
        when(mockInteractor.isRegistering()).thenReturn(false);

        showUserInterface();
    }

    @Test
    public void loading_indicator_is_shown_while_registering() {
        presenter.onUserTriesToRegister();

        verify(mockUserInterface).showLoadingIndicator();
    }

    @Test
    public void loading_indicator_is_not_shown_if_trying_to_register_while_registering() {
        when(mockInteractor.isRegistering()).thenReturn(true);

        presenter.onUserTriesToRegister();

        verifyNoMoreInteractions(mockUserInterface);
    }

    @Test
    public void loading_indicator_is_not_shown_if_trying_to_register_when_registration_credentials_are_incorrect() {
        when(mockInteractor.canRegister()).thenReturn(false);

        presenter.onUserTriesToRegister();

        verifyNoMoreInteractions(mockUserInterface);
    }

    @Test
    public void loading_indicator_is_hidden_when_error_alert_is_dismissed()
    {
        presenter.onUserDismissesErrorMessage();

        verify(mockUserInterface).hideLoadingIndicator();
    }

    @Test
    public void user_interface_is_navigated_if_registration_completed_before_user_interface_appeared() {
        when(mockInteractor.getRegistrationRequestResult()).thenReturn(RegistrationInteractor.RegistrationRequestResult.SUCCESSFUL);

        presenter.onUserInterfaceAppeared();

        verify(mockUserInterface).navigateAfterSuccessfulRegistration();
    }

    private void showUserInterface() {
        presenter.onUserInterfaceAppeared();
        verify(mockUserInterface).hideLoadingIndicator();
    }

    @Test
    public void connection_error_alert_is_shown_when_registration_request_finishes_with_an_error() {
        presenter.onUserInterfaceAppeared();

        eventDispatcher.dispatchEvent(new RegistrationInteractor.RegistrationFailedEvent(RegistrationInteractor.RegistrationRequestResult.CONNECTION_ERROR));

        verify(mockUserInterface).showConnectionErrorMessage();
        assertTrue(scheduler.invoked);
    }

    @Test
    public void generic_error_alert_is_shown_when_registration_finishes_with_an_error() {
        presenter.onUserInterfaceAppeared();

        eventDispatcher.dispatchEvent(new RegistrationInteractor.RegistrationFailedEvent(RegistrationInteractor.RegistrationRequestResult.GENERIC_ERROR));

        verify(mockUserInterface).showGenericErrorMessage();
        assertTrue(scheduler.invoked);
    }

    @Test
    public void invalid_email_or_insurer_code_error_alert_is_shown_when_registration_finishes_with_an_error() {
        presenter.onUserInterfaceAppeared();

        eventDispatcher.dispatchEvent(new RegistrationInteractor.RegistrationFailedEvent(RegistrationInteractor.RegistrationRequestResult.INVALID_EMAIL_INSURER_CODE_ERROR));

        verify(mockUserInterface).showInvalidEmailOrInsurerCodeErrorMessage();
        assertTrue(scheduler.invoked);
    }

    @Test
    public void already_registered_error_alert_is_shown_when_registration_finishes_with_an_error() {
        presenter.onUserInterfaceAppeared();
        eventDispatcher.dispatchEvent(new RegistrationInteractor.RegistrationFailedEvent(RegistrationInteractor.RegistrationRequestResult.ALREADY_REGISTERED_ERROR));

        verify(mockUserInterface).showAlreadyRegisteredErrorMessage();
        assertTrue(scheduler.invoked);
    }

    @Test
    public void user_interface_is_navigated_when_registration_request_completes_successfully() {
        presenter.onUserInterfaceAppeared();

        eventDispatcher.dispatchEvent(new RegistrationInteractor.RegistrationSucceededEvent());

        verify(mockUserInterface).navigateAfterSuccessfulRegistration();
        assertTrue(scheduler.invoked);
    }

    @Test
    public void the_user_interface_does_not_react_if_registration_completes_after_the_user_interface_disappeared() {
        showUserInterface();
        presenter.onUserInterfaceDisappeared(false);

        eventDispatcher.dispatchEvent(new RegistrationInteractor.RegistrationSucceededEvent());
        eventDispatcher.dispatchEvent(new RegistrationInteractor.RegistrationFailedEvent(RegistrationInteractor.RegistrationRequestResult.GENERIC_ERROR));
        eventDispatcher.dispatchEvent(new RegistrationInteractor.RegistrationFailedEvent(RegistrationInteractor.RegistrationRequestResult.CONNECTION_ERROR));

        verifyNoMoreInteractions(mockUserInterface);
    }

    @Test
    public void connection_error_alert_is_shown_if_registration_failed_in_the_background() {
        RegistrationInteractor.RegistrationRequestResult requestResult = RegistrationInteractor.RegistrationRequestResult.CONNECTION_ERROR;
        showUserInterfaceAfterRequestFailedInTheBackground(requestResult);

        verify(mockUserInterface).showConnectionErrorMessage();
    }

    @Test
    public void generic_error_alert_is_shown_if_registration_failed_in_the_background() {
        showUserInterfaceAfterRequestFailedInTheBackground(RegistrationInteractor.RegistrationRequestResult.GENERIC_ERROR);

        verify(mockUserInterface).showGenericErrorMessage();
    }

    @Test
    public void invalid_email_or_insurer_code_error_alert_is_shown_if_registration_failed_in_the_background() {
        showUserInterfaceAfterRequestFailedInTheBackground(RegistrationInteractor.RegistrationRequestResult.INVALID_EMAIL_INSURER_CODE_ERROR);

        verify(mockUserInterface).showInvalidEmailOrInsurerCodeErrorMessage();
    }

    @Test
    public void already_registered_error_alert_is_shown_if_registration_failed_in_the_background() {
        showUserInterfaceAfterRequestFailedInTheBackground(RegistrationInteractor.RegistrationRequestResult.ALREADY_REGISTERED_ERROR);

        verify(mockUserInterface).showAlreadyRegisteredErrorMessage();
    }

    @Test
    public void error_alert_is_not_shown_again_if_registration_failed_in_the_background_and_it_has_already_been_shown_when_the_ui_was_recreated() {
        showUserInterfaceAfterRequestFailedInTheBackground(RegistrationInteractor.RegistrationRequestResult.ALREADY_REGISTERED_ERROR);

        presenter.onUserInterfaceAppeared();

        assertErrorAlertIsShownOnlyOnce();
    }

    @Test
    public void error_alert_is_not_shown_again_if_registration_failed_in_the_foreground_and_the_ui_appears_again()
    {
        presenter.onUserInterfaceAppeared();

        when(mockInteractor.getRegistrationRequestResult()).thenReturn(RegistrationInteractor.RegistrationRequestResult.ALREADY_REGISTERED_ERROR);

        eventDispatcher.dispatchEvent(new RegistrationInteractor.RegistrationFailedEvent(RegistrationInteractor.RegistrationRequestResult.ALREADY_REGISTERED_ERROR));

        presenter.onUserInterfaceAppeared();

        assertErrorAlertIsShownOnlyOnce();
    }

    @Test
    public void error_alert_is_shown_again_after_another_registration_attempt_completes_in_the_background()
    {
        showUserInterfaceAfterRequestFailedInTheBackground(RegistrationInteractor.RegistrationRequestResult.ALREADY_REGISTERED_ERROR);

        presenter.onUserTriesToRegister();

        presenter.onUserInterfaceAppeared();

        assertTwoErrorAlertsWereShown();
    }

    @Test
    public void error_alert_is_shown_again_after_another_registration_attempt_completes_in_the_foreground() {
        showUserInterfaceAfterRequestFailedInTheBackground(RegistrationInteractor.RegistrationRequestResult.ALREADY_REGISTERED_ERROR);

        presenter.onUserTriesToRegister();

        eventDispatcher.dispatchEvent(new RegistrationInteractor.RegistrationFailedEvent(RegistrationInteractor.RegistrationRequestResult.ALREADY_REGISTERED_ERROR));

        assertTwoErrorAlertsWereShown();
    }

    @Test
    public void error_alert_is_not_shown_if_registering_when_user_interface_appears()
    {
        when(mockInteractor.isRegistering()).thenReturn(true);

        showUserInterfaceAfterRequestFailedInTheBackground(RegistrationInteractor.RegistrationRequestResult.ALREADY_REGISTERED_ERROR);

        verify(mockUserInterface, never()).showAlreadyRegisteredErrorMessage();
    }

    @Test
    public void registration_is_attempted_when_user_attempts_to_register() {
        presenter.onUserTriesToRegister();

        verify(mockInteractor).register();
    }

    @Test
    public void user_interface_is_not_invoked_if_it_has_disappeared_before_scheduling_completes() {
        Scheduler mockScheduler = mock(Scheduler.class);
        presenter = new RegistrationPresenterImpl(eventDispatcher, mockInteractor, mockScheduler);
        presenter.setUserInterface(mockUserInterface);

        showUserInterface();

        eventDispatcher.dispatchEvent(new RegistrationInteractor.RegistrationSucceededEvent());
        eventDispatcher.dispatchEvent(new RegistrationInteractor.RegistrationFailedEvent(RegistrationInteractor.RegistrationRequestResult.GENERIC_ERROR));
        eventDispatcher.dispatchEvent(new RegistrationInteractor.RegistrationFailedEvent(RegistrationInteractor.RegistrationRequestResult.CONNECTION_ERROR));

        presenter.onUserInterfaceDisappeared(false);

        verify(mockScheduler).cancel();

        verifyNoMoreInteractions(mockUserInterface);
    }

    private void showUserInterfaceAfterRequestFailedInTheBackground(RegistrationInteractor.RegistrationRequestResult requestResult) {
        when(mockInteractor.getRegistrationRequestResult()).thenReturn(requestResult);

        showUserInterface();
    }

    private void assertErrorAlertIsShownOnlyOnce() {
        verify(mockUserInterface).showAlreadyRegisteredErrorMessage();
    }

    private void assertTwoErrorAlertsWereShown() {
        verify(mockUserInterface, times(2)).showAlreadyRegisteredErrorMessage();
    }
}
