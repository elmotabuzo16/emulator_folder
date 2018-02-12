package com.vitalityactive.va.register;

import com.vitalityactive.va.BaseTest;
import com.vitalityactive.va.DeviceSpecificPreferences;
import com.vitalityactive.va.NetworkingTestUtilities;
import com.vitalityactive.va.RetryRule;
import com.vitalityactive.va.SameThreadScheduler;
import com.vitalityactive.va.authentication.BasicAuthorizationProvider;
import com.vitalityactive.va.events.EventDispatcher;
import com.vitalityactive.va.events.EventListener;
import com.vitalityactive.va.login.BaseURLSwitcher;
import com.vitalityactive.va.networking.ServiceGenerator;
import com.vitalityactive.va.networking.WebServiceClient;
import com.vitalityactive.va.networking.model.RegistrationServiceRequest;
import com.vitalityactive.va.register.entity.Username;
import com.vitalityactive.va.register.interactor.RegistrationInteractor;
import com.vitalityactive.va.register.interactor.RegistrationInteractorImpl;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import java.io.IOException;
import java.net.UnknownHostException;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import junitparams.naming.TestCaseName;
import okhttp3.Request;
import okhttp3.mockwebserver.RecordedRequest;
import retrofit2.Call;
import retrofit2.Retrofit;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@RunWith(JUnitParamsRunner.class)
public class RegistrationInteractorTest extends BaseTest {
    private static final String USERNAME = "newuser@gmail.com";
    private static final String PASSWORD = "Password1";
    private static final String INSURER_CODE = "registrationCode";
    private static final String AUTHORIZATION_HEADER = "authorization_header_yo";
    private boolean succeeded;
    private EventDispatcher eventDispatcher;
    private boolean failed;
    private RegistrationInteractor.RegistrationRequestResult registrationRequestResult;
    private boolean gotRegistrationEvent;

    @Mock
    DeviceSpecificPreferences mockPreferences;
    @Mock
    BasicAuthorizationProvider mockBasicAuthorizationProvider;
    @Mock
    private Call<String> mockCall;

