package com.vitalityactive.va.myhealth.landing;

import com.vitalityactive.va.events.EventDispatcher;
import com.vitalityactive.va.events.EventListener;
import com.vitalityactive.va.myhealth.dto.VitalityAgeHealthAttributeDTO;
import com.vitalityactive.va.myhealth.entity.VitalityAge;
import com.vitalityactive.va.myhealth.events.HealthAttributeFeedbackTipsEvent;
import com.vitalityactive.va.myhealth.service.HealthAttributeInformationServiceClient;
import com.vitalityactive.va.myhealth.shared.VitalityAgeConstants;
import com.vitalityactive.va.networking.RequestResult;
import com.vitalityactive.va.vhc.dto.HealthAttributeFeedbackDTO;

import java.util.Collections;

public class MyHealthLandingPresenterImpl implements MyHealthLandingPresenter {

    private final EventDispatcher eventDispatcher;
    private final HealthAttributeInformationServiceClient attributeInformationServiceClient;
    private final MyHealthLandingInteractor myHealthInteractor;
    private UserInterface userInterface;
    private final EventListener<HealthAttributeFeedbackTipsEvent> tipsEventEventListener = new EventListener<HealthAttributeFeedbackTipsEvent>() {
        @Override
        public void onEvent(HealthAttributeFeedbackTipsEvent healthAttributeFeedbackTipsEvent) {
            onHealthAttributeFeedbackEventReceived();
        }
    };

    public MyHealthLandingPresenterImpl(EventDispatcher eventDispatcher, HealthAttributeInformationServiceClient attributeInformationServiceClient, MyHealthLandingInteractor myHealthInteractor) {
        this.eventDispatcher = eventDispatcher;
        this.attributeInformationServiceClient = attributeInformationServiceClient;
        this.myHealthInteractor = myHealthInteractor;
    }

    public void onUserInterfaceCreated(boolean isNewNavigation) {
    }

    @Override
    public void onUserInterfaceAppeared() {
        eventDispatcher.addEventListener(HealthAttributeFeedbackTipsEvent.class, tipsEventEventListener);
        attributeInformationServiceClient.fetchHealthAttributeFeedbackTips(Collections.singletonList(VitalityAgeConstants.VITALITY_AGE_SECTION), false);
        userInterface.showLoadingIndicator();
    }

    @Override
    public void onUserInterfaceDisappeared(boolean isFinishing) {
        eventDispatcher.removeEventListener(HealthAttributeFeedbackTipsEvent.class, tipsEventEventListener);
    }

    @Override
    public void onUserInterfaceDestroyed() {

    }

    @Override
    public void setUserInterface(UserInterface userInterface) {
        this.userInterface = userInterface;
    }


    public void onHealthAttributeFeedbackEventReceived() {
        try {
            userInterface.hideLoadingIndicator();
            if (attributeInformationServiceClient.getHealthAtttributeTipsFeedbackResult().equals(RequestResult.SUCCESSFUL)) {
                VitalityAgeHealthAttributeDTO attributeDTO = myHealthInteractor.getVitalityAgeHealthAttribute();
                if (attributeDTO != null) {
                    HealthAttributeFeedbackDTO effectiveFeedback = attributeDTO.getEffectiveFeedback();
                    if (effectiveFeedback != null) {
                        userInterface.loadVitalityAge(new VitalityAge.Builder()
                                .age(attributeDTO.getValue())
                                .feedbackTitle(effectiveFeedback.getFeedbackTypeName())
                                .effectiveType(effectiveFeedback.getFeedbackTypeKey())
                                .feedbackContent(effectiveFeedback.getFeedbackTypeCode())
                                .variance(attributeDTO.getVariance())
                                .build());
                        return;
                    }
                }
            }
            userInterface.loadVitalityAge(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
