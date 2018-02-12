package com.vitalityactive.va.nonsmokersdeclaration;

import com.vitalityactive.va.MainThreadScheduler;
import com.vitalityactive.va.events.EventDispatcher;
import com.vitalityactive.va.termsandconditions.TermsAndConditionsConsenter;
import com.vitalityactive.va.termsandconditions.TermsAndConditionsInteractor;
import com.vitalityactive.va.termsandconditions.TermsAndConditionsPresenter;
import com.vitalityactive.va.termsandconditions.TermsAndConditionsPresenterImpl;

public class NonSmokersDeclarePresenter extends TermsAndConditionsPresenterImpl<TermsAndConditionsPresenter.UserInterface> {
    public NonSmokersDeclarePresenter(MainThreadScheduler scheduler, TermsAndConditionsInteractor interactor, TermsAndConditionsConsenter consenter, EventDispatcher eventDispatcher) {
        super(scheduler, interactor, consenter, eventDispatcher);
    }

    @Override
    public void onUserAgreesToTermsAndConditions() {
        userInterface.navigateAfterTermsAndConditionsAccepted();
    }
}
