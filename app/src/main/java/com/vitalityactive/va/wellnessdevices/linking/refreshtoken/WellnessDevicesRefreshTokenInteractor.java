package com.vitalityactive.va.wellnessdevices.linking.refreshtoken;

import com.vitalityactive.va.networking.WebServiceResponseParser;
import com.vitalityactive.va.wellnessdevices.landing.service.GetFullListResponse;

public interface WellnessDevicesRefreshTokenInteractor {
    void sendRefreshTokenRequest();
    void sendRefreshTokenRequest(WebServiceResponseParser<GetFullListResponse> listener);
    boolean isRequestRunning();
    boolean isTokenUpdated();
}
