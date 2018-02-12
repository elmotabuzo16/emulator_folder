package com.vitalityactive.va.myhealth.tipdetail;

import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.vitalityactive.va.Scheduler;
import com.vitalityactive.va.networking.RequestResult;

public class MyHealthTipDetailPresenterImpl implements MyHealthTipDetailPresenter, MyHealthTipDetailInteractor.Callback {
    protected final Scheduler scheduler;
    protected MyHealthTipDetailPresenter.UserInterface userInterface;
    protected MyHealthTipDetailInteractor interactor;
    protected boolean didShowRequestErrorMessage;
    private boolean isUserInterfaceVisible;
    private int tipTypeKey;
    private String tipCode;

    public MyHealthTipDetailPresenterImpl(final Scheduler scheduler,
                                          MyHealthTipDetailInteractor interactor) {
        this.scheduler = scheduler;
        this.interactor = interactor;
        interactor.setCallback(this);
    }

    protected void resetUserInterfaceAfterRequestFinished() {
        userInterface.hideLoadingIndicator();
    }

    @Override
    public void setUserInterface(MyHealthTipDetailPresenter.UserInterface userInterface) {
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
        if (TextUtils.isEmpty(content)) {
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
        return interactor.isFetchingContent(getTipArticleId());
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
    }


    @Override
    public void fetchContent() {
        didShowRequestErrorMessage = false;
        userInterface.showLoadingIndicator();
        interactor.clearContentRequestResult();
        interactor.fetchContent(getTipArticleId());
    }

    @Override
    public void setTipTypeKey(int tipTypeKey) {
        this.tipTypeKey = tipTypeKey;
    }

    @Override
    public void setTipCode(String tipCode) {
        this.tipCode = tipCode;
    }

    public void setInteractor(MyHealthTipDetailInteractor interactor) {
        this.interactor = interactor;
    }

    @Override
    public void onContentRetrieved(final @NonNull String tipDetailContent) {
        if (isUserInterfaceVisible) {
            scheduler.schedule(new Runnable() {
                @Override
                public void run() {
                    userInterface.hideLoadingIndicator();
                    userInterface.showContent(tipDetailContent);
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

    protected boolean isUserInterfaceVisible() {
        return isUserInterfaceVisible;
    }

    private String getTipArticleId() {
        return tipCode != null ? tipTypeKey + "-" + tipCode.toLowerCase() : "";
    }
}
