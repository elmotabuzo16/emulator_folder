package com.vitalityactive.va.myhealth.tipdetail;


import android.support.annotation.NonNull;

import com.vitalityactive.va.networking.RequestResult;

public interface MyHealthTipDetailInteractor {

    void fetchContent(String articleId);

    void setCallback(MyHealthTipDetailInteractor.Callback callback);

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
