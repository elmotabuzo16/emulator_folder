package com.vitalityactive.va.myhealth.vitalityage;


import com.vitalityactive.va.DeviceSpecificPreferences;
import com.vitalityactive.va.myhealth.content.MyHealthContent;
import com.vitalityactive.va.myhealth.dto.VitalityAgeHealthAttributeDTO;
import com.vitalityactive.va.myhealth.entity.VitalityAge;
import com.vitalityactive.va.vhc.dto.HealthAttributeFeedbackDTO;

public class VitalityAgePresenterImpl implements VitalityAgePresenter {

    private final VitalityAgeInteractor vitalityAgeInteractor;
    UserInterface userInterface;
    int vitalityAgeDisplayMode;
    DeviceSpecificPreferences deviceSpecificPreferences;


    public VitalityAgePresenterImpl(VitalityAgeInteractor vitalityAgeInteractor, DeviceSpecificPreferences deviceSpecificPreferences) {
        this.vitalityAgeInteractor = vitalityAgeInteractor;
        this.deviceSpecificPreferences = deviceSpecificPreferences;
    }

    @Override
    public void onUserInterfaceCreated(boolean isNewNavigation) {

    }

    @Override
    public void onUserInterfaceAppeared() {
        switch (vitalityAgeDisplayMode) {
            case VitalityAgeActivity.VHR_MODE:
                loadVitalityAge();
                break;
            case VitalityAgeActivity.VHC__DONE_VHR_PENDING_MODE:
                userInterface.hideLoadingIndicator();
                userInterface.loadVitalityAgeVHCDoneVHRPending();
                break;
            default:
                break;

        }
    }

    @Override
    public void onUserInterfaceDisappeared(boolean isFinishing) {
        userInterface.hideLoadingIndicator();
    }

    @Override
    public void onUserInterfaceDestroyed() {
    }

    public void loadVitalityAge() {
        userInterface.hideLoadingIndicator();
        VitalityAgeHealthAttributeDTO attributeDTO = vitalityAgeInteractor.getVitalityAgeHealthAttribute();
        if (attributeDTO != null) {
            HealthAttributeFeedbackDTO effectiveFeedback = attributeDTO.getEffectiveFeedback();
            if (effectiveFeedback != null&&effectiveFeedback.getFeedbackTypeKey()!=null&& MyHealthContent.vitalityAgeCalculated(Integer.valueOf(effectiveFeedback.getFeedbackTypeKey()))) {
                userInterface.initialize(new VitalityAge.Builder()
                        .age(attributeDTO.getValue())
                        .feedbackTitle(effectiveFeedback.getFeedbackTypeName())
                        .effectiveType(effectiveFeedback.getFeedbackTypeKey() != null ? effectiveFeedback.getFeedbackTypeKey() : 0)
                        .actualType(attributeDTO.getActualFeedback() != null ? attributeDTO.getActualFeedback().getFeedbackTypeKey() : 0)
                        .feedbackContent(effectiveFeedback.getFeedbackTypeCode())
                        .variance(attributeDTO.getVariance())
                        .build());
                return;
            }
        }else{
            userInterface.initialize(null);
        }
    }

    @Override
    public boolean shouldShowVitalityAge() {
        return !deviceSpecificPreferences.hasCurrentUserSeenVitalityAge();
    }

    @Override
    public void setHasShownVitalityAge() {
        deviceSpecificPreferences.setCurrentUserHasSeenVitalityAge();
    }

    @Override
    public VitalityAge getPersistedVitalityAge() {
        return vitalityAgeInteractor.getPersistedVitalityAge();
    }

    @Override
    public void setVitalityAgeDisplayMode(int vitalityAgeDisplayMode) {
        this.vitalityAgeDisplayMode = vitalityAgeDisplayMode;
    }

    @Override
    public void setUserInterface(VitalityAgePresenter.UserInterface userInterface) {
        this.userInterface = userInterface;
    }
}
