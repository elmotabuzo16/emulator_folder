package com.vitalityactive.va.termsandconditions;

import com.vitalityactive.va.networking.RequestResult;

public class TermsAndConditionsConsenterBase implements TermsAndConditionsConsenter {

    @Override
    public void agreeToTermsAndConditions() {

    }

    @Override
    public void agreeToTermsAndConditions(String name, String rewardKey, String instructionType) {

    }

    @Override
    public void disagreeToTermsAndConditions(String name, String rewardKey, String instructionType) {

    }

    @Override
    public void disagreeToTermsAndConditions() {

    }

    @Override
    public RequestResult getAgreeRequestResult() {
        return null;
    }

    @Override
    public RequestResult getDisagreeRequestResult() {
        return null;
    }

    @Override
    public boolean isRequestInProgress() {
        return false;
    }
}
