package com.vitalityactive.va.cms;

public class CMSContentFetchFailedEvent {
    public final Exception exception;

    public CMSContentFetchFailedEvent(Exception exception) {
        this.exception = exception;
    }
}
