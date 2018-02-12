package com.vitalityactive.va.testutilities;

import android.support.test.espresso.IdlingResource;
import android.util.Log;

import com.vitalityactive.va.ServiceLocator;
import com.vitalityactive.va.events.EventDispatcher;
import com.vitalityactive.va.events.EventListener;

public class ServiceIdlingResource<SucceededEvent, FailedEvent> implements IdlingResource {
    private final EventDispatcher eventDispatcher;
    private final Class<SucceededEvent> succeededEventClass;
    private final Class<FailedEvent> failedEventClass;
    private final SucceededListener succeededListener;
    private final FailedListener failedListener;
    private String name;
    private ResourceCallback callback;
    private boolean isIdle;

    public ServiceIdlingResource(EventDispatcher eventDispatcher, Class<SucceededEvent> succeededEventClass, Class<FailedEvent> failedEventClass) {
        this.eventDispatcher = eventDispatcher;
        this.succeededEventClass = succeededEventClass;
        this.failedEventClass = failedEventClass;
        succeededListener = new SucceededListener();
        eventDispatcher.addEventListener(succeededEventClass, succeededListener);
        failedListener = new FailedListener();
        eventDispatcher.addEventListener(failedEventClass, failedListener);
        name = String.format("%s<%s,%s>", getClass().getName(), succeededEventClass.getName(), failedEventClass.getName());
    }

    public ServiceIdlingResource(Class<SucceededEvent> succeededEventClass, Class<FailedEvent> failedEventClass) {
        this(ServiceLocator.getInstance().eventDispatcher, succeededEventClass, failedEventClass);
    }

    public ServiceIdlingResource(String name, Class<SucceededEvent> succeededEventClass, Class<FailedEvent> failedEventClass) {
        this(succeededEventClass, failedEventClass);
        this.name = String.format("%s (%s)", name, this.name);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean isIdleNow() {
        boolean isIdle = this.isIdle;
        if (isIdle && callback != null) {
            callback.onTransitionToIdle();
        }
        return isIdle;
    }

    @Override
    public void registerIdleTransitionCallback(ResourceCallback callback) {
        this.callback = callback;
    }

    public void close() {
        eventDispatcher.removeEventListener(succeededEventClass, succeededListener);
        eventDispatcher.removeEventListener(failedEventClass, failedListener);
    }

    private class SucceededListener implements EventListener<SucceededEvent> {
        @Override
        public void onEvent(SucceededEvent event) {
            Log.d("CMSServiceIdling", "Got SucceededEvent: " + event.getClass().getSimpleName());
            isIdle = true;
        }
    }

    private class FailedListener implements EventListener<FailedEvent> {
        @Override
        public void onEvent(FailedEvent event) {
            Log.w("CMSServiceIdling", "Got FailedEvent" + event.getClass().getSimpleName());
            isIdle = true;
        }
    }
}
