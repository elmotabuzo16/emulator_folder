package com.vitalityactive.va.register;

import com.vitalityactive.va.DeviceSpecificPreferences;
import com.vitalityactive.va.SameThreadScheduler;
import com.vitalityactive.va.authentication.BasicAuthorizationProvider;
import com.vitalityactive.va.events.EventDispatcher;
import com.vitalityactive.va.login.BaseURLSwitcher;
import com.vitalityactive.va.networking.ServiceGenerator;
import com.vitalityactive.va.networking.WebServiceClient;
import com.vitalityactive.va.register.entity.Username;
import com.vitalityactive.va.register.interactor.RegistrationInteractorImpl;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import junitparams.naming.TestCaseName;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(JUnitParamsRunner.class)
public class RegistrationInteractorCanRegisterTest
{
    private static final String VALID_USERNAME = "valid@email.address";
    private static final String VALID_USERNAME_WITH_TWO_CHARACTER_LOCAL_PART = "pj@email.address";
    private static final String VALID_PASSWORD = "Password1";
    private static final String USERNAME_WITHOUT_AT_OR_DOT = "invalidUsername";
    private static final String USERNAME_WITHOUT_AT = "invalid.username";
    private static final String USERNAME_WITHOUT_DOT = "us\"ername@invalid";
    private static final boolean CAN_REGISTER = true;
    private static final boolean CANNOT_REGISTER = false;
    private static final String TOO_SHORT_PASSWORD = "As1f";
    private static final String PASSWORD_ALL_LOWERCASE = "asdfasd1";
    private static final String PASSWORD_NO_NUMBERS = "asdfASD";
    private static final String PASSWORD_ALL_UPPERCASE = "ASDFASD1";
    private static final String VALID_INSURER_CODE = "qwerty";
    private static final String EMPTY_INSURER_CODE = "";
    private static final String WHITESPACE_INSURER_CODE = "   ";

    @Mock
    DeviceSpecificPreferences mockPreferences;

    @Mock
    private ServiceGenerator mockServiceGenerator;
    @Mock
    private BaseURLSwitcher mockBaseURLSwitcher;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @SuppressWarnings("unused")
    private Iterable<Object[]> data()
    {
        return Arrays.asList(new Object[][]{
                {VALID_USERNAME, VALID_PASSWORD, VALID_PASSWORD, VALID_INSURER_CODE, CAN_REGISTER},
                {VALID_USERNAME_WITH_TWO_CHARACTER_LOCAL_PART, VALID_PASSWORD, VALID_PASSWORD, VALID_INSURER_CODE, CAN_REGISTER},
                {USERNAME_WITHOUT_AT_OR_DOT, VALID_PASSWORD, VALID_PASSWORD, VALID_INSURER_CODE, CANNOT_REGISTER},
                {USERNAME_WITHOUT_AT, VALID_PASSWORD, VALID_PASSWORD, VALID_INSURER_CODE, CANNOT_REGISTER},
                {USERNAME_WITHOUT_DOT, VALID_PASSWORD, VALID_PASSWORD, VALID_INSURER_CODE, CANNOT_REGISTER},
                {VALID_USERNAME, TOO_SHORT_PASSWORD, TOO_SHORT_PASSWORD, VALID_INSURER_CODE, CANNOT_REGISTER},
                {VALID_USERNAME, PASSWORD_ALL_LOWERCASE, PASSWORD_ALL_LOWERCASE, VALID_INSURER_CODE, CANNOT_REGISTER},
                {VALID_USERNAME, PASSWORD_NO_NUMBERS, PASSWORD_NO_NUMBERS, VALID_INSURER_CODE, CANNOT_REGISTER},
                {VALID_USERNAME, PASSWORD_ALL_UPPERCASE, PASSWORD_ALL_UPPERCASE, VALID_INSURER_CODE, CANNOT_REGISTER},
                {VALID_USERNAME, VALID_PASSWORD, PASSWORD_ALL_UPPERCASE, VALID_INSURER_CODE, CANNOT_REGISTER},
                {VALID_USERNAME, VALID_PASSWORD, VALID_PASSWORD, EMPTY_INSURER_CODE, CANNOT_REGISTER},
                {VALID_USERNAME, VALID_PASSWORD, VALID_PASSWORD, WHITESPACE_INSURER_CODE, CANNOT_REGISTER},
        });
    }

    @Test
    @Parameters(method = "data")
    @TestCaseName("Username: {0}, Password: {1}, Confirm password: {2}, Insurer code: {3} - Can register: {4}")
    public void it_can_register_given_credentials(CharSequence username, CharSequence password, CharSequence confirmationPassword, CharSequence insurerCode, boolean canRegister) throws Exception
    {
        EventDispatcher eventDispatcher = new EventDispatcher();
        BasicAuthorizationProvider mockBasicAuthorizationProvider = mock(BasicAuthorizationProvider.class);
        when(mockBasicAuthorizationProvider.getAuthorization()).thenReturn("authorization_header_yo");
        when(mockBaseURLSwitcher.switchBaseURL(any(Username.class))).thenReturn(new Username(username));
        RegistrationInteractorImpl interactor = new RegistrationInteractorImpl(mockPreferences, eventDispatcher, new RegistrationServiceClient(new WebServiceClient(new SameThreadScheduler(), eventDispatcher), mockBasicAuthorizationProvider, mockServiceGenerator), mockBaseURLSwitcher);
        interactor.setUsername(username);
        interactor.setPassword(password);
        interactor.setConfirmationPassword(confirmationPassword);
        interactor.setInsurerCode(insurerCode);
        assertEquals(canRegister, interactor.canRegister());
    }
}
