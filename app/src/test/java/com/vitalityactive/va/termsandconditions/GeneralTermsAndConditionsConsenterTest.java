package com.vitalityactive.va.termsandconditions;

import android.support.annotation.NonNull;

import com.vitalityactive.va.BaseTest;
import com.vitalityactive.va.SameThreadScheduler;
import com.vitalityactive.va.Scheduler;
import com.vitalityactive.va.constants.BooleanString;
import com.vitalityactive.va.constants.UserInstructions;
import com.vitalityactive.va.events.EventListener;
import com.vitalityactive.va.networking.RequestResult;
import com.vitalityactive.va.networking.WebServiceClient;
import com.vitalityactive.va.networking.model.CaptureLoginEventServiceRequest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import junitparams.naming.TestCaseName;
import okhttp3.Request;
import okhttp3.mockwebserver.RecordedRequest;
import retrofit2.Call;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(JUnitParamsRunner.class)
public class GeneralTermsAndConditionsConsenterTest extends BaseTest {

    @Mock
    Call<String> mockCall;

    @Mock
    private GeneralTermsAndConditionsInstructionRepository mockRepository;

    private static RequestResult agreeRequestResult;

    private static RequestResult disagreeRequestResult;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        eventDispatcher.addEventListener(TermsAndConditionsAgreeRequestCompletedEvent.class, new EventListener<TermsAndConditionsAgreeRequestCompletedEvent>() {
            @Override
            public void onEvent(TermsAndConditionsAgreeRequestCompletedEvent event) {
                agreeRequestResult = event.getRequestResult();
            }
        });
        eventDispatcher.addEventListener(TermsAndConditionsDisagreeRequestCompletedEvent.class, new EventListener<TermsAndConditionsDisagreeRequestCompletedEvent>() {
            @Override
            public void onEvent(TermsAndConditionsDisagreeRequestCompletedEvent event) {
                disagreeRequestResult = event.getRequestResult();
            }
        });
        agreeRequestResult = RequestResult.NONE;
    }

    @SuppressWarnings("unused")
    private Iterable<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {BooleanString.TRUE.value},
                {BooleanString.FALSE.value}
        });
    }

    @Test
    @Parameters(method = "data")
    public void it_calls_the_correct_service(String value) throws Exception {

        int expectedEventId = 55123;

        setUpMockWebServerWithSuccessResponse();
        TermsAndConditionsConsenter consenter = getTermsAndConditionsConsenter(new SameThreadScheduler(), expectedEventId);

        if (isTrue(value)) {
            consenter.agreeToTermsAndConditions();
        } else {
            consenter.disagreeToTermsAndConditions();
        }

        RecordedRequest request = mockWebServer.takeRequest(1, TimeUnit.SECONDS);
        assertEquals("/api/vitality-manage-user-service-v1/1.0/svc/2/captureLoginEvents/100002", request.getPath());
        assertEquals(AUTHORIZATION_HEADER, request.getHeader("Authorization"));
        assertNotNull(request.getHeader("session-Id"));
        assertNotNull(request.getHeader("correlation-Id"));
        assertEquals("application/json", request.getHeader("Content-Type"));
        assertEquals("en_ZA", request.getHeader("locale"));

        CaptureLoginEventServiceRequest actualRequest = getServiceRequestBody(CaptureLoginEventServiceRequest.class, getRecordedRequestBodyString(request));

        assertEquals(expectedEventId, actualRequest.userInstructionResponses[0].id);
        assertEquals(Long.parseLong(UserInstructions.Types.LOGIN_T_AND_C), actualRequest.userInstructionResponses[0].type);
        assertEquals(value, actualRequest.userInstructionResponses[0].value);
        assertEquals(isTrue(value), actualRequest.loginSuccess);
        assertEquals(NOW, actualRequest.loginDate);
    }

    private boolean isTrue(String value) {
        return value.equals(BooleanString.TRUE.value);
    }

    @SuppressWarnings("unused")
    private Iterable<Object[]> consentActions() {
        return Arrays.asList(new Object[][]{
                {new ConsentAction() {
                    @Override
                    public void execute(TermsAndConditionsConsenter consenter) {
                        consenter.agreeToTermsAndConditions();
                        consenter.agreeToTermsAndConditions();
                    }
                }, "agree", "agreeing"},
                {new ConsentAction() {
                    @Override
                    public void execute(TermsAndConditionsConsenter consenter) {
                        consenter.agreeToTermsAndConditions();
                        consenter.disagreeToTermsAndConditions();
                    }
                }, "disagree", "agreeing"},
                {new ConsentAction() {
                    @Override
                    public void execute(TermsAndConditionsConsenter consenter) {
                        consenter.disagreeToTermsAndConditions();
                        consenter.agreeToTermsAndConditions();
                    }
                }, "agree", "disagreeing"},
                {new ConsentAction() {
                    @Override
                    public void execute(TermsAndConditionsConsenter consenter) {
                        consenter.disagreeToTermsAndConditions();
                        consenter.disagreeToTermsAndConditions();
                    }
                }, "disagree", "disagreeing"}
        });
    }

    @Test
    @Parameters(method = "consentActions")
    @TestCaseName("It does not try to {1} to terms and conditions, while already {2}")
    @SuppressWarnings("unused")
    public void it_does_not_execute_consent_requests_while_one_is_in_progress(ConsentAction consentAction, String a, String b) throws Exception {
        testConsentActionsCannotBeExecutedWhileAnActionIsAlreadyInProgress(consentAction);
    }

    private void testConsentActionsCannotBeExecutedWhileAnActionIsAlreadyInProgress(ConsentAction consentAction) throws IOException {
        Scheduler mockScheduler = mock(Scheduler.class);

        setUpMockWebServerWithSuccessResponse();
        TermsAndConditionsConsenter consenter = getTermsAndConditionsConsenter(mockScheduler);

        consentAction.execute(consenter);

        verify(mockScheduler, times(1)).schedule(any(Runnable.class));
    }

    interface ConsentAction {
        void execute(TermsAndConditionsConsenter consenter);
    }

    @Test
    public void it_reports_agree_success_correctly() throws Exception {
        setUpMockWebServerWithSuccessResponse();
        TermsAndConditionsConsenter consenter = getTermsAndConditionsConsenter(new SameThreadScheduler());

        consenter.agreeToTermsAndConditions();

        assertEquals(RequestResult.SUCCESSFUL, agreeRequestResult);
        assertEquals(RequestResult.SUCCESSFUL, consenter.getAgreeRequestResult());
        verify(mockRepository).removeInstruction();
    }

    @Test
    public void it_reports_agree_generic_error_correctly() throws Exception {
        setUpMockWebServer(500, "{}");
        TermsAndConditionsConsenter consenter = getTermsAndConditionsConsenter(new SameThreadScheduler());

        consenter.agreeToTermsAndConditions();

        assertEquals(RequestResult.GENERIC_ERROR, agreeRequestResult);
        assertEquals(RequestResult.GENERIC_ERROR, consenter.getAgreeRequestResult());
    }

    @Test
    public void it_reports_agree_generic_exception_correctly() throws Exception {
        TermsAndConditionsConsenter consenter = getTermsAndConditionsConsenter(new SameThreadScheduler(), 12345, getMockServiceThrowingAnException(new IOException("What a mess")));

        consenter.agreeToTermsAndConditions();

        assertEquals(RequestResult.GENERIC_ERROR, agreeRequestResult);
        assertEquals(RequestResult.GENERIC_ERROR, consenter.getAgreeRequestResult());
    }

    @Test
    public void it_reports_agree_connection_error_correctly() throws Exception {
        TermsAndConditionsConsenter consenter = getTermsAndConditionsConsenter(new SameThreadScheduler(), 12345, getMockServiceThrowingAnException(new UnknownHostException()));

        consenter.agreeToTermsAndConditions();

        assertEquals(RequestResult.CONNECTION_ERROR, agreeRequestResult);
        assertEquals(RequestResult.CONNECTION_ERROR, consenter.getAgreeRequestResult());
    }

    @Test
    public void it_reports_disagree_success_correctly() throws Exception {
        setUpMockWebServerWithSuccessResponse();
        TermsAndConditionsConsenter consenter = getTermsAndConditionsConsenter(new SameThreadScheduler());

        consenter.disagreeToTermsAndConditions();

        assertEquals(RequestResult.SUCCESSFUL, disagreeRequestResult);
        assertEquals(RequestResult.SUCCESSFUL, consenter.getDisagreeRequestResult());
    }

    @Test
    public void it_reports_disagree_generic_error_correctly() throws Exception {
        setUpMockWebServer(500, "{}");
        TermsAndConditionsConsenter consenter = getTermsAndConditionsConsenter(new SameThreadScheduler());

        consenter.disagreeToTermsAndConditions();

        assertEquals(RequestResult.GENERIC_ERROR, disagreeRequestResult);
        assertEquals(RequestResult.GENERIC_ERROR, consenter.getDisagreeRequestResult());
    }

    @Test
    public void it_reports_disagree_generic_exception_correctly() throws Exception {
        TermsAndConditionsConsenter consenter = getTermsAndConditionsConsenter(new SameThreadScheduler(), 12345, getMockServiceThrowingAnException(new IOException("What a mess")));

        consenter.disagreeToTermsAndConditions();

        assertEquals(RequestResult.GENERIC_ERROR, disagreeRequestResult);
        assertEquals(RequestResult.GENERIC_ERROR, consenter.getDisagreeRequestResult());
    }

    @Test
    public void it_reports_disagree_connection_error_correctly() throws Exception {
        TermsAndConditionsConsenter consenter = getTermsAndConditionsConsenter(new SameThreadScheduler(), 12345, getMockServiceThrowingAnException(new UnknownHostException()));

        consenter.disagreeToTermsAndConditions();

        assertEquals(RequestResult.CONNECTION_ERROR, disagreeRequestResult);
        assertEquals(RequestResult.CONNECTION_ERROR, consenter.getDisagreeRequestResult());
    }

    private CaptureLoginEventService getMockServiceThrowingAnException(IOException exception) throws IOException {
        CaptureLoginEventService mockService = mock(CaptureLoginEventService.class);
        when(mockService.getCaptureLoginEventRequest(anyString(), anyLong(), anyLong(), any(CaptureLoginEventServiceRequest.class))).thenReturn(mockCall);
        when(mockCall.execute()).thenThrow(exception);
        when(mockCall.request()).thenReturn(new Request.Builder().url("http://www.google.com").build());
        return mockService;
    }

    @NonNull
    private TermsAndConditionsConsenter getTermsAndConditionsConsenter(Scheduler scheduler) throws IOException {
        return getTermsAndConditionsConsenter(scheduler, 551657);
    }

    @NonNull
    private TermsAndConditionsConsenter getTermsAndConditionsConsenter(Scheduler scheduler, int eventId) throws IOException {
        return getTermsAndConditionsConsenter(scheduler, eventId, getService(CaptureLoginEventService.class, getMockWebServerBaseURL()));
    }

    @NonNull
    private TermsAndConditionsConsenter getTermsAndConditionsConsenter(Scheduler scheduler, long eventId, CaptureLoginEventService captureLoginEventService) {

        when(mockRepository.getInstructionId()).thenReturn(eventId);

        return new GeneralTermsAndConditionsConsenter(eventDispatcher, new CaptureLoginEventServiceClient(new WebServiceClient(scheduler, eventDispatcher), captureLoginEventService, getPartyInformationRepository(), mockTimeUtilities, mockAccessTokenAuthorizationProvider), mockRepository);
    }

}
