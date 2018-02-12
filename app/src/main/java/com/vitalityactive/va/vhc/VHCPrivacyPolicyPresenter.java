package com.vitalityactive.va.vhc;

import com.vitalityactive.va.termsandconditions.TermsAndConditionsPresenter;
import com.vitalityactive.va.vhc.privacypolicy.VHCPrivacyPolicyUserInterface;

public interface VHCPrivacyPolicyPresenter extends TermsAndConditionsPresenter<VHCPrivacyPolicyUserInterface> {
    void onUserTriesToSubmitAgain();
}
