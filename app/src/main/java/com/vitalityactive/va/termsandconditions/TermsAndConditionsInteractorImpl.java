package com.vitalityactive.va.termsandconditions;

import android.support.annotation.NonNull;

import com.vitalityactive.va.cms.CMSContentFetchSucceededEvent;
import com.vitalityactive.va.cms.CMSServiceClient;
import com.vitalityactive.va.events.EventDispatcher;
import com.vitalityactive.va.networking.RequestResult;
import com.vitalityactive.va.networking.WebServiceResponseParser;

public class TermsAndConditionsInteractorImpl implements TermsAndConditionsInteractor, WebServiceResponseParser<String> {
    @NonNull
    private final EventDispatcher eventDispatcher;
    private Callback callback;
    @NonNull
    private String articleId;
    @NonNull
    private final CMSServiceClient serviceClient;
    private RequestResult contentRequestResult = RequestResult.NONE;
    private volatile String termsAndConditions = "";
    private String liferayGroupId;

    public TermsAndConditionsInteractorImpl(@NonNull CMSServiceClient cmsServiceClient, @NonNull EventDispatcher eventDispatcher, @NonNull String articleId, String liferayGroupId) {
        serviceClient = cmsServiceClient;
        this.eventDispatcher = eventDispatcher;
        this.articleId = articleId;
        this.liferayGroupId = liferayGroupId;
    }

    @Override
    public void setArticleId(@NonNull String articleId) {
        this.articleId = articleId;
    }

    @Override
    public void fetchTermsAndConditions() {
        serviceClient.fetchContentWithId(liferayGroupId, articleId, this);
    }

    @Override
    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    @Override
    public boolean isFetchingTermsAndConditions() {
        return serviceClient.isFetchingContentWithId(articleId);
    }

    @Override
    public synchronized RequestResult getContentRequestResult() {
        return contentRequestResult;
    }

    @Override
    public String getTermsAndConditions() {
        return termsAndConditions;
    }

    @Override
    public void parseResponse(String response) {
        termsAndConditions = response;
        setContentRequestResult(RequestResult.SUCCESSFUL);
        callback.onTermsAndConditionsRetrieved(response);
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

}
