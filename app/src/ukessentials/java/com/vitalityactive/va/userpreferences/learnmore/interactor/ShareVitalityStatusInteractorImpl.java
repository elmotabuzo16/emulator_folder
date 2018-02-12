package com.vitalityactive.va.userpreferences.learnmore.interactor;

import android.support.annotation.NonNull;
import android.util.Log;

import com.vitalityactive.va.cms.CMSServiceClient;
import com.vitalityactive.va.events.EventDispatcher;
import com.vitalityactive.va.networking.RequestResult;
import com.vitalityactive.va.networking.WebServiceResponseParser;
import com.vitalityactive.va.userpreferences.learnmore.presenter.ShareVitalityLearnMoreSuccessEvent;

/**
 * Created by dharel.h.rosell on 1/4/2018.
 */

public class ShareVitalityStatusInteractorImpl implements ShareVitalityStatusInteractor, WebServiceResponseParser<String> {

    private final String hardcode_articleId = "profile-settings-vitality-status";

    @NonNull
    private final CMSServiceClient serviceClient;
    @NonNull
    private final String articleId;
    private String liferayGroupId;
    private volatile String vitalityStatusContent;
    private EventDispatcher eventDispatcher;
    private Callback callback;
    private RequestResult contentRequestResult = RequestResult.NONE;

    public ShareVitalityStatusInteractorImpl(@NonNull CMSServiceClient cmsServiceClient, @NonNull EventDispatcher eventDispatcher,
                                             @NonNull String articleId, String liferayGroupId) {
        this.serviceClient = cmsServiceClient;
        this.liferayGroupId = liferayGroupId;
        this.articleId = articleId; //to be use for the actual
        this.eventDispatcher = eventDispatcher;
    }

    @Override
    public void fetchShareVitalityStatusContent() {
        serviceClient.fetchContentWithId(this.liferayGroupId, this.articleId, this);
    }

    @Override
    public String gethShareVitalityStatusContent() {
        return vitalityStatusContent;
    }

    @Override
    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    @Override
    public boolean isFetchingShareVitalittyStatus() {
        return serviceClient.isFetchingContentWithId(this.articleId);
    }

    @Override
    public RequestResult getContentRequestResult() {
        return contentRequestResult;
    }

    @Override
    public void parseResponse(String response) {
        vitalityStatusContent = response;
        setContentRequestResult(RequestResult.SUCCESSFUL);
        eventDispatcher.dispatchEvent(new ShareVitalityLearnMoreSuccessEvent(response));
        callback.onShareVitalityStatusRetrieved(response);
    }

    @Override
    public void parseErrorResponse(String errorBody, int code) {
        setContentRequestResult(RequestResult.GENERIC_ERROR);
        callback.onGenericContentError();
    }

    @Override
    public void handleGenericError(Exception exception) {
        setContentRequestResult(RequestResult.GENERIC_ERROR);
        callback.onGenericContentError();
    }

    @Override
    public void handleConnectionError() {
        setContentRequestResult(RequestResult.GENERIC_ERROR);
        callback.onConnectionContentError();
    }

    private synchronized void setContentRequestResult(RequestResult requestResult) {
        contentRequestResult = requestResult;
    }
}
