package com.vitalityactive.va.myhealth.vitalityage;

import com.vitalityactive.va.Presenter;
import com.vitalityactive.va.myhealth.BaseVitalityAgeInterface;
import com.vitalityactive.va.myhealth.entity.VitalityAge;


public interface VitalityAgePresenter extends Presenter<VitalityAgePresenter.UserInterface> {

    VitalityAge getPersistedVitalityAge();

    void setVitalityAgeDisplayMode(int vitalityAgeDisplayMode);

    void setUserInterface(VitalityAgePresenter.UserInterface userInterface);

    boolean shouldShowVitalityAge();

    void setHasShownVitalityAge();

    interface UserInterface extends BaseVitalityAgeInterface {

        void loadVitalityAgeVHCDoneVHRPending();

        void showLoadingIndicator();

        void hideLoadingIndicator();
    }


}
