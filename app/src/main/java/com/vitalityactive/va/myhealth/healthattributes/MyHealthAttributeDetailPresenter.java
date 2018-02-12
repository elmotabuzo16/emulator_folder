package com.vitalityactive.va.myhealth.healthattributes;


import com.vitalityactive.va.Presenter;
import com.vitalityactive.va.myhealth.content.FeedbackTip;
import com.vitalityactive.va.myhealth.content.HealthAttributeRecommendationItem;

import java.util.List;

public interface MyHealthAttributeDetailPresenter extends Presenter<MyHealthAttributeDetailPresenter.UserInterface> {

    void setHealthAttributeTypeKey(Integer attributeTypeKey);

    void setSectionTypeKey(Integer sectionTypeKey);

    interface UserInterface {

        void showHealthAttributeRecommendationAndFeedbackTip(HealthAttributeRecommendationItem attributeRecommendation, List<FeedbackTip> feedbackTipItem);

        void showTitle(String title);

        void hideLoadingIndicator();

        void showLoadingIndicator();
    }
}
