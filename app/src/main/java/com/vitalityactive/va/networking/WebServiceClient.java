package com.vitalityactive.va.networking;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.vitalityactive.va.Scheduler;
import com.vitalityactive.va.events.EventDispatcher;
import com.vitalityactive.va.events.EventListener;
import com.vitalityactive.va.events.QuitApplicationEvent;

import java.io.IOException;
import java.net.ConnectException;
import java.net.UnknownHostException;
import java.util.HashMap;

import okhttp3.Headers;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

public class WebServiceClient implements EventListener<QuitApplicationEvent> {
    public static final int HTTP_RESPONSE_CODE_UNAUTHORIZED = 401;
    public static final int HTTP_RESPONSE_CODE_REDIRECT = 302;
    private final HashMap<String, Call> calls = new HashMap<>();
    private final Scheduler scheduler;
    private EventDispatcher eventDispatcher;

    public WebServiceClient(Scheduler scheduler, EventDispatcher eventDispatcher) {
        this.scheduler = scheduler;
        this.eventDispatcher = eventDispatcher;
    }

    public <ResponseType> void executeAsynchronousRequest(@NonNull final Call<ResponseType> call, @NonNull final WebServiceResponseParser<ResponseType> parser) {
        final String requestIdentifier = call.request().toString();
        if (!startRequest(requestIdentifier, call)) {
            eventDispatcher.dispatchEvent(new RequestInProgressEvent(requestIdentifier));
            return;
        }

        scheduler.schedule(new Runnable() {
            @Override
            public void run() {
                executeRequest(call, parser, requestIdentifier);
            }
        });
    }

    public <ResponseType> void executeSynchronousRequest(@NonNull final Call<ResponseType> call, @NonNull final WebServiceResponseParser<ResponseType> parser) {
        String requestIdentifier = call.request().toString();
        if (!startRequest(requestIdentifier, call)) {
            eventDispatcher.dispatchEvent(new RequestInProgressEvent(requestIdentifier));
            return;
        }

        executeRequest(call, parser, requestIdentifier);
    }

    private <ResponseType> void executeRequest(@NonNull Call<ResponseType> call,
                                               @NonNull WebServiceResponseParser<ResponseType> parser,
                                               String requestIdentifier) {
        try {
            Response<ResponseType> response = call.execute();
            if (response.isSuccessful()) {
                stopRequest(requestIdentifier);
                WebServiceResponseHeaderHelper.headers = response.headers();
                parser.parseResponse(response.body());
                eventDispatcher.dispatchEvent(new RequestSucceededEvent(requestIdentifier, response.body(), response.code(), getOKMessage(response.message())));
            } else if (response.code() == HTTP_RESPONSE_CODE_UNAUTHORIZED) {
                cancelAllRequests();
                eventDispatcher.dispatchEvent(new RequestUnauthorizedEvent());
            } else if(response.code() == HTTP_RESPONSE_CODE_REDIRECT){
                stopRequest(requestIdentifier);
                tryToHandleRedirect(parser, response);
                eventDispatcher.dispatchEvent(new RequestRedirectedEvent(requestIdentifier, response.headers(), response.body(), response.code(), getOKMessage(response.message())));
            } else {
                stopRequest(requestIdentifier);
                String errorBody = getErrorBody(response.errorBody());
                parser.parseErrorResponse(errorBody, response.code());
                eventDispatcher.dispatchEvent(new RequestFailedEvent(requestIdentifier, response.code(), getErrorMessage(response.message()), errorBody));
            }
        } catch (IOException exception) {
            exception.printStackTrace();
            stopRequest(requestIdentifier);
            if (isConnectionError(exception)) {
                parser.handleConnectionError();
            } else {
                parser.handleGenericError(exception);
            }
            eventDispatcher.dispatchEvent(new RequestCancelledEvent(requestIdentifier, exception));
        } catch (Exception exception) {
            exception.printStackTrace();
            stopRequest(requestIdentifier);
            parser.handleGenericError(exception);
        } catch (Throwable t) {
            t.printStackTrace();
            stopRequest(requestIdentifier);
            parser.handleGenericError(new RuntimeException(t.getMessage()));
        } finally {
            stopRequest(requestIdentifier);
        }
    }

