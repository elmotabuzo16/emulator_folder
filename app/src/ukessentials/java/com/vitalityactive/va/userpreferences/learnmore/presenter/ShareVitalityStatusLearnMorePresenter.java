package com.vitalityactive.va.userpreferences.learnmore.presenter;


import android.support.annotation.NonNull;

import com.vitalityactive.va.Presenter;

public interface ShareVitalityStatusLearnMorePresenter<UserInterface extends ShareVitalityStatusLearnMorePresenter.UI>
        extends Presenter<UserInterface> {

    @Override
    void onUserInterfaceCreated(boolean isNewNavigation);

    @Override
    void onUserInterfaceAppeared();

    @Override
    void onUserInterfaceDisappeared(boolean isFinishing);

    @Override
    void onUserInterfaceDestroyed();

    void fetchShareVitalityStatus();

    interface UI {

            void showVitalityStatusLearnMore(final @NonNull String shareVitalityStatusHtml);

            void showLoadingIndicator();

            void hideLoadingIndicator();
    }

}
