package com.vitalityactive.va.feedback.presenter;

import android.support.annotation.NonNull;
import android.util.Log;

import com.vitalityactive.va.MainThreadScheduler;
import com.vitalityactive.va.events.EventDispatcher;
import com.vitalityactive.va.feedback.interactor.FeedbackInteractor;

/**
 * Created by christian.j.p.capin on 1/5/2018.
 */

public class FeedbackPresenterImpl implements FeedbackPresenter, FeedbackInteractor.Callback {

    private MainThreadScheduler scheduler;
    private final EventDispatcher eventDispatcher;

    public FeedbackInteractor interactor;
    private FeedbackPresenter.UserInterface userInterface;

    public FeedbackPresenterImpl(FeedbackInteractor interactor, EventDispatcher eventDispatcher, @NonNull MainThreadScheduler scheduler) {
        this.interactor = interactor;
        this.eventDispatcher = eventDispatcher;
        this.scheduler = scheduler;
        this.interactor.setCallback(this);
    }

    @Override
    public void onUserInterfaceCreated(boolean isNewNavigation) {
        Log.e("", "onUserInterfaceCreated");
        fetchFeedbacks();
    }

    @Override
    public void onUserInterfaceAppeared() {

    }

    @Override
    public void onUserInterfaceDisappeared(boolean isFinishing) {

    }

    @Override
    public void onUserInterfaceDestroyed() {
        if (userInterface != null) userInterface.activityDestroyed();
    }

    @Override
    public void setUserInterface(FeedbackPresenter.UserInterface userInterface) {
        this.userInterface = userInterface;
    }


    @Override
    public void onBackPressed() {

    }

    @Override
    public void fetchFeedbacks() {
        Log.e("fetchFeedbacks", "start");
        interactor.fetchFeedbacks();
    }

    @Override
    public void onFeedbackRetrieved(final @NonNull String feedbacks) {
        scheduler.schedule(new Runnable() {
                               @Override
                               public void run() {
                                  if (userInterface != null) userInterface.showFeedbacks(feedbacks);
                               }
                           }
        );

    }

    @Override
    public void onGenericContentError() {
        Log.e("Feedback", "Problem on fetching request");
    }

    @Override
    public void onConnectionContentError() {
        Log.e("Feedback", "Problem on connection request");
    }


}
