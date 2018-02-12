package com.vitalityactive.va;

import android.content.res.Resources;
import android.support.annotation.CallSuper;
import android.support.test.InstrumentationRegistry;

import com.google.gson.Gson;
import com.vitalityactive.va.dependencyinjection.DefaultModule;
import com.vitalityactive.va.dependencyinjection.ModuleCollection;
import com.vitalityactive.va.dependencyinjection.NetworkModule;
import com.vitalityactive.va.dependencyinjection.PersistenceModule;
import com.vitalityactive.va.networking.ServiceGenerator;

import org.junit.Before;
import org.junit.Rule;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import retrofit2.Retrofit;
import retrofit2.mock.MockRetrofit;
import retrofit2.mock.NetworkBehavior;

public abstract class BaseAndroidTest {
    @Rule
    public MockWebServer mockWebServer = new MockWebServer();

    protected Resources resources;
    protected MockRetrofit mockRetrofit;
    protected PersistenceModule persistenceModule;

    @Before
    @CallSuper
    public void setUp() {
        resources = InstrumentationRegistry.getTargetContext().getResources();
    }

    protected void setUpRealNetworkModuleWithResponseCode(int responseCode) {
        setUpRealNetworkModuleWithMockWebServerUrl();
        mockWebServer.enqueue(new MockResponse().setResponseCode(responseCode));
    }

    protected void setUpRealNetworkModuleWithResponseCode(int responseCode, String responseBody) {
        setUpRealNetworkModuleWithMockWebServerUrl();
        mockWebServer.enqueue(new MockResponse().setResponseCode(responseCode).setBody(responseBody));
    }

    protected void setUpRealNetworkModuleWithMockWebServerUrl() {
        setUpNetworkModuleAndLaunchActivity(mockWebServer.url("").toString());
    }

    protected void setUpNetworkModuleAndLaunchActivity(final String baseUrl) {
        setUpNetworkModuleAndLaunchActivity(new NetworkModule(baseUrl, UUID.randomUUID().toString()) {
                                                @Override
                                                public ServiceGenerator provideServiceGenerator(OkHttpClient httpClient, Gson gson, DeviceSpecificPreferences preferences) {
                                                    return new ServiceGenerator(baseUrl, httpClient, gson, preferences) {
                                                        @Override
                                                        public void setBaseUrl(String baseUrl, String basicAuthToken) {
                                                            // Don't override base Url because we may be pointing to mock web server
                                                        }
                                                    };
                                                }
                                            },
                new DefaultModule(MockJUnitRunner.getInstance().getApplication()));
    }

    protected void setUpNetworkModuleAndLaunchActivity(NetworkModule networkModule, DefaultModule defaultModule) {
        setUpNetworkModule(networkModule, defaultModule);
        launchActivity();
    }

    protected abstract void launchActivity();

    protected void setUpNetworkModule(NetworkModule networkModule, DefaultModule defaultModule) {
        Retrofit retrofit = networkModule.provideServiceGenerator(networkModule.provideHTTPClient(new LanguageProvider(resources), new AppConfigRepositoryWithFixedVersionNumber(InstrumentationRegistry.getTargetContext())), networkModule.provideGson(), defaultModule.provideDeviceSpecificPreferences()).getRetrofit();

        NetworkBehavior networkBehavior = NetworkBehavior.create();
        networkBehavior.setDelay(0, TimeUnit.SECONDS);
        mockRetrofit = new MockRetrofit.Builder(retrofit)
                .networkBehavior(networkBehavior)
                .build();

        ModuleCollection moduleCollection = MockJUnitRunner.getModuleCollection();
        moduleCollection.defaultModule = defaultModule;
        moduleCollection.networkModule = networkModule;
        if (persistenceModule != null) {
            moduleCollection.persistenceModule = persistenceModule;
        }
        MockJUnitRunner.initialiseTestObjectGraph(moduleCollection);
    }
}
