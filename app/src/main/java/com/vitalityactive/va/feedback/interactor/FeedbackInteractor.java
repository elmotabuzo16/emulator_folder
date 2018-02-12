package com.vitalityactive.va.feedback.interactor;

import android.support.annotation.NonNull;

import com.vitalityactive.va.networking.RequestResult;

/**
 * Created by christian.j.p.capin on 1/5/2018.
 */

public interface FeedbackInteractor {
    void fetchFeedbacks();

    void setCallback(Callback callback);

    boolean isFetchingFeedbacks();

    RequestResult getContentRequestResult();

    String getFeedbacks();

    interface Callback {
        void onFeedbackRetrieved(@NonNull String feedbacks);

        void onGenericContentError();

        void onConnectionContentError();
    }
}
