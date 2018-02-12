package com.vitalityactive.va.wellnessdevices;

import com.vitalityactive.va.events.BaseEvent;
import com.vitalityactive.va.networking.RequestResult;

public abstract class BaseWellnessDeviceEvent<T> extends BaseEvent{
    private final RequestResult requestResult;
    private final T responseBody;
    private final String redirectUrl;
    protected final String requestType;

    public BaseWellnessDeviceEvent(RequestResult requestResult,
                                   String requestType){
        this(requestResult, null, null, requestType);
    }

    public BaseWellnessDeviceEvent(RequestResult requestResult,
                                   T responseBody,
                                   String redirectUrl,
                                   String requestType){
        this.requestResult = requestResult;
        this.responseBody = responseBody;
        this.redirectUrl = redirectUrl;
        this.requestType = requestType;
    }

    public boolean isSuccessfull(){
        return requestResult == RequestResult.SUCCESSFUL;
    }

    public boolean isRedirect(){
        return requestResult == RequestResult.REDIRECT;
    }

    public RequestResult getRequestResult(){
        return requestResult;
    }

    public T getResponseBody(){
        return responseBody;
    }

    public String getRedirectUrl(){
        return redirectUrl.replaceAll(" ", "%20");
    }

    public String getRequestType(){
        return requestType;
    }
}
