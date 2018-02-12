package com.vitalityactive.va.myhealth.vitalityageprofile;

import com.vitalityactive.va.Presenter;
import com.vitalityactive.va.myhealth.BaseVitalityAgeInterface;
import com.vitalityactive.va.myhealth.content.SectionItem;
import com.vitalityactive.va.myhealth.entity.VitalityAge;

import java.util.List;

public interface MyHealthVitalityAgeProfilePresenter extends Presenter<MyHealthVitalityAgeProfilePresenter.UserInterface> {

    boolean sectionHasSubsections(long typeKey);

    interface UserInterface extends BaseVitalityAgeInterface {

        void loadVitalityAgeTips(List<SectionItem> vitalityAgeTipList);

        void hideLoadingIndicator();

        void showLoadingIndicator();

        void showVitalityAgeAndTips(VitalityAge vitalityAge, List<SectionItem> feedbackTipItems);
    }
}
