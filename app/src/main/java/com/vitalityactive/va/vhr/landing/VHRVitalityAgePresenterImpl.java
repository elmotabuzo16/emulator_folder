package com.vitalityactive.va.vhr.landing;


import com.vitalityactive.va.DeviceSpecificPreferences;
import com.vitalityactive.va.events.EventDispatcher;
import com.vitalityactive.va.events.EventListener;
import com.vitalityactive.va.myhealth.events.HealthAttributeFeedbackTipsEvent;
import com.vitalityactive.va.myhealth.service.HealthAttributeInformationServiceClient;
import com.vitalityactive.va.myhealth.shared.VitalityAgeConstants;
import com.vitalityactive.va.myhealth.vitalityage.VitalityAgeInteractor;
import com.vitalityactive.va.networking.RequestResult;

import java.util.Collections;

public class VHRVitalityAgePresenterImpl implements VHRVitalityAgePresenter {

    boolean isReturnfromFormSubmission;
    VitalityAgeInteractor vitalityAgeInteractor;
    DeviceSpecificPreferences deviceSpecificPreferences;
    private final EventDispatcher eventDispatcher;
    private final HealthAttributeInformationServiceClient attributeInformationServiceClient;
    private final EventListener<HealthAttributeFeedbackTipsEvent> tipsEventEventListener = new EventListener<HealthAttributeFeedbackTipsEvent>() {
        @Override
        public void onEvent(HealthAttributeFeedbackTipsEvent healthAttributeFeedbackTipsEvent) {
            onHealthAttributeFeedbackEventReceived();
        }
    };

    UserInterface userInterface;

    private void onHealthAttributeFeedbackEventReceived() {
        boolean hasVitalityAgeToShow = attributeInformationServiceClient.getHealthAtttributeTipsFeedbackResult().equals(RequestResult.SUCCESSFUL) && vitalityAgeInteractor.getVitalityAgeHealthAttribute() != null;
        userInterface.onVitalityAgeResponse(hasVitalityAgeToShow);
        unRegisterVitalityAgeRequest();
    }

    public VHRVitalityAgePresenterImpl(EventDispatcher eventDispatcher, HealthAttributeInformationServiceClient attributeInformationServiceClient, VitalityAgeInteractor vitalityAgeInteractor, DeviceSpecificPreferences deviceSpecificPreferences) {
        this.vitalityAgeInteractor = vitalityAgeInteractor;
        this.deviceSpecificPreferences = deviceSpecificPreferences;
        this.eventDispatcher = eventDispatcher;
        this.attributeInformationServiceClient = attributeInformationServiceClient;
    }

    @Override
    public void setReturnfromFormSubmission(boolean isReturnfromFormSubmission) {
        this.isReturnfromFormSubmission = isReturnfromFormSubmission;
    }

    @Override
    public boolean shouldShowVitalityAge() {
        return isReturnfromFormSubmission && !deviceSpecificPreferences.hasCurrentUserSeenVitalityAge();
    }

    @Override
    public void setHasShownVitalityAge() {
        deviceSpecificPreferences.setCurrentUserHasSeenVitalityAge();
    }

    @Override
    public void setUserInterface(UserInterface userInterface) {
        this.userInterface = userInterface;
    }

    @Override
    public void fetchVitalityAge() {
        if (!attributeInformationServiceClient.isFetchingHealthAttributeInformation()) {
            eventDispatcher.addEventListener(HealthAttributeFeedbackTipsEvent.class, tipsEventEventListener);
            attributeInformationServiceClient.fetchHealthAttributeFeedbackTips(Collections.singletonList(VitalityAgeConstants.VITALITY_AGE_SECTION), false);
        }
    }

    @Override
    public void unRegisterVitalityAgeRequest() {
        try {
            eventDispatcher.removeEventListener(HealthAttributeFeedbackTipsEvent.class, tipsEventEventListener);
            attributeInformationServiceClient.cancel();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean isReturnfromFormSubmission() {
        return isReturnfromFormSubmission;
    }
}
