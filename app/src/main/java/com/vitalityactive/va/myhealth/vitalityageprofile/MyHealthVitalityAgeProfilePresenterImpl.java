package com.vitalityactive.va.myhealth.vitalityageprofile;

import com.vitalityactive.va.myhealth.content.SectionItem;
import com.vitalityactive.va.myhealth.dto.VitalityAgeHealthAttributeDTO;
import com.vitalityactive.va.myhealth.entity.VitalityAge;
import com.vitalityactive.va.myhealth.shared.MyHealthUtils;
import com.vitalityactive.va.myhealth.shared.VitalityAgeConstants;
import com.vitalityactive.va.vhc.dto.HealthAttributeFeedbackDTO;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MyHealthVitalityAgeProfilePresenterImpl implements MyHealthVitalityAgeProfilePresenter {

    MyHealthVitalityAgeProfileInteractor myHealthVitalityAgeProfileInteractor;
    UserInterface userInterface;

    public MyHealthVitalityAgeProfilePresenterImpl(MyHealthVitalityAgeProfileInteractor myHealthVitalityAgeProfileInteractor) {
        this.myHealthVitalityAgeProfileInteractor = myHealthVitalityAgeProfileInteractor;
    }

    private List<SectionItem> getFeedbackTipItems() {
        List<SectionItem> sectionItems = new ArrayList<>();
        SectionItem sectionItemGood = MyHealthUtils.toSectionItem(myHealthVitalityAgeProfileInteractor.getHealthAttributeSectionDTOByTypeKey(VitalityAgeConstants.V_AGE_TYPE_KEY_RESULT_GOOD));
        if (sectionItemGood != null) {
            sectionItemGood.setHasSubsection(sectionHasSubsections(VitalityAgeConstants.V_AGE_TYPE_KEY_RESULT_GOOD));
            sectionItems.add(sectionItemGood);
        }

        SectionItem sectionItemUnknown = MyHealthUtils.toSectionItem(myHealthVitalityAgeProfileInteractor.getHealthAttributeSectionDTOByTypeKey(VitalityAgeConstants.V_AGE_TYPE_KEY_RESULT_UNKNOWN));
        if (sectionItemUnknown != null) {
            sectionItemUnknown.setHasSubsection(sectionHasSubsections(VitalityAgeConstants.V_AGE_TYPE_KEY_RESULT_UNKNOWN));
            sectionItems.add(sectionItemUnknown);
        }
        SectionItem sectionItemBad = MyHealthUtils.toSectionItem(myHealthVitalityAgeProfileInteractor.getHealthAttributeSectionDTOByTypeKey(VitalityAgeConstants.V_AGE_TYPE_KEY_RESULT_BAD));
        if (sectionItemBad != null) {
            sectionItemBad.setHasSubsection(sectionHasSubsections(VitalityAgeConstants.V_AGE_TYPE_KEY_RESULT_BAD));
            sectionItems.add(sectionItemBad);
        }
        Collections.sort(sectionItems, new Comparator<SectionItem>() {
            @Override
            public int compare(SectionItem t1, SectionItem t2) {
                return t1.sectionSortOrder > t2.sectionSortOrder ? 1 : -1;
            }
        });
        return sectionItems;
    }

    @Override
    public void onUserInterfaceCreated(boolean isNewNavigation) {

    }

    @Override
    public void onUserInterfaceAppeared() {
        userInterface.showVitalityAgeAndTips(getVitalityAge(), getFeedbackTipItems());
    }


    private VitalityAge getVitalityAge() {
        VitalityAgeHealthAttributeDTO attributeDTO = myHealthVitalityAgeProfileInteractor.getVitalityAgeHealthAttribute();
        if (attributeDTO != null) {
            HealthAttributeFeedbackDTO effectiveFeedback = attributeDTO.getEffectiveFeedback();
            if (effectiveFeedback != null) {
                return new VitalityAge.Builder()
                        .age(attributeDTO.getValue())
                        .feedbackTitle(effectiveFeedback.getFeedbackTypeName())
                        .effectiveType(effectiveFeedback.getFeedbackTypeKey())
                        .actualType(attributeDTO.getActualFeedback() != null ? attributeDTO.getActualFeedback().getFeedbackTypeKey() : 0)
                        .feedbackContent(effectiveFeedback.getFeedbackTypeCode())
                        .variance(attributeDTO.getVariance())
                        .build();
            }
        }
        return null;
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

    @Override
    public boolean sectionHasSubsections(long typeKey) {
        try {
            return myHealthVitalityAgeProfileInteractor.healthinformationSectionHasSubsections(typeKey);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
