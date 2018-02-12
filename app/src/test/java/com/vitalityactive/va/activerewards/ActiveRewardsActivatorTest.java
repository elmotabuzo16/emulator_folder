package com.vitalityactive.va.activerewards;

import com.vitalityactive.va.BaseTest;
import com.vitalityactive.va.PartyInformationRepository;
import com.vitalityactive.va.activerewards.landing.service.ActiveRewardsActivationPayload;
import com.vitalityactive.va.authentication.AccessTokenAuthorizationProvider;
import com.vitalityactive.va.events.EventDispatcher;
import com.vitalityactive.va.events.EventListener;
import com.vitalityactive.va.networking.WebServiceClient;
import com.vitalityactive.va.networking.WebServiceResponseParser;
import com.vitalityactive.va.networking.model.ar.ActivateServiceResponse;
import com.vitalityactive.va.wellnessdevices.landing.WellnessDevicesLandingInteractorImpl;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;

import java.io.IOException;

import okhttp3.Request;
import retrofit2.Call;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ActiveRewardsActivatorTest extends BaseTest {
    @Captor
    ArgumentCaptor<WebServiceResponseParser<ActivateServiceResponse>> parserCaptor;

    @Mock
    Call<ActivateServiceResponse> mockCall;

    @Mock
    ActiveRewardsService mockActiveRewardsService;

    @Mock
    WebServiceClient mockWebServiceClient;

    @Mock
    private AccessTokenAuthorizationProvider mockAccessTokenAuthorizationProvider;

    @Mock
    private PartyInformationRepository mockPartyInformationRepository;

    @Mock
    private WellnessDevicesLandingInteractorImpl mockWellnessDevicesLandingInteractor;

    private ActiveRewardsActivatorImpl activator;

    private ActivationErrorType activationErrorType;
    private EventDispatcher eventDispatcher;
    private EventListener<ActiveRewardsActivator.ActivationSucceededEvent> activationSucceededEventListener;
    private EventListener<ActiveRewardsActivator.ActivationFailedEvent> activationFailedEventListener;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        initialiseAndroidThreeTen();
        when(mockActiveRewardsService.getActivateRequest(anyString(), anyLong(), anyLong(), any(ActiveRewardsActivationPayload.class))).thenReturn(mockCall);
        when(mockCall.clone()).thenReturn(mockCall);
        when(mockCall.request()).thenReturn(new Request.Builder().url("http://www.google.com").get().build());

        eventDispatcher = new EventDispatcher();
        activator = new ActiveRewardsActivatorImpl(eventDispatcher, new ActiveRewardsActivationServiceClient(mockWebServiceClient, mockPartyInformationRepository, mockActiveRewardsService, mockAccessTokenAuthorizationProvider), mockWellnessDevicesLandingInteractor);

        activationSucceededEventListener = new EventListener<ActiveRewardsActivator.ActivationSucceededEvent>() {
            @Override
            public void onEvent(ActiveRewardsActivator.ActivationSucceededEvent event) {
            }
        };
        activationFailedEventListener = new EventListener<ActiveRewardsActivator.ActivationFailedEvent>() {
            @Override
            public void onEvent(ActiveRewardsActivator.ActivationFailedEvent event) {
                activationErrorType = event.activationErrorType;
            }
        };

        eventDispatcher.addEventListener(ActiveRewardsActivator.ActivationSucceededEvent.class, activationSucceededEventListener);
        eventDispatcher.addEventListener(ActiveRewardsActivator.ActivationFailedEvent.class, activationFailedEventListener);
    }

    @After
    public void tearDown() throws Exception {
        super.tearDown();
        eventDispatcher.removeEventListener(ActiveRewardsActivator.ActivationSucceededEvent.class, activationSucceededEventListener);
        eventDispatcher.removeEventListener(ActiveRewardsActivator.ActivationFailedEvent.class, activationFailedEventListener);
    }

    @Test
    public void repository_is_updated_and_callback_is_notified_when_activation_fails_because_of_a_connection_error() {
        activator.activate();

        captureResponseParser();
        parserCaptor.getValue().handleConnectionError();

        assertEquals(ActivationErrorType.CONNECTION, activationErrorType);
    }

    @Test
    public void repository_is_updated_and_callback_is_notified_when_activation_fails_because_of_a_generic_error() {
        activator.activate();

        captureResponseParser();
        parserCaptor.getValue().handleGenericError(new IOException("Some random exception"));

        assertEquals(ActivationErrorType.GENERIC, activationErrorType);
    }

    @Test
    public void is_not_activating_if_request_is_not_in_progress() {
        when(mockWebServiceClient.isRequestInProgress(anyString())).thenReturn(false);

        assertFalse(activator.isActivateRequestInProgress());
    }

    @Test
    public void is_activating_if_request_in_progress() {
        when(mockWebServiceClient.isRequestInProgress(anyString())).thenReturn(true);

        activator.activate();
        assertTrue(activator.isActivateRequestInProgress());
    }

    private void captureResponseParser() {
        verify(mockWebServiceClient).executeAsynchronousRequest(eq(mockCall), parserCaptor.capture());
    }
}
