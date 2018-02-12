package com.vitalityactive.va.networking;

public interface ResponseParserWithRedirect<ResponseType> extends WebServiceResponseParser<ResponseType> {
    void handleRedirect(String redirectUrl);
}