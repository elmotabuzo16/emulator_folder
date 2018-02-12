package com.vitalityactive.va.termsandconditions;

import android.support.annotation.NonNull;

import com.vitalityactive.va.BuildConfig;
import com.vitalityactive.va.EventServiceClient;
import com.vitalityactive.va.ExecutorServiceScheduler;
import com.vitalityactive.va.LanguageProvider;
import com.vitalityactive.va.NetworkingTestUtilities;
import com.vitalityactive.va.PartyInformationRepository;
import com.vitalityactive.va.RetryRule;
import com.vitalityactive.va.SameThreadScheduler;
import com.vitalityactive.va.Scheduler;
import com.vitalityactive.va.appconfig.AppConfigRepository;
import com.vitalityactive.va.authentication.AccessTokenAuthorizationProvider;
import com.vitalityactive.va.cms.CMSService;
import com.vitalityactive.va.cms.CMSServiceClient;
import com.vitalityactive.va.events.EventDispatcher;
import com.vitalityactive.va.networking.RequestResult;
import com.vitalityactive.va.networking.WebServiceClient;
import com.vitalityactive.va.networking.model.CaptureLoginEventServiceRequest;
import com.vitalityactive.va.utilities.TimeUtilities;
import com.vitalityactive.va.vhc.addproof.ProofImageFetcher;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.net.UnknownHostException;

import okhttp3.Request;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import retrofit2.Call;
import retrofit2.Retrofit;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

public class TermsAndConditionsPresenterTest {
    private SameThreadScheduler scheduler;
    private TermsAndConditionsPresenterImpl<TermsAndConditionsPresenter.UserInterface> presenter;

    @Mock
    private TermsAndConditionsPresenter.UserInterface mockUserInterface;

    @Mock
    private CMSServiceClient mockCMSServiceClient;

    @Mock
    private EventServiceClient mockEventServiceClient;

    @Mock
    private TermsAndConditionsConsenter mockTermsAndConditionsConsenter;

    @Mock
    private LanguageProvider mockLanguageProvider;

    @Mock
    private GeneralTermsAndConditionsInstructionRepositoryImpl mockRepository;

    @Mock
    private TimeUtilities mockTimeUtilities;

    @Mock
    AccessTokenAuthorizationProvider mockAccessTokenAuthorizationProvider;

    private EventDispatcher eventDispatcher = new EventDispatcher();

    private MockWebServer mockWebServer;
    @Mock
    private AppConfigRepository mockAppConfigRepository;
    @Mock
    private PartyInformationRepository mockPartyInformationRepository;
    @Mock
    private ProofImageFetcher mockProofImageFetcher;
    @Mock
    private ExecutorServiceScheduler mockExecutorServiceScheduler;
    @Mock
    private Call<String> mockCall;

    @Rule
    public RetryRule retryRule = new RetryRule();

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        scheduler = new SameThreadScheduler();

        when(mockAppConfigRepository.getLiferayGroupId()).thenReturn("83709");
        when(mockLanguageProvider.getCurrentAppLocale()).thenReturn("en_US");
        when(mockAccessTokenAuthorizationProvider.getAuthorization()).thenReturn("authorization_header");

        when(mockTimeUtilities.getNowAsISODateTimeWithZoneId()).thenReturn("2016-12-06T10:30:40+02:00[Africa/Johannesburg]");

        TermsAndConditionsInteractorImpl interactor = getTermsAndConditionsInteractor();

