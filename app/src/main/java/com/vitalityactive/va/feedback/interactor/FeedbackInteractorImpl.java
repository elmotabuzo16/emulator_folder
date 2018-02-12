package com.vitalityactive.va.feedback.interactor;

import android.support.annotation.NonNull;
import android.util.Log;

import com.vitalityactive.va.appconfig.AppConfigRepository;
import com.vitalityactive.va.appconfig.AppConfigService;
import com.vitalityactive.va.appconfig.AppConfigServiceClient;
import com.vitalityactive.va.cms.CMSContentFetchSucceededEvent;
import com.vitalityactive.va.cms.CMSServiceClient;
import com.vitalityactive.va.events.EventDispatcher;
import com.vitalityactive.va.networking.RequestResult;
import com.vitalityactive.va.networking.WebServiceResponseParser;

import javax.inject.Inject;

/**
 * Created by christian.j.p.capin on 1/5/2018.
 */


public class FeedbackInteractorImpl implements FeedbackInteractor, WebServiceResponseParser<String> {

    @NonNull
    private final EventDispatcher eventDispatcher;
    private FeedbackInteractor.Callback callback;

    @NonNull
    private String articleId;

    @NonNull
    private final CMSServiceClient serviceClient;

    private RequestResult contentRequestResult = RequestResult.NONE;
    private volatile String feedbacks = "";
    private String liferayGroupId;


    public FeedbackInteractorImpl(@NonNull CMSServiceClient cmsServiceClient, @NonNull EventDispatcher eventDispatcher, @NonNull String articleId, String liferayGroupId) {
        this.serviceClient = cmsServiceClient;
        this.eventDispatcher = eventDispatcher;
        this.articleId = articleId;
        this.liferayGroupId = liferayGroupId;
    }

    @Override
    public void parseResponse(String response) {
        feedbacks = response;
        setContentRequestResult(RequestResult.SUCCESSFUL);
        callback.onFeedbackRetrieved(response);
        eventDispatcher.dispatchEvent(new CMSContentFetchSucceededEvent(response));
    }
    private synchronized void setContentRequestResult(RequestResult requestResult) {
        contentRequestResult = requestResult;
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
        setContentRequestResult(RequestResult.CONNECTION_ERROR);
        callback.onConnectionContentError();
    }

    @Override
    public void fetchFeedbacks() {
        serviceClient.fetchContentWithId(liferayGroupId,  articleId,this);
    }

    @Override
    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    @Override
    public boolean isFetchingFeedbacks() {
        return serviceClient.isFetchingContentWithId(articleId);
    }

    @Override
    public RequestResult getContentRequestResult() {
        return contentRequestResult;
    }

    @Override
    public String getFeedbacks() {  return feedbacks;   }
}
