package com.vitalityactive.va.wellnessdevices.informative;

import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.vitalityactive.va.Scheduler;
import com.vitalityactive.va.networking.RequestResult;

public class WellnessDevicesInformativePresenterImpl
        implements WellnessDevicesInformativePresenter, WellnessDevicesInformativeInteractor.Callback {
    protected UserInterface userInterface;
    protected final Scheduler scheduler;
    protected WellnessDevicesInformativeInteractor interactor;
    protected boolean didShowRequestErrorMessage;
    private boolean isUserInterfaceVisible;
    private String articleId;

    public WellnessDevicesInformativePresenterImpl(final Scheduler scheduler,
                                                   WellnessDevicesInformativeInteractor interactor) {
        this.scheduler = scheduler;
        this.interactor = interactor;
        interactor.setCallback(this);
    }

    protected void resetUserInterfaceAfterRequestFinished() {
        userInterface.hideLoadingIndicator();
    }

    @Override
    public void setUserInterface(UserInterface userInterface) {
        this.userInterface = userInterface;
    }

    @Override
    public void onUserInterfaceCreated(boolean isNewNavigation) {
        fetchContent();
    }

    @Override
    @CallSuper
    public void onUserInterfaceAppeared() {
        isUserInterfaceVisible = true;

        if (isRequestInProgress()) {
            configureUserInterfaceForRequestInProgress();
            return;
        }

        resetUserInterfaceAfterRequestFinished();

        final String content = interactor.getTermsAndConditions();
        if(TextUtils.isEmpty(content)){
            if (!didShowRequestErrorMessage) {
                didShowRequestErrorMessage = true;

                if (interactor.getContentRequestResult() == RequestResult.GENERIC_ERROR) {
                    userInterface.showGenericContentRequestErrorMessage();
                    return;
                }

                if (interactor.getContentRequestResult() == RequestResult.CONNECTION_ERROR) {
                    userInterface.showConnectionContentRequestErrorMessage();
                }
            }
        } else {
            userInterface.showContent(content);
        }
    }


    @CallSuper
    protected boolean isRequestInProgress() {
        return interactor.isFetchingContent(articleId);
    }

    @Override
    public final void onUserInterfaceDisappeared(boolean isFinishing) {
        isUserInterfaceVisible = false;
        scheduler.cancel();
    }

    protected void configureUserInterfaceForRequestInProgress() {
        userInterface.showLoadingIndicator();
    }

    @Override
    public void onUserInterfaceDestroyed() {
        // NOP
    }

    @Override
    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }

    @Override
    public void fetchContent() {
        didShowRequestErrorMessage = false;
        userInterface.showLoadingIndicator();
        interactor.clearContentRequestResult();
        interactor.fetchContent(this.articleId);
    }

    public void setInteractor(WellnessDevicesInformativeInteractor interactor) {
        this.interactor = interactor;
    }

    @Override
    public void onContentRetrieved(final @NonNull String termsAndConditions) {
        if (isUserInterfaceVisible) {
            scheduler.schedule(new Runnable() {
                @Override
                public void run() {
                    userInterface.hideLoadingIndicator();
                    userInterface.showContent(termsAndConditions);
                }
            });
        }
    }

    @Override
    public void onGenericContentError() {
        if (isUserInterfaceVisible) {
            scheduler.schedule(new Runnable() {
                @Override
                public void run() {
                    didShowRequestErrorMessage = true;
                    userInterface.hideLoadingIndicator();
                    userInterface.showGenericContentRequestErrorMessage();
                }
            });
        }
    }

    @Override
    public void onConnectionContentError() {
        if (isUserInterfaceVisible) {
            scheduler.schedule(new Runnable() {
                @Override
                public void run() {
                    didShowRequestErrorMessage = true;
                    userInterface.hideLoadingIndicator();
                    userInterface.showConnectionContentRequestErrorMessage();
                }
            });
        }
    }

    protected boolean isUserInterfaceVisible(){
        return isUserInterfaceVisible;
    }

}
