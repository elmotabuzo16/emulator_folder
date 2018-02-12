package com.vitalityactive.va.myhealth.vitalityageprofile;


import com.vitalityactive.va.myhealth.content.SectionItem;
import com.vitalityactive.va.myhealth.dto.HealthInformationSectionDTO;
import com.vitalityactive.va.myhealth.shared.MyHealthUtils;

import java.util.List;

public class MyHealthTipsMoreResultsPresenterImpl implements MyHealthTipsMoreResultsPresenter {

    private static final int TIPS_TO_LOAD = 3;
    MyHealthTipsInteractor myHealthTipsInteractor;
    UserInterface userInterface;
    private int sectionTypeKey;

    public MyHealthTipsMoreResultsPresenterImpl(MyHealthTipsInteractor myHealthTipsInteractor) {
        this.myHealthTipsInteractor = myHealthTipsInteractor;
    }

    @Override
    public void onUserInterfaceCreated(boolean isNewNavigation) {

    }

    @Override
    public void onUserInterfaceAppeared() {
        loadFeedbackTips();
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

    public void setSectionTypeKey(int sectionTypeKey) {
        this.sectionTypeKey = sectionTypeKey;
    }

    private void loadFeedbackTips() {
        List<HealthInformationSectionDTO> sections = myHealthTipsInteractor.getHealthAttributeInformationByParentTypeKey(sectionTypeKey);
        if (sections != null && !sections.isEmpty()) {
            HealthInformationSectionDTO firstSection = sections.get(0);
            HealthInformationSectionDTO secondSection = null;
            if (sections.size() > 1) {
                secondSection = sections.get(1);
            }
            userInterface.loadFeedbackTips(fetchDisplayFeedbackTips(firstSection), fetchDisplayFeedbackTips(secondSection));
        }
    }

    private SectionItem fetchDisplayFeedbackTips(HealthInformationSectionDTO healthInformationSectionDTO) {
        try {
            if (healthInformationSectionDTO != null) {
                SectionItem sectionItem = MyHealthUtils.toSectionItem(healthInformationSectionDTO);
                return sectionItem;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
