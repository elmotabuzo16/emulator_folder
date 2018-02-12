package com.vitalityactive.va.events;

public interface EventListener<T> {
    void onEvent(T event);
}
