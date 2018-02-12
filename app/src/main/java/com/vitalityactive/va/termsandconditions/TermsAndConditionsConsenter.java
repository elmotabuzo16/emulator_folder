package com.vitalityactive.va.termsandconditions;

import com.vitalityactive.va.networking.RequestResult;

public interface TermsAndConditionsConsenter {

    void agreeToTermsAndConditions();

    void agreeToTermsAndConditions(String name, String rewardKey, String instructionType);

    void disagreeToTermsAndConditions(String name, String rewardKey, String instructionType);

    void disagreeToTermsAndConditions();

    RequestResult getAgreeRequestResult();

    RequestResult getDisagreeRequestResult();

    boolean isRequestInProgress();
}
