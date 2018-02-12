package com.vitalityactive.va.vna;

import com.vitalityactive.va.termsandconditions.TermsAndConditionsPresenter;

public interface VNAPrivacyPolicyPresenter extends TermsAndConditionsPresenter<VNAPrivacyPolicyUserInterface>{
    void setQuestionnaireTypeKey(long questionnaireTypeKey);

    void onUserTriesToSubmitAgain();
}
