package com.vitalityactive.va.activerewards.rewards;

import com.vitalityactive.va.termsandconditions.TermsAndConditionsPresenter;

public interface StarbucksDataPrivacyUserInterface extends TermsAndConditionsPresenter.UserInterface {
    void navigateAfterStarbucksRewardConfirmed(long uniqueID);

    void showConnectionErrorMessage();

    void showGenericErrorMessage();
}
