package com.vitalityactive.va.wellnessdevices.linking;

import android.support.annotation.NonNull;

import com.vitalityactive.va.InsurerConfigurationRepository;
import com.vitalityactive.va.MainThreadScheduler;
import com.vitalityactive.va.events.EventDispatcher;
import com.vitalityactive.va.events.EventListener;
import com.vitalityactive.va.networking.RequestResult;
import com.vitalityactive.va.wellnessdevices.Utils;
import com.vitalityactive.va.wellnessdevices.dto.AssetsDto;
import com.vitalityactive.va.wellnessdevices.dto.PartnerDto;
import com.vitalityactive.va.wellnessdevices.dto.PartnerLinkDto;
import com.vitalityactive.va.wellnessdevices.dto.PotentialPointsDto;
import com.vitalityactive.va.wellnessdevices.landing.events.FetchDevicesResponseEvent;
import com.vitalityactive.va.wellnessdevices.landing.service.GetFullListResponse;
import com.vitalityactive.va.wellnessdevices.linking.cascaderequests.DelinkCascade;
import com.vitalityactive.va.wellnessdevices.linking.events.LinkDeviceResponseEvent;
import com.vitalityactive.va.wellnessdevices.linking.refreshtoken.WellnessDevicesRefreshTokenInteractor;

import java.util.HashSet;
import java.util.Set;

import static com.vitalityactive.va.wellnessdevices.linking.WellnessDevicesLinkingInteractor.WD_DELINK_DEVICE;

public class WellnessDevicesLinkingPresenterImpl implements WellnessDevicesLinkingPresenter {
    private final WellnessDevicesLinkingInteractorImpl linkingInteractor;
    private final WellnessDevicesRefreshTokenInteractor tokenInteractor;
    private final DelinkCascade delinkCascade;
    private final EventDispatcher eventDispatcher;
    private final MainThreadScheduler scheduler;
    private final InsurerConfigurationRepository insurerConfigurationRepository;
    private WellnessDevicesLinkingPresenter.UserInterface userInterface;
    private EventListener<LinkDeviceResponseEvent> linkDeviceResponseEventListener;
    private EventListener<FetchDevicesResponseEvent> fetchTokenResponseEventListener;
    private boolean isUserInterfaceVisible;
    private PartnerDto partner;
    private boolean isLinked;
    private boolean shouldRefreshToken;

    public WellnessDevicesLinkingPresenterImpl(@NonNull WellnessDevicesLinkingInteractorImpl linkingInteractor,
                                               @NonNull WellnessDevicesRefreshTokenInteractor tokenInteractor,
                                               @NonNull DelinkCascade delinkCascade,
                                               @NonNull EventDispatcher eventDispatcher,
                                               @NonNull MainThreadScheduler scheduler,
                                               @NonNull InsurerConfigurationRepository insurerConfigurationRepository) {
        this.linkingInteractor = linkingInteractor;
        this.tokenInteractor = tokenInteractor;
        this.delinkCascade = delinkCascade;
        this.eventDispatcher = eventDispatcher;
        this.scheduler = scheduler;
        this.insurerConfigurationRepository = insurerConfigurationRepository;

        createEventListeners();
    }

    @Override
    public void onUserInterfaceCreated(boolean isNewNavigation) {
        // NOP
    }

    @Override
    public void onUserInterfaceAppeared() {
        isUserInterfaceVisible = true;
        addEventListeners();

        if (isLinkingRequestRunning() ||
                isTokenRequestRunning()) {
            userInterface.showLoadingIndicator();
            return;
        } else if(linkingInteractor.getLinkDeviceRequestResult() != null){
            handleLinkDeviceEvent(linkingInteractor.getLinkDeviceRequestResult());
        }

        userInterface.hideLoadingIndicator();
    }

    @Override
    public void onUserInterfaceDisappeared(boolean isFinishing) {
        isUserInterfaceVisible = false;
        scheduler.cancel();
        removeEventListeners();
    }

    @Override
    public void onUserInterfaceDestroyed() {
        // NOP
    }

    @Override
    public void setUserInterface(UserInterface userInterface) {
        this.userInterface = userInterface;
    }

    @Override
    public void setPartner(PartnerDto partner) {
        this.partner = partner;
        isLinked = Utils.isDeviceLinked(getPartner()); // TODO rough workaround to handle status changes wihtout additional request

        // TODO hide measurements which are missed in ManageVitalityPointsService response
        Set<Integer> availableActivities = new HashSet<>();
        Set<Integer> deviceAvailableActivities = linkingInteractor.getDeviceAvailableActivities(partner.getDevice());
        if (deviceAvailableActivities != null) {
            for (int activityCode : deviceAvailableActivities) {
                if (getPotentialPoints(activityCode) != null) {
                    availableActivities.add(activityCode);
                }
            }
        }

        this.partner.setAvailableOptions(availableActivities/*linkingInteractor.getDeviceAvailableActivities(partner.getDevice())*/);

        // setPartner() is called when user navigates from Landing screen. This mean that token has been already updated
        shouldRefreshToken = false;
    }

