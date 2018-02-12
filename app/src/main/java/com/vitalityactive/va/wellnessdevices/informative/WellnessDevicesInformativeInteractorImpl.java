package com.vitalityactive.va.wellnessdevices.informative;

import android.support.annotation.NonNull;

import com.vitalityactive.va.cms.CMSContentFetchSucceededEvent;
import com.vitalityactive.va.cms.CMSServiceClient;
import com.vitalityactive.va.connectivity.ConnectivityListener;
import com.vitalityactive.va.events.EventDispatcher;
import com.vitalityactive.va.networking.RequestResult;
import com.vitalityactive.va.networking.WebServiceResponseParser;

public class WellnessDevicesInformativeInteractorImpl
        implements WellnessDevicesInformativeInteractor,
        WebServiceResponseParser<String> {
    @NonNull
    private final EventDispatcher eventDispatcher;
    private final ConnectivityListener connectivityListener;
    private Callback callback;
    @NonNull
    private final CMSServiceClient serviceClient;
    private RequestResult contentRequestResult = RequestResult.NONE;
    private volatile String termsAndConditions = "";
    private String liferayGroupId;

    public WellnessDevicesInformativeInteractorImpl(@NonNull CMSServiceClient cmsServiceClient,
                                                    @NonNull EventDispatcher eventDispatcher,
                                                    @NonNull ConnectivityListener connectivityListener,
                                                    String liferayGroupId) {
        serviceClient = cmsServiceClient;
        this.eventDispatcher = eventDispatcher;
        this.liferayGroupId = liferayGroupId;
        this.connectivityListener = connectivityListener;
    }

    @Override
    public void fetchContent(String articleId) {
        if(connectivityListener.isOnline()) {
            serviceClient.fetchContentWithId(liferayGroupId, articleId, this);
        } else {
            callback.onConnectionContentError();
        }
    }

    @Override
    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    @Override
    public boolean isFetchingContent(String articleId) {
        return serviceClient.isFetchingContentWithId(articleId);
    }

    @Override
    public synchronized RequestResult getContentRequestResult() {
        return contentRequestResult;
    }

    @Override
    public void clearContentRequestResult() {
        contentRequestResult = RequestResult.NONE;
    }

    @Override
    public String getTermsAndConditions() {
        return termsAndConditions;
    }

    @Override
    public void parseResponse(String response) {
        termsAndConditions = response;
        setContentRequestResult(RequestResult.SUCCESSFUL);
        callback.onContentRetrieved(response);
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
