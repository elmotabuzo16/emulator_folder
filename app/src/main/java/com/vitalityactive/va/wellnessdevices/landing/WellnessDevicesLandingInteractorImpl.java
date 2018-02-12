package com.vitalityactive.va.wellnessdevices.landing;

import android.support.annotation.NonNull;
import android.util.Log;

import com.vitalityactive.va.appconfig.AppConfigRepository;
import com.vitalityactive.va.cms.CMSServiceClient;
import com.vitalityactive.va.connectivity.ConnectivityListener;
import com.vitalityactive.va.events.EventDispatcher;
import com.vitalityactive.va.networking.RequestResult;
import com.vitalityactive.va.networking.ResponseParserWithRedirect;
import com.vitalityactive.va.networking.WebServiceResponseParser;
import com.vitalityactive.va.wellnessdevices.BaseWellnessDeviceEvent;
import com.vitalityactive.va.wellnessdevices.Utils;
import com.vitalityactive.va.wellnessdevices.landing.events.DeviceActivityMappingResponseEvent;
import com.vitalityactive.va.wellnessdevices.landing.events.FetchDevicesResponseEvent;
import com.vitalityactive.va.wellnessdevices.landing.repository.DeviceListRepository;
import com.vitalityactive.va.wellnessdevices.landing.service.GetFullListResponse;
import com.vitalityactive.va.wellnessdevices.landing.service.WellnessDevicesServiceClient;
import com.vitalityactive.va.wellnessdevices.pointsmonitor.events.PotentialPointsResponseEvent;
import com.vitalityactive.va.wellnessdevices.pointsmonitor.service.EventType;
import com.vitalityactive.va.wellnessdevices.pointsmonitor.service.PotentialPointsServiceClient;

import java.io.IOException;
import java.util.Map;

import okhttp3.ResponseBody;

public class WellnessDevicesLandingInteractorImpl implements WellnessDevicesLandingInteractor {
    private final static String TAG = "LANDING";
    private final WellnessDevicesServiceClient wellnessDevicesServiceClient;
    private final EventDispatcher eventDispatcher;
    private final ConnectivityListener connectivityListener;
    private final DeviceListRepository deviceListRepository;
    @NonNull
    private final AppConfigRepository appConfigRepository;

    private final PotentialPointsServiceClient pointsService;
    private PotentialPointsResponseEvent potentialPointsResponseEvent = null;

    private final CMSServiceClient cmsServiceClient;
    private DeviceActivityMappingResponseEvent deviceActivityMappingReponseEvent = null;
    private Callback callback;

    public WellnessDevicesLandingInteractorImpl(@NonNull WellnessDevicesServiceClient wellnessDevicesServiceClient,
                                                @NonNull PotentialPointsServiceClient pointsService,
                                                @NonNull CMSServiceClient cmsServiceClient,
                                                @NonNull EventDispatcher eventDispatcher,
                                                @NonNull ConnectivityListener connectivityListener,
                                                @NonNull DeviceListRepository deviceListRepository,
                                                @NonNull AppConfigRepository appConfigRepository) {
        this.wellnessDevicesServiceClient = wellnessDevicesServiceClient;
        this.pointsService = pointsService;
        this.cmsServiceClient = cmsServiceClient;
        this.eventDispatcher = eventDispatcher;
        this.connectivityListener = connectivityListener;
        this.deviceListRepository = deviceListRepository;
        this.appConfigRepository = appConfigRepository;
    }

    @Override
    public void fetchDeviceList(Callback callback) {
        this.callback = callback;
        fetchDeviceList();
    }

    @Override
    public void fetchDeviceList() {
        if (connectivityListener.isOnline()) {
            wellnessDevicesServiceClient.getFullList(new DeviceListResponseParser());
        } else {
            passConnectionErrorToDispatcher(WD_FETCH_DEVICE_LIST);
            notifyCallback();
        }
    }

    private void notifyCallback() {
        if (callback != null) {
            callback.deviceListFetched();
            callback = null;
        }
    }

    @Override
    public void fetchDeviceActivityMapping() {
        clearDeviceActivityMappingRequestResult();
        if (connectivityListener.isOnline()) {
            cmsServiceClient.fetchPublicFile(appConfigRepository.getLiferayGroupId(), appConfigRepository.getWellnessDevicesActivityMappingFileName(), new DeviceActivityMappingResponseParser());
        } else {
            passConnectionErrorToDispatcher(WD_FETCH_DEVICE_ACTIVITY_MAPPING);
        }
    }

    @Override
    public DeviceActivityMappingResponseEvent getDeviceActivityMappingRequestResult() {
        return deviceActivityMappingReponseEvent;
    }

    @Override
    public void clearDeviceActivityMappingRequestResult() {
        deviceActivityMappingReponseEvent = null;
    }

    @Override
    public void loadDeviceDetails(int typeKey) {
        clearPotentialPointsRequestResult();
        if (connectivityListener.isOnline()) {
            pointsService.getPotentialPoints(typeKey, new PotentialPointsResponseParser());
        } else {
            passConnectionErrorToDispatcher(WD_GET_POTENTIAL_POINTS);
        }
    }

    @Override
    public PotentialPointsResponseEvent getPotentialPointsRequestResult() {
        return potentialPointsResponseEvent;
    }

