package com.vitalityactive.va.forgotpassword;

import com.vitalityactive.va.events.EventDispatcher;
import com.vitalityactive.va.events.EventListener;
import com.vitalityactive.va.forgotpassword.service.ForgotPasswordServiceClient;
import com.vitalityactive.va.forgotpassword.service.ForgotPasswordSuccessEvent;
import com.vitalityactive.va.login.BaseURLSwitcher;
import com.vitalityactive.va.networking.RequestFailedEvent;
import com.vitalityactive.va.networking.WebServiceResponseParser;
import com.vitalityactive.va.register.entity.Username;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.io.IOException;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ForgotPasswordInteractorTests {
    @Mock
    ForgotPasswordServiceClient serviceClient;
    private EventDispatcher eventDispatcher;
    private boolean successEventFired = false;
    private RequestFailedEvent failedEventReceived = null;
    @Mock
    private BaseURLSwitcher mockBaseURLSwitcher;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        eventDispatcher = new EventDispatcher();
        eventDispatcher.addEventListener(ForgotPasswordSuccessEvent.class, new EventListener<ForgotPasswordSuccessEvent>() {
            @Override
            public void onEvent(ForgotPasswordSuccessEvent event) {
                successEventFired = true;
            }
        });
        eventDispatcher.addEventListener(RequestFailedEvent.class, new EventListener<RequestFailedEvent>() {
            @Override
            public void onEvent(RequestFailedEvent event) {
                failedEventReceived = event;
            }
        });
    }

    @Test
    public void interactor_calls_service_client() {
        ForgotPasswordInteractor interactor = new ForgotPasswordInteractor(serviceClient, eventDispatcher, mockBaseURLSwitcher);
        when(mockBaseURLSwitcher.switchBaseURL(any(Username.class))).thenReturn(new Username("username"));
        interactor.forgotPassword("username");

        verify(serviceClient, times(1)).forgotPassword(eq("username"), eq(interactor));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void interactor_fires_success_event() {
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                WebServiceResponseParser<String> parser = (WebServiceResponseParser<String>) invocation.getArgumentAt(1, WebServiceResponseParser.class);
                parser.parseResponse("");
                return null;
            }
        }).when(serviceClient).forgotPassword(any(String.class), any(WebServiceResponseParser.class));

        when(mockBaseURLSwitcher.switchBaseURL(any(Username.class))).thenReturn(new Username("username"));
        ForgotPasswordInteractor interactor = new ForgotPasswordInteractor(serviceClient, eventDispatcher, mockBaseURLSwitcher);

        interactor.forgotPassword("username");

        Assert.assertTrue(successEventFired);
        Assert.assertNull("expected no failed response to be received", failedEventReceived);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void interactor_fires_failed_event_generic() {
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                WebServiceResponseParser<String> parser = (WebServiceResponseParser<String>) invocation.getArgumentAt(1, WebServiceResponseParser.class);
                parser.handleGenericError(new IOException("test"));
                return null;
            }
        }).when(serviceClient).forgotPassword(any(String.class), any(WebServiceResponseParser.class));

        when(mockBaseURLSwitcher.switchBaseURL(any(Username.class))).thenReturn(new Username("username"));
        ForgotPasswordInteractor interactor = new ForgotPasswordInteractor(serviceClient, eventDispatcher, mockBaseURLSwitcher);

        interactor.forgotPassword("username");

        Assert.assertFalse(successEventFired);
        Assert.assertNotNull("expected a failed response to be received", failedEventReceived);
        Assert.assertEquals(RequestFailedEvent.Type.GENERIC_ERROR, failedEventReceived.getType());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void interactor_fires_failed_event_connection() {
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                WebServiceResponseParser<String> parser = (WebServiceResponseParser<String>) invocation.getArgumentAt(1, WebServiceResponseParser.class);
                parser.handleConnectionError();
                return null;
            }
        }).when(serviceClient).forgotPassword(any(String.class), any(WebServiceResponseParser.class));

        when(mockBaseURLSwitcher.switchBaseURL(any(Username.class))).thenReturn(new Username("username"));
        ForgotPasswordInteractor interactor = new ForgotPasswordInteractor(serviceClient, eventDispatcher, mockBaseURLSwitcher);

        interactor.forgotPassword("username");

        Assert.assertFalse(successEventFired);
        Assert.assertNotNull("expected a failed response to be received", failedEventReceived);
        Assert.assertEquals(RequestFailedEvent.Type.CONNECTION_ERROR, failedEventReceived.getType());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void interactor_fires_failed_event_not_registered() {
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                WebServiceResponseParser<String> parser = (WebServiceResponseParser<String>) invocation.getArgumentAt(1, WebServiceResponseParser.class);
                parser.parseErrorResponse("", 400);
                return null;
            }
        }).when(serviceClient).forgotPassword(any(String.class), any(WebServiceResponseParser.class));

        when(mockBaseURLSwitcher.switchBaseURL(any(Username.class))).thenReturn(new Username("username"));
        ForgotPasswordInteractor interactor = new ForgotPasswordInteractor(serviceClient, eventDispatcher, mockBaseURLSwitcher);

        interactor.forgotPassword("username");

        Assert.assertFalse(successEventFired);
        Assert.assertNotNull("expected a failed response to be received", failedEventReceived);
        Assert.assertEquals(RequestFailedEvent.Type.INVALID_USERNAME, failedEventReceived.getType());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void interactor_fires_failed_event_non_400_response_as_generic() {
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                WebServiceResponseParser<String> parser = (WebServiceResponseParser<String>) invocation.getArgumentAt(1, WebServiceResponseParser.class);
                parser.parseErrorResponse("", 500);     // not 400
                return null;
            }
        }).when(serviceClient).forgotPassword(any(String.class), any(WebServiceResponseParser.class));

        when(mockBaseURLSwitcher.switchBaseURL(any(Username.class))).thenReturn(new Username("username"));
        ForgotPasswordInteractor interactor = new ForgotPasswordInteractor(serviceClient, eventDispatcher, mockBaseURLSwitcher);

        interactor.forgotPassword("username");

        Assert.assertFalse(successEventFired);
        Assert.assertNotNull("expected a failed response to be received", failedEventReceived);
        Assert.assertEquals(RequestFailedEvent.Type.GENERIC_ERROR, failedEventReceived.getType());
    }
}
