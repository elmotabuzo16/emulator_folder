package com.vitalityactive.va.wellnessdevices.informative;

import android.support.annotation.NonNull;

import com.vitalityactive.va.Presenter;

public interface WellnessDevicesInformativePresenter extends Presenter<WellnessDevicesInformativePresenter.UserInterface> {
    void setArticleId(String articleId);
    void fetchContent();

    interface UserInterface {
        void showContent(@NonNull String termsAndConditions);

        void showLoadingIndicator();

        void hideLoadingIndicator();

        void showGenericContentRequestErrorMessage();

        void showConnectionContentRequestErrorMessage();
    }
}