package com.vitalityactive.va.myhealth.vitalityage;


import com.vitalityactive.va.InsurerConfigurationRepository;
import com.vitalityactive.va.NetworkingTestUtilities;
import com.vitalityactive.va.PartyInformationRepository;
import com.vitalityactive.va.SameThreadScheduler;
import com.vitalityactive.va.TestUtilities;
import com.vitalityactive.va.authentication.AccessTokenAuthorizationProvider;
import com.vitalityactive.va.events.EventDispatcher;
import com.vitalityactive.va.myhealth.events.VitalityAgeEvents;
import com.vitalityactive.va.myhealth.service.VitalityAgeService;
import com.vitalityactive.va.myhealth.service.VitalityAgeServiceClient;
import com.vitalityactive.va.networking.WebServiceClient;
import com.vitalityactive.va.networking.model.VitalityAgeResponse;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.Date;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import retrofit2.Call;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


public class VitalityAgeServiceClientTest {

    @Mock
    public WebServiceClient webServiceClient;
    @Mock
    public AccessTokenAuthorizationProvider accessTokenAuthorizationProvider;
    @Mock
    public PartyInformationRepository partyInformationRepository;
    @Mock
    public Call<VitalityAgeResponse> vitalityAgeServiceRequest;
    @Mock
    public InsurerConfigurationRepository insurerConfigurationRepository;
    @Mock
    EventDispatcher mockEventDispatcher;
    @Mock
    VitalityAgeService vitalityAgeService;
    VitalityAgeServiceClient vitalityAgeServiceClient;
    private MockWebServer server;

    @Before
    public void setUp() throws IOException {
        MockitoAnnotations.initMocks(this);

        server = new MockWebServer();

        String baseURL = server.url("api/").toString();

        final WebServiceClient webServiceClient =
                new WebServiceClient(new SameThreadScheduler(), new EventDispatcher());

        final VitalityAgeService vitalityAgeService =
                NetworkingTestUtilities.getRetrofit(baseURL, "expectedSessionId", "en_ZA").create(VitalityAgeService.class);

        final PartyInformationRepository partyInformationRepository = mock(PartyInformationRepository.class);
        when(partyInformationRepository.getTenantId()).thenReturn(10002L);
        when(partyInformationRepository.getPartyId()).thenReturn(2L);

        final AccessTokenAuthorizationProvider accessTokenAuthorizationProvider = mock(AccessTokenAuthorizationProvider.class);
        when(accessTokenAuthorizationProvider.getAuthorization()).thenReturn("RandomToken");

        when(insurerConfigurationRepository.getCurrentMembershipPeriodStart()).thenReturn(new Date());
        when(insurerConfigurationRepository.getCurrentMembershipPeriodEnd()).thenReturn(new Date());

        vitalityAgeServiceClient = new VitalityAgeServiceClient(webServiceClient,
                partyInformationRepository,
                vitalityAgeService,
                accessTokenAuthorizationProvider, insurerConfigurationRepository, mockEventDispatcher);

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
    public void get_vitality_age_value_response_is_parsed() throws Exception {
        setUpMockWebServer("get_health_attribute.json");
        vitalityAgeServiceClient.getVitalityAgeValue();
        verify(mockEventDispatcher, times(1)).dispatchEvent(any(VitalityAgeEvents.VitalityAgeResponseEvent.class));
    }

    @Test
    public void can_handle_empty_response() throws Exception {
        setUpMockWebServer("get_health_attribute_empty_response.json");
        vitalityAgeServiceClient.getVitalityAgeValue();
        verify(mockEventDispatcher, times(1)).dispatchEvent(any(VitalityAgeEvents.VitalityAgeResponseEvent.class));
    }

}
