package com.vitalityactive.va.vhc;

import android.support.annotation.NonNull;

import com.vitalityactive.va.Presenter;
import com.vitalityactive.va.vhc.summary.PresentableProof;

import java.util.List;

public interface VHCSummaryPresenter extends Presenter<VHCSummaryPresenter.UserInterface> {
    @NonNull
    List<PresentableCapturedGroup> getCapturedGroups();

    @NonNull
    PresentableProof getUploadedProof();

    void onUserConfirmed();

    interface UserInterface {
        void navigateAfterUserConfirmed();

        void showLoadingIndicator();

        void showConnectionErrorMessage();

        void showGenericErrorMessage();

        void hideLoadingIndicator();
    }
}
