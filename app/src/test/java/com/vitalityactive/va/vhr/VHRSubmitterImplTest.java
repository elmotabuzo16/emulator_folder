package com.vitalityactive.va.vhr;

import com.vitalityactive.va.InsurerConfigurationRepository;
import com.vitalityactive.va.NetworkingTestUtilities;
import com.vitalityactive.va.PartyInformationRepository;
import com.vitalityactive.va.SameThreadScheduler;
import com.vitalityactive.va.TestUtilities;
import com.vitalityactive.va.authentication.AccessTokenAuthorizationProvider;
import com.vitalityactive.va.events.EventDispatcher;
import com.vitalityactive.va.networking.RequestResult;
import com.vitalityactive.va.networking.WebServiceClient;
import com.vitalityactive.va.networking.WebServiceResponseParser;
import com.vitalityactive.va.questionnaire.QuestionnaireStateManager;
import com.vitalityactive.va.shared.questionnaire.service.QuestionnaireSubmissionService;
import com.vitalityactive.va.shared.questionnaire.service.QuestionnaireSubmissionServiceClient;
import com.vitalityactive.va.shared.questionnaire.service.QuestionnaireSubmitResponse;
import com.vitalityactive.va.vhr.service.VHRSubmitterImpl;
import com.vitalityactive.va.myhealth.dto.VitalityAgeRepository;
import com.vitalityactive.va.myhealth.entity.VitalityAge;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.Date;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import retrofit2.Call;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class VHRSubmitterImplTest {

    @Mock
    private QuestionnaireStateManager questionnaireStateManager;
    @Mock
    private EventDispatcher eventDispatcher;

    @Mock
    public WebServiceClient webServiceClient;
    @Mock
    public AccessTokenAuthorizationProvider accessTokenAuthorizationProvider;
    @Mock
    public PartyInformationRepository partyInformationRepository;
    @Mock
    public Call<QuestionnaireSubmitResponse> questionnaireSubmitResponseCall;
    @Mock
    public InsurerConfigurationRepository insurerConfigurationRepository;
    @Mock
    EventDispatcher mockEventDispatcher;
    @Mock
    QuestionnaireSubmissionService questionnaireSubmissionService;
    @Captor
    private ArgumentCaptor<VitalityAge> eventDispatcherArgumentCaptor;

    @Mock
    private WebServiceResponseParser<EventDispatcher> vhrSubmitterClass;

    private MockWebServer server;

    QuestionnaireSubmissionServiceClient questionnaireSubmissionServiceClient;
    private RequestResult submissionRequestResult;
    VHRSubmitterImpl vHRSubmitterImpl;

    @Mock
    VitalityAgeRepository vitalityAgeRepository;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        server = new MockWebServer();

        String baseURL = server.url("api/").toString();

        final WebServiceClient webServiceClient =
                new WebServiceClient(new SameThreadScheduler(), new EventDispatcher());

        final QuestionnaireSubmissionService questionnaireSubmissionService =
                NetworkingTestUtilities.getRetrofit(baseURL, "expectedSessionId", "en_ZA").create(QuestionnaireSubmissionService.class);

        final PartyInformationRepository partyInformationRepository = mock(PartyInformationRepository.class);
        when(partyInformationRepository.getTenantId()).thenReturn(10002L);
        when(partyInformationRepository.getPartyId()).thenReturn(2L);

        final AccessTokenAuthorizationProvider accessTokenAuthorizationProvider = mock(AccessTokenAuthorizationProvider.class);
        when(accessTokenAuthorizationProvider.getAuthorization()).thenReturn("RandomToken");

        when(insurerConfigurationRepository.getCurrentMembershipPeriodStart()).thenReturn(new Date());
        when(insurerConfigurationRepository.getCurrentMembershipPeriodEnd()).thenReturn(new Date());

        questionnaireSubmissionServiceClient = new QuestionnaireSubmissionServiceClient(accessTokenAuthorizationProvider,
                questionnaireSubmissionService,
                webServiceClient,
                partyInformationRepository);

        vHRSubmitterImpl = new VHRSubmitterImpl(questionnaireStateManager,
                questionnaireSubmissionServiceClient,
                mockEventDispatcher,vitalityAgeRepository);

        questionnaireSubmissionServiceClient = new QuestionnaireSubmissionServiceClient(accessTokenAuthorizationProvider,
                questionnaireSubmissionService,
                webServiceClient,
                partyInformationRepository);
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
    public void can_parse_capture_assessment_feedback() throws Exception {
        setUpMockWebServer("capture_assessment_response.json");
        vHRSubmitterImpl.submit(1l);
        assertNotNull(vHRSubmitterImpl.getSubmissionRequestResult());
        assertTrue(vHRSubmitterImpl.getSubmissionRequestResult().equals(RequestResult.SUCCESSFUL));
        verify(vitalityAgeRepository, times(1)).saveVitalityAgeValue(eventDispatcherArgumentCaptor.capture());
        assertNotNull(eventDispatcherArgumentCaptor.getValue());
    }
}
