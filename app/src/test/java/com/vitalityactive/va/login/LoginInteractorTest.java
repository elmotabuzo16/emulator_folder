package com.vitalityactive.va.login;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.vitalityactive.va.BaseTest;
import com.vitalityactive.va.DeviceSpecificPreferences;
import com.vitalityactive.va.NetworkingTestUtilities;
import com.vitalityactive.va.PartyInformationRepository;
import com.vitalityactive.va.RetryRule;
import com.vitalityactive.va.SameThreadScheduler;
import com.vitalityactive.va.Scheduler;
import com.vitalityactive.va.TestUtilities;
import com.vitalityactive.va.authentication.BasicAuthorizationProvider;
import com.vitalityactive.va.events.EventDispatcher;
import com.vitalityactive.va.events.EventListener;
import com.vitalityactive.va.networking.ServiceGenerator;
import com.vitalityactive.va.networking.WebServiceClient;
import com.vitalityactive.va.networking.WebServiceResponseParser;
import com.vitalityactive.va.networking.model.LoginServiceRequest;
import com.vitalityactive.va.networking.model.LoginServiceResponse;
import com.vitalityactive.va.onboarding.LoginRepository;
import com.vitalityactive.va.register.entity.Password;
import com.vitalityactive.va.register.entity.Username;
import com.vitalityactive.va.myhealth.vitalityage.VitalityAgePreferencesManager;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;

import java.io.IOException;
import java.net.UnknownHostException;

