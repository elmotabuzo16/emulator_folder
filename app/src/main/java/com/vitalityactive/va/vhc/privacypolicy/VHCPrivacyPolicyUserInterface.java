package com.vitalityactive.va.vhc.privacypolicy;

import com.vitalityactive.va.termsandconditions.TermsAndConditionsPresenter;

public interface VHCPrivacyPolicyUserInterface extends TermsAndConditionsPresenter.UserInterface {
    void showGenericSubmissionRequestErrorMessage();

    void showConnectionSubmissionRequestErrorMessage();
}
