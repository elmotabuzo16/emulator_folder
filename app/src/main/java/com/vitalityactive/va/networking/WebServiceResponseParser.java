package com.vitalityactive.va.networking;

public interface WebServiceResponseParser<ResponseType> {
    void parseResponse(ResponseType response);

    void parseErrorResponse(String errorBody, int code);

    void handleGenericError(Exception exception);

    void handleConnectionError();
}
