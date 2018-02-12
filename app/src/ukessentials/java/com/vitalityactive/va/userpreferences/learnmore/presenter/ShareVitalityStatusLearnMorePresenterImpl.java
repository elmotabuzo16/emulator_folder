package com.vitalityactive.va.userpreferences.learnmore.presenter;

import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.util.Log;

import com.vitalityactive.va.BasePresenter;
import com.vitalityactive.va.Scheduler;
import com.vitalityactive.va.events.EventDispatcher;
import com.vitalityactive.va.events.EventListener;
import com.vitalityactive.va.networking.RequestResult;
import com.vitalityactive.va.userpreferences.learnmore.interactor.ShareVitalityStatusInteractor;



public class ShareVitalityStatusLearnMorePresenterImpl<UI extends ShareVitalityStatusLearnMorePresenter.UI>
        extends BasePresenter<UI> implements ShareVitalityStatusLearnMorePresenter<UI>,
        ShareVitalityStatusInteractor.Callback {

    private final Scheduler scheduler;
    private EventDispatcher eventDispatcher;
    private ShareVitalityStatusInteractor interactor;
    @NonNull
    private RequestResult contentRequestResult = RequestResult.NONE;

    private final EventListener<ShareVitalityLearnMoreSuccessEvent> successEventEvent = new EventListener<ShareVitalityLearnMoreSuccessEvent>() {
        @Override
        public void onEvent(ShareVitalityLearnMoreSuccessEvent event) {
            userInterface.showVitalityStatusLearnMore(event.getTemplateHTML());
        }
    };

    public ShareVitalityStatusLearnMorePresenterImpl(final Scheduler scheduler, EventDispatcher eventDispatcher,
                                                     ShareVitalityStatusInteractor interactorParam) {

        this.scheduler = scheduler;
        this.eventDispatcher = eventDispatcher;
        this.interactor = interactorParam;
        this.interactor.setCallback(this);
    }

    @CallSuper
    protected void resetRequestResults() {
        contentRequestResult = RequestResult.NONE;
    }

    @Override
    public void onUserInterfaceCreated(boolean isNewNavigation) {
        if (isNewNavigation) {
            userInterface.showLoadingIndicator();
            eventDispatcher.addEventListener(ShareVitalityLearnMoreSuccessEvent.class, successEventEvent);
            resetRequestResults();
            fetchShareVitalityStatus();
        }
    }

    @Override
    public void onUserInterfaceAppeared() {
        super.onUserInterfaceAppeared();
        userInterface.hideLoadingIndicator();
    }

    @Override
    public void onUserInterfaceDisappeared(boolean isFinishing) {
        super.onUserInterfaceDisappeared(isFinishing);
        if(isFinishing) {
            eventDispatcher.removeEventListener(ShareVitalityLearnMoreSuccessEvent.class, successEventEvent);
        }
    }

    @Override
    public void onUserInterfaceDestroyed() {
        super.onUserInterfaceDestroyed();
    }

    @Override
    public void fetchShareVitalityStatus() {
        interactor.fetchShareVitalityStatusContent();
    }

    @Override
    public void setUserInterface(UI ui) {
        this.userInterface = ui;
    }

    @Override
    public void onShareVitalityStatusRetrieved(final @NonNull String shareVitalityStatusTemplate) {
        scheduler.schedule(new Runnable() {
            @Override
            public void run() {
                if (isUserInterfaceVisible()) {
                    userInterface.hideLoadingIndicator();
                    userInterface.showVitalityStatusLearnMore(shareVitalityStatusTemplate);
                }
            }
        });

    }

    @Override
    public void onGenericContentError() {
        Log.d("sLearnMorePresenterImpl", "Generic Error");
    }

    @Override
    public void onConnectionContentError() {
        Log.d("sLearnMorePresenterImpl", "Generic Error");
    }

}
