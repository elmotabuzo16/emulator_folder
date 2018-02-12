package com.vitalityactive.va.wellnessdevices.linking.refreshtoken;

import android.support.annotation.NonNull;

import com.vitalityactive.va.connectivity.ConnectivityListener;
import com.vitalityactive.va.events.EventDispatcher;
import com.vitalityactive.va.networking.RequestResult;
import com.vitalityactive.va.networking.WebServiceResponseParser;
import com.vitalityactive.va.wellnessdevices.landing.events.FetchDevicesResponseEvent;
import com.vitalityactive.va.wellnessdevices.landing.service.GetFullListResponse;
import com.vitalityactive.va.wellnessdevices.landing.service.WellnessDevicesServiceClient;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class WellnessDevicesRefreshTokenInteractorImpl
        implements WellnessDevicesRefreshTokenInteractor, WebServiceResponseParser<GetFullListResponse> {
    private final EventDispatcher eventDispatcher;
    private final ConnectivityListener connectivityListener;
    private final WellnessDevicesServiceClient wellnessDevicesServiceClient;

    private boolean isRequestRunning;
    private boolean isTokenUpdated;

    @Inject
    public WellnessDevicesRefreshTokenInteractorImpl(@NonNull EventDispatcher eventDispatcher,
                                                     @NonNull ConnectivityListener connectivityListener,
                                                     @NonNull WellnessDevicesServiceClient wellnessDevicesServiceClient) {
        this.eventDispatcher = eventDispatcher;
        this.connectivityListener = connectivityListener;
        this.wellnessDevicesServiceClient = wellnessDevicesServiceClient;
    }

    /**
     * Implementation of WellnessDevicesRefreshTokenInteractor
     */
    @Override
    public void sendRefreshTokenRequest() {
        isTokenUpdated = false;
        if (connectivityListener.isOnline()) {
            isRequestRunning = true;
            wellnessDevicesServiceClient.getFullList(this);
        } else {
            sendErrorToDispatcher();
        }
    }

    @Override
    public void sendRefreshTokenRequest(WebServiceResponseParser<GetFullListResponse> listener) {
        isTokenUpdated = false;
        if (connectivityListener.isOnline()) {
            isRequestRunning = true;
            wellnessDevicesServiceClient.getFullList(listener);
        } else {
            listener.handleConnectionError();
        }
    }

    @Override
    public boolean isRequestRunning() {
        return isRequestRunning;
    }

    @Override
    public boolean isTokenUpdated() {
        return isTokenUpdated;
    }

    /**
     * WebServiceResponseParser<GetFullListResponse>
     */
    @Override
    public void parseResponse(GetFullListResponse response) {
        isTokenUpdated = true;
        isRequestRunning = false;
        eventDispatcher.dispatchEvent(new FetchDevicesResponseEvent(RequestResult.SUCCESSFUL, response));
    }

    @Override
    public void parseErrorResponse(String errorBody, int code) {
        sendErrorToDispatcher();
    }

    @Override
    public void handleGenericError(Exception exception) {
        sendErrorToDispatcher();
    }

    @Override
    public void handleConnectionError() {
        sendErrorToDispatcher();
    }

    private void sendErrorToDispatcher(){
        isTokenUpdated = false;
        isRequestRunning = false;
        eventDispatcher.dispatchEvent(new FetchDevicesResponseEvent(RequestResult.GENERIC_ERROR));
    }
}
