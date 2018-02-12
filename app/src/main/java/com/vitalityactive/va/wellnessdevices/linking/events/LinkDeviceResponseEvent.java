package com.vitalityactive.va.wellnessdevices.linking.events;

import com.vitalityactive.va.networking.RequestResult;
import com.vitalityactive.va.wellnessdevices.BaseWellnessDeviceEvent;
import com.vitalityactive.va.wellnessdevices.linking.WellnessDevicesLinkingInteractor;

public class LinkDeviceResponseEvent extends BaseWellnessDeviceEvent<String> {
    public LinkDeviceResponseEvent(RequestResult requestResult,
                                   @WellnessDevicesLinkingInteractor.RequestType String requestType) {
        super(requestResult, requestType);
    }

    public LinkDeviceResponseEvent(RequestResult requestResult,
                                   String responseBody,
                                   String redirectUrl,
                                   @WellnessDevicesLinkingInteractor.RequestType String requestType) {
        super(requestResult, responseBody, redirectUrl, requestType);
    }

    @Override
    public @WellnessDevicesLinkingInteractor.RequestType String getRequestType() {
        return requestType;
    }
}