package com.vitalityactive.va.register;

import com.vitalityactive.va.register.presenter.ConfirmationPasswordPresenter;
import com.vitalityactive.va.register.view.CredentialUserInterface;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class ConfirmationPasswordPresenterTest extends CredentialPresenterTestBase
{
    @Before
    public void setUp()
    {
        MockitoAnnotations.initMocks(this);
        createRegistrationInteractor();
    }

    @Test
    public void itDoesNotTryToEnableRegistrationIfOtherFieldsAreInvalid()
    {
        registrationInteractor.setUsername(INVALID_USERNAME_NO_AT);
        registrationInteractor.setPassword(VALID_PASSWORD);
        registrationInteractor.setInsurerCode(VALID_INSURER_CODE);
        presenter = new ConfirmationPasswordPresenter.Builder()
                .setRegistrationInteractor(registrationInteractor)
                .setRegistrationUserInterface(mockUserInterface)
                .build();

        presenter.onValueChanged(VALID_PASSWORD);
        verify(mockUserInterface, never()).allowRegistration();
    }

    @Test
    public void validationErrorMessageIsNotShownWhenCredentialIsOnlyChangedToAnIncorrectValue()
    {
        registrationInteractor.setUsername(VALID_USERNAME);
        registrationInteractor.setPassword(VALID_PASSWORD);
        registrationInteractor.setInsurerCode(VALID_INSURER_CODE);
        presenter = new ConfirmationPasswordPresenter.Builder()
                .setRegistrationInteractor(registrationInteractor)
                .setRegistrationUserInterface(mockUserInterface)
                .build();

        presenter.onValueChanged(VALID_PASSWORD);
        assertFalse(presenter.shouldShowValidationErrorMessage());

        presenter.onValueChanged(PASSWORD_NO_NUMBERS);
        assertFalse(presenter.shouldShowValidationErrorMessage());

        verify(mockUserInterface, times(1)).allowRegistration();
        verify(mockUserInterface, times(1)).disallowRegistration();
    }

    @Test
    public void validationErrorMessageIsShownWhenCredentialIsIncorrectlyEntered()
    {
        registrationInteractor.setUsername(VALID_USERNAME);
        registrationInteractor.setInsurerCode(VALID_INSURER_CODE);
        presenter = new ConfirmationPasswordPresenter.Builder()
                .setRegistrationInteractor(registrationInteractor)
                .setRegistrationUserInterface(mockUserInterface)
                .build();

        presenter.onValueEntered();
        assertFalse(presenter.shouldShowValidationErrorMessage());

        registrationInteractor.setPassword(VALID_PASSWORD);

        assertTrue(presenter.shouldShowValidationErrorMessage());

        presenter.onValueChanged(VALID_PASSWORD);
        assertFalse(presenter.shouldShowValidationErrorMessage());

        presenter.onValueChanged(PASSWORD_ALL_LOWERCASE);
        assertTrue(presenter.shouldShowValidationErrorMessage());

        presenter.onValueChanged(PASSWORD_EMPTY);
        assertTrue(presenter.shouldShowValidationErrorMessage());
    }

    @Test
    public void itUpdatesTheValidationErrorMessageOnItsUserInterfaceWhenItsCallbackMethodIsCalled()
    {
        registrationInteractor.setPassword(VALID_PASSWORD);
        ConfirmationPasswordPresenter presenter = (ConfirmationPasswordPresenter)new ConfirmationPasswordPresenter.Builder()
                .setRegistrationInteractor(registrationInteractor)
                .setRegistrationUserInterface(mockUserInterface)
                .build();

        CredentialUserInterface mockCredentialUserInterface = mock(CredentialUserInterface.class);
        presenter.bindWith(mockCredentialUserInterface);

        presenter.onValueEntered();
        presenter.onCredentialChanged();

        verify(mockCredentialUserInterface).showValidationErrorMessage();

        registrationInteractor.setConfirmationPassword(PASSWORD_ALL_LOWERCASE);
        registrationInteractor.setPassword(PASSWORD_ALL_LOWERCASE);

        presenter.onCredentialChanged();

        verify(mockCredentialUserInterface).hideValidationErrorMessage();
    }
}
