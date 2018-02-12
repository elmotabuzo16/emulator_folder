package com.vitalityactive.va.profile;

public class ChangeEntityNumberEvent {

    private EventType eventType;

    enum EventType{
        CHANGE_ENTITY_SUCCESS,
        CHANGE_ENTITY_FAILED,
        CHANGE_ENTITY_BIRTHDAY_INVALID,
        CHANGE_ENTITY_CONNECTION_FAILED,
        CHANGE_ENTITY_ERROR
    }

    public ChangeEntityNumberEvent(EventType eventType) {
        this.eventType = eventType;
    }

    public EventType getEventType(){
        return eventType;
    }
}