import okhttp3.Request;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import retrofit2.Call;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class LoginInteractorTest extends BaseTest {
    private static final String AUTHORIZATION_HEADER = "this_is_the_authorization_header";
    @Mock
    LoginServiceClient mockLoginServiceClient;

    @Mock
    EventDispatcher mockEventDispatcher;

    @Mock
    Scheduler mockScheduler;

    private LoginInteractorImpl loginInteractor;

    @Mock
    LoginRepository mockLoginRepository;

    @Mock
    DeviceSpecificPreferences mockDeviceSpecificPreferences;

    @Mock
    BasicAuthorizationProvider mockBasicAuthorizationProvider;

    @Mock
    PartyInformationRepository partyInformationRepository;

    @Rule
    public RetryRule retryRule = new RetryRule();

    private EventDispatcher eventDispatcher;
    private boolean succeeded;
    private boolean failed;
    private LoginInteractor.LoginRequestResult loginRequestResult;

    @Mock
    private Call<LoginServiceResponse> mockCall;
    @Mock
    private BaseURLSwitcher mockBaseURLSwitcher;
    @Mock
    private ServiceGenerator mockServiceGenerator;
    @Mock
    private VitalityAgePreferencesManager mockVitalityAgePreferencesManager;

    @Before
    public void setUp() throws Exception {
        super.setUp();

        when(mockLoginRepository.persistLoginResponse(any(LoginServiceResponse.class), any(Username.class))).thenReturn(true);

        when(mockBasicAuthorizationProvider.getAuthorization()).thenReturn(AUTHORIZATION_HEADER);

        loginRequestResult = LoginInteractor.LoginRequestResult.NONE;

        eventDispatcher = new EventDispatcher();
        eventDispatcher.addEventListener(AuthenticationSucceededEvent.class, new EventListener<AuthenticationSucceededEvent>() {
            @Override
            public void onEvent(AuthenticationSucceededEvent event) {
                succeeded = true;
            }
        });
        eventDispatcher.addEventListener(AuthenticationFailedEvent.class, new EventListener<AuthenticationFailedEvent>() {
            @Override
            public void onEvent(AuthenticationFailedEvent event) {
                failed = true;
                loginRequestResult = event.getLoginRequestResult();
            }
        });

        loginInteractor = new LoginInteractorImpl(mockLoginServiceClient, eventDispatcher, mockLoginRepository, mockDeviceSpecificPreferences, mockBaseURLSwitcher, mockVitalityAgePreferencesManager, partyInformationRepository);
        succeeded = false;
        failed = false;
    }

    @After
    public void tearDown() throws Exception {
        super.tearDown();
    }

    @Test
    public void login_request_is_correct() throws Exception {
        setUpLoginInteractor("login/Login_successful.json", "expectedSessionId", "en_ZA");

        Username username = new Username("bacon@sandwich.yum");
        when(mockBaseURLSwitcher.switchBaseURL(username)).thenReturn(username);
        loginInteractor.logIn(username, new Password("baconSandwichesAreGood"));

        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals("/api/tstc-integration-platform-services-service-v1/1.0/login", request.getPath());
        assertEquals(AUTHORIZATION_HEADER, request.getHeader("Authorization"));
        assertEquals("application/json", request.getHeader("Content-Type"));
        assertEquals("expectedSessionId", request.getHeader("session-id"));
        assertEquals("en_ZA", request.getHeader("locale"));
        assertNotNull(request.getHeader("correlation-id"));
        LoginServiceRequest expectedRequest = new Gson().fromJson(getJSONStringFromFile("login/login_request.json"), LoginServiceRequest.class);
        LoginServiceRequest actualRequest = new Gson().fromJson(request.getBody().readUtf8(), LoginServiceRequest.class);
        compareRequests(expectedRequest, actualRequest);
    }

    private void setUpLoginInteractor(String responsePath, String expectedSessionId, String expectedLocale) throws IOException {
        setUpMockWebServer(responsePath);
        String baseURL = mockWebServer.url("api/").toString();
        loginInteractor = new LoginInteractorImpl(getLoginServiceClient(new WebServiceClient(new SameThreadScheduler(), eventDispatcher), NetworkingTestUtilities.getRetrofit(baseURL, expectedSessionId, expectedLocale).create(LoginService.class)), eventDispatcher, mockLoginRepository, mockDeviceSpecificPreferences, mockBaseURLSwitcher,mockVitalityAgePreferencesManager, partyInformationRepository);
    }

    private void compareRequests(LoginServiceRequest expectedRequest, LoginServiceRequest actualRequest) {
        assertEquals(expectedRequest.loginRequest.username, actualRequest.loginRequest.username);
        assertEquals(expectedRequest.loginRequest.password, actualRequest.loginRequest.password);
    }

    @Test
    public void it_logs_in_correctly() throws Exception {
        setUpMockWebServer(200, "{}");

        Username username = new Username("bacon@sandwich.yum");
        loginInteractor = getLoginInteractor(getService(LoginService.class), username);

        assertFalse(loginInteractor.isBusyLoggingIn());

        loginInteractor.logIn(username, new Password("baconSandwichesAreGood"));

        assertTrue(succeeded);
        assertEquals(LoginInteractor.LoginRequestResult.SUCCESSFUL, loginInteractor.getLoginRequestResult());

        verify(mockLoginRepository).persistLoginResponse(any(LoginServiceResponse.class), eq(username));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void it_does_not_log_in_if_username_is_not_valid() {
        Username username = new Username("invalid_username");
        when(mockBaseURLSwitcher.switchBaseURL(username)).thenReturn(username);
        loginInteractor = new LoginInteractorImpl(mockLoginServiceClient, new EventDispatcher(), mockLoginRepository, mockDeviceSpecificPreferences, mockBaseURLSwitcher,mockVitalityAgePreferencesManager, partyInformationRepository);

        loginInteractor.logIn(username, new Password("somePassword"));

        verify(mockLoginServiceClient, never()).authenticate(any(Username.class), any(Password.class), any(WebServiceResponseParser.class));
    }

    @Test
    public void it_reports_a_generic_exception_correctly() throws Exception {
        Username username = new Username("valid@email.address");
        loginInteractor = getLoginInteractor(getMockServiceThrowingAnException(new IOException("What a mess")), username);

        loginInteractor.logIn(username, new Password("somePassword1"));

        assertFalse(succeeded);
        assertTrue(failed);
        assertEquals(LoginInteractor.LoginRequestResult.GENERIC_ERROR, loginRequestResult);
        assertEquals(LoginInteractor.LoginRequestResult.GENERIC_ERROR, loginInteractor.getLoginRequestResult());
    }

    @Test
    public void it_reports_a_generic_error_response_correctly() throws Exception {
        verifyErrorMessage(500, LoginInteractor.LoginRequestResult.GENERIC_ERROR);
    }

    @Test
    public void it_reports_a_connection_error_correctly() throws Exception {
        Username username = new Username("valid@email.address");
        loginInteractor = getLoginInteractor(getMockServiceThrowingAnException(new UnknownHostException("Connection error")), username);

        loginInteractor.logIn(username, new Password("somePassword1"));

        assertFalse(succeeded);
        assertTrue(failed);
        assertEquals(LoginInteractor.LoginRequestResult.CONNECTION_ERROR, loginRequestResult);
    }

    private LoginService getMockServiceThrowingAnException(Exception exception) throws IOException {
        LoginService mockService = mock(LoginService.class);
        when(mockService.getLoginRequest(any(LoginServiceRequest.class), anyString())).thenReturn(mockCall);
        when(mockCall.execute()).thenThrow(exception);
        when(mockCall.request()).thenReturn(new Request.Builder().url("http://www.google.com").build());
        return mockService;
    }

    @Test
    public void it_reports_an_invalid_credentials_error_correctly() throws IOException {
        verifyErrorMessage(400, LoginInteractor.LoginRequestResult.INCORRECT_USERNAME_OR_PASSWORD);
    }

    @Test
    public void it_reports_a_locked_account_error_correctly() throws Exception {
        verifyErrorMessage(423, LoginInteractor.LoginRequestResult.LOCKED_ACCOUNT);
    }

    @Test
    public void it_reports_valid_credentials_if_username_is_valid() {
        assertTrue(loginInteractor.areLoginCredentialsValid(new Username("valid@email.address"), new Password("somePassword")));
    }

    @Test
    public void it_reports_invalid_credentials_if_username_is_invalid() {
        assertFalse(loginInteractor.areLoginCredentialsValid(new Username("invalidEmail.address"), new Password("somePassword")));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void it_cannot_log_in_while_login_is_in_progress() {
        when(mockLoginServiceClient.isAuthenticating()).thenReturn(true);

        logInWithValidCredentials();

        assertTrue(loginInteractor.isBusyLoggingIn());

        verify(mockLoginServiceClient, never()).authenticate(any(Username.class), any(Password.class), any(WebServiceResponseParser.class));

    }

    @Test
    public void login_request_result_is_initially_correct() throws Exception {
        setUpMockWebServerWithSuccessResponse();
        loginInteractor = getLoginInteractor(getService(LoginService.class), new Username(""));

        assertEquals(LoginInteractor.LoginRequestResult.NONE, loginInteractor.getLoginRequestResult());
    }

    @Test
    public void it_reports_a_generic_error_if_login_response_parsing_fails() throws Exception {
        setUpMockWebServerWithSuccessResponse();
        when(mockLoginRepository.persistLoginResponse(any(LoginServiceResponse.class), any(Username.class))).thenReturn(false);

        Username username = new Username("valid@gmail.com");
        loginInteractor = getLoginInteractor(getService(LoginService.class), username);

        logInWithValidCredentials(username);

        assertEquals(LoginInteractor.LoginRequestResult.GENERIC_ERROR, loginInteractor.getLoginRequestResult());
    }

    @Test
    public void it_invokes_membership_period_preferences_manager()throws Exception{
        setUpMockWebServer(200, "{}");

        Username username = new Username("bacon@sandwich.yum");
        loginInteractor = getLoginInteractor(getService(LoginService.class), username);

        assertFalse(loginInteractor.isBusyLoggingIn());

        loginInteractor.logIn(username, new Password("baconSandwichesAreGood"));

        assertTrue(succeeded);
        assertEquals(LoginInteractor.LoginRequestResult.SUCCESSFUL, loginInteractor.getLoginRequestResult());

       verify(mockVitalityAgePreferencesManager).onLogin();
    }

    private void verifyErrorMessage(int responseCode, LoginInteractor.LoginRequestResult requestResult) throws IOException {
        setUpMockWebServer(responseCode, "{}");
        Username username = new Username("valid@email.address");
        loginInteractor = getLoginInteractor(getService(LoginService.class), username);

        loginInteractor.logIn(username, new Password("somePassword1"));

        assertFalse(succeeded);
        assertTrue(failed);
        assertEquals(requestResult, loginRequestResult);
        assertEquals(requestResult, loginInteractor.getLoginRequestResult());
    }

    private void logInWithValidCredentials() {
        logInWithValidCredentials(new Username("valid@gmail.com"));
    }

    private void logInWithValidCredentials(Username username) {
        loginInteractor.logIn(username, new Password("somePassword"));
    }

    private void setUpMockWebServer(String path) throws IOException {
        mockWebServer = new MockWebServer();

        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(getJSONStringFromFile(path)));

        mockWebServer.start();
    }

    private String getJSONStringFromFile(String path) throws IOException {
        return new TestUtilities().readFile(path);
    }

    private LoginInteractorImpl getLoginInteractor(LoginService mockService, Username username) {
        when(mockBaseURLSwitcher.switchBaseURL(username)).thenReturn(username);
        return new LoginInteractorImpl(getLoginServiceClient(new WebServiceClient(new SameThreadScheduler(), eventDispatcher), mockService), eventDispatcher, mockLoginRepository, mockDeviceSpecificPreferences, mockBaseURLSwitcher,mockVitalityAgePreferencesManager, partyInformationRepository);
    }

    @NonNull
    @SuppressWarnings("unchecked")
    private LoginServiceClient getLoginServiceClient(WebServiceClient webServiceClient, LoginService loginService) {
        when(mockServiceGenerator.create(any(Class.class))).thenReturn(loginService);
        return new LoginServiceClient(webServiceClient, mockBasicAuthorizationProvider, mockServiceGenerator);
    }

}
