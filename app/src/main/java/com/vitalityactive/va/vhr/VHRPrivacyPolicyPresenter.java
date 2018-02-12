package com.vitalityactive.va.vhr;

import com.vitalityactive.va.termsandconditions.TermsAndConditionsPresenter;

public interface VHRPrivacyPolicyPresenter extends TermsAndConditionsPresenter<VHRPrivacyPolicyUserInterface>{
    void setQuestionnaireTypeKey(long questionnaireTypeKey);

    void onUserTriesToSubmitAgain();
}
