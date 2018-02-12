package com.vitalityactive.va.wellnessdevices.linking.service;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.vitalityactive.va.BuildConfig;
import com.vitalityactive.va.dependencyinjection.NetworkModule;
import com.vitalityactive.va.networking.ResponseParserWithRedirect;
import com.vitalityactive.va.utilities.http.VACertificatePinner;

import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Singleton;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.CertificatePinner;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.internal.http.HttpMethod;
import okio.BufferedSink;

@Singleton
public class DelinkDeviceService {

    private final OkHttpClient client;

    @Inject
    public DelinkDeviceService() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        VACertificatePinner.enableCertificatePinning(builder);
        this.client = builder
                .followRedirects(true)
                .followSslRedirects(true)
                .build();
    }

    public void sendRequest(final String url,
                            final String method,
                            @NonNull ResponseParserWithRedirect<String> responseParser) {

        final Request request = new Request.Builder()
                .url(url)
                .method(method, getRequestBody(method))
                .build();

        client.newCall(request).enqueue(new DelinkDeviceCallback(responseParser));
    }

    public RequestBody getRequestBody(String method) {
        return HttpMethod.requiresRequestBody(method) ? requestBody : null;
    }

    private static class DelinkDeviceCallback implements Callback {
        private final ResponseParserWithRedirect<String> responseParser;

        DelinkDeviceCallback(ResponseParserWithRedirect<String> responseParser) {
            this.responseParser = responseParser;
        }

        @Override
        public void onFailure(Call call, IOException e) {
            responseParser.handleGenericError(e);
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            Log.d(">>>", call.toString() + " -> " + response.request());

            for (String header : response.headers().names()) {
                Log.d(">>>", header + " -> " + response.headers().get(header));
            }

            String location = response.header("Location");
            if (TextUtils.isEmpty(location)) {
                responseParser.parseResponse(response.body().string());
            } else {
                responseParser.handleRedirect(location);
            }
        }
    }

    // Empty RequestBody to support POST requests
    private static RequestBody requestBody = new RequestBody() {
        @Override
        public MediaType contentType() {
            return null;
        }

        @Override
        public void writeTo(BufferedSink sink) throws IOException {
            // NOP
        }
    };
}