package com.vitalityactive.va.termsandconditions;

import android.annotation.SuppressLint;

import com.vitalityactive.va.Scheduler;
import com.vitalityactive.va.events.EventDispatcher;

public class TermsAndConditionsWithoutAgreeButtonBarPresenter extends TermsAndConditionsPresenterImpl<TermsAndConditionsWithoutAgreeButtonBarUserInterface> {
    public TermsAndConditionsWithoutAgreeButtonBarPresenter(Scheduler scheduler,
                                                            TermsAndConditionsInteractor interactor,
                                                            TermsAndConditionsConsenter termsAndConditionsConsenter,
                                                            EventDispatcher eventDispatcher) {
        super(scheduler, interactor, termsAndConditionsConsenter, eventDispatcher);
    }

    @Override
    public void onUserInterfaceCreated(boolean isNewNavigation) {
        super.onUserInterfaceCreated(isNewNavigation);

        userInterface.hideButtonBar();
    }

    @SuppressLint("MissingSuperCall")
    @Override
    protected boolean shouldNavigateAfterSuccessfulAgreeRequest() {
        return false;
    }
}
