package com.vitalityactive.va.networking;

import android.support.annotation.NonNull;

public class RequestFailedEvent {
    private Type type;

    public RequestFailedEvent(Type type) {
        this.type = type;
    }

    @NonNull
    public static RequestFailedEvent invalidUsername() {
        return new RequestFailedEvent(Type.INVALID_USERNAME);
    }

    @NonNull
    public static RequestFailedEvent genericError() {
        return new RequestFailedEvent(Type.GENERIC_ERROR);
    }

    @NonNull
    public static RequestFailedEvent connectionError() {
        return new RequestFailedEvent(Type.CONNECTION_ERROR);
    }

    public Type getType() {
        return type;
    }

    public enum Type {INVALID_USERNAME, CONNECTION_ERROR, GENERIC_ERROR}
}
