package com.vitalityactive.va.myhealth.vitalityageprofile;


import com.vitalityactive.va.Presenter;
import com.vitalityactive.va.myhealth.content.SectionItem;

public interface MyHealthTipsMoreResultsPresenter extends Presenter<MyHealthTipsMoreResultsPresenter.UserInterface> {

    void setSectionTypeKey(int typeKey);

    interface UserInterface {
        void loadFeedbackTips(SectionItem vitalityAgeTips, SectionItem vitalityAgeTipSummaries);

    }
}
