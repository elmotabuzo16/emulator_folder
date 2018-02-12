package com.vitalityactive.va.profile;

public class PersonalDetailsEvent {

    private EventType eventType;
    enum EventType {
        CHANGE_EMAIL_SUCCESS,
        CHANGE_EMAIL_FAILED,
        CHANGE_EMAIL_AUTH_ERROR,

        VERIFY_EMAIL_EXISTING,
        VERIFY_EMAIL_OK,
        VERIFY_EMAIL_FAILED,
        
        AUTHENTICATE_USER_OK,
        AUTHENTICATE_USER_FAILED
    }

    public PersonalDetailsEvent(EventType eventType) {
        this.eventType = eventType;
    }

    public EventType getEventType() {
        return eventType;
    }
}
