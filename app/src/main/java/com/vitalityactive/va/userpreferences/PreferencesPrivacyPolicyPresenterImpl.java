package com.vitalityactive.va.userpreferences;

import com.vitalityactive.va.Scheduler;
import com.vitalityactive.va.events.EventDispatcher;
import com.vitalityactive.va.termsandconditions.TermsAndConditionsConsenter;
import com.vitalityactive.va.termsandconditions.TermsAndConditionsInteractor;
import com.vitalityactive.va.termsandconditions.TermsAndConditionsWithoutAgreeButtonBarPresenter;

public class PreferencesPrivacyPolicyPresenterImpl extends TermsAndConditionsWithoutAgreeButtonBarPresenter {

    public PreferencesPrivacyPolicyPresenterImpl(Scheduler scheduler,
                                                 TermsAndConditionsInteractor interactor,
                                                 TermsAndConditionsConsenter consenter,
                                                 EventDispatcher eventDispatcher) {
        super(scheduler, interactor, consenter, eventDispatcher);
    }
}