        setUpPresenter(interactor, mockTermsAndConditionsConsenter);
    }

    @After
    public void tearDown() throws Exception {
        if (mockWebServer != null) {
            mockWebServer.shutdown();
        }
    }

    @Test
    public void it_shows_the_correct_terms_and_conditions() throws Exception {
        String expected = "Expected terms and conditions";

        presenter.onUserInterfaceAppeared();
        presenter.onTermsAndConditionsRetrieved(expected);

        verify(mockUserInterface).showTermsAndConditions(expected);
        assertTrue(scheduler.invoked);
    }

    @Test
    public void agree_button_is_disabled_while_agree_request_is_in_progress() {
        presenter.onUserAgreesToTermsAndConditions();

        verify(mockUserInterface).disableAgreeButton();
    }

    @Test
    public void user_interface_navigates_after_agree_request_completes() {
        presenter.onUserInterfaceCreated(true);
        presenter.onUserInterfaceAppeared();
        eventDispatcher.dispatchEvent(new TermsAndConditionsAgreeRequestCompletedEvent(RequestResult.SUCCESSFUL));

        verify(mockUserInterface).navigateAfterTermsAndConditionsAccepted();
        assertTrue(scheduler.invoked);
    }

    @Test
    public void it_handles_successful_agree_correctly() throws Exception {
        setUpMockWebServerWithSuccessResponse();
        setUpPresenter();
        presenter.onUserInterfaceCreated(true);

        presenter.onUserInterfaceAppeared();
        presenter.onUserAgreesToTermsAndConditions();

        verify(mockUserInterface, atLeastOnce()).showLoadingIndicator();
        verify(mockUserInterface, atLeastOnce()).disableAgreeButton();
        verify(mockUserInterface).navigateAfterTermsAndConditionsAccepted();
        assertTrue(scheduler.invoked);
    }

    @Test
    public void it_handles_generic_agree_error_correctly() throws Exception {
        setUpMockWebServer("{}", 404);
        setUpPresenter();
        presenter.onUserInterfaceCreated(true);

        presenter.onUserInterfaceAppeared();
        presenter.onUserAgreesToTermsAndConditions();

        verify(mockUserInterface, atLeastOnce()).hideLoadingIndicator();
        verify(mockUserInterface, atLeastOnce()).enableAgreeButton();
        verify(mockUserInterface).showGenericAgreeRequestErrorMessage();
        assertTrue(scheduler.invoked);
    }

    @Test
    public void it_handles_connection_agree_error_correctly() throws Exception {
        setUpPresenter(getTermsAndConditionsInteractor(), getTermsAndConditionsConsenter(new SameThreadScheduler(), 12345, getMockCaptureLoginEventsServiceThrowingAnException(new UnknownHostException())));
        presenter.onUserInterfaceCreated(true);

        presenter.onUserInterfaceAppeared();
        presenter.onUserAgreesToTermsAndConditions();

        verify(mockUserInterface, atLeastOnce()).hideLoadingIndicator();
        verify(mockUserInterface, atLeastOnce()).enableAgreeButton();
        verify(mockUserInterface).showConnectionAgreeRequestErrorMessage();
        assertTrue(scheduler.invoked);
    }

    @Test
    public void it_shows_disagree_alert_on_back_pressed() throws Exception {
        setUpMockWebServerWithSuccessResponse();
        presenter.onUserInterfaceAppeared();
        presenter.onBackPressed();

        verify(mockUserInterface).showDisagreeAlert();
    }

    @Test
    public void it_handles_successful_disagree_correctly() throws Exception {
        setUpMockWebServerWithSuccessResponse();
        setUpPresenter();

        presenter.onUserInterfaceCreated(true);
        presenter.onUserInterfaceAppeared();
        presenter.onUserDisagreesToTermsAndConditions();

        verify(mockUserInterface, atLeastOnce()).showLoadingIndicator();
        verify(mockUserInterface, atLeastOnce()).disableAgreeButton();
        verify(mockUserInterface).navigateAfterTermsAndConditionsDeclined();
        assertTrue(scheduler.invoked);
    }

    @Test
    public void it_handles_generic_disagree_error_correctly() throws Exception {
        setUpMockWebServerWithErrorResponse();
        setUpPresenter();
        presenter.onUserInterfaceCreated(true);

        presenter.onUserInterfaceAppeared();
        presenter.onUserDisagreesToTermsAndConditions();

        verify(mockUserInterface, atLeastOnce()).hideLoadingIndicator();
        verify(mockUserInterface, atLeastOnce()).enableAgreeButton();
        verify(mockUserInterface).showGenericDisagreeRequestErrorMessage();
        assertTrue(scheduler.invoked);
    }

    @Test
    public void it_handles_connection_disagree_error_correctly() throws Exception {
        setUpPresenter(getTermsAndConditionsInteractor(), getTermsAndConditionsConsenter(new SameThreadScheduler(), 12345, getMockCaptureLoginEventsServiceThrowingAnException(new UnknownHostException())));
        presenter.onUserInterfaceCreated(true);

        presenter.onUserInterfaceAppeared();
        presenter.onUserDisagreesToTermsAndConditions();

        verify(mockUserInterface, atLeastOnce()).hideLoadingIndicator();
        verify(mockUserInterface, atLeastOnce()).enableAgreeButton();
        verify(mockUserInterface).showConnectionDisagreeRequestErrorMessage();
        assertTrue(scheduler.invoked);
    }

    @Test
    public void it_removes_loading_indicator_and_enables_agree_button_if_no_requests_are_in_progress_when_the_user_interface_appears() throws Exception {

        TermsAndConditionsConsenter mockConsenter = mock(TermsAndConditionsConsenter.class);
        when(mockConsenter.getAgreeRequestResult()).thenReturn(RequestResult.NONE);
        setUpPresenter(getTermsAndConditionsInteractor(), mockConsenter);

        presenter.onUserInterfaceAppeared();

        verify(mockUserInterface).hideLoadingIndicator();
    }

    @Test
    public void it_adds_loading_indicator_and_disables_agree_button_if_agree_request_is_in_progress_when_the_user_interface_appears() throws Exception {
        setUpMockWebServerWithSuccessResponse();
        TermsAndConditionsConsenter consenter = getTermsAndConditionsConsenterWithMockScheduler();
        setUpPresenter(getTermsAndConditionsInteractor(), consenter);

        consenter.agreeToTermsAndConditions();

        presenter.onUserInterfaceAppeared();

        verify(mockUserInterface).showLoadingIndicator();
        verify(mockUserInterface).disableAgreeButton();
    }

    @Test
    public void it_adds_loading_indicator_and_disables_agree_button_if_disagree_request_is_in_progress_when_the_user_interface_appears() throws Exception {
        setUpMockWebServerWithSuccessResponse();
        TermsAndConditionsConsenter consenter = getTermsAndConditionsConsenterWithMockScheduler();
        setUpPresenter(getTermsAndConditionsInteractor(), consenter);

        consenter.disagreeToTermsAndConditions();

        presenter.onUserInterfaceAppeared();

        verify(mockUserInterface).showLoadingIndicator();
        verify(mockUserInterface).disableAgreeButton();
    }

    @Test
    public void user_interface_is_not_invoked_if_it_has_disappeared_before_scheduling_completes() throws Exception {
        setUpMockWebServerWithSuccessResponse();

        Scheduler mockScheduler = mock(Scheduler.class);
        setUpPresenter(getTermsAndConditionsInteractor(), getTermsAndConditionsConsenterWithMockScheduler(), mockScheduler);

        presenter.onUserInterfaceAppeared();

        eventDispatcher.dispatchEvent(new TermsAndConditionsAgreeRequestCompletedEvent(RequestResult.SUCCESSFUL));
        eventDispatcher.dispatchEvent(new TermsAndConditionsAgreeRequestCompletedEvent(RequestResult.GENERIC_ERROR));
        eventDispatcher.dispatchEvent(new TermsAndConditionsAgreeRequestCompletedEvent(RequestResult.CONNECTION_ERROR));

        presenter.onUserInterfaceDisappeared(false);

        verify(mockUserInterface).hideLoadingIndicator();
        verify(mockUserInterface).showTermsAndConditions(anyString());

        verifyNoMoreInteractions(mockUserInterface);
    }

    @Test
    public void it_navigates_the_user_interface_if_agree_request_succeeded_in_the_background() throws Exception {
        TermsAndConditionsConsenter consenter = givenSuccessResponse();
        presenter.onUserInterfaceCreated(true);

        consenter.agreeToTermsAndConditions();

        presenter.onUserInterfaceAppeared();

        verify(mockUserInterface).navigateAfterTermsAndConditionsAccepted();
    }

    @Test
    public void generic_error_alert_is_shown_if_agree_request_failed_in_the_background() throws Exception {
        TermsAndConditionsConsenter consenter = givenErrorResponse();
        presenter.onUserInterfaceCreated(true);

        consenter.agreeToTermsAndConditions();

        presenter.onUserInterfaceAppeared();

        verify(mockUserInterface).showGenericAgreeRequestErrorMessage();
    }

    @Test
    public void connection_error_alert_is_shown_if_agree_request_failed_in_the_background() throws Exception {
        TermsAndConditionsConsenter consenter = getTermsAndConditionsConsenter(new SameThreadScheduler(), 12345, getMockCaptureLoginEventsServiceThrowingAnException(new UnknownHostException()));
        setUpPresenter(getTermsAndConditionsInteractor(), consenter);
        presenter.onUserInterfaceCreated(true);

        consenter.agreeToTermsAndConditions();

        presenter.onUserInterfaceAppeared();

        verify(mockUserInterface).showConnectionAgreeRequestErrorMessage();
    }

    @Test
    public void it_navigates_the_user_interface_if_disagree_request_succeeded_in_the_background() throws Exception {
        TermsAndConditionsConsenter consenter = givenSuccessResponse();
        presenter.onUserInterfaceCreated(true);

        consenter.disagreeToTermsAndConditions();

        presenter.onUserInterfaceAppeared();

        verify(mockUserInterface).navigateAfterTermsAndConditionsDeclined();
    }

    @Test
    public void generic_error_alert_is_shown_if_disagree_request_failed_in_the_background() throws Exception {
        TermsAndConditionsConsenter consenter = givenErrorResponse();
        presenter.onUserInterfaceCreated(true);

        consenter.disagreeToTermsAndConditions();

        presenter.onUserInterfaceAppeared();

        verify(mockUserInterface).showGenericDisagreeRequestErrorMessage();
    }

    @Test
    public void connection_error_alert_is_shown_if_disagree_request_failed_in_the_background() throws Exception {
        TermsAndConditionsConsenter consenter = getTermsAndConditionsConsenter(new SameThreadScheduler(), 12345, getMockCaptureLoginEventsServiceThrowingAnException(new UnknownHostException()));
        setUpPresenter(getTermsAndConditionsInteractor(), consenter);
        presenter.onUserInterfaceCreated(true);

        consenter.disagreeToTermsAndConditions();

        presenter.onUserInterfaceAppeared();

        verify(mockUserInterface).showConnectionDisagreeRequestErrorMessage();
    }

    @Test
    public void error_alert_is_not_shown_again_if_agree_failed_in_the_background_and_it_has_already_been_shown_when_the_ui_was_recreated() throws Exception {
        TermsAndConditionsConsenter consenter = givenErrorResponse();

        presenter.onUserInterfaceCreated(true);

        consenter.agreeToTermsAndConditions();

        presenter.onUserInterfaceAppeared();
        presenter.onUserInterfaceDisappeared(false);
        presenter.onUserInterfaceAppeared();

        verify(mockUserInterface, times(1)).showGenericAgreeRequestErrorMessage();
    }

    @Test
    public void error_alert_is_shown_again_after_another_agree_attempt_completes_in_the_foreground() throws Exception {
        TermsAndConditionsConsenter consenter = givenErrorResponse();
        presenter.onUserInterfaceCreated(true);

        enqueueErrorResponse();

        consenter.agreeToTermsAndConditions();

        presenter.onUserInterfaceAppeared();

        presenter.onUserAgreesToTermsAndConditions();

        verify(mockUserInterface, times(2)).showGenericAgreeRequestErrorMessage();
    }

    @Test
    public void error_alert_is_shown_again_after_another_disagree_attempt_completes_in_the_foreground() throws Exception {
        TermsAndConditionsConsenter consenter = givenErrorResponse();
        presenter.onUserInterfaceCreated(true);

        enqueueErrorResponse();

        consenter.agreeToTermsAndConditions();

        presenter.onUserInterfaceAppeared();

        presenter.onUserDisagreesToTermsAndConditions();

        verify(mockUserInterface).showGenericAgreeRequestErrorMessage();
        verify(mockUserInterface).showGenericDisagreeRequestErrorMessage();
    }

    @Test
    public void agree_button_is_disabled_and_loading_indicator_is_shown_if_terms_and_conditions_have_not_been_downloaded_when_the_user_interface_appears() throws Exception {
        setUpMockWebServerWithSuccessResponse();
        setUpPresenter(getTermsAndConditionsInteractor(getCMSServiceClientWithMockScheduler()), getTermsAndConditionsConsenter(new SameThreadScheduler()));

        presenter.onUserInterfaceCreated(true);
        presenter.onUserInterfaceAppeared();

        verify(mockUserInterface).disableAgreeButton();
        verify(mockUserInterface, atLeastOnce()).showLoadingIndicator();
    }

    @Test
    public void loading_indicator_is_removed_and_agree_button_is_enabled_when_terms_and_conditions_request_completes() throws Exception {
        presenter.onUserInterfaceAppeared();

        presenter.onTermsAndConditionsRetrieved("blah blah");

        verify(mockUserInterface, atLeastOnce()).hideLoadingIndicator();
        verify(mockUserInterface, atLeastOnce()).enableAgreeButton();
    }

    @Test
    public void it_hides_loading_indicator_when_user_interface_appears_and_there_was_a_request_error() throws Exception {
        TermsAndConditionsConsenter consenter = givenErrorResponse();

        consenter.agreeToTermsAndConditions();

        presenter.onUserInterfaceAppeared();

        verify(mockUserInterface).hideLoadingIndicator();
    }

    @Test
    public void error_alert_is_not_shown_again_when_the_ui_appears_if_an_agree_request_error_occurred_in_the_foreground() throws Exception {
        setUpMockWebServerWithErrorResponse();
        setUpPresenter();
        presenter.onUserInterfaceCreated(true);

        presenter.onUserInterfaceAppeared();
        presenter.onUserAgreesToTermsAndConditions();

        presenter.onUserInterfaceAppeared();

        verify(mockUserInterface).showGenericAgreeRequestErrorMessage();
    }

    @Test
    public void error_alert_is_not_shown_again_when_the_ui_appears_if_a_disagree_request_error_occurred_in_the_foreground() throws Exception {
        setUpMockWebServerWithErrorResponse();
        setUpPresenter();
        presenter.onUserInterfaceCreated(true);

        presenter.onUserInterfaceAppeared();
        presenter.onUserDisagreesToTermsAndConditions();

        presenter.onUserInterfaceAppeared();

        verify(mockUserInterface).showGenericDisagreeRequestErrorMessage();
    }

    @Test
    public void generic_error_message_is_shown_and_loading_indicator_is_hidden_if_content_request_failed_in_the_foreground() throws Exception {
        setUpMockWebServerWithErrorResponse();
        setUpPresenter(getTermsAndConditionsInteractor(getCMSServiceClient(new SameThreadScheduler(), getMockWebServerBaseURL())), getTermsAndConditionsConsenter(new SameThreadScheduler()));

        presenter.onUserInterfaceCreated(true);
        presenter.onUserInterfaceAppeared();

        verify(mockUserInterface).showGenericContentRequestErrorMessage();
        verify(mockUserInterface).hideLoadingIndicator();
    }

    @Test
    public void connection_error_message_is_shown_and_loading_indicator_is_hidden_if_content_request_failed_in_the_foreground() throws Exception {
        setUpPresenter(getTermsAndConditionsInteractor(getCMSServiceClient(scheduler, getMockCMSServiceThrowingAnException(new UnknownHostException()))), mock(TermsAndConditionsConsenter.class));

        presenter.onUserInterfaceCreated(true);
        presenter.onUserInterfaceAppeared();

        verify(mockUserInterface).showConnectionContentRequestErrorMessage();
        verify(mockUserInterface).hideLoadingIndicator();
    }

    @Test
    public void user_interface_is_not_invoked_if_content_error_occurs_in_the_background() throws Exception {
        presenter.onUserInterfaceDisappeared(false);

        presenter.onGenericContentError();
        presenter.onConnectionContentError();

        verifyNoMoreInteractions(mockUserInterface);
    }

    @Test
    public void user_interface_is_not_invoked_if_content_request_completes_successfully_in_the_background() throws Exception {
        presenter.onUserInterfaceDisappeared(false);

        presenter.onTermsAndConditionsRetrieved("Blarg");

        verifyNoMoreInteractions(mockUserInterface);
    }

    @Test
    public void generic_error_message_is_shown_if_content_request_failed_in_the_background() throws Exception {
        TermsAndConditionsInteractorImpl interactor = givenContentErrorResponse();

        interactor.fetchTermsAndConditions();

        presenter.onUserInterfaceAppeared();

        verify(mockUserInterface).showGenericContentRequestErrorMessage();
        verify(mockUserInterface).disableAgreeButton();
    }

    @Test
    public void connection_error_message_is_shown_if_content_request_failed_in_the_background() throws Exception {
        TermsAndConditionsInteractorImpl interactor = getTermsAndConditionsInteractor(getCMSServiceClient(scheduler, getMockCMSServiceThrowingAnException(new UnknownHostException())));
        setUpPresenter(interactor, mock(TermsAndConditionsConsenter.class));

        interactor.fetchTermsAndConditions();

        presenter.onUserInterfaceAppeared();

        verify(mockUserInterface).showConnectionContentRequestErrorMessage();
        verify(mockUserInterface).disableAgreeButton();
    }

    @Test
    public void content_is_shown_if_content_request_completed_successfully_in_the_background() throws Exception {
        String expectedContent = "This is the content we expect to show";
        setUpMockWebServer(expectedContent, 200);
        TermsAndConditionsInteractorImpl interactor = getTermsAndConditionsInteractor(getCMSServiceClient(new SameThreadScheduler(), getMockWebServerBaseURL()));
        setUpPresenter(interactor, getTermsAndConditionsConsenter(new SameThreadScheduler()));

        interactor.fetchTermsAndConditions();

        presenter.onUserInterfaceAppeared();

        verify(mockUserInterface).showTermsAndConditions(expectedContent);
    }

    @Test
    public void error_alert_is_not_shown_again_if_content_request_failed_in_the_background_and_it_has_already_been_shown_when_the_ui_was_recreated() throws Exception {
        TermsAndConditionsInteractorImpl interactor = givenContentErrorResponse();

        interactor.fetchTermsAndConditions();

        presenter.onUserInterfaceAppeared();
        presenter.onUserInterfaceAppeared();

        verify(mockUserInterface, times(1)).showGenericContentRequestErrorMessage();
    }

    @Test
    public void error_alert_is_not_shown_again_when_the_ui_appears_if_a_content_request_error_occurred_in_the_foreground() throws Exception {
        givenContentErrorResponse();

        presenter.onUserInterfaceCreated(true);
        presenter.onUserInterfaceAppeared();

        verify(mockUserInterface, times(1)).showGenericContentRequestErrorMessage();
    }

    @Test
    public void error_alert_is_shown_again_after_another_content_request_completes_in_the_background() throws Exception {
        givenContentErrorResponse();

        enqueueErrorResponse();

        presenter.onUserInterfaceCreated(true);

        presenter.onUserInterfaceAppeared();
        presenter.onUserInterfaceDisappeared(false);

        presenter.onUserInterfaceCreated(true);
        presenter.onUserInterfaceAppeared();

        verify(mockUserInterface, times(2)).showGenericContentRequestErrorMessage();
    }

    @Test
    public void loading_indicator_is_shown_when_terms_and_conditions_are_fetched() throws Exception {
        presenter.fetchTermsAndConditions();

        verify(mockUserInterface).showLoadingIndicator();
    }

    private void enqueueErrorResponse() {
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(500)
                .setBody("{}"));
    }

    private void setUpPresenter(TermsAndConditionsInteractorImpl interactor, TermsAndConditionsConsenter termsAndConditionsConsenter) {
        setUpPresenter(interactor, termsAndConditionsConsenter, scheduler);
    }

    @NonNull
    private TermsAndConditionsInteractorImpl getTermsAndConditionsInteractor() {
        return getTermsAndConditionsInteractor(mockCMSServiceClient);
    }

    @NonNull
    private TermsAndConditionsConsenter getTermsAndConditionsConsenterWithMockScheduler() throws IOException {
        return getTermsAndConditionsConsenter(mock(Scheduler.class));
    }

    @NonNull
    private TermsAndConditionsConsenter givenErrorResponse() throws IOException {
        return givenResponseCode(500);
    }

    @NonNull
    private TermsAndConditionsConsenter givenSuccessResponse() throws IOException {
        return givenResponseCode(201);
    }

    @NonNull
    private TermsAndConditionsConsenter givenResponseCode(int responseCode) throws IOException {
        setUpMockWebServer("{}", responseCode);
        TermsAndConditionsConsenter consenter = getTermsAndConditionsConsenterWithSameThreadScheduler();
        setUpPresenter(getTermsAndConditionsInteractor(), consenter);
        return consenter;
    }

    @NonNull
    private TermsAndConditionsInteractorImpl givenContentErrorResponse() throws IOException {
        setUpMockWebServerWithErrorResponse();
        TermsAndConditionsInteractorImpl interactor = getTermsAndConditionsInteractor(getCMSServiceClient(new SameThreadScheduler(), getMockWebServerBaseURL()));
        setUpPresenter(interactor, getTermsAndConditionsConsenter(new SameThreadScheduler()));
        return interactor;
    }

    private void setUpMockWebServerWithErrorResponse() throws IOException {
        setUpMockWebServer("{}", 500);
    }

    private CMSService getMockCMSServiceThrowingAnException(UnknownHostException exception) throws IOException {
        CMSService mockService = mock(CMSService.class);
        when(mockService.getContentRequest(anyString(), anyString(), anyString())).thenReturn(mockCall);
        when(mockCall.execute()).thenThrow(exception);
        when(mockCall.request()).thenReturn(new Request.Builder().url("http://www.google.com").build());
        return mockService;
    }

    private TermsAndConditionsInteractorImpl getTermsAndConditionsInteractor(CMSServiceClient cmsServiceClient) {
        return new TermsAndConditionsInteractorImpl(cmsServiceClient, eventDispatcher, "31240", "83709");
    }

    private CMSServiceClient getCMSServiceClient(Scheduler scheduler) {
        return getCMSServiceClient(scheduler, BuildConfig.TEST_BASE_URL);
    }

    @NonNull
    private CMSServiceClient getCMSServiceClient(Scheduler scheduler, String baseUrl) {
        return getCMSServiceClient(scheduler, NetworkingTestUtilities.getRetrofit(baseUrl).create(CMSService.class));
    }

    @NonNull
    private CMSServiceClient getCMSServiceClient(Scheduler scheduler, CMSService cmsService) {
        return new CMSServiceClient(new WebServiceClient(scheduler, eventDispatcher), cmsService, eventDispatcher, mockAccessTokenAuthorizationProvider, mockPartyInformationRepository, mockProofImageFetcher, mockExecutorServiceScheduler);
    }

    private CMSServiceClient getCMSServiceClientWithMockScheduler() {
        return getCMSServiceClient(mock(Scheduler.class));
    }

    @NonNull
    private TermsAndConditionsConsenter getTermsAndConditionsConsenterWithSameThreadScheduler() throws IOException {
        return getTermsAndConditionsConsenter(new SameThreadScheduler());
    }

    private void setUpPresenter(TermsAndConditionsInteractor interactor, TermsAndConditionsConsenter consenter, Scheduler scheduler) {
        presenter = new TermsAndConditionsPresenterImpl<>(scheduler, interactor, consenter, eventDispatcher);
        presenter.setUserInterface(mockUserInterface);
    }

    private void setUpPresenter() throws IOException {
        setUpPresenter(getTermsAndConditionsInteractor(), getTermsAndConditionsConsenterWithSameThreadScheduler());
    }

    private void setUpMockWebServerWithSuccessResponse() throws IOException {
        setUpMockWebServer("{}", 201);
    }

    private void setUpMockWebServer(String body, int responseCode) throws IOException {
        mockWebServer = new MockWebServer();

        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(responseCode)
                .setBody(body));

        mockWebServer.start();
    }

    @NonNull
    private TermsAndConditionsConsenter getTermsAndConditionsConsenter(Scheduler scheduler) throws IOException {
        return getTermsAndConditionsConsenter(scheduler, 551657);
    }

    @NonNull
    private TermsAndConditionsConsenter getTermsAndConditionsConsenter(Scheduler scheduler, int eventId) throws IOException {
        Retrofit retrofit = NetworkingTestUtilities.getRetrofit(getMockWebServerBaseURL());
        CaptureLoginEventService captureLoginEventService = retrofit.create(CaptureLoginEventService.class);

        return getTermsAndConditionsConsenter(scheduler, eventId, captureLoginEventService);
    }

    private String getMockWebServerBaseURL() {
        return mockWebServer.url("api/").toString();
    }

    @NonNull
    private TermsAndConditionsConsenter getTermsAndConditionsConsenter(Scheduler scheduler, long eventId, CaptureLoginEventService captureLoginEventService) {
        PartyInformationRepository mockPartyInformationRepository = mock(PartyInformationRepository.class);
        when(mockPartyInformationRepository.getTenantId()).thenReturn(2L);
        when(mockPartyInformationRepository.getPartyId()).thenReturn(100002L);

        when(mockRepository.getInstructionId()).thenReturn(eventId);

        return new GeneralTermsAndConditionsConsenter(eventDispatcher, new CaptureLoginEventServiceClient(new WebServiceClient(scheduler, eventDispatcher), captureLoginEventService, mockPartyInformationRepository, mockTimeUtilities, mockAccessTokenAuthorizationProvider), mockRepository);
    }

    private CaptureLoginEventService getMockCaptureLoginEventsServiceThrowingAnException(IOException exception) throws IOException {
        CaptureLoginEventService mockService = mock(CaptureLoginEventService.class);
        when(mockService.getCaptureLoginEventRequest(anyString(), anyLong(), anyLong(), any(CaptureLoginEventServiceRequest.class))).thenReturn(mockCall);
        when(mockCall.execute()).thenThrow(exception);
        when(mockCall.request()).thenReturn(new Request.Builder().url("http://www.google.com").build());
        return mockService;
    }

}
