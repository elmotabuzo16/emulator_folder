package com.vitalityactive.va.myhealth.tipdetail;

import android.support.annotation.NonNull;

import com.vitalityactive.va.Presenter;

public interface MyHealthTipDetailPresenter extends Presenter<MyHealthTipDetailPresenter.UserInterface> {

    void fetchContent();

    void setTipTypeKey(int tipTypeKey);

    void setTipCode(String tipCode);

    interface UserInterface {
        void showContent(@NonNull String termsAndConditions);

        void showLoadingIndicator();

        void hideLoadingIndicator();

        void showGenericContentRequestErrorMessage();

        void showConnectionContentRequestErrorMessage();
    }
}
