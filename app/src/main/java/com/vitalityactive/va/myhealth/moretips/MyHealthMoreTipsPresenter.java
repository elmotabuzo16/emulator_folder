package com.vitalityactive.va.myhealth.moretips;

import com.vitalityactive.va.Presenter;
import com.vitalityactive.va.myhealth.content.FeedbackTip;

import java.util.List;

public interface MyHealthMoreTipsPresenter extends Presenter<MyHealthMoreTipsPresenter.UserInterface> {

    void setSectionTypeKey(Integer sectionTypeKey);

    void setAttributeTypeKey(Integer attributeTypeKey);

    interface UserInterface {
        void loadFeedbackTips(List<FeedbackTip> feedbackTipList);

    }
}
