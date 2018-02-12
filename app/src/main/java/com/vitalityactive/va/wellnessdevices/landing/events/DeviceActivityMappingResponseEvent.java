package com.vitalityactive.va.wellnessdevices.landing.events;

import com.vitalityactive.va.networking.RequestResult;
import com.vitalityactive.va.wellnessdevices.BaseWellnessDeviceEvent;

import static com.vitalityactive.va.wellnessdevices.landing.WellnessDevicesLandingInteractor.WD_FETCH_DEVICE_LIST;

public class DeviceActivityMappingResponseEvent extends BaseWellnessDeviceEvent<String> {
    public DeviceActivityMappingResponseEvent(RequestResult requestResult) {
        super(requestResult, WD_FETCH_DEVICE_LIST);
    }

    public DeviceActivityMappingResponseEvent(RequestResult requestResult,
                                              String responseBody) {
        super(requestResult, responseBody, null, WD_FETCH_DEVICE_LIST);
    }

    public DeviceActivityMappingResponseEvent(RequestResult requestResult,
                                              String responseBody,
                                              String redirectUrl) {
        super(requestResult, responseBody, redirectUrl, WD_FETCH_DEVICE_LIST);
    }

}
