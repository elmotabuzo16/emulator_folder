package com.vitalityactive.va.userpreferences.learnmore.interactor;

import android.support.annotation.NonNull;

import com.vitalityactive.va.networking.RequestResult;
import com.vitalityactive.va.termsandconditions.TermsAndConditionsInteractor;

/**
 * Created by dharel.h.rosell on 1/4/2018.
 */

public interface ShareVitalityStatusInteractor {

    void fetchShareVitalityStatusContent();

    String gethShareVitalityStatusContent();

    void setCallback(Callback callback);

    boolean isFetchingShareVitalittyStatus();

    RequestResult getContentRequestResult();

    interface Callback {

        void onShareVitalityStatusRetrieved(@NonNull String shareVitalityStatusTemplate);

        void onGenericContentError();

        void onConnectionContentError();
    }
}
