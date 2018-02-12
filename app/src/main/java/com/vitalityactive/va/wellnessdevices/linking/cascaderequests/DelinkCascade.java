package com.vitalityactive.va.wellnessdevices.linking.cascaderequests;

import android.support.annotation.NonNull;

import com.vitalityactive.va.connectivity.ConnectivityListener;
import com.vitalityactive.va.events.EventDispatcher;
import com.vitalityactive.va.networking.RequestResult;
import com.vitalityactive.va.networking.WebServiceResponseParser;
import com.vitalityactive.va.wellnessdevices.dto.PartnerDto;
import com.vitalityactive.va.wellnessdevices.dto.PartnerLinkDto;
import com.vitalityactive.va.wellnessdevices.landing.service.GetFullListResponse;
import com.vitalityactive.va.wellnessdevices.linking.WellnessDevicesLinkingInteractorImpl;
import com.vitalityactive.va.wellnessdevices.linking.events.LinkDeviceResponseEvent;
import com.vitalityactive.va.wellnessdevices.linking.refreshtoken.WellnessDevicesRefreshTokenInteractor;

import javax.inject.Inject;
import javax.inject.Singleton;

import static com.vitalityactive.va.wellnessdevices.linking.WellnessDevicesLinkingInteractor.WD_DELINK_DEVICE;

@Singleton
public class DelinkCascade implements WebServiceResponseParser<GetFullListResponse> {
    private final EventDispatcher eventDispatcher;
    private final ConnectivityListener connectivityListener;
    private final WellnessDevicesLinkingInteractorImpl linkingInteractor;
    private final WellnessDevicesRefreshTokenInteractor tokenInteractor;

    private PartnerDto partner;

    @Inject
    public DelinkCascade(@NonNull WellnessDevicesLinkingInteractorImpl linkingInteractor,
                         @NonNull WellnessDevicesRefreshTokenInteractor tokenInteractor,
                         @NonNull EventDispatcher eventDispatcher,
                         @NonNull ConnectivityListener connectivityListener) {
        this.linkingInteractor = linkingInteractor;
        this.tokenInteractor = tokenInteractor;
        this.eventDispatcher = eventDispatcher;
        this.connectivityListener = connectivityListener;
    }

    public void refreshTokenAndDelink(PartnerDto partner) {
        this.partner = partner;
        if (connectivityListener.isOnline()) {
            tokenInteractor.sendRefreshTokenRequest(this);
        } else {
            sendErrorToDispatcher();
        }
    }

    private void sendErrorToDispatcher() {
        partner = null;
        eventDispatcher.dispatchEvent(new LinkDeviceResponseEvent(RequestResult.GENERIC_ERROR, WD_DELINK_DEVICE));
    }

    /**
     * Implementation for WebServiceResponseParser<GetFullListResponse>
     */
    @Override
    public void parseResponse(GetFullListResponse response) {
        if (response != null &&
                response.markets != null &&
                partner != null) {
            for(GetFullListResponse.Market market : response.markets){
                if(partner.getDevice().equals(market.partner.device)){
                    partner.setPartnerDelink(new PartnerLinkDto(market.partner.partnerDelink));
                }
            }
//            eventDispatcher.dispatchEvent(new FetchDevicesResponseEvent(RequestResult.SUCCESSFUL, response));
            linkingInteractor.delinkDevice(partner);
        } else {
            eventDispatcher.dispatchEvent(new LinkDeviceResponseEvent(RequestResult.GENERIC_ERROR, WD_DELINK_DEVICE));
        }
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
}
