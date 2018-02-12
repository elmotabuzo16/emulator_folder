package com.vitalityactive.va.wellnessdevices.landing;

import android.support.annotation.StringDef;

import com.vitalityactive.va.wellnessdevices.landing.events.DeviceActivityMappingResponseEvent;
import com.vitalityactive.va.wellnessdevices.pointsmonitor.events.PotentialPointsResponseEvent;

import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.SOURCE;

public interface WellnessDevicesLandingInteractor {

    void fetchDeviceList(Callback callback);

    @Retention(SOURCE)
    @StringDef({
            WD_FETCH_DEVICE_LIST,
            WD_FETCH_DEVICE_ACTIVITY_MAPPING,
            WD_GET_POTENTIAL_POINTS
    })
    @interface RequestType {}
    String WD_FETCH_DEVICE_LIST = "WD_FETCH_DEVICE_LIST";
    String WD_FETCH_DEVICE_ACTIVITY_MAPPING = "WD_FETCH_DEVICE_ACTIVITY_MAPPING";
    String WD_GET_POTENTIAL_POINTS = "WD_GET_POTENTIAL_POINTS";

    void fetchDeviceList();

    void fetchDeviceActivityMapping();
    DeviceActivityMappingResponseEvent getDeviceActivityMappingRequestResult();
    void clearDeviceActivityMappingRequestResult();

    void loadDeviceDetails(int typeKey);
    PotentialPointsResponseEvent getPotentialPointsRequestResult();
    void clearPotentialPointsRequestResult();

    boolean isRequestInProgress();

    interface Callback {
        void deviceListFetched();
    }
}
