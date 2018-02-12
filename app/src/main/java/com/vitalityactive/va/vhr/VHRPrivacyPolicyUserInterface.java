package com.vitalityactive.va.vhr;

import com.vitalityactive.va.termsandconditions.TermsAndConditionsPresenter;

public interface VHRPrivacyPolicyUserInterface extends TermsAndConditionsPresenter.UserInterface {
    void navigateAfterTermsAndConditionsAccepted();

    void showGenericSubmissionRequestErrorMessage();

    void showConnectionSubmissionRequestErrorMessage();
}
