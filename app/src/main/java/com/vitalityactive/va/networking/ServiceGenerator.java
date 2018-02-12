package com.vitalityactive.va.networking;

import android.support.annotation.VisibleForTesting;
import android.util.Log;

import com.google.gson.Gson;
import com.vitalityactive.va.DeviceSpecificPreferences;
import com.vitalityactive.va.VitalityActiveApplication;
import com.vitalityactive.va.networking.parsing.StringConverterFactory;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceGenerator {
    private final Retrofit.Builder retrofitBuilder;
    private Retrofit retrofit;
    private DeviceSpecificPreferences preferences;

    public ServiceGenerator(String baseUrl, OkHttpClient httpClient, Gson gson, DeviceSpecificPreferences preferences) {
        this.preferences = preferences;
        retrofitBuilder = new Retrofit.Builder()
                .client(httpClient)
                .baseUrl(baseUrl)
                .addConverterFactory(new StringConverterFactory())
                .addConverterFactory(GsonConverterFactory.create(gson));
        retrofit = retrofitBuilder.build();
    }

    public void setBaseUrl(String baseUrl, String basicAuthToken) {
        if (VitalityActiveApplication.isDebugBuild()) {
            Log.d("ServiceGenerator", String.format("switched base url to %s with auth token %s", baseUrl, basicAuthToken));
        }
        retrofit = retrofitBuilder
                .baseUrl(baseUrl)
                .build();
        preferences.setCurrentBaseURL(baseUrl);
        preferences.setCurrentBasicAuthToken(basicAuthToken);
    }

    public <ServiceClass> ServiceClass create(Class<ServiceClass> serviceClass) {
        return retrofit.create(serviceClass);
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    public Retrofit getRetrofit() {
        return retrofit;
    }
}
