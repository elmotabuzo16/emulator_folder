package com.vitalityactive.va.termsandconditions;

import android.support.annotation.NonNull;

import com.vitalityactive.va.Presenter;

public interface TermsAndConditionsPresenter<UserInterface extends TermsAndConditionsPresenter.UserInterface> extends Presenter<UserInterface> {
    void onUserAgreesToTermsAndConditions();

    void onBackPressed();

    void onUserDisagreesToTermsAndConditions();

    void fetchTermsAndConditions();

    interface UserInterface {
        void showTermsAndConditions(@NonNull String termsAndConditions);

        void navigateAfterTermsAndConditionsAccepted();

        void navigateAfterTermsAndConditionsDeclined();

        void disableAgreeButton();

        void enableAgreeButton();

        void showLoadingIndicator();

        void hideLoadingIndicator();

        void showGenericAgreeRequestErrorMessage();

        void showConnectionAgreeRequestErrorMessage();

        void showDisagreeAlert();

        void showGenericDisagreeRequestErrorMessage();

        void showConnectionDisagreeRequestErrorMessage();

        void showGenericContentRequestErrorMessage();

        void showConnectionContentRequestErrorMessage();

    }
}