    @Rule
    public RetryRule retryRule = new RetryRule();
    @Mock
    private ServiceGenerator mockServiceGenerator;
    @Mock
    private BaseURLSwitcher mockBaseURLSwitcher;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        when(mockBasicAuthorizationProvider.getAuthorization()).thenReturn(AUTHORIZATION_HEADER);
        succeeded = false;
        failed = false;
        registrationRequestResult = RegistrationInteractor.RegistrationRequestResult.NONE;
        eventDispatcher = new EventDispatcher();
        eventDispatcher.addEventListener(RegistrationInteractor.RegistrationSucceededEvent.class, new EventListener<RegistrationInteractor.RegistrationSucceededEvent>() {
            @Override
            public void onEvent(RegistrationInteractor.RegistrationSucceededEvent event) {
                succeeded = true;
                gotRegistrationEvent = true;
            }
        });
        eventDispatcher.addEventListener(RegistrationInteractor.RegistrationFailedEvent.class, new EventListener<RegistrationInteractor.RegistrationFailedEvent>() {
            @Override
            public void onEvent(RegistrationInteractor.RegistrationFailedEvent event) {
                failed = true;
                registrationRequestResult = event.getRegistrationRequestResult();
                gotRegistrationEvent = true;
            }
        });
    }

    @Test
    public void it_registers_successfully() throws Exception {
        setUpMockWebServerWithSuccessResponse();
        RegistrationInteractorImpl interactor = getRegistrationInteractor(getService(RegistrationService.class));
        setCorrectRegistrationCredentials(interactor);

        interactor.register();

        assertTrue(succeeded);
        assertEquals(RegistrationInteractor.RegistrationRequestResult.SUCCESSFUL, interactor.getRegistrationRequestResult());
    }

    private void setCorrectRegistrationCredentials(RegistrationInteractor interactor) {
        interactor.setUsername(USERNAME);
        interactor.setPassword(PASSWORD);
        interactor.setConfirmationPassword(PASSWORD);
        interactor.setInsurerCode(INSURER_CODE);
        when(mockBaseURLSwitcher.switchBaseURL(any(Username.class))).thenReturn(new Username(USERNAME));
    }

    @Test
    public void it_reports_a_connection_error_correctly() throws Exception {
        testExceptionHandling(RegistrationInteractor.RegistrationRequestResult.CONNECTION_ERROR, new UnknownHostException());
    }

    @Test
    public void it_reports_a_generic_error_correctly() throws Exception {
        testExceptionHandling(RegistrationInteractor.RegistrationRequestResult.GENERIC_ERROR, new IOException());
    }

    @SuppressWarnings("unused")
    private Object[][] errorResponseData() {
        return new Object[][]{
                new Object[]{400, RegistrationInteractor.RegistrationRequestResult.INVALID_EMAIL_INSURER_CODE_ERROR},
                new Object[]{404, RegistrationInteractor.RegistrationRequestResult.INVALID_EMAIL_INSURER_CODE_ERROR},
                new Object[]{409, RegistrationInteractor.RegistrationRequestResult.ALREADY_REGISTERED_ERROR},
                new Object[]{500, RegistrationInteractor.RegistrationRequestResult.GENERIC_ERROR}
        };
    }

    @Test
    @Parameters(method = "errorResponseData")
    public void it_handles_an_error_response_correctly(int code, RegistrationInteractor.RegistrationRequestResult requestResult) throws Exception {
        setUpMockWebServer(code, "{}");
        RegistrationInteractorImpl interactor = getRegistrationInteractor(getService(RegistrationService.class));
        setCorrectRegistrationCredentials(interactor);

        // clear previous results
        failed = false;
        registrationRequestResult = null;
        gotRegistrationEvent = false;

        interactor.register();

        assertTrue(gotRegistrationEvent);
        assertTrue(failed);
        assertEquals(requestResult, registrationRequestResult);
        assertEquals(requestResult, interactor.getRegistrationRequestResult());
    }

    @Test
    public void it_does_not_try_to_register_with_invalid_credentials() throws Exception {
        setUpMockWebServerWithSuccessResponse();
        RegistrationInteractorImpl interactor = getRegistrationInteractor(getService(RegistrationService.class));

        interactor.register();

        assertFalse(succeeded);
        assertFalse(failed);
        assertEquals(RegistrationInteractor.RegistrationRequestResult.NONE, registrationRequestResult);
    }

    @Test
    public void it_does_not_try_to_register_if_registration_is_in_progress() throws Exception {
        RegistrationServiceClient mockRegistrationServiceClient = mock(RegistrationServiceClient.class);
        when(mockRegistrationServiceClient.isRegistering()).thenReturn(true);

        RegistrationInteractorImpl interactor = new RegistrationInteractorImpl(mockPreferences, eventDispatcher, mockRegistrationServiceClient, mockBaseURLSwitcher);
        setCorrectRegistrationCredentials(interactor);

        assertFalse(succeeded);
        assertFalse(failed);
        assertEquals(RegistrationInteractor.RegistrationRequestResult.NONE, registrationRequestResult);
        verifyNoMoreInteractions(mockRegistrationServiceClient);
    }

    @Test
    @Parameters({"true, ", "false, not"})
    @TestCaseName("It reports registration status correctly when registration is {1} in progress")
    @SuppressWarnings("unused")
    public void it_reports_registration_is_busy_correctly(boolean isRegistering, String not) throws Exception {
        RegistrationServiceClient mockRegistrationServiceClient = mock(RegistrationServiceClient.class);
        when(mockRegistrationServiceClient.isRegistering()).thenReturn(isRegistering);

        RegistrationInteractorImpl interactor = new RegistrationInteractorImpl(mockPreferences, eventDispatcher, mockRegistrationServiceClient, mockBaseURLSwitcher);
        setCorrectRegistrationCredentials(interactor);

        assertThat(interactor.isRegistering(), is(isRegistering));
    }

    @Test
    public void registration_request_result_is_initially_correct() throws Exception {
        setUpMockWebServerWithSuccessResponse();
        RegistrationInteractorImpl interactor = getRegistrationInteractor(getService(RegistrationService.class));

        assertEquals(RegistrationInteractor.RegistrationRequestResult.NONE, interactor.getRegistrationRequestResult());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void registration_request_is_correct() throws Exception {
        setUpMockWebServerWithSuccessResponse();

        String expectedSessionId = "expectedSessionId";
        String expectedLocale = "en_ZA";

        Retrofit retrofit = NetworkingTestUtilities.getRetrofit(getMockWebServerBaseURL(), expectedSessionId, expectedLocale);
        when(mockServiceGenerator.getRetrofit()).thenReturn(retrofit);
        when(mockServiceGenerator.create(any(Class.class))).thenReturn(retrofit.create(RegistrationService.class));

        RegistrationInteractor registrationInteractor = new RegistrationInteractorImpl(mockPreferences, eventDispatcher, new RegistrationServiceClient(new WebServiceClient(new SameThreadScheduler(), eventDispatcher), mockBasicAuthorizationProvider, mockServiceGenerator), mockBaseURLSwitcher);
        setCorrectRegistrationCredentials(registrationInteractor);

        registrationInteractor.register();

        RecordedRequest request = mockWebServer.takeRequest();
        assertEquals("/api/tstc-integration-platform-services-service-v1/1.0/register", request.getPath());
        assertEquals(AUTHORIZATION_HEADER, request.getHeader("Authorization"));
        assertEquals("application/json", request.getHeader("Content-Type"));
        assertEquals(expectedSessionId, request.getHeader("session-id"));
        assertEquals(expectedLocale, request.getHeader("locale"));
        assertNotNull(request.getHeader("correlation-id"));
        // TODO: verify the request body
    }

    @SuppressWarnings("unchecked")
    private RegistrationInteractorImpl getRegistrationInteractor(RegistrationService mockService) {
        when(mockServiceGenerator.create(any(Class.class))).thenReturn(mockService);
        return new RegistrationInteractorImpl(mockPreferences, eventDispatcher, new RegistrationServiceClient(new WebServiceClient(new SameThreadScheduler(), eventDispatcher), mockBasicAuthorizationProvider, mockServiceGenerator), mockBaseURLSwitcher);
    }

    private void testExceptionHandling(RegistrationInteractor.RegistrationRequestResult expectedError, IOException exception) throws IOException {
        RegistrationInteractorImpl interactor = getRegistrationInteractor(getMockServiceThrowingAnException(exception));
        setCorrectRegistrationCredentials(interactor);

        interactor.register();
        assertFalse(succeeded);
        assertTrue(failed);
        assertEquals(expectedError, registrationRequestResult);
        assertEquals(expectedError, interactor.getRegistrationRequestResult());
    }

    private RegistrationService getMockServiceThrowingAnException(IOException exception) throws IOException {
        RegistrationService mockService = mock(RegistrationService.class);
        when(mockService.getRegistrationRequest(any(RegistrationServiceRequest.class), anyString())).thenReturn(mockCall);
        when(mockCall.execute()).thenThrow(exception);
        when(mockCall.request()).thenReturn(new Request.Builder().url("http://www.google.com").build());
        return mockService;
    }

}
