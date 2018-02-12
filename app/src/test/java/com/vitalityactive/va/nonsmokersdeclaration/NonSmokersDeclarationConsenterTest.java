package com.vitalityactive.va.nonsmokersdeclaration;

import com.vitalityactive.va.BaseTest;
import com.vitalityactive.va.EventService;
import com.vitalityactive.va.EventServiceClient;
import com.vitalityactive.va.InsurerConfigurationRepository;
import com.vitalityactive.va.NetworkingTestUtilities;
import com.vitalityactive.va.SameThreadScheduler;
import com.vitalityactive.va.authentication.AccessTokenAuthorizationProvider;
import com.vitalityactive.va.constants.EventSource;
import com.vitalityactive.va.constants.EventType;
import com.vitalityactive.va.networking.WebServiceClient;
import com.vitalityactive.va.networking.model.ProcessEventsServiceRequest;
import com.vitalityactive.va.termsandconditions.TermsAndConditionsConsenter;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import okhttp3.mockwebserver.RecordedRequest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(JUnitParamsRunner.class)
public class NonSmokersDeclarationConsenterTest extends BaseTest {

    private static final String AUTHORIZATION_HEADER = "this_is_the_authorization_header";

    @Mock
    AccessTokenAuthorizationProvider mockAccessTokenAuthorizationProvider;

    @SuppressWarnings("unused")
    private Iterable<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {2, getInsurerConfigurationRepository(true), new long[] {EventType._NONSMOKERSDECLRTN, EventType._NONSMOKERSDATAGR}},
                {1, getInsurerConfigurationRepository(false), new long[] {EventType._NONSMOKERSDECLRTN}}
        });
    }

    @Test
    @Parameters(method="data")
    public void it_calls_the_correct_service(int numberOfEvents, InsurerConfigurationRepository insurerConfigurationRepository, long[] eventTypes) throws Exception {

        when(mockAccessTokenAuthorizationProvider.getAuthorization()).thenReturn(AUTHORIZATION_HEADER);

        setUpMockWebServerWithSuccessResponse();
        TermsAndConditionsConsenter consenter = new NonSmokersDeclarationConsenter(eventDispatcher, new EventServiceClient(new WebServiceClient(new SameThreadScheduler(), eventDispatcher), getEventService(), getPartyInformationRepository(), mockTimeUtilities, mockAccessTokenAuthorizationProvider), insurerConfigurationRepository);

        consenter.agreeToTermsAndConditions();

        RecordedRequest request = mockWebServer.takeRequest(1, TimeUnit.SECONDS);
        assertEquals("/api/vitality-manage-events-service-v1/1.0/svc/2/processEvents", request.getPath());
        assertEquals(AUTHORIZATION_HEADER, request.getHeader("Authorization"));
        assertNotNull(request.getHeader("session-Id"));
        assertNotNull(request.getHeader("correlation-Id"));
        assertEquals("application/json", request.getHeader("Content-Type"));
        assertEquals("en_ZA", request.getHeader("locale"));

        ProcessEventsServiceRequest actualRequest = NetworkingTestUtilities.getGson().fromJson(getRecordedRequestBodyString(request), ProcessEventsServiceRequest.class);

        assertEquals(numberOfEvents, actualRequest.processEventsRequest.events.length);
        for (int i = 0; i < numberOfEvents; ++i) {
            verifyEvent(actualRequest.processEventsRequest.events[i], eventTypes[i]);
        }
    }

    private void verifyEvent(ProcessEventsServiceRequest.Event event, long eventType) {
        assertEquals(Long.valueOf(eventType), event.typeKey);
        assertEquals(Integer.valueOf(EventSource._MOBILEAPP), event.eventSourceTypeKey);
        assertEquals(NOW, event.eventOccurredOn);
        assertEquals(NOW, event.eventCapturedOn);
        assertEquals(100002, event.applicableTo);
        assertEquals(100002, event.reportedBy);
    }

    private InsurerConfigurationRepository getInsurerConfigurationRepository(boolean shouldShowNonSmokersPrivacyPolicy) {
        InsurerConfigurationRepository mockInsurerConfigurationRepository = mock(InsurerConfigurationRepository.class);
        when(mockInsurerConfigurationRepository.shouldShowNonSmokersPrivacyPolicy()).thenReturn(shouldShowNonSmokersPrivacyPolicy);
        return mockInsurerConfigurationRepository;
    }

    private EventService getEventService() {
        return getRetrofit(getMockWebServerBaseURL()).create(EventService.class);
    }
}
