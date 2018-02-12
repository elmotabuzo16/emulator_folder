package com.vitalityactive.va.activerewards.termsandconditions;

import com.vitalityactive.va.termsandconditions.TermsAndConditionsPresenter;

public interface ActiveRewardsTermsAndConditionsUserInterface extends TermsAndConditionsPresenter.UserInterface
{
    void showGenericActivationErrorMessage();

    void showConnectionActivationErrorMessage();

    void navigateAfterSkippingTermsAndConditions();
}
