package com.vitalityactive.va.cucumber.utils;

import android.util.Log;

import com.vitalityactive.va.TestUtilities;
import com.vitalityactive.va.cucumber.TestData;
import com.vitalityactive.va.cucumber.testData.TestDataForRegistration;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.QueueDispatcher;
import okhttp3.mockwebserver.RecordedRequest;

public class MockNetworkHandler {
    private static final String TAG = "MockNetworkHandler";
    private static MockWebServer server;

    public static String getBaseUrl() {
        return server.url("api/").toString();
    }

    public static void start() {
        try {
            Log.i(TAG, "Starting a new MockWebServer");
            server = new MockWebServer();
            server.start();
            Log.d(TAG, "started server, base url: " + getBaseUrl());
        } catch (IOException e) {
            Log.e(TAG, "failed to start mock server", e);
        }
    }

    public static void shutdownAndDeallocateInstance() {
        shutdown();
        server = null;
    }

    public static void shutdown() {
        try {
            Log.i(TAG, "shutting down MockWebServer");
            server.shutdown();
        } catch (IOException e) {
            Log.e(TAG, "failed to shutdown mock server", e);
        }
    }

    public static String takeNextRequestUrl() throws InterruptedException {
        return takeNextRequest().getPath();
    }

    public static RecordedRequest takeNextRequest() throws InterruptedException {
        return server.takeRequest();
    }

    public static void ignoreRecordedRequests() {
        try {
            while (true) {
                RecordedRequest request = server.takeRequest(1, TimeUnit.MICROSECONDS);
                if (request == null)
                    return;
                Log.d(TAG, "ignoring request " + request.getPath());
            }
        } catch (InterruptedException ignored) {
            ignoreRecordedRequests();
        }
    }

    public static int getRequestCount() {
        return server.getRequestCount();
    }

    public static void setupMockServerIssues() {
        final Dispatcher dispatcher = new QueueDispatcher() {
            @Override
            public MockResponse dispatch(RecordedRequest request) throws InterruptedException {
                Log.d(TAG, "dispatching generic http 500 response");
                return new MockResponse().setResponseCode(500);
            }
        };
        server.setDispatcher(dispatcher);
    }

    public static void setupMockConnectionIssues() {
        final Dispatcher dispatcher = new QueueDispatcher() {
            @Override
            public MockResponse dispatch(RecordedRequest request) throws InterruptedException {
                Log.d(TAG, "throwing exception to simulate connection error");
                throw new InterruptedException("mock connection exception");
            }
        };
        server.setDispatcher(dispatcher);
    }

    public static void resetMockIssues() {
        server.setDispatcher(new QueueDispatcher());
    }

    public static void enqueueResponseFromFile(int code, String file) {
        String body = new TestUtilities().readFile(file);
        Log.d(TAG, String.format("enqueued %d response from %s", code, file));
        enqueueResponseFromText(code, body);
    }

    public static void enqueueResponseWithEmptyJsonObject(int code) {
        enqueueResponseFromText(code, "{}");
    }

    public static void enqueueResponseFromText(int code, String body) {
        MockResponse response = new MockResponse()
                .setResponseCode(code)
                .setBody(body);
        server.enqueue(response);
    }

    public static void enqueueResponseWithoutBody(int code) {
        server.enqueue(new MockResponse().setResponseCode(code));
    }

    public static void enqueueLoginResponse(String enteredUsername, String enteredPassword) {
        if (enteredUsername.equals(TestData.USERNAME) && enteredPassword.equals(TestData.PASSWORD)) {
            enqueueResponseFromFile(200, "login/Login_successful.json");
            enqueueResponseFromText(200, "empty-splash-screen");
        } else {
            enqueueResponseFromFile(400, "login/Login_invalid_details.json");
        }
    }

    public static void enqueueRegisterResponse(String email, String insurerCode) {
        if (email.equals(TestData.INCORRECT_USERNAME) || insurerCode.equals(TestDataForRegistration.INCORRECT_INSURER_CODE)) {
            enqueueResponseWithoutBody(400);
        }
    }
}
