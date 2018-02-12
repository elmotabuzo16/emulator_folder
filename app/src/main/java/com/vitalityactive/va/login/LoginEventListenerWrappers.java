package com.vitalityactive.va.login;

import com.vitalityactive.va.events.EventDispatcher;
import com.vitalityactive.va.events.EventListener;

// use this only where the callback class will remain active independently of activity
// life cycles (like in presenters etc)
// this will auto remove the event listening before the callback is callback
public class LoginEventListenerWrappers {
    private final EventDispatcher eventDispatcher;
    private final Callback callback;
    private final EventListener<AuthenticationFailedEvent> failedListener = new EventListener<AuthenticationFailedEvent>() {
        @Override
        public void onEvent(AuthenticationFailedEvent event) {
            removeListeners();
            callback.onAuthenticationFailed(event);
        }
    };
    private final EventListener<AuthenticationSucceededEvent> succeededListener = new EventListener<AuthenticationSucceededEvent>() {
        @Override
        public void onEvent(AuthenticationSucceededEvent event) {
            removeListeners();
            callback.onAuthenticationSucceeded();
        }
    };
    private boolean listening;

    public LoginEventListenerWrappers(EventDispatcher eventDispatcher, Callback callback) {
        this.eventDispatcher = eventDispatcher;
        this.callback = callback;
        listening = false;
    }

    public void addListeners() {
        if (listening) {
            return;
        }
        eventDispatcher.addEventListener(AuthenticationFailedEvent.class, failedListener);
        eventDispatcher.addEventListener(AuthenticationSucceededEvent.class, succeededListener);
        listening = true;
    }

    public void removeListeners() {
        if (!listening) {
            return;
        }
        eventDispatcher.removeEventListener(AuthenticationFailedEvent.class, failedListener);
        eventDispatcher.removeEventListener(AuthenticationSucceededEvent.class, succeededListener);
        listening = false;
    }

    public interface Callback {
        void onAuthenticationSucceeded();
        void onAuthenticationFailed(AuthenticationFailedEvent event);
    }
}
