package com.vitalityactive.va.snv.confirmandsubmit.view;

import com.vitalityactive.va.termsandconditions.TermsAndConditionsPresenter;

public interface SNVPrivacyPolicyUserInterface extends TermsAndConditionsPresenter.UserInterface {
    void showGenericSubmissionRequestErrorMessage();

    void showConnectionSubmissionRequestErrorMessage();
}
