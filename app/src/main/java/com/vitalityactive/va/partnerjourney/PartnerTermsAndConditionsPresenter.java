package com.vitalityactive.va.partnerjourney;

import com.vitalityactive.va.Scheduler;
import com.vitalityactive.va.events.EventDispatcher;
import com.vitalityactive.va.termsandconditions.TermsAndConditionsConsenter;
import com.vitalityactive.va.termsandconditions.TermsAndConditionsInteractor;
import com.vitalityactive.va.termsandconditions.TermsAndConditionsWithoutAgreeButtonBarPresenter;

public class PartnerTermsAndConditionsPresenter extends TermsAndConditionsWithoutAgreeButtonBarPresenter {

    private final TermsAndConditionsInteractor interactor;

    public PartnerTermsAndConditionsPresenter(Scheduler scheduler, TermsAndConditionsInteractor interactor, TermsAndConditionsConsenter termsAndConditionsConsenter, EventDispatcher eventDispatcher) {
        super(scheduler, interactor, termsAndConditionsConsenter, eventDispatcher);
        this.interactor = interactor;
    }

    public void setArticleId(String articleId) {
        interactor.setArticleId(articleId);
    }
}
