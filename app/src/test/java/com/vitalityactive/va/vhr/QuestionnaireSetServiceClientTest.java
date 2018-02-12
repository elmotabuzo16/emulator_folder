package com.vitalityactive.va.vhr;

import com.vitalityactive.va.NetworkingTestUtilities;
import com.vitalityactive.va.PartyInformationRepository;
import com.vitalityactive.va.SameThreadScheduler;
import com.vitalityactive.va.TestUtilities;
import com.vitalityactive.va.authentication.AccessTokenAuthorizationProvider;
import com.vitalityactive.va.cms.keyvaluecontent.FallbackContentInputStreamLoader;
import com.vitalityactive.va.constants.Questionnaire;
import com.vitalityactive.va.constants.QuestionnaireSet;
import com.vitalityactive.va.events.EventDispatcher;
import com.vitalityactive.va.networking.WebServiceClient;
import com.vitalityactive.va.questionnaire.QuestionnaireSetRepository;
import com.vitalityactive.va.shared.questionnaire.service.QuestionnaireSetResponse;
import com.vitalityactive.va.shared.questionnaire.service.QuestionnaireSetService;
import com.vitalityactive.va.shared.questionnaire.service.QuestionnaireSetServiceClient;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.io.InputStream;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class QuestionnaireSetServiceClientTest {
    @Mock
    QuestionnaireSetRepository questionnaireSetRepository;

    private MockWebServer server;
    private QuestionnaireSetServiceClient questionnaireSetServiceClient;

    @Before
    public void setUp() throws IOException {
        MockitoAnnotations.initMocks(this);

        server = new MockWebServer();

        String baseURL = server.url("api/").toString();

        final WebServiceClient webServiceClient =
                new WebServiceClient(new SameThreadScheduler(), new EventDispatcher());

        final QuestionnaireSetService questionnaireSetService =
                NetworkingTestUtilities.getRetrofit(baseURL, "expectedSessionId", "en_ZA")
                        .create(QuestionnaireSetService.class);

        final PartyInformationRepository partyInformationRepository = mock(PartyInformationRepository.class);
        when(partyInformationRepository.getTenantId()).thenReturn(10002L);
        when(partyInformationRepository.getPartyId()).thenReturn(2L);
        when(partyInformationRepository.getVitalityMembershipId()).thenReturn("444");

        final AccessTokenAuthorizationProvider accessTokenAuthorizationProvider = mock(AccessTokenAuthorizationProvider.class);
        when(accessTokenAuthorizationProvider.getAuthorization()).thenReturn("HalloToken");

        final FallbackContentInputStreamLoader fallbackContentInputStreamLoader = mock(FallbackContentInputStreamLoader.class);
        when(fallbackContentInputStreamLoader.open("testfile.json")).thenReturn(new InputStream() {
            @Override
            public int read() throws IOException {
                return 0;
            }
        });

        questionnaireSetServiceClient =
                new QuestionnaireSetServiceClient(accessTokenAuthorizationProvider,
                        questionnaireSetService,
                        webServiceClient,
                        partyInformationRepository,
                        new EventDispatcher(),
                        fallbackContentInputStreamLoader
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
    public void if_health_attribute_response_given_response_is_persisted() throws IOException {
        setUpMockWebServer("vhr_questionnaire_progress_and_points_tracker_1.json");

        questionnaireSetServiceClient.fetchQuestionnaireSets(QuestionnaireSet._VHR, new Integer[]{Questionnaire._GENERALHEALTH,
                Questionnaire._LIFESTYLEHABITS,
                Questionnaire._SOCIALHABITS},
                questionnaireSetRepository);

        verify(questionnaireSetRepository, times(1)).persistQuestionnaireSetResponse(any(QuestionnaireSetResponse.class));
    }
}
