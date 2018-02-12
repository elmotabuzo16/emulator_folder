package com.vitalityactive.va.vna;

import com.vitalityactive.va.termsandconditions.TermsAndConditionsPresenter;

public interface VNAPrivacyPolicyUserInterface extends TermsAndConditionsPresenter.UserInterface {
    void navigateAfterTermsAndConditionsAccepted();

    void showGenericSubmissionRequestErrorMessage();

    void showConnectionSubmissionRequestErrorMessage();
}
