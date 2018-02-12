package com.vitalityactive.va.activerewards.rewards;

import com.vitalityactive.va.termsandconditions.TermsAndConditionsPresenter;

public interface ActiveRewardsDataPrivacyUserInterface extends TermsAndConditionsPresenter.UserInterface {
    void navigateAfterRewardChoiceConfirmed(long uniqueID);

    void showConnectionErrorMessage();

    void showGenericErrorMessage();
}
