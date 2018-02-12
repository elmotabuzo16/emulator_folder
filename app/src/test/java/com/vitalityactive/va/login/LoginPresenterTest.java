package com.vitalityactive.va.login;

import com.vitalityactive.va.MainThreadScheduler;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

public class LoginPresenterTest extends LoginPresenterTestBase {

    @Test
    public void it_reports_logging_in_if_logging_in() {
        when(mockLoginInteractor.isBusyLoggingIn()).thenReturn(true);
        assertTrue(loginPresenter.getViewModel().shouldShowLoadingIndicator());
    }

    @Test
    public void it_reports_not_logging_in_if_not_logging_in() {
        when(mockLoginInteractor.isBusyLoggingIn()).thenReturn(false);
        assertFalse(loginPresenter.getViewModel().shouldShowLoadingIndicator());
    }

    @Test
    public void it_shows_loading_indicator_when_login_starts() {
        loginPresenter.onUserTriesToLogIn();
        verify(mockUserInterface).showLoadingIndicator();
    }

    @Test
    public void connection_error_alert_is_shown_when_login_request_finishes_with_an_error() {
        loginPresenter.onUserInterfaceCreated(true);
        loginPresenter.onUserInterfaceAppeared();

        eventDispatcher.dispatchEvent(new AuthenticationFailedEvent(LoginInteractor.LoginRequestResult.CONNECTION_ERROR));

        verify(mockUserInterface).showConnectionErrorMessage();
        assertTrue(scheduler.invoked);
    }

    @Test
    public void generic_error_alert_is_shown_when_login_request_finishes_with_an_error() {
        loginPresenter.onUserInterfaceCreated(true);
        loginPresenter.onUserInterfaceAppeared();

        eventDispatcher.dispatchEvent(new AuthenticationFailedEvent(LoginInteractor.LoginRequestResult.GENERIC_ERROR));

        verify(mockUserInterface).showGenericLoginErrorMessage();
        assertTrue(scheduler.invoked);
    }

    @Test
    public void invalid_credentials_error_alert_is_shown_when_login_request_finishes_with_an_error() {
        loginPresenter.onUserInterfaceCreated(true);
        loginPresenter.onUserInterfaceAppeared();

        eventDispatcher.dispatchEvent(new AuthenticationFailedEvent(LoginInteractor.LoginRequestResult.INCORRECT_USERNAME_OR_PASSWORD));

        verify(mockUserInterface).showInvalidCredentialsLoginErrorMessage();
        assertTrue(scheduler.invoked);
    }

    @Test
    public void locked_account_error_alert_is_shown_when_login_request_finishes_with_an_error() {
        loginPresenter.onUserInterfaceCreated(true);
        loginPresenter.onUserInterfaceAppeared();

        eventDispatcher.dispatchEvent(new AuthenticationFailedEvent(LoginInteractor.LoginRequestResult.LOCKED_ACCOUNT));

        verify(mockUserInterface).showLockedAccountLoginErrorMessage();
        assertTrue(scheduler.invoked);
    }

    @Test
    public void it_hides_loading_indicator_and_shows_an_error_message_when_login_fails_because_of_invalid_credentials() {
        loginPresenter.onUserInterfaceCreated(true);
        loginPresenter.onUserInterfaceAppeared();

        eventDispatcher.dispatchEvent(new AuthenticationFailedEvent(LoginInteractor.LoginRequestResult.INCORRECT_USERNAME_OR_PASSWORD));

        verify(mockUserInterface, times(2)).hideLoadingIndicator();
        verify(mockUserInterface).showInvalidCredentialsLoginErrorMessage();
        assertTrue(scheduler.invoked);
    }

    @Test
    public void it_hides_loading_indicator_and_shows_an_error_message_when_login_fails_because_of_a_generic_error() {
        loginPresenter.onUserInterfaceCreated(true);
        loginPresenter.onUserInterfaceAppeared();
        eventDispatcher.dispatchEvent(new AuthenticationFailedEvent(LoginInteractor.LoginRequestResult.GENERIC_ERROR));

        verify(mockUserInterface, times(2)).hideLoadingIndicator();
        verify(mockUserInterface).showGenericLoginErrorMessage();
        assertTrue(scheduler.invoked);
    }

    @Test
    public void it_shows_loading_indicator_if_logging_in_when_user_interface_appears() {
        when(mockLoginInteractor.isBusyLoggingIn()).thenReturn(true);
        loginPresenter.onUserInterfaceAppeared();
        verify(mockUserInterface).showLoadingIndicator();
    }

    @Test
    public void it_hides_loading_indicator_if_not_logging_in_when_user_interface_appears() {
        loginPresenter.onUserInterfaceAppeared();
        verify(mockUserInterface).hideLoadingIndicator();
    }

    @Test
    public void it_navigates_the_user_interface_if_login_succeeded_in_the_background() {
        when(mockLoginInteractor.getLoginRequestResult()).thenReturn(LoginInteractor.LoginRequestResult.SUCCESSFUL);

        loginPresenter.onUserInterfaceAppeared();

        verify(mockUserInterface).navigateAfterSuccessfulLogin();
    }

