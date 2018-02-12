package com.vitalityactive.va.wellnessdevices.pointsmonitor.events;

import com.vitalityactive.va.networking.RequestResult;
import com.vitalityactive.va.wellnessdevices.BaseWellnessDeviceEvent;
import com.vitalityactive.va.wellnessdevices.pointsmonitor.service.EventType;

import static com.vitalityactive.va.wellnessdevices.landing.WellnessDevicesLandingInteractor.WD_GET_POTENTIAL_POINTS;

public class PotentialPointsResponseEvent extends BaseWellnessDeviceEvent<EventType> {
    public PotentialPointsResponseEvent(RequestResult requestResult) {
        super(requestResult, WD_GET_POTENTIAL_POINTS);
    }

    public PotentialPointsResponseEvent(RequestResult requestResult,
                                        EventType responseBody) {
        super(requestResult, responseBody, null, WD_GET_POTENTIAL_POINTS);
    }

    public PotentialPointsResponseEvent(RequestResult requestResult,
                                        EventType responseBody,
                                        String redirectUrl) {
        super(requestResult, responseBody, redirectUrl, WD_GET_POTENTIAL_POINTS);
    }

}
