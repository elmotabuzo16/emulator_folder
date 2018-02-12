package com.vitalityactive.va.myhealth.disclaimer;

import com.vitalityactive.va.Scheduler;
import com.vitalityactive.va.events.EventDispatcher;
import com.vitalityactive.va.termsandconditions.TermsAndConditionsConsenter;
import com.vitalityactive.va.termsandconditions.TermsAndConditionsInteractor;
import com.vitalityactive.va.termsandconditions.TermsAndConditionsWithoutAgreeButtonBarPresenter;

public class MyHealthDisclaimerPresenter extends TermsAndConditionsWithoutAgreeButtonBarPresenter {

    public MyHealthDisclaimerPresenter(Scheduler scheduler,
                                       TermsAndConditionsInteractor interactor,
                                       TermsAndConditionsConsenter termsAndConditionsConsenter,
                                       EventDispatcher eventDispatcher) {
        super(scheduler, interactor, termsAndConditionsConsenter, eventDispatcher);
    }
}
