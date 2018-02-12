package com.vitalityactive.va.dependencyinjection;

import android.os.Build;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.vitalityactive.va.BuildConfig;
import com.vitalityactive.va.DeviceSpecificPreferences;
import com.vitalityactive.va.LanguageProvider;
import com.vitalityactive.va.appconfig.AppConfigRepository;
import com.vitalityactive.va.networking.ServiceGenerator;
import com.vitalityactive.va.networking.parsing.ArrayAdapterFactory;
import com.vitalityactive.va.utilities.http.VACertificatePinner;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.CertificatePinner;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

@Module
public class NetworkModule {
    private String baseUrl;
    private final String sessionId;

    public NetworkModule(String baseUrl, String sessionId) {
        this.baseUrl = baseUrl;
        this.sessionId = sessionId;
    }

    @Provides
    @Singleton
    public ServiceGenerator provideServiceGenerator(OkHttpClient httpClient, Gson gson, DeviceSpecificPreferences preferences) {
        return new ServiceGenerator(baseUrl, httpClient, gson, preferences);
    }

    @Provides
    @Singleton
    public OkHttpClient provideHTTPClient(LanguageProvider languageProvider, AppConfigRepository appConfigRepository) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.addInterceptor(getHeaderInterceptor(languageProvider, appConfigRepository));
        setupLogging(builder);
        setupTimeouts(builder);
        disableRedirect(builder);
        VACertificatePinner.enableCertificatePinning(builder);
        return builder.build();
    }



    @NonNull
    private Interceptor getHeaderInterceptor(final LanguageProvider languageProvider, final AppConfigRepository appConfigRepository) {
        return new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                return chain.proceed(chain.request().newBuilder()
                        .header("session-id", sessionId)
                        .header("correlation-id", UUID.randomUUID().toString())
                        .header("locale", languageProvider.getCurrentAppLocale())
                        .header("User-Agent", getUserAgent(appConfigRepository.getReleaseVersion()))
                        .build());
            }
        };
    }

    private void setupLogging(OkHttpClient.Builder builder) {
        if (BuildConfig.DEBUG || BuildConfig.FLAVOR.equals("dev")) {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(interceptor);
        }
    }

    private void setupTimeouts(OkHttpClient.Builder builder) {
        builder.connectTimeout(120, TimeUnit.SECONDS);
        builder.readTimeout(120, TimeUnit.SECONDS);
    }

    private void disableRedirect(OkHttpClient.Builder builder) {
        builder.followRedirects(false);
        builder.followSslRedirects(false);
    }

    @Provides
    @Singleton
    public Gson provideGson() {
        return new GsonBuilder().registerTypeAdapterFactory(new ArrayAdapterFactory()).create();
    }

    @NonNull
    private String getUserAgent(String appConfigVersion) {
        String appConfigIdentifier = BuildConfig.APP_CONFIG_IDENTIFIER;
        String OSName = "Android";
        String androidBuildDetails = Build.MODEL + "; " + OSName + " " + Build.VERSION.RELEASE;
        return appConfigIdentifier + "/" + BuildConfig.VERSION_NAME + "/" + appConfigVersion + " (" + androidBuildDetails + ")";
    }
}
