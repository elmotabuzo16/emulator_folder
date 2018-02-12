package com.vitalityactive.va.activerewards;

import com.vitalityactive.va.events.EventDispatcher;
import com.vitalityactive.va.networking.WebServiceResponseParser;
import com.vitalityactive.va.networking.model.ar.ActivateServiceResponse;
import com.vitalityactive.va.wellnessdevices.landing.WellnessDevicesLandingInteractor;
import com.vitalityactive.va.wellnessdevices.landing.WellnessDevicesLandingInteractorImpl;

public class ActiveRewardsActivatorImpl implements ActiveRewardsActivator, WebServiceResponseParser<ActivateServiceResponse>,WellnessDevicesLandingInteractor.Callback {
    private final ActiveRewardsActivationServiceClient activeRewardsActivationServiceClient;
    private WellnessDevicesLandingInteractorImpl wellnessDevicesLandingInteractor;
    private EventDispatcher eventDispatcher;

    public ActiveRewardsActivatorImpl(final EventDispatcher eventDispatcher, ActiveRewardsActivationServiceClient activeRewardsActivationServiceClient, WellnessDevicesLandingInteractorImpl wellnessDevicesLandingInteractor) {
        this.eventDispatcher = eventDispatcher;
        this.activeRewardsActivationServiceClient = activeRewardsActivationServiceClient;
        this.wellnessDevicesLandingInteractor = wellnessDevicesLandingInteractor;
    }

    @Override
    public void activate() {
        activeRewardsActivationServiceClient.activate(this);
    }

    @Override
    public boolean isActivateRequestInProgress() {
        return activeRewardsActivationServiceClient.isActivateRequestInProgress();
    }

    @Override
    public void parseResponse(ActivateServiceResponse response) {
        wellnessDevicesLandingInteractor.fetchDeviceList(this);
    }

    @Override
    public void parseErrorResponse(String errorBody, int code) {
        eventDispatcher.dispatchEvent(new ActivationFailedEvent(ActivationErrorType.GENERIC));
    }

    @Override
    public void handleGenericError(Exception exception) {
        eventDispatcher.dispatchEvent(new ActivationFailedEvent(ActivationErrorType.GENERIC));
    }

    @Override
    public void handleConnectionError() {
        eventDispatcher.dispatchEvent(new ActivationFailedEvent(ActivationErrorType.CONNECTION));
    }

    private void dispatchActivationSucceededEvent() {
        eventDispatcher.dispatchEvent(new ActivationSucceededEvent());
    }

    @Override
    public void deviceListFetched() {
        dispatchActivationSucceededEvent();
    }
}
