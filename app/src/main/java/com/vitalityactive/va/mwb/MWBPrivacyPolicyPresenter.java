package com.vitalityactive.va.mwb;

import com.vitalityactive.va.termsandconditions.TermsAndConditionsPresenter;

/**
 * Created by christian.j.p.capin on 1/30/2018.
 */

public interface MWBPrivacyPolicyPresenter extends TermsAndConditionsPresenter<MWBPrivacyPolicyUserInterface> {
    void setQuestionnaireTypeKey(long questionnaireTypeKey);

    void onUserTriesToSubmitAgain();
}