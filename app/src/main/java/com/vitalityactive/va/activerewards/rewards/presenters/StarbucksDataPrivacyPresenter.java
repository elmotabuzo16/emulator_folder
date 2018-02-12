package com.vitalityactive.va.activerewards.rewards.presenters;

import com.vitalityactive.va.activerewards.rewards.StarbucksDataPrivacyUserInterface;
import com.vitalityactive.va.termsandconditions.TermsAndConditionsPresenter;

public interface StarbucksDataPrivacyPresenter extends TermsAndConditionsPresenter<StarbucksDataPrivacyUserInterface> {
    void setRewardUniqueId(long rewardUniqueId);

    void setUserEmailAddress(String userEmailAddress);

    void onUserAgreesToTermsAndConditions();

    void setEventSource(String instructionType);
}
