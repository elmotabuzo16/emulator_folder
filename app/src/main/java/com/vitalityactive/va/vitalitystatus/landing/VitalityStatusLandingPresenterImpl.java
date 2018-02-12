package com.vitalityactive.va.vitalitystatus.landing;

import android.support.annotation.NonNull;

import com.vitalityactive.va.events.EventDispatcher;
import com.vitalityactive.va.events.EventListener;
import com.vitalityactive.va.home.events.GetProductFeaturePointsResponseEvent;

public class VitalityStatusLandingPresenterImpl implements VitalityStatusLandingPresenter {
    private UserInterface userInterface;
    private StatusLandingInteractor interactor;
    private EventDispatcher eventDispatcher;
    private EventListener<GetProductFeaturePointsResponseEvent> featurePointsResponseEventListener;
    private boolean isNewNavigation;

    public VitalityStatusLandingPresenterImpl(StatusLandingInteractor interactor,
                                              EventDispatcher eventDispatcher) {
        this.interactor = interactor;
        this.eventDispatcher = eventDispatcher;
    }

    @Override
    public void onUserInterfaceCreated(boolean isNewNavigation) {
        this.isNewNavigation = isNewNavigation;
        featurePointsResponseEventListener = getEventListener();
    }

    @NonNull
    private EventListener<GetProductFeaturePointsResponseEvent> getEventListener() {
        return new EventListener<GetProductFeaturePointsResponseEvent>() {
            @Override
            public void onEvent(GetProductFeaturePointsResponseEvent event) {
                if (event.isSuccessful()) {
                    userInterface.displayProductFeaturePoints(interactor.loadPointsCategories());
                } else {
                    userInterface.showError(event);
                }
            }
        };
    }

    @Override
    public void onUserInterfaceAppeared() {
        addEventListeners();

        userInterface.displayContent(interactor.loadVitalityStatus(), interactor.shouldShowMyRewards());

        if (interactor.hasFeaturePointsData()) {
            userInterface.displayProductFeaturePoints(interactor.loadPointsCategories());
        }

        if (isNewNavigation) {
            interactor.fetchProductFeaturePoints();
            isNewNavigation = false;
        }
    }

    private void addEventListeners() {
        eventDispatcher.addEventListener(GetProductFeaturePointsResponseEvent.class, featurePointsResponseEventListener);
    }

    @Override
    public void onUserInterfaceDisappeared(boolean isFinishing) {
        eventDispatcher.removeEventListener(GetProductFeaturePointsResponseEvent.class, featurePointsResponseEventListener);
    }

    @Override
    public void onUserInterfaceDestroyed() {

    }

    @Override
    public void setUserInterface(UserInterface userInterface) {
        this.userInterface = userInterface;
    }

    @Override
    public void retryLoading() {
        interactor.fetchProductFeaturePoints();
    }

    @Override
    public boolean requestInProgress() {
        return interactor.requestInProgress();
    }

    @Override
    public boolean hasFeatureCategories() {
        return interactor.hasFeaturePointsData();
    }
}
