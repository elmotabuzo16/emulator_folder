package com.vitalityactive.va.snv.onboarding.presenter;

import com.vitalityactive.va.Presenter;


/**
 * Created by kerry.e.lawagan on 11/26/2017.
 */

public interface ScreeningsAndVaccinationsOnboardingPresenter<UserInterface extends ScreeningsAndVaccinationsOnboardingPresenter.UserInterface> extends Presenter<UserInterface> {

    interface UserInterface {
        void showLoadingIndicator();
        void hideLoadingIndicator();
        void showConnectionContentRequestErrorMessage();
        void showGenericContentRequestErrorMessage();
        void updateScreeningsPoints(int points);
        void updateVaccinationsPoints(int points);
    }
}