    @Override
    public PartnerDto getPartner() {
        return partner;
    }

    @Override
    public AssetsDto getAssets() {
        return linkingInteractor.getAssets(getPartner().getDevice());
    }

    @Override
    public boolean shouldShowWDPrivacyPolicy() {
        return insurerConfigurationRepository.shouldShowWDPrivacyPolicy();
    }

    @Override
    public void linkDevice() {
        showProgress();
        linkingInteractor.linkDevice(partner);
    }

    @Override
    public void delinkDevice() {
        showProgress(WellnessDevicesLinkingInteractor.WD_DELINK_DEVICE);
        linkingInteractor.clearLinkDeviceRequestResult();
        if(shouldRefreshToken){
            if(tokenInteractor.isTokenUpdated()){
                shouldRefreshToken = false;
                linkingInteractor.delinkDevice(partner);
            } else {
                delinkCascade.refreshTokenAndDelink(partner);
            }
        } else {
            linkingInteractor.delinkDevice(partner);
        }
    }

    @Override
    public void sendUpdateTokenRequest() {
        if(isUserInterfaceVisible) {
            showProgress();
        }
        shouldRefreshToken = true;
        tokenInteractor.sendRefreshTokenRequest();
    }

    @Override
    public boolean manufacturerContainsTypeKey(int typeKey) {
        return partner.getAvailableOptions() != null &&
                partner.getAvailableOptions().contains(typeKey);
    }

    @Override
    public PotentialPointsDto getPotentialPoints(int typeKey){
        return linkingInteractor.getPotentialPoints(typeKey);
    }

    @Override
    public void setLinked(boolean isLinked) {
        this.isLinked = isLinked;
    }

    @Override
    public boolean isLinked() {
        return isLinked;
    }

    private void createEventListeners() {
        linkDeviceResponseEventListener = new EventListener<LinkDeviceResponseEvent>() {
            @Override
            public void onEvent(final LinkDeviceResponseEvent event) {
                scheduler.schedule(new Runnable() {
                    @Override
                    public void run() {
                        hideProgress();
                        handleLinkDeviceEvent(event);
                    }
                });
            }
        };

        fetchTokenResponseEventListener = new EventListener<FetchDevicesResponseEvent>() {
            @Override
            public void onEvent(final FetchDevicesResponseEvent event) {
                scheduler.schedule(new Runnable() {
                    @Override
                    public void run() {
                        if(event.isSuccessfull()){
                            shouldRefreshToken = false;
                            for(GetFullListResponse.Market market : event.getResponseBody().markets){
                                if(partner.getDevice().equals(market.partner.device)){
                                    partner.setPartnerDelink(new PartnerLinkDto(market.partner.partnerDelink));
                                }
                            }
                        }
                        hideProgress();
                    }
                });
            }
        };
    }

    private void handleLinkDeviceEvent(final LinkDeviceResponseEvent event) {
        if (event.isSuccessfull()) {
            if(event.getRequestType().equals(WD_DELINK_DEVICE)){
                setLinked(false);
                        userInterface.showDelinkSuccessfullMessage();
                        userInterface.updateUi();

            }
        } else if (event.isRedirect()) {
            if (isUserInterfaceVisible) {
                userInterface.redirectToPartnerWebSite(event.getRedirectUrl(), event.getRequestType());
            }
        } else {
            handleErrorEvent(event);
        }
        linkingInteractor.clearLinkDeviceRequestResult();
    }

    private void handleErrorEvent(LinkDeviceResponseEvent event) {
        if (event.getRequestResult() == RequestResult.CONNECTION_ERROR) {
            userInterface.showConnectionErrorMessage(event.getRequestType());
        } else if (event.getRequestResult() == RequestResult.GENERIC_ERROR) {
            userInterface.showGenericErrorMessage(event.getRequestType());
        }
    }

    private void addEventListeners() {
        eventDispatcher.addEventListener(LinkDeviceResponseEvent.class, linkDeviceResponseEventListener);
        eventDispatcher.addEventListener(FetchDevicesResponseEvent.class, fetchTokenResponseEventListener);
    }

    private void removeEventListeners() {
        eventDispatcher.removeEventListener(LinkDeviceResponseEvent.class, linkDeviceResponseEventListener);
        eventDispatcher.removeEventListener(FetchDevicesResponseEvent.class, fetchTokenResponseEventListener);
    }

    private boolean isLinkingRequestRunning(){
        return linkingInteractor.isRequestRunning();
    }

    private boolean isTokenRequestRunning(){
        return tokenInteractor.isRequestRunning();
    }

    private void showProgress(@WellnessDevicesLinkingInteractor.RequestType String requestType) {
        userInterface.showProgressWithText(requestType);
    }

    private void showProgress() {
        userInterface.showLoadingIndicator();
    }

    private void hideProgress() {
        userInterface.hideLoadingIndicator();
    }
}
