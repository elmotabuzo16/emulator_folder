package com.vitalityactive.va;

import android.content.Context;
import android.content.res.AssetManager;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;

import com.jakewharton.threetenabp.AndroidThreeTen;
import com.vitalityactive.va.authentication.AccessTokenAuthorizationProvider;
import com.vitalityactive.va.events.EventDispatcher;
import com.vitalityactive.va.pointsmonitor.PointsMonitorPresenterTest;
import com.vitalityactive.va.utilities.TimeUtilities;

import org.junit.After;
import org.junit.Before;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.threeten.bp.zone.ZoneRulesException;

import java.io.IOException;
import java.util.UUID;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import retrofit2.Retrofit;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BaseTest {
    protected static final String NOW = "2016-12-12T12:30:40+02:00[Africa/Johannesburg]";
    protected static final String AUTHORIZATION_HEADER = "authorization_header";
    protected static EventDispatcher eventDispatcher;
    protected MockWebServer mockWebServer;

    @Mock
    protected TimeUtilities mockTimeUtilities;
    @Mock
    protected AccessTokenAuthorizationProvider mockAccessTokenAuthorizationProvider;

    protected SameThreadScheduler sameThreadScheduler;

    protected static void initialiseAndroidThreeTen() throws IOException {
        try {
            Context mockContext = mock(Context.class);
            AssetManager mockAssets = mock(AssetManager.class);
            when(mockAssets.open(anyString())).thenReturn(PointsMonitorPresenterTest.class.getClassLoader().getResourceAsStream("TZDB.dat"));
            when(mockContext.getAssets()).thenReturn(mockAssets);
            AndroidThreeTen.init(mockContext);
        } catch (ZoneRulesException ignored) {

        }
    }

    @Before
    @CallSuper
    public void setUp() throws Exception  {
        MockitoAnnotations.initMocks(this);
        eventDispatcher = new EventDispatcher();
        sameThreadScheduler = new SameThreadScheduler();
        when(mockAccessTokenAuthorizationProvider.getAuthorization()).thenReturn(AUTHORIZATION_HEADER);
        when(mockTimeUtilities.getNowAsISODateTimeWithZoneId()).thenReturn(NOW);
    }

    @After
    @CallSuper
    public void tearDown() throws Exception {
        if (mockWebServer != null) {
            mockWebServer.shutdown();
        }
    }

    protected void setUpMockWebServerWithSuccessResponse() throws IOException {
        setUpMockWebServer(201, "{}");
    }

    protected void setUpMockWebServer(int responseCode, String body) throws IOException {
        mockWebServer = new MockWebServer();

        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(responseCode)
                .setBody(body));
        mockWebServer.start();
    }

    protected Retrofit getRetrofit(String baseUrl) {
        return NetworkingTestUtilities.getRetrofit(baseUrl, UUID.randomUUID().toString(), "en_ZA");
    }

    @NonNull
    protected PartyInformationRepository getPartyInformationRepository() {
        PartyInformationRepository mockPartyInformationRepository = mock(PartyInformationRepository.class);
        when(mockPartyInformationRepository.getTenantId()).thenReturn(2L);
        when(mockPartyInformationRepository.getPartyId()).thenReturn(100002L);
        return mockPartyInformationRepository;
    }

    protected String getMockWebServerBaseURL() {
        return mockWebServer.url("api/").toString();
    }

    protected <T> T getService(Class<T> serviceClass) {
        return NetworkingTestUtilities.getRetrofit(getMockWebServerBaseURL()).create(serviceClass);
    }

    protected <T> T getService(Class<T> serviceClass, String baseURL) {
        return NetworkingTestUtilities.getRetrofit(baseURL).create(serviceClass);
    }

    protected <T> T getServiceRequestBody(Class<T> requestClass, String json) {
        return NetworkingTestUtilities.getGson().fromJson(json, requestClass);
    }

    @NonNull
    protected String getRecordedRequestBodyString(RecordedRequest request) {
        return request.getBody().readUtf8();
    }

}
