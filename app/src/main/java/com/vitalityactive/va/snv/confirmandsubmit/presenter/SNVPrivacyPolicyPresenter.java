package com.vitalityactive.va.snv.confirmandsubmit.presenter;

import com.vitalityactive.va.snv.confirmandsubmit.view.SNVPrivacyPolicyUserInterface;
import com.vitalityactive.va.termsandconditions.TermsAndConditionsPresenter;

public interface SNVPrivacyPolicyPresenter extends TermsAndConditionsPresenter<SNVPrivacyPolicyUserInterface> {
    void onUserTriesToSubmitAgain();
}
