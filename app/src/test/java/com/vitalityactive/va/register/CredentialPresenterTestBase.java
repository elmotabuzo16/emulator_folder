package com.vitalityactive.va.register;

import com.vitalityactive.va.DeviceSpecificPreferences;
import com.vitalityactive.va.SameThreadScheduler;
import com.vitalityactive.va.authentication.BasicAuthorizationProvider;
import com.vitalityactive.va.events.EventDispatcher;
import com.vitalityactive.va.login.BaseURLSwitcher;
import com.vitalityactive.va.networking.ServiceGenerator;
import com.vitalityactive.va.networking.WebServiceClient;
import com.vitalityactive.va.register.interactor.RegistrationInteractor;
import com.vitalityactive.va.register.interactor.RegistrationInteractorImpl;
import com.vitalityactive.va.register.presenter.CredentialPresenter;
import com.vitalityactive.va.register.view.RegistrationUserInterface;

import org.mockito.Mock;

abstract class CredentialPresenterTestBase {
    static final String VALID_PASSWORD = "Password1";
    static final String VALID_INSURER_CODE = "validInsurerCode";
    static final String VALID_USERNAME = "valid@username.com";
    static final String TOO_SHORT_PASSWORD = "As1f";
    static final String PASSWORD_ALL_LOWERCASE = "lowercase1";
    static final String PASSWORD_NO_NUMBERS = "no_numbersASD";
    static final String PASSWORD_ALL_UPPERCASE = "UPPERCASE1";
    static final String PASSWORD_EMPTY = "";
    static final String INVALID_USERNAME_NO_AT_OR_DOT = "invalidUsername";
    static final String INVALID_USERNAME_NO_DOT = "in@valid@Username";
    static final String INVALID_USERNAME_NO_AT = "invalidUser.name";
    static final String INVALID_USERNAME_EMPTY = "";
    static final String INVALID_USERNAME_NULL = null;
    static final String INVALID_INSURER_CODE_NULL = null;
    static String INVALID_INSURER_CODE_EMPTY = "";
    static String INVALID_INSURER_CODE_WHITESPACE = "   ";

    @Mock
    DeviceSpecificPreferences mockPreferences;

    @Mock
    RegistrationUserInterface mockUserInterface;

    @Mock
    BasicAuthorizationProvider mockBasicAuthorizationProvider;

    CredentialPresenter presenter;

    RegistrationInteractor registrationInteractor;
    @Mock
    ServiceGenerator mockServiceGenerator;
    @Mock
    BaseURLSwitcher mockBaseURLSwitcher;

    void createRegistrationInteractor() {
        EventDispatcher eventDispatcher = new EventDispatcher();
        registrationInteractor = new RegistrationInteractorImpl(mockPreferences, eventDispatcher, new RegistrationServiceClient(new WebServiceClient(new SameThreadScheduler(), eventDispatcher), mockBasicAuthorizationProvider, mockServiceGenerator), mockBaseURLSwitcher);
    }
}
