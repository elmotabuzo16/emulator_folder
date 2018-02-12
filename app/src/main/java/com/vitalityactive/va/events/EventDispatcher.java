package com.vitalityactive.va.events;

import android.support.annotation.NonNull;

import java.util.HashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class EventDispatcher {
    private final HashMap<String, CopyOnWriteArrayList<EventListener>> listeners = new HashMap<>();

    public <T> void addEventListener(Class<T> eventClass, EventListener<T> eventListener) {
        synchronized (listeners) {
            CopyOnWriteArrayList<EventListener> list = listeners.get(eventClass.getName());
            if (list == null) {
                list = new CopyOnWriteArrayList<>();
                listeners.put(eventClass.getName(), list);
            }
            list.add(eventListener);
        }
    }

    public <T> void removeEventListener(Class<T> eventClass, EventListener<T> eventListener) {
        synchronized (listeners) {
            CopyOnWriteArrayList<EventListener> list = listeners.get(eventClass.getName());
            if (list == null) {
                return;
            }
            list.remove(eventListener);
            if (list.size() == 0) {
                listeners.remove(eventClass.getName());
            }
        }
    }

    @SuppressWarnings("unchecked")
    public <T> void dispatchEvent(@NonNull T event) {
        String eventType = event.getClass().getName();
        CopyOnWriteArrayList<EventListener> list;
        synchronized (listeners) {
            list = listeners.get(eventType);
        }
        if (list == null) {
            return;
        }
        for (EventListener l : list) {
            l.onEvent(event);
        }
    }

    public void dispose() {
        synchronized (listeners) {
            listeners.clear();
        }
    }

}
