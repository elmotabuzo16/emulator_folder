package com.vitalityactive.va.pointsmonitor;

import com.vitalityactive.va.BaseTest;
import com.vitalityactive.va.NetworkingTestUtilities;
import com.vitalityactive.va.PartyInformationRepository;
import com.vitalityactive.va.SameThreadScheduler;
import com.vitalityactive.va.authentication.AccessTokenAuthorizationProvider;
import com.vitalityactive.va.dto.PointsEntryCategoryDTO;
import com.vitalityactive.va.events.EventListener;
import com.vitalityactive.va.networking.WebServiceClient;
import com.vitalityactive.va.networking.model.PointsHistoryServiceRequest;
import com.vitalityactive.va.utilities.TimeUtilities;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import okhttp3.mockwebserver.RecordedRequest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(JUnitParamsRunner.class)
public class PointsMonitorPresenterTest extends BaseTest {

    private static final long EXPECTED_TENANT_ID = 2;
    private static final String EXPECTED_VITALITY_MEMBERSHIP_ID = "100001";
    private static final int JANUARY = 1;
    private static final int JUNE = 6;
    private static final int AUGUST = 8;
    private static final int CURRENT_MONTH = AUGUST;
    private static final int LAST_INDEX = 35;
    private static final PointsEntryCategoryDTO AVAILABLE_CATEGORY_1 = new PointsEntryCategoryDTO(1, "Blabla");
    private static final PointsEntryCategoryDTO AVAILABLE_CATEGORY_2 = new PointsEntryCategoryDTO(2, "Ladida");
    private static final PointsEntryCategoryDTO AVAILABLE_CATEGORY_3 = new PointsEntryCategoryDTO(3, "Blarg");
    private static final PointsEntryCategoryDTO AVAILABLE_CATEGORY_4 = new PointsEntryCategoryDTO(4, "Hodor");
    private static final PointsEntryCategoryDTO OTHER = new PointsEntryCategoryDTO(-1, "RandomCategoryNotInAvailableList");
    private static final String AUTHORIZATION_HEADER = "authorization_header";
    private PointsMonitorPresenter presenter;

    @Mock
    PointsMonitorPresenter.UserInterface mockUserInterface;
    @Mock
    PointsMonitorRepository mockPointsMonitorRepository;
    @Mock
    PointsMonitorInteractor mockPointsMonitorInteractor;

    @Captor
    ArgumentCaptor<List<PointsHistoryMonth>> monthsArgumentCaptor;

    @Mock
    private AccessTokenAuthorizationProvider mockAccessTokenAuthorizationProvider;

    private SameThreadScheduler mainThreadScheduler;
    private List<PointsEntryCategoryDTO> availableCategories = Arrays.asList(
            AVAILABLE_CATEGORY_1,
            AVAILABLE_CATEGORY_2,
            AVAILABLE_CATEGORY_3,
            AVAILABLE_CATEGORY_4
    );

