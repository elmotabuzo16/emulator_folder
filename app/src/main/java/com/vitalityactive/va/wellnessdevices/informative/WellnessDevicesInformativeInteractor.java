package com.vitalityactive.va.wellnessdevices.informative;

import android.support.annotation.NonNull;

import com.vitalityactive.va.networking.RequestResult;

public interface WellnessDevicesInformativeInteractor {
    void fetchContent(String articleId);
    void setCallback(Callback callback);
    boolean isFetchingContent(String articleId);
    RequestResult getContentRequestResult();
    void clearContentRequestResult();
    String getTermsAndConditions();

    interface Callback {
        void onContentRetrieved(@NonNull String result);
        void onGenericContentError();
        void onConnectionContentError();
    }
}
