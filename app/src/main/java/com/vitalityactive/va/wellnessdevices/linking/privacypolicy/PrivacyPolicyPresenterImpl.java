package com.vitalityactive.va.wellnessdevices.linking.privacypolicy;

import com.vitalityactive.va.Scheduler;
import com.vitalityactive.va.events.EventDispatcher;
import com.vitalityactive.va.events.EventListener;
import com.vitalityactive.va.networking.RequestResult;
import com.vitalityactive.va.termsandconditions.TermsAndConditionsConsenter;
import com.vitalityactive.va.termsandconditions.TermsAndConditionsInteractor;
import com.vitalityactive.va.termsandconditions.TermsAndConditionsPresenterImpl;
import com.vitalityactive.va.wellnessdevices.dto.PartnerDto;
import com.vitalityactive.va.wellnessdevices.linking.WellnessDevicesLinkingInteractor;
import com.vitalityactive.va.wellnessdevices.linking.events.LinkDeviceResponseEvent;

public class PrivacyPolicyPresenterImpl extends TermsAndConditionsPresenterImpl<PrivacyPolicyPresenter.UserInterface>
        implements PrivacyPolicyPresenter{
    private final WellnessDevicesLinkingInteractor wellnessDevicesLinkingInteractor;
    private EventListener<LinkDeviceResponseEvent> linkDeviceResponseEventListener;
    private PartnerDto partner;

    public PrivacyPolicyPresenterImpl(final Scheduler scheduler,
                                      TermsAndConditionsInteractor interactor,
                                      WellnessDevicesLinkingInteractor wellnessDevicesLinkingInteractor,
                                      TermsAndConditionsConsenter termsAndConditionsConsenter,
                                      EventDispatcher eventDispatcher) {
        super(scheduler, interactor, termsAndConditionsConsenter, eventDispatcher);
        this.wellnessDevicesLinkingInteractor = wellnessDevicesLinkingInteractor;
        linkDeviceResponseEventListener = new EventListener<LinkDeviceResponseEvent>() {
            @Override
            public void onEvent(LinkDeviceResponseEvent event) {
                scheduler.schedule(new Runnable() {
                    @Override
                    public void run() {
                        hideProgress();
                    }
                });

                handleLinkDeviceEvent(event);
            }
        };
    }

    @Override
    public void onUserInterfaceAppeared() {
        super.onUserInterfaceAppeared();
        if (termsAndConditionsConsenter.getAgreeRequestResult() == RequestResult.SUCCESSFUL) {
            linkDevice();
        }
    }

    @Override
    protected boolean shouldNavigateAfterSuccessfulAgreeRequest() {
        return false;
    }

    @Override
    public void setUserInterface(PrivacyPolicyPresenter.UserInterface userInterface) {
        this.userInterface = userInterface;
    }

    @Override
    protected void onTermsAndConditionsAccepted() {
        if (isUserInterfaceVisible()) {
            linkDevice();
        }
    }

    @Override
    public void setPartner(PartnerDto partner) {
        this.partner = partner;
    }

    @Override
    public void linkDevice() {
        showProgress();
        wellnessDevicesLinkingInteractor.linkDevice(partner);
    }

    @Override
    protected void addEventListeners() {
        super.addEventListeners();
        if (linkDeviceResponseEventListener != null) {
            eventDispatcher.addEventListener(LinkDeviceResponseEvent.class, linkDeviceResponseEventListener);
        }
    }

    @Override
    protected void removeEventListeners() {
        super.removeEventListeners();
        if (linkDeviceResponseEventListener != null) {
            eventDispatcher.removeEventListener(LinkDeviceResponseEvent.class, linkDeviceResponseEventListener);
        }
    }

    private void handleLinkDeviceEvent(final LinkDeviceResponseEvent event) {
        if (event.isSuccessfull()) {
            hideProgress();
            getUserInterface().navigateToPreviousScreenWithSuccessStatus();
        } else if (event.isRedirect()) {
            if (isUserInterfaceVisible()) {
                getUserInterface().redirectToPartnerWebSite(event.getRedirectUrl());
            }
        } else {
            handleErrorEvent(event);
        }
        wellnessDevicesLinkingInteractor.clearLinkDeviceRequestResult();
    }

    private void handleErrorEvent(LinkDeviceResponseEvent event) {
        if (event.getRequestResult() == RequestResult.CONNECTION_ERROR) {
            getUserInterface().showConnectionLinkErrorMessage();
        } else if (event.getRequestResult() == RequestResult.GENERIC_ERROR) {
            getUserInterface().showGenericLinkErrorMessage();
        }
    }

    private void showProgress() {
        userInterface.showLoadingIndicator();
    }

    private void hideProgress() {
        userInterface.hideLoadingIndicator();
    }

    private PrivacyPolicyPresenter.UserInterface getUserInterface(){
        return userInterface;
    }
}
