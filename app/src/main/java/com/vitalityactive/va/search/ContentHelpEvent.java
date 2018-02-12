package com.vitalityactive.va.search;


/**
 * Created by chelsea.b.pioquinto on 1/30/2018.
 */

public class ContentHelpEvent {

    private ContentHelpEvent.EventType eventType;

    enum EventType {
        DETAILS_SUCCESS,
        DETAILS_FAILED,
        DETAILS_AUTH_ERROR,
        DETAILS_CONNECTION_ERROR
    }

    public ContentHelpEvent(ContentHelpEvent.EventType eventType){
        this.eventType = eventType;
    }

    public ContentHelpEvent.EventType getEventType(){
        return eventType;
    }
}
