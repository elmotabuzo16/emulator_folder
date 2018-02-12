package com.vitalityactive.va.wellnessdevices.linking;


import android.support.annotation.NonNull;

import com.vitalityactive.va.connectivity.ConnectivityListener;
import com.vitalityactive.va.events.EventDispatcher;
import com.vitalityactive.va.networking.RequestResult;
import com.vitalityactive.va.networking.ResponseParserWithRedirect;
import com.vitalityactive.va.wellnessdevices.BaseWellnessDeviceEvent;
import com.vitalityactive.va.wellnessdevices.dto.AssetsDto;
import com.vitalityactive.va.wellnessdevices.dto.PartnerDto;
import com.vitalityactive.va.wellnessdevices.dto.PotentialPointsDto;
import com.vitalityactive.va.wellnessdevices.linking.events.LinkDeviceResponseEvent;
import com.vitalityactive.va.wellnessdevices.linking.repository.LinkingPageRepository;
import com.vitalityactive.va.wellnessdevices.linking.service.DelinkDeviceService;
import com.vitalityactive.va.wellnessdevices.linking.service.LinkDeviceServiceClient;

import java.util.Set;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class WellnessDevicesLinkingInteractorImpl implements WellnessDevicesLinkingInteractor {
    private final EventDispatcher eventDispatcher;
    private final ConnectivityListener connectivityListener;
    private final LinkingPageRepository linkingPageRepository;

    private final LinkDeviceServiceClient linkRequestService;
    private final DelinkDeviceService delinkRequestService;
    private LinkDeviceResponseEvent linkDeviceResponseEvent = null;

    private @WellnessDevicesLinkingInteractor.RequestType String requestType = null;

    @Inject
    public WellnessDevicesLinkingInteractorImpl(@NonNull EventDispatcher eventDispatcher,
                                                @NonNull ConnectivityListener connectivityListener,
                                                @NonNull LinkDeviceServiceClient linkRequestService,
                                                @NonNull DelinkDeviceService delinkDeviceService,
                                                @NonNull LinkingPageRepository linkingPageRepository){
        this.eventDispatcher = eventDispatcher;
        this.connectivityListener = connectivityListener;
        this.linkRequestService = linkRequestService;
        this.delinkRequestService = delinkDeviceService;
        this.linkingPageRepository = linkingPageRepository;
    }

    /**
     * WellnessDevicesLandingInteractor implementation
     */
    @Override
    public void linkDevice(PartnerDto partner) {
        clearLinkDeviceRequestResult();
        requestType = WD_LINK_DEVICE;
        if(connectivityListener.isOnline()) {
            linkRequestService.linkDevice(partner, new LinkDeviceResponseParser());
        } else {
            passConnectionErrorToDispather(WD_LINK_DEVICE);
        }
    }

    @Override
    public LinkDeviceResponseEvent getLinkDeviceRequestResult() {
        return linkDeviceResponseEvent;
    }

    @Override
    public void clearLinkDeviceRequestResult() {
        requestType = null;
        linkDeviceResponseEvent = null;
    }

    @Override
    public void delinkDevice(PartnerDto partner) {
        clearLinkDeviceRequestResult();
        requestType = WD_DELINK_DEVICE;
        if(connectivityListener.isOnline()) {
            linkRequestService.delinkDevice(partner, new LinkDeviceResponseParser());
        } else {
            passConnectionErrorToDispather(WD_DELINK_DEVICE);
        }
    }

    @Override
    public PotentialPointsDto getPotentialPoints(int typeKey){
        return linkingPageRepository.getPotentialPoints(typeKey);
    }

    @Override
    public AssetsDto getAssets(String device) {
        return linkingPageRepository.getAssets(device);
    }

    @Override
    public Set<Integer> getDeviceAvailableActivities(String device) {
        return linkingPageRepository.getDeviceAvailableActivities(device);
    }

    @Override
    public boolean isRequestRunning() {
        return requestType != null && linkDeviceResponseEvent == null;
    }

    private void passConnectionErrorToDispather(@RequestType String requestType){
        BaseWellnessDeviceEvent event = null;
        switch (requestType){
            case WD_LINK_DEVICE:
                event = new LinkDeviceResponseEvent(RequestResult.CONNECTION_ERROR, WD_LINK_DEVICE);
                break;
            case WD_DELINK_DEVICE:
                event = new LinkDeviceResponseEvent(RequestResult.CONNECTION_ERROR, WD_DELINK_DEVICE);
                break;
            default:
                return;
        }
        eventDispatcher.dispatchEvent(event);
    }

    private void passGenericErrorToDispather(@RequestType String requestType){
        BaseWellnessDeviceEvent event = null;
        switch (requestType){
            case WD_LINK_DEVICE:
                event = new LinkDeviceResponseEvent(RequestResult.GENERIC_ERROR, WD_LINK_DEVICE);
                break;
            case WD_DELINK_DEVICE:
                event = new LinkDeviceResponseEvent(RequestResult.GENERIC_ERROR, WD_DELINK_DEVICE);
                break;
            default:
                return;
        }
        eventDispatcher.dispatchEvent(event);
    }

    /**
     * ResponseParserWithRedirect implementation
     */
    private class LinkDeviceResponseParser implements ResponseParserWithRedirect<String> {
        @Override
        public void parseResponse(String body) {
            linkDeviceResponseEvent = new LinkDeviceResponseEvent(RequestResult.SUCCESSFUL, body, null, requestType);
            eventDispatcher.dispatchEvent(linkDeviceResponseEvent);
        }

        @Override
        public void parseErrorResponse(String errorBody, int code) {
            passGenericErrorToDispather(requestType);
        }

        @Override
        public void handleGenericError(Exception exception) {
            passGenericErrorToDispather(requestType);
        }

        @Override
        public void handleConnectionError() {
            passConnectionErrorToDispather(requestType);
        }

        @Override
        public void handleRedirect(String redirectUrl) {
            if(requestType == WD_LINK_DEVICE) {
                linkDeviceResponseEvent = new LinkDeviceResponseEvent(RequestResult.REDIRECT, null, redirectUrl, requestType);
                eventDispatcher.dispatchEvent(linkDeviceResponseEvent);
            } else {
                delinkRequestService.sendRequest(redirectUrl, "GET", new LinkDeviceResponseParser());
            }
        }
    }
}