    private int eventCount;
    @Mock
    private PointsMonitorContent mockContent;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        mainThreadScheduler = new SameThreadScheduler();
        when(mockAccessTokenAuthorizationProvider.getAuthorization()).thenReturn(AUTHORIZATION_HEADER);
        when(mockPointsMonitorRepository.getPointsEntryCategories()).thenReturn(availableCategories);
        initialiseAndroidThreeTen();
        eventCount = 0;
    }

    @Test
    public void months_are_shown_when_the_ui_is_created() {
        setUpPresenter(mockPointsMonitorRepository, NetworkingTestUtilities.getDefaultBaseURL(), new MockTimeUtilities());

        presenter.onUserInterfaceCreated(true);

        verify(mockUserInterface).showMonths(monthsArgumentCaptor.capture());

        List<PointsHistoryMonth> months = monthsArgumentCaptor.getValue();

        int indexJune2017 = CURRENT_MONTH - JUNE;
        assertEquals("Jun", months.get(indexJune2017).getMonthName());
        assertEquals("2017", months.get(indexJune2017).getYear());

        int indexJanuary2017 = CURRENT_MONTH - JANUARY;
        assertEquals("Jan", months.get(indexJanuary2017).getMonthName());
        assertEquals("2017", months.get(indexJanuary2017).getYear());

        assertEquals("Sep", months.get(LAST_INDEX).getMonthName());
        assertEquals("2014", months.get(LAST_INDEX).getYear());
    }

    @Test
    public void loading_indicator_is_shown_if_there_is_no_data_when_the_ui_appears() throws Exception {
        setUpPresenterAndMockWebServerWithSuccessResponse();

        presenter.onUserInterfaceAppeared();

        verify(mockUserInterface).showLoadingIndicator();
    }

    @Test
    public void always_load_data_on_interface_appeared() throws Exception {
        when(mockPointsMonitorRepository.hasPointsEntries()).thenReturn(true);

        setUpPresenter(mockPointsMonitorRepository);
        presenter = new PointsMonitorPresenterImpl(mainThreadScheduler, mockPointsMonitorInteractor, eventDispatcher, mockTimeUtilities, mockContent);
        presenter.setUserInterface(mockUserInterface);

        presenter.onUserInterfaceAppeared();

        verify(mockUserInterface, never()).hideLoadingIndicator();
    }

    @Test
    public void it_calls_the_correct_service() throws Exception {
        setUpPresenterAndMockWebServerWithSuccessResponse();

        presenter.onUserInterfaceAppeared();

        RecordedRequest request = mockWebServer.takeRequest(1, TimeUnit.SECONDS);
        assertEquals("/api/vitality-goals-points-services-service-v1/1.0/svc/" + EXPECTED_TENANT_ID + "/getAllPointsHistory/" + EXPECTED_VITALITY_MEMBERSHIP_ID, request.getPath());
        assertEquals(AUTHORIZATION_HEADER, request.getHeader("Authorization"));
        assertNotNull(request.getHeader("session-Id"));
        assertNotNull(request.getHeader("correlation-Id"));
        assertEquals("application/json", request.getHeader("Content-Type"));
        assertEquals("en_ZA", request.getHeader("locale"));

        PointsHistoryServiceRequest requestBody = getServiceRequestBody(PointsHistoryServiceRequest.class, getRecordedRequestBodyString(request));
        assertEquals(0, requestBody.getAllPointsHistoryRequest.pointsPeriodOffsets[0].value);
        assertEquals(1, requestBody.getAllPointsHistoryRequest.pointsPeriodOffsets[1].value);
    }

    @Test
    public void loading_indicator_is_hidden_after_a_successful_points_request() throws Exception {
        setUpPresenterAndMockWebServerWithSuccessResponse();

        presenter.onUserInterfaceAppeared();

        verify(mockUserInterface).hideLoadingIndicator();
        assertTrue(mainThreadScheduler.invoked);
    }

    @SuppressWarnings("unused")
    private Iterable<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {availableCategories, AVAILABLE_CATEGORY_1},
                {availableCategories, AVAILABLE_CATEGORY_2},
                {availableCategories, OTHER},
        });
    }

    @Test
    @Parameters(method = "data")
    public void selected_categories_event_is_broadcast_and_categories_are_filtered_correctly(List<PointsEntryCategoryDTO> availableCategories, PointsEntryCategoryDTO selectedCategory) throws Exception {
        setUpPresenterAndMockWebServerWithSuccessResponse();
        when(mockPointsMonitorRepository.getPointsEntryCategories()).thenReturn(availableCategories);

        EventListener<PointsMonitorCategoriesSelectedEvent> eventListener = new EventListener<PointsMonitorCategoriesSelectedEvent>() {
            @Override
            public void onEvent(PointsMonitorCategoriesSelectedEvent event) {
                ++eventCount;
            }
        };
        eventDispatcher.addEventListener(PointsMonitorCategoriesSelectedEvent.class, eventListener);
        presenter.onUserSelectsCategory(selectedCategory.getTypeKey());

        eventDispatcher.removeEventListener(PointsMonitorCategoriesSelectedEvent.class, eventListener);

        assertEquals(selectedCategory.getTypeKey(), presenter.getSelectedCategory().getTypeKey());
        assertEquals(1, eventCount);
    }

    @Test
    public void event_is_not_broadcast_if_selected_categories_are_the_same_as_the_currently_selected_ones() throws Exception {
        setUpPresenterAndMockWebServerWithSuccessResponse();

        EventListener<PointsMonitorCategoriesSelectedEvent> eventListener = new EventListener<PointsMonitorCategoriesSelectedEvent>() {
            @Override
            public void onEvent(PointsMonitorCategoriesSelectedEvent event) {
                ++eventCount;
            }
        };
        eventDispatcher.addEventListener(PointsMonitorCategoriesSelectedEvent.class, eventListener);
        presenter.onUserSelectsCategory(AVAILABLE_CATEGORY_3.getTypeKey());
        presenter.onUserSelectsCategory(AVAILABLE_CATEGORY_3.getTypeKey());

        eventDispatcher.removeEventListener(PointsMonitorCategoriesSelectedEvent.class, eventListener);

        assertEquals(1, eventCount);
    }

    private void setUpPresenterAndMockWebServerWithSuccessResponse() throws IOException {
        setUpMockWebServerWithSuccessResponse();
        setUpPresenterWithMockWebServerURL();
    }

    private void setUpPresenterWithMockWebServerURL() {
        setUpPresenter(getMockWebServerBaseURL());
    }

    private void setUpPresenter(String baseURL) {
        setUpPresenter(mockPointsMonitorRepository, baseURL, mockTimeUtilities);
    }

    private void setUpPresenter(PointsMonitorRepository pointsMonitorRepository) {
        setUpPresenter(pointsMonitorRepository, NetworkingTestUtilities.getDefaultBaseURL(), mockTimeUtilities);
    }

    private void setUpPresenter(PointsMonitorRepository pointsMonitorRepository, String baseURL, TimeUtilities timeUtilities) {
        PartyInformationRepository mockPartyInformationRepository = mock(PartyInformationRepository.class);
        when(mockPartyInformationRepository.getTenantId()).thenReturn(EXPECTED_TENANT_ID);
        when(mockPartyInformationRepository.getVitalityMembershipId()).thenReturn(EXPECTED_VITALITY_MEMBERSHIP_ID);
        presenter = new PointsMonitorPresenterImpl(mainThreadScheduler, new PointsMonitorInteractorImpl(pointsMonitorRepository, new PointsMonitorServiceClient(new WebServiceClient(new SameThreadScheduler(), eventDispatcher), getService(PointsMonitorService.class, baseURL), mockPartyInformationRepository, mockAccessTokenAuthorizationProvider), eventDispatcher), eventDispatcher, timeUtilities, mockContent);
        presenter.setUserInterface(mockUserInterface);
    }

    private static class MockTimeUtilities extends TimeUtilities {
        @Override
        public int getCurrentYear() {
            return 2017;
        }

        @Override
        public int getCurrentMonth() {
            return CURRENT_MONTH;
        }
    }
}
