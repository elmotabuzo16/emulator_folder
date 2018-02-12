package com.vitalityactive.va.termsandconditions;

import android.support.annotation.NonNull;

import com.vitalityactive.va.networking.RequestResult;

public interface TermsAndConditionsInteractor {
    void fetchTermsAndConditions();

    void setArticleId(@NonNull String articleId);

    void setCallback(Callback callback);

    boolean isFetchingTermsAndConditions();

    RequestResult getContentRequestResult();

    String getTermsAndConditions();

    interface Callback {
        void onTermsAndConditionsRetrieved(@NonNull String termsAndConditions);

        void onGenericContentError();

        void onConnectionContentError();
    }
}