    @Test
    public void user_interface_is_not_invoked_if_it_has_disappeared_before_scheduling_completes() {
        MainThreadScheduler mockScheduler = mock(MainThreadScheduler.class);
        loginPresenter = new LoginPresenterImpl(eventDispatcher, mockLoginInteractor, mockScheduler, mockDeviceSpecificPreferences);
        loginPresenter.setUserInterface(mockUserInterface);

        showUserInterface();

        eventDispatcher.dispatchEvent(new AuthenticationSucceededEvent());
        eventDispatcher.dispatchEvent(new AuthenticationFailedEvent(LoginInteractor.LoginRequestResult.GENERIC_ERROR));
        eventDispatcher.dispatchEvent(new AuthenticationFailedEvent(LoginInteractor.LoginRequestResult.CONNECTION_ERROR));

        loginPresenter.onUserInterfaceDisappeared(false);

        verify(mockScheduler).cancel();

        verifyNoMoreInteractions(mockUserInterface);
    }

    @Test
    public void connection_error_alert_is_shown_if_login_failed_in_the_background() {
        showUserInterfaceAfterRequestFailedInTheBackground(LoginInteractor.LoginRequestResult.CONNECTION_ERROR);

        verify(mockUserInterface).showConnectionErrorMessage();
    }

    @Test
    public void generic_error_alert_is_shown_if_login_failed_in_the_background() {
        showUserInterfaceAfterRequestFailedInTheBackground(LoginInteractor.LoginRequestResult.GENERIC_ERROR);

        verify(mockUserInterface).showGenericLoginErrorMessage();
    }

    @Test
    public void invalid_credentials_error_alert_is_shown_if_login_failed_in_the_background() {
        showUserInterfaceAfterRequestFailedInTheBackground(LoginInteractor.LoginRequestResult.INCORRECT_USERNAME_OR_PASSWORD);

        verify(mockUserInterface).showInvalidCredentialsLoginErrorMessage();
    }

    @Test
    public void forgot_password_is_shown_after_incorrectly_entering_password_twice() {
        loginPresenter.onUserInterfaceCreated(true);
        loginPresenter.onUserInterfaceAppeared();

        eventDispatcher.dispatchEvent(new AuthenticationFailedEvent(LoginInteractor.LoginRequestResult.INCORRECT_USERNAME_OR_PASSWORD));
        eventDispatcher.dispatchEvent(new AuthenticationFailedEvent(LoginInteractor.LoginRequestResult.INCORRECT_USERNAME_OR_PASSWORD));

        verify(mockUserInterface).showInvalidCredentialsLoginErrorMessageWithForgotPassword();
        assertTrue(scheduler.invoked);
    }

    @Test
    public void forgot_password_is_shown_after_login_failed_in_the_background_because_of_the_second_incorrect_password() {
        loginPresenter.onUserInterfaceCreated(true);
        loginPresenter.onUserInterfaceAppeared();
        loginPresenter.onUserInterfaceDisappeared(false);

        loginPresenter.onUserTriesToLogIn();
        eventDispatcher.dispatchEvent(new AuthenticationFailedEvent(LoginInteractor.LoginRequestResult.INCORRECT_USERNAME_OR_PASSWORD));

        loginPresenter.onUserTriesToLogIn();
        eventDispatcher.dispatchEvent(new AuthenticationFailedEvent(LoginInteractor.LoginRequestResult.INCORRECT_USERNAME_OR_PASSWORD));

        when(mockLoginInteractor.getLoginRequestResult()).thenReturn(LoginInteractor.LoginRequestResult.INCORRECT_USERNAME_OR_PASSWORD);

        loginPresenter.onUserInterfaceAppeared();
        verify(mockUserInterface).showInvalidCredentialsLoginErrorMessageWithForgotPassword();
        assertTrue(scheduler.invoked);
    }

    @Test
    public void error_alert_is_not_shown_again_if_login_failed_in_the_background_and_it_has_already_been_shown_when_the_ui_was_recreated() {
        showUserInterfaceAfterRequestFailedInTheBackground(LoginInteractor.LoginRequestResult.GENERIC_ERROR);

        loginPresenter.onUserInterfaceAppeared();

        verify(mockUserInterface).showGenericLoginErrorMessage();
    }

    @Test
    public void error_alert_is_shown_again_after_another_login_attempt_completes_in_the_foreground() {
        loginPresenter.onUserInterfaceCreated(true);
        showUserInterfaceAfterRequestFailedInTheBackground(LoginInteractor.LoginRequestResult.GENERIC_ERROR);

        loginPresenter.onUserTriesToLogIn();

        eventDispatcher.dispatchEvent(new AuthenticationFailedEvent(LoginInteractor.LoginRequestResult.GENERIC_ERROR));

        verify(mockUserInterface, times(2)).showGenericLoginErrorMessage();
    }

    @Test
    public void error_alert_is_shown_again_after_another_login_attempt_after_the_user_interface_was_destroyed() {
        loginPresenter.onUserInterfaceCreated(true);
        showUserInterfaceAfterRequestFailedInTheBackground(LoginInteractor.LoginRequestResult.GENERIC_ERROR);

        loginPresenter.onUserInterfaceDestroyed();

        loginPresenter.onUserInterfaceCreated(true);

        loginPresenter.onUserTriesToLogIn();

        eventDispatcher.dispatchEvent(new AuthenticationFailedEvent(LoginInteractor.LoginRequestResult.GENERIC_ERROR));

        verify(mockUserInterface, times(2)).showGenericLoginErrorMessage();
    }

    private void showUserInterfaceAfterRequestFailedInTheBackground(LoginInteractor.LoginRequestResult requestResult) {
        when(mockLoginInteractor.getLoginRequestResult()).thenReturn(requestResult);

        showUserInterface();
    }

    private void showUserInterface() {
        loginPresenter.onUserInterfaceAppeared();
        verify(mockUserInterface).hideLoadingIndicator();
    }
}
