package com.vitalityactive.va.feedback.presenter;

import android.support.annotation.NonNull;

import com.vitalityactive.va.Presenter;


/**
 * Created by christian.j.p.capin on 1/5/2018.
 */

public interface FeedbackPresenter extends Presenter<FeedbackPresenter.UserInterface> {
// <UserInterface extends FeedbackPresenter.UserInterface> extends Presenter<UserInterface> {

    void onBackPressed();
    void fetchFeedbacks();

    interface UserInterface {
        void showFeedbacks(@NonNull String feedbacks);
        void showLoadingIndicator();
        void hideLoadingIndicator();
        void activityDestroyed();
    }
}
