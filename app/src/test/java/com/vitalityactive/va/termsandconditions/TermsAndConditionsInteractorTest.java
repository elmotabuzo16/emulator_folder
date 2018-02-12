package com.vitalityactive.va.termsandconditions;

import android.support.annotation.NonNull;

import com.vitalityactive.va.EventServiceClient;
import com.vitalityactive.va.ExecutorServiceScheduler;
import com.vitalityactive.va.LanguageProvider;
import com.vitalityactive.va.NetworkingTestUtilities;
import com.vitalityactive.va.PartyInformationRepository;
import com.vitalityactive.va.SameThreadScheduler;
import com.vitalityactive.va.TestUtilities;
import com.vitalityactive.va.appconfig.AppConfigRepository;
import com.vitalityactive.va.authentication.AccessTokenAuthorizationProvider;
import com.vitalityactive.va.cms.CMSService;
import com.vitalityactive.va.cms.CMSServiceClient;
import com.vitalityactive.va.events.EventDispatcher;
import com.vitalityactive.va.networking.WebServiceClient;
import com.vitalityactive.va.vhc.addproof.ProofImageFetcher;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TermsAndConditionsInteractorTest implements TermsAndConditionsInteractor.Callback {
    private static final String AUTHORIZATION_HEADER = "authorization_header";
    private ArrayList<String> values = new ArrayList<>();

    private EventDispatcher eventDispatcher;

    @Mock
    private CMSServiceClient mockCMSServiceClient;

    @Mock
    private LanguageProvider mockLanguageProvider;

    @Mock
    private EventServiceClient mockEventServiceClient;

    @Mock
    private AccessTokenAuthorizationProvider mockAccessTokenAuthorizationProvider;
    private MockWebServer mockWebServer;
    @Mock
    private AppConfigRepository mockAppConfigRepository;

    @Mock
    private PartyInformationRepository mockPartyInformationRepository;
    @Mock
    private ProofImageFetcher mockProofImageFetcher;
    @Mock
    private ExecutorServiceScheduler scheduler;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        eventDispatcher = new EventDispatcher();
        when(mockAppConfigRepository.getLiferayGroupId()).thenReturn("83709");
        when(mockLanguageProvider.getCurrentAppLocale()).thenReturn("en_US");
        when(mockAccessTokenAuthorizationProvider.getAuthorization()).thenReturn(AUTHORIZATION_HEADER);
        values.clear();
    }

    @After
    public void tearDown() throws Exception {
        if (mockWebServer != null) {
            mockWebServer.shutdown();
        }
    }

    @Test
    public void it_returns_the_correct_text() throws Exception {
        mockWebServer = new MockWebServer();

        String content = new TestUtilities().readFile("terms_and_conditions_successful.txt");
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(content));

        mockWebServer.start();

        TermsAndConditionsInteractorImpl interactor = new TermsAndConditionsInteractorImpl(new CMSServiceClient(new WebServiceClient(new SameThreadScheduler(), eventDispatcher), NetworkingTestUtilities.getRetrofit(mockWebServer.url("api/").toString()).create(CMSService.class), eventDispatcher, mockAccessTokenAuthorizationProvider, mockPartyInformationRepository, mockProofImageFetcher, scheduler), eventDispatcher, "31240", "83709");
        interactor.setCallback(this);

        interactor.fetchTermsAndConditions();

        assertEquals(content, values.get(0));

        RecordedRequest request = mockWebServer.takeRequest(1, TimeUnit.SECONDS);
        assertEquals("/api/liferay-content-management/1.0/content-service/get-article-by-url-title/groupId/83709/urlTitle/31240", request.getPath());
        assertEquals(AUTHORIZATION_HEADER, request.getHeader("Authorization"));
        assertNotNull(request.getHeader("session-Id"));
        assertNotNull(request.getHeader("correlation-Id"));
        assertEquals("application/json", request.getHeader("Content-Type"));
        assertEquals("en_ZA", request.getHeader("locale"));
    }

    public void onTermsAndConditionsRetrieved(@NonNull String termsAndConditions) {
        values.add(termsAndConditions);
    }

    @Override
    public void onGenericContentError() {

    }

    @Override
    public void onConnectionContentError() {

    }
}
