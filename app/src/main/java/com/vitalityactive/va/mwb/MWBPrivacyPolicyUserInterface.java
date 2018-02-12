package com.vitalityactive.va.mwb;

import com.vitalityactive.va.termsandconditions.TermsAndConditionsPresenter;

/**
 * Created by christian.j.p.capin on 1/30/2018.
 */

public interface MWBPrivacyPolicyUserInterface extends TermsAndConditionsPresenter.UserInterface {
    void navigateAfterTermsAndConditionsAccepted();

    void showGenericSubmissionRequestErrorMessage();

    void showConnectionSubmissionRequestErrorMessage();
}
