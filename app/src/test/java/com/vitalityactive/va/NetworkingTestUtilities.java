package com.vitalityactive.va;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.vitalityactive.va.appconfig.AppConfigRepository;
import com.vitalityactive.va.dependencyinjection.NetworkModule;

import java.util.UUID;

import retrofit2.Retrofit;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class NetworkingTestUtilities {
    @NonNull
    private static NetworkModule getNetworkModule(String expectedSessionId, String baseUrl) {
        return new NetworkModule(baseUrl, expectedSessionId);
    }

    public static Retrofit getRetrofit(String baseURL, String sessionId, String locale) {
        NetworkModule networkModule = getNetworkModule(sessionId, baseURL);

        LanguageProvider mockLanguageProvider = mock(LanguageProvider.class);
        when(mockLanguageProvider.getCurrentAppLocale()).thenReturn(locale);

        AppConfigRepository mockAppConfigRepository = mock(AppConfigRepository.class);
        when(mockAppConfigRepository.getReleaseVersion()).thenReturn("0.0");

        DeviceSpecificPreferences mockPreferences = mock(DeviceSpecificPreferences.class);

        return networkModule.provideServiceGenerator(networkModule.provideHTTPClient(mockLanguageProvider, mockAppConfigRepository),
                networkModule.provideGson(), mockPreferences).getRetrofit();
    }

    public static Retrofit getRetrofit(String baseURL) {
        return getRetrofit(baseURL, UUID.randomUUID().toString(), "en_ZA");
    }

    public static Gson getGson() {
        return getNetworkModule().provideGson();
    }

    @NonNull
    private static NetworkModule getNetworkModule() {
        return getNetworkModule(UUID.randomUUID().toString(), "en_ZA");
    }

    public static String getDefaultBaseURL() {
        return BuildConfig.TEST_BASE_URL;
    }
}
