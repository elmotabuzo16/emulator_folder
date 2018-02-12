package com.vitalityactive.va.settings;

public class ChangePasswordEvent {
    private EventType eventType;

    enum EventType {
        CHANGE_PASSWORD_SUCCESS,
        CHANGE_PASSWORD_FAILED,
        CHANGE_PASSWORD_AUTH_ERROR
    }

    public ChangePasswordEvent(EventType eventType){
        this.eventType = eventType;
    }

    public EventType getEventType(){
        return eventType;
    }
}
