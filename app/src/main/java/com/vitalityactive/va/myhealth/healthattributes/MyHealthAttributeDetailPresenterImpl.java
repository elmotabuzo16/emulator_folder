package com.vitalityactive.va.myhealth.healthattributes;


import com.vitalityactive.va.myhealth.content.FeedbackTip;
import com.vitalityactive.va.myhealth.content.HealthAttributeRecommendationItem;
import com.vitalityactive.va.myhealth.dto.AttributeDTO;
import com.vitalityactive.va.myhealth.shared.MyHealthUtils;

import java.util.List;

public class MyHealthAttributeDetailPresenterImpl implements MyHealthAttributeDetailPresenter {

    Integer attributeTypeKey;
    Integer sectionTypeKey;
    UserInterface userInterface;

    MyHealthAttributeDetailInteractor myHealthAttributeDetailInteractor;

    public MyHealthAttributeDetailPresenterImpl(MyHealthAttributeDetailInteractor myHealthAttributeDetailInteractor) {
        this.myHealthAttributeDetailInteractor = myHealthAttributeDetailInteractor;
    }

    @Override
    public void onUserInterfaceCreated(boolean isNewNavigation) {
        fetchHealthAttributes();
    }

    @Override
    public void onUserInterfaceAppeared() {
        fetchHealthAttributes();
    }

    @Override
    public void onUserInterfaceDisappeared(boolean isFinishing) {
    }

    @Override
    public void onUserInterfaceDestroyed() {
    }

    @Override
    public void setUserInterface(UserInterface userInterface) {
        this.userInterface = userInterface;
    }

    private void fetchHealthAttributes() {
        if (attributeTypeKey != null && sectionTypeKey != null) {
            AttributeDTO attribute = myHealthAttributeDetailInteractor.getHealthAttributeBySectionTypeKeyAndAttributeTypeKey(sectionTypeKey, attributeTypeKey);
            if (attribute != null) {
                HealthAttributeRecommendationItem recommendation = toHealthAttributeRecommendation(attribute);
                userInterface.showHealthAttributeRecommendationAndFeedbackTip(recommendation, toFeedbackTip(attribute));
                userInterface.showTitle(getHealthAttributeName(attribute));
            }
        }
    }

    public String getHealthAttributeName(AttributeDTO attributeDTO) {
        return attributeDTO.getAttributeTypeName();
    }

    public HealthAttributeRecommendationItem toHealthAttributeRecommendation(AttributeDTO attributeDTO) {
        return MyHealthUtils.toAttributeFeedbackRecommendationItem(this.sectionTypeKey, attributeDTO);
    }

    public List<FeedbackTip> toFeedbackTip(AttributeDTO attributeDTO) {
        return MyHealthUtils.toFeedbackTips(attributeDTO);
    }

    @Override
    public void setHealthAttributeTypeKey(Integer attributeTypeKey) {
        this.attributeTypeKey = attributeTypeKey;
    }

    public void setSectionTypeKey(Integer sectionTypeKey) {
        this.sectionTypeKey = sectionTypeKey;
    }
}
