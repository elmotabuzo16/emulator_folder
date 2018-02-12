package com.vitalityactive.va.myhealth.landing;

import com.vitalityactive.va.Presenter;
import com.vitalityactive.va.myhealth.entity.VitalityAge;

public interface MyHealthLandingPresenter extends Presenter<MyHealthLandingPresenter.UserInterface> {

    interface UserInterface {

        void showLoadingIndicator();

        void hideLoadingIndicator();

        void loadVitalityAge(VitalityAge vitalityAge);
    }
}
