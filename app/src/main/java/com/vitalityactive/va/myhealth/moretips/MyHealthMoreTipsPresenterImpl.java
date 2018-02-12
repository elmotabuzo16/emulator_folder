package com.vitalityactive.va.myhealth.moretips;

import com.vitalityactive.va.myhealth.content.FeedbackTip;
import com.vitalityactive.va.myhealth.dto.AttributeDTO;
import com.vitalityactive.va.myhealth.shared.MyHealthUtils;

import java.util.List;

public class MyHealthMoreTipsPresenterImpl implements MyHealthMoreTipsPresenter {

    MyHealthMoreTipsInteractor myHealthMoreTipsInteractor;
    MyHealthMoreTipsPresenter.UserInterface userInterface;
    Integer sectionTypeKey;
    Integer attributeTypeKey;

    private int typeKey;

    public MyHealthMoreTipsPresenterImpl(MyHealthMoreTipsInteractor myHealthMoreTipsInteractor) {
        this.myHealthMoreTipsInteractor = myHealthMoreTipsInteractor;
    }

    @Override
    public void onUserInterfaceCreated(boolean isNewNavigation) {

    }

    @Override
    public void onUserInterfaceAppeared() {
        fetchFeedbackTips();
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

    private void fetchFeedbackTips() {
        if (attributeTypeKey != null && sectionTypeKey != null) {
            AttributeDTO attribute = myHealthMoreTipsInteractor.getHealthAttributeBySectionTypeKeyAndAttributeTypeKey(sectionTypeKey, attributeTypeKey);
            if (attribute != null) {
                userInterface.loadFeedbackTips(toFeedbackTip(attribute));
            }
        }
    }

    public List<FeedbackTip> toFeedbackTip(AttributeDTO attributeDto) {
        return MyHealthUtils.toFeedbackTips(attributeDto);
    }

    @Override
    public void setSectionTypeKey(Integer sectionTypeKey) {
        this.sectionTypeKey = sectionTypeKey;
    }

    @Override
    public void setAttributeTypeKey(Integer attributeTypeKey) {
        this.attributeTypeKey = attributeTypeKey;
    }
}
