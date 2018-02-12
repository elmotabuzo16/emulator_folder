package com.vitalityactive.va.myhealth.service;

import com.vitalityactive.va.NetworkingTestUtilities;
import com.vitalityactive.va.PartyInformationRepository;
import com.vitalityactive.va.SameThreadScheduler;
import com.vitalityactive.va.TestUtilities;
import com.vitalityactive.va.appconfig.AppConfigRepository;
import com.vitalityactive.va.authentication.AccessTokenAuthorizationProvider;
import com.vitalityactive.va.events.EventDispatcher;
import com.vitalityactive.va.myhealth.dto.MyHealthRepository;
import com.vitalityactive.va.networking.RequestResult;
import com.vitalityactive.va.networking.WebServiceClient;
import com.vitalityactive.va.networking.model.HealthAttributeInformationResponse;
import com.vitalityactive.va.vhc.dto.HealthAttributeRepository;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.isA;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


public class HealthAttributeInformationClientTest {

    @Mock
    HealthAttributeRepository mockHealthAttributeRepository;
    @Mock
    AppConfigRepository mockAppConfigRepository;
    @Mock
    EventDispatcher mockEventDispatcher;
    @Mock
    MyHealthRepository mockMyHealthRepository;
    @Captor
    private ArgumentCaptor<HealthAttributeInformationResponse> healthAttributeInformationResponse;
    private MockWebServer server;
    private HealthAttributeInformationServiceClient healthAttributeTipsServiceClient;

    @Before
    public void setUp() throws IOException {
        MockitoAnnotations.initMocks(this);

        server = new MockWebServer();

        String baseURL = server.url("api/").toString();

        final WebServiceClient webServiceClient =
                new WebServiceClient(new SameThreadScheduler(), new EventDispatcher());

        final HealthAttributeFeedbackService healthAttributeFeedbackService =
                NetworkingTestUtilities.getRetrofit(baseURL, "expectedSessionId", "en_ZA").create(HealthAttributeFeedbackService.class);

        final PartyInformationRepository partyInformationRepository = mock(PartyInformationRepository.class);
        when(partyInformationRepository.getTenantId()).thenReturn(10002L);
        when(partyInformationRepository.getPartyId()).thenReturn(2L);

        final AccessTokenAuthorizationProvider accessTokenAuthorizationProvider = mock(AccessTokenAuthorizationProvider.class);
        when(accessTokenAuthorizationProvider.getAuthorization()).thenReturn("RandomToken");

        healthAttributeTipsServiceClient =
                new HealthAttributeInformationServiceClient(webServiceClient,
                        healthAttributeFeedbackService,
                        partyInformationRepository,
                        mockEventDispatcher,
                        accessTokenAuthorizationProvider, mockMyHealthRepository
                );
    }

    @After
    public void tearDown() throws Exception {
        if (server != null) {
            server.shutdown();
        }
    }

    private void setUpMockWebServer(String path) throws IOException {
        server.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(new TestUtilities().readFile(path)));
    }

    @Test
    public void correctly_maps_feedback_response() throws Exception {
        setUpMockWebServer("myhealth/health_information_response.json");
        healthAttributeTipsServiceClient.fetchHealthAttributeFeedbackTips(Collections.singletonList(2l), false);
        assertNotNull(healthAttributeTipsServiceClient.getHealthAtttributeTipsFeedbackResult());
        assertTrue(healthAttributeTipsServiceClient.getHealthAtttributeTipsFeedbackResult().equals(RequestResult.SUCCESSFUL));
    }

    @Test
    public void response_is_mapped() throws Exception {
        setUpMockWebServer("myhealth/health_information_response.json");
        healthAttributeTipsServiceClient.fetchHealthAttributeFeedbackTips(Collections.singletonList(25l),false );
        verify(mockEventDispatcher, atLeastOnce()).dispatchEvent(healthAttributeInformationResponse.capture());
        verify(mockMyHealthRepository).persistHealthAttributeTipResponse(healthAttributeInformationResponse.capture());
        assertNotNull(healthAttributeInformationResponse.getValue());
        HealthAttributeInformationResponse healthAttributeInformationResponse = this.healthAttributeInformationResponse.getValue();
        assertThat(healthAttributeInformationResponse.sections.isEmpty(), is(false));
        assertThat(healthAttributeInformationResponse.sections.get(0).attributes.isEmpty(), is(false));
        assertThat(healthAttributeInformationResponse.sections.get(0).attributes.get(0).healthAttributeFeedbacks.isEmpty(), is(false));
        List<HealthAttributeInformationResponse.FeedbackTip> healthAttributeTips = healthAttributeInformationResponse.sections.get(0).attributes.get(0).healthAttributeFeedbacks.get(0).feedbackTips;
        assertThat(healthAttributeTips.get(0).note, isA(String.class));
        assertThat(healthAttributeTips.get(0).typeCode, isA(String.class));
        assertThat(healthAttributeTips.get(0).typeKey, isA(Integer.class));
        assertThat(healthAttributeTips.get(0).typeName, isA(String.class));
    }
}