    @Override
    public void clearPotentialPointsRequestResult() {
        potentialPointsResponseEvent = null;
    }

    @Override
    public boolean isRequestInProgress() {
        return wellnessDevicesServiceClient.isRequestInProgress() ||
                pointsService.isRequestInProgress() ||
                cmsServiceClient.isFetchingFileWithName(appConfigRepository.getWellnessDevicesActivityMappingFileName());
    }

    private void passConnectionErrorToDispatcher(@WellnessDevicesLandingInteractor.RequestType String requestType) {
        BaseWellnessDeviceEvent event;
        switch (requestType) {
            case WD_FETCH_DEVICE_LIST:
                event = new FetchDevicesResponseEvent(RequestResult.CONNECTION_ERROR);
                break;
            case WD_GET_POTENTIAL_POINTS:
                event = new PotentialPointsResponseEvent(RequestResult.CONNECTION_ERROR);
                break;
            case WD_FETCH_DEVICE_ACTIVITY_MAPPING:
                event = new DeviceActivityMappingResponseEvent(RequestResult.GENERIC_ERROR);
                break;
            default:
                return;
        }
        eventDispatcher.dispatchEvent(event);
    }

    private void passGenericErrorToDispatcher(@WellnessDevicesLandingInteractor.RequestType String requestType) {
        BaseWellnessDeviceEvent event;
        switch (requestType) {
            case WD_FETCH_DEVICE_LIST:
                event = new FetchDevicesResponseEvent(RequestResult.GENERIC_ERROR);
                break;
            case WD_GET_POTENTIAL_POINTS:
                event = new PotentialPointsResponseEvent(RequestResult.GENERIC_ERROR);
                break;
            case WD_FETCH_DEVICE_ACTIVITY_MAPPING:
                event = new DeviceActivityMappingResponseEvent(RequestResult.GENERIC_ERROR);
                break;
            default:
                return;
        }
        eventDispatcher.dispatchEvent(event);
    }

    private class DeviceListResponseParser implements WebServiceResponseParser<GetFullListResponse> {
        @Override
        public void parseResponse(GetFullListResponse response) {
            deviceListRepository.persistDeviceListResponse(response);
            eventDispatcher.dispatchEvent(new FetchDevicesResponseEvent(RequestResult.SUCCESSFUL, response));
            notifyCallback();
        }

        @Override
        public void parseErrorResponse(String errorBody, int code) {
            passGenericErrorToDispatcher(WD_FETCH_DEVICE_LIST);
            notifyCallback();
        }

        @Override
        public void handleGenericError(Exception exception) {
            passGenericErrorToDispatcher(WD_FETCH_DEVICE_LIST);
            notifyCallback();
        }

        @Override
        public void handleConnectionError() {
            passConnectionErrorToDispatcher(WD_FETCH_DEVICE_LIST);
            notifyCallback();
        }
    }

    private class PotentialPointsResponseParser implements ResponseParserWithRedirect<EventType> {
        @Override
        public void parseResponse(EventType body) {
            deviceListRepository.persistPotentialPoints(body);
            potentialPointsResponseEvent = new PotentialPointsResponseEvent(RequestResult.SUCCESSFUL, body);
            eventDispatcher.dispatchEvent(potentialPointsResponseEvent);
        }

        @Override
        public void parseErrorResponse(String errorBody, int code) {
            passGenericErrorToDispatcher(WD_GET_POTENTIAL_POINTS);
        }

        @Override
        public void handleGenericError(Exception exception) {
            passGenericErrorToDispatcher(WD_GET_POTENTIAL_POINTS);
        }

        @Override
        public void handleConnectionError() {
            passConnectionErrorToDispatcher(WD_GET_POTENTIAL_POINTS);
        }

        @Override
        public void handleRedirect(String redirectUrl) {
            // NOP
        }
    }

    private class DeviceActivityMappingResponseParser implements WebServiceResponseParser<ResponseBody> {
        @Override
        public void parseResponse(ResponseBody response) {
            try {
                final String responseStr = response.string();
                Map<String, int[]> resultMap = Utils.parseDeviceActivityResponse(responseStr);
                deviceListRepository.persistDeviceActivityMap(resultMap);

                deviceActivityMappingReponseEvent = new DeviceActivityMappingResponseEvent(RequestResult.SUCCESSFUL,
                        responseStr);
                eventDispatcher.dispatchEvent(deviceActivityMappingReponseEvent);
            } catch (IOException e) {
                Log.e(TAG, "error during DeviceActivityMappingRequest execution: " + e.getMessage());
                passGenericErrorToDispatcher(WD_FETCH_DEVICE_ACTIVITY_MAPPING);
            }

        }

        @Override
        public void parseErrorResponse(String errorBody, int code) {
            passGenericErrorToDispatcher(WD_FETCH_DEVICE_ACTIVITY_MAPPING);
        }

        @Override
        public void handleGenericError(Exception exception) {
            passGenericErrorToDispatcher(WD_FETCH_DEVICE_ACTIVITY_MAPPING);
        }

        @Override
        public void handleConnectionError() {
            passConnectionErrorToDispatcher(WD_FETCH_DEVICE_ACTIVITY_MAPPING);
        }
    }
}
