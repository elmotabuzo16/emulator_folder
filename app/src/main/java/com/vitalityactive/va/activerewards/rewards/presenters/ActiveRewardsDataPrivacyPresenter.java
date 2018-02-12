package com.vitalityactive.va.activerewards.rewards.presenters;

import com.vitalityactive.va.activerewards.rewards.ActiveRewardsDataPrivacyUserInterface;
import com.vitalityactive.va.termsandconditions.TermsAndConditionsPresenter;

public interface ActiveRewardsDataPrivacyPresenter extends TermsAndConditionsPresenter<ActiveRewardsDataPrivacyUserInterface> {
    void setRewardUniqueId(long rewardUniqueId);

    void setRewardId(int rewardId);

    void onUserAgreesToTermsAndConditions();

    void setEventSource(String instructionType);
}
