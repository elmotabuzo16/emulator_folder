package com.vitalityactive.va.register;

import android.support.annotation.NonNull;

import com.vitalityactive.va.SameThreadScheduler;
import com.vitalityactive.va.events.EventDispatcher;
import com.vitalityactive.va.networking.WebServiceClient;
import com.vitalityactive.va.register.entity.RegistrationCredentials;
import com.vitalityactive.va.register.interactor.RegistrationInteractorImpl;
import com.vitalityactive.va.register.presenter.CredentialPresenterBase;
import com.vitalityactive.va.register.presenter.InsurerCodePresenter;
import com.vitalityactive.va.register.presenter.PasswordPresenter;
import com.vitalityactive.va.register.presenter.UsernamePresenter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(Parameterized.class)
public class CredentialPresenterValidationErrorMessageTest extends CredentialPresenterTestBase {
    private final CredentialPresenterBase.CredentialPresenterBaseBuilder builder;
    private final String username;
    private final String password;
    private final String confirmationPassword;
    private final String insurerCode;
    private final String valid;
    private final Object[] invalidValues;

    public CredentialPresenterValidationErrorMessageTest(CredentialPresenterBase.CredentialPresenterBaseBuilder builder,
                                                         String username,
                                                         String password,
                                                         String confirmationPassword,
                                                         String insurerCode,
                                                         String valid,
                                                         Object[] invalidValues) {
        this.builder = builder;
        this.username = username;
        this.password = password;
        this.confirmationPassword = confirmationPassword;
        this.insurerCode = insurerCode;
        this.valid = valid;
        this.invalidValues = invalidValues;
    }

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        when(mockBasicAuthorizationProvider.getAuthorization()).thenReturn("authorization_header_yo");
        EventDispatcher eventDispatcher = new EventDispatcher();
        RegistrationInteractorImpl registrationService = getRegistrationInteractor(eventDispatcher, new RegistrationServiceClient(new WebServiceClient(new SameThreadScheduler(), eventDispatcher), mockBasicAuthorizationProvider, mockServiceGenerator));
        registrationService.setUsername(username);
        registrationService.setPassword(password);
        registrationService.setConfirmationPassword(confirmationPassword);
        registrationService.setInsurerCode(insurerCode);
        RegistrationCredentials registrationCredentials = new RegistrationCredentials();
        registrationCredentials.setUsername(username);
        registrationCredentials.setPassword(password);
        registrationCredentials.setConfirmationPassword(confirmationPassword);
        registrationCredentials.setInsurerCode(insurerCode);
        presenter = builder
                .setRegistrationInteractor(registrationService)
                .setRegistrationUserInterface(mockUserInterface)
                .build();
    }

    @NonNull
    private RegistrationInteractorImpl getRegistrationInteractor(EventDispatcher eventDispatcher, RegistrationServiceClient registrationServiceClient) {
        return new RegistrationInteractorImpl(mockPreferences, eventDispatcher, registrationServiceClient, mockBaseURLSwitcher);
    }

    @Parameterized.Parameters
    public static Iterable<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {new PasswordPresenter.Builder(), VALID_USERNAME, "", VALID_PASSWORD, VALID_INSURER_CODE, VALID_PASSWORD, new Object[]{TOO_SHORT_PASSWORD, PASSWORD_ALL_LOWERCASE, PASSWORD_ALL_UPPERCASE, PASSWORD_NO_NUMBERS}},
                {new UsernamePresenter.Builder(), "", VALID_PASSWORD, VALID_PASSWORD, VALID_INSURER_CODE, VALID_USERNAME, new Object[]{INVALID_USERNAME_NO_AT_OR_DOT, INVALID_USERNAME_EMPTY, INVALID_USERNAME_NO_AT, INVALID_USERNAME_NO_DOT, INVALID_USERNAME_NULL}},
                {new InsurerCodePresenter.Builder(), VALID_USERNAME, VALID_PASSWORD, VALID_PASSWORD, "", VALID_INSURER_CODE, new Object[]{INVALID_INSURER_CODE_NULL, INVALID_INSURER_CODE_EMPTY, INVALID_INSURER_CODE_WHITESPACE}},
        });
    }

    @Test
    public void validationErrorMessageIsNotShownWhenCredentialIsOnlyChangedToAnIncorrectValue() {
        presenter.onValueChanged(valid);
        assertFalse(presenter.shouldShowValidationErrorMessage());

        for (Object object : invalidValues) {
            presenter.onValueChanged((CharSequence) object);
            assertFalse(presenter.shouldShowValidationErrorMessage());
        }
    }

    @Test
    public void validationErrorMessageIsShownWhenCredentialIsIncorrectlyEntered() {
        presenter.onValueEntered();
        assertTrue(presenter.shouldShowValidationErrorMessage());

        presenter.onValueChanged(valid);
        presenter.onValueEntered();
        assertFalse(presenter.shouldShowValidationErrorMessage());

        for (Object object : invalidValues) {
            presenter.onValueChanged((CharSequence) object);
            presenter.onValueEntered();
            assertTrue(presenter.shouldShowValidationErrorMessage());
        }
    }
}