    private void tryToHandleRedirect(@NonNull WebServiceResponseParser parser,
                                     Response response){
        final String location = response.headers().get("Location");
        if(parser instanceof ResponseParserWithRedirect &&
                !TextUtils.isEmpty(location)){
            ((ResponseParserWithRedirect)parser).handleRedirect(location);
        } else {
            String errorBody = getErrorBody(response.errorBody());
            parser.parseErrorResponse(errorBody, response.code());
        }
    }

    private boolean isConnectionError(IOException exception) {
        return exception instanceof UnknownHostException
                || exception instanceof ConnectException;
    }

    private String getErrorBody(ResponseBody errorBody) {
        try {
            return errorBody.string();
        } catch (IOException e) {
            return "";
        }
    }

    private synchronized void stopRequest(String requestIdentifier) {
        calls.remove(requestIdentifier);
    }

    private synchronized boolean startRequest(String requestIdentifier, Call call) {
        if (calls.containsKey(requestIdentifier)) {
            return false;
        }
        calls.put(requestIdentifier, call);
        return true;
    }

    public synchronized boolean isRequestInProgress(String requestIdentifier) {
        return calls.containsKey(requestIdentifier);
    }

    public synchronized void cancelRequest(String requestIdentifier) {
        if (calls.containsKey(requestIdentifier)) {
            calls.get(requestIdentifier).cancel();
            calls.remove(requestIdentifier);
        }
    }

    private String getMessage(String message, String defaultMessage) {
        return message != null ? message : defaultMessage;
    }

    private String getOKMessage(String message) {
        return getMessage(message, "OK");
    }

    private String getErrorMessage(String message) {
        return getMessage(message, "Error");
    }

    @Override
    public void onEvent(QuitApplicationEvent event) {
        cancelAllRequests();
    }

    public void cancelAllRequests() {
        synchronized (calls) {
            for (Call call : calls.values()) {
                call.cancel();
            }
            calls.clear();
        }
    }

    private static class WebServiceClientEvent {
        final String requestDescription;

        WebServiceClientEvent(String requestDescription) {
            this.requestDescription = requestDescription;
        }

        public String getRequestDescription() {
            return requestDescription;
        }
    }

    private static class ResponseEvent extends WebServiceClientEvent {
        final int code;
        final String message;
        final String rawResponse;

        ResponseEvent(String requestDescription, int code, String message, String rawResponse) {
            super(requestDescription);
            this.code = code;
            this.message = message;
            this.rawResponse = rawResponse;
        }

        public int getCode() {
            return code;
        }

        public String getMessage() {
            return message;
        }

        public String getRawResponse() {
            return rawResponse;
        }
    }

    public static class RequestCancelledEvent extends WebServiceClientEvent {
        private final Exception exception;

        public RequestCancelledEvent(String requestDescription, Exception exception) {
            super(requestDescription);
            this.exception = exception;
        }
    }

    public static class RequestInProgressEvent extends WebServiceClientEvent {
        public RequestInProgressEvent(String requestDescription) {
            super(requestDescription);
        }
    }

    public static class RequestSucceededEvent extends ResponseEvent {
        private final Object response;

        public RequestSucceededEvent(String requestDescription, Object response, int code, String message) {
            super(requestDescription, code, message, "");
            this.response = response;
        }

        public Object getResponse() {
            return response;
        }
    }

    public static class RequestFailedEvent extends ResponseEvent {
        public RequestFailedEvent(String requestDescription, int code, String message, String errorResponse) {
            super(requestDescription, code, message, errorResponse);
        }

    }

    public static class RequestUnauthorizedEvent {
    }

    public static class RequestRedirectedEvent extends ResponseEvent {
        private final Object response;
        private final Headers headers;

        public RequestRedirectedEvent(String requestDescription,
                                      Headers headers,
                                      Object response,
                                      int code,
                                      String message) {
            super(requestDescription, code, message, "");
            this.headers = headers;
            this.response = response;
        }

        public Object getResponse() {
            return response;
        }
    }
}
