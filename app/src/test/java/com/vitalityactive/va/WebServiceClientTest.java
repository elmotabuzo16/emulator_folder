package com.vitalityactive.va;

import android.support.annotation.NonNull;

import com.vitalityactive.va.events.EventListener;
import com.vitalityactive.va.login.LoginService;
import com.vitalityactive.va.networking.WebServiceClient;
import com.vitalityactive.va.networking.WebServiceResponseParser;
import com.vitalityactive.va.networking.model.LoginServiceRequest;
import com.vitalityactive.va.networking.model.LoginServiceResponse;
import com.vitalityactive.va.register.entity.Password;
import com.vitalityactive.va.register.entity.Username;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import java.io.IOException;
import java.net.ConnectException;
import java.net.UnknownHostException;
import java.util.Arrays;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import okhttp3.Request;
import retrofit2.Call;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(JUnitParamsRunner.class)
public class WebServiceClientTest extends BaseTest {

    private boolean called;
    private WebServiceClient webServiceClient;

    @Mock
    Call<LoginServiceResponse> mockCall;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        called = false;
        webServiceClient = new WebServiceClient(new SameThreadScheduler(), eventDispatcher);
    }

    @SuppressWarnings("unused")
    public Iterable<Object[]> normalTestData() {
        return Arrays.asList(new Object[]{200, WebServiceClient.RequestSucceededEvent.class},
                new Object[]{500, WebServiceClient.RequestFailedEvent.class},
                new Object[]{WebServiceClient.HTTP_RESPONSE_CODE_UNAUTHORIZED, WebServiceClient.RequestUnauthorizedEvent.class});
    }

    @Test
    @Parameters(method = "normalTestData")
    public <T> void request_is_no_longer_in_progress_when_event_fires(int responseCode, Class<T> eventClass) throws IOException {
        setUpMockWebServer(responseCode, "{}");

        final Call<LoginServiceResponse> loginRequest = getLoginRequest();

        verify(eventClass, loginRequest);
    }

    private <T> void verify(Class<T> eventClass, final Call<LoginServiceResponse> loginRequest) {
        eventDispatcher.addEventListener(eventClass, new EventListener<T>() {
            @Override
            public void onEvent(T event) {
                called = true;
                assertFalse(webServiceClient.isRequestInProgress(loginRequest.request().toString()));
            }
        });

        webServiceClient.executeAsynchronousRequest(loginRequest, getParser());
        assertTrue(called);
    }

    @SuppressWarnings("unused")
    public Iterable<Object[]> throwableTestData() {
        return Arrays.asList(new Object[]{new UnknownHostException(), WebServiceClient.RequestCancelledEvent.class},
                new Object[]{new ConnectException(), WebServiceClient.RequestCancelledEvent.class},
                new Object[]{new IOException(), WebServiceClient.RequestCancelledEvent.class});
    }

    @Test
    @Parameters(method = "throwableTestData")
    public <T> void request_is_no_longer_in_progress_when_event_fires_after_exception(Throwable throwable, Class<T> eventClass) throws IOException {
        when(mockCall.execute()).thenThrow(throwable);
        Request request = new Request.Builder().url("http://www.google.com").build();
        when(mockCall.request()).thenReturn(request);

        verify(eventClass, mockCall);
    }

    private Call<LoginServiceResponse> getLoginRequest() {
        return getService(LoginService.class).getLoginRequest(new LoginServiceRequest(new LoginServiceRequest.LoginRequest(new Username("hello"), new Password("world"))), AUTHORIZATION_HEADER);
    }

    @NonNull
    private WebServiceResponseParser<LoginServiceResponse> getParser() {
        return new WebServiceResponseParser<LoginServiceResponse>() {
            @Override
            public void parseResponse(LoginServiceResponse body) {

            }

            @Override
            public void parseErrorResponse(String errorBody, int code) {

            }

            @Override
            public void handleGenericError(Exception exception) {

            }

            @Override
            public void handleConnectionError() {

            }
        };
    }
}
