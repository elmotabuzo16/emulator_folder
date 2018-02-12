package com.vitalityactive.va.wellnessdevices.landing.events;

import com.vitalityactive.va.networking.RequestResult;
import com.vitalityactive.va.wellnessdevices.BaseWellnessDeviceEvent;
import com.vitalityactive.va.wellnessdevices.landing.service.GetFullListResponse;

import static com.vitalityactive.va.wellnessdevices.landing.WellnessDevicesLandingInteractor.WD_FETCH_DEVICE_LIST;

public class FetchDevicesResponseEvent extends BaseWellnessDeviceEvent<GetFullListResponse> {
    public FetchDevicesResponseEvent(RequestResult requestResult) {
        super(requestResult, WD_FETCH_DEVICE_LIST);
    }

    public FetchDevicesResponseEvent(RequestResult requestResult,
                                     GetFullListResponse responseBody) {
        super(requestResult, responseBody, null, WD_FETCH_DEVICE_LIST);
    }

    public FetchDevicesResponseEvent(RequestResult requestResult,
                                     GetFullListResponse responseBody,
                                     String redirectUrl) {
        super(requestResult, responseBody, redirectUrl, WD_FETCH_DEVICE_LIST);
    }

}
