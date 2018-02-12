package com.vitalityactive.va;

import com.vitalityactive.va.events.EventDispatcher;

public class ServiceLocator {
    private static ServiceLocator instance;
    public final EventDispatcher eventDispatcher;
    public DeviceSpecificPreferences deviceSpecificPreferences;

    private ServiceLocator(Builder builder) {
        this.eventDispatcher = builder.eventDispatcher;
        this.deviceSpecificPreferences = builder.deviceSpecificPreferences;
    }

    public static void setInstance(ServiceLocator instance) {
        ServiceLocator.instance = instance;
    }

    public static ServiceLocator getInstance() {
        return instance;
    }

    public static class Builder {
        private EventDispatcher eventDispatcher;
        private DeviceSpecificPreferences deviceSpecificPreferences;

        public Builder setEventDispatcher(EventDispatcher eventDispatcher) {
            this.eventDispatcher = eventDispatcher;
            return this;
        }

        public Builder setDeviceSpecificPreferences(DeviceSpecificPreferences deviceSpecificPreferences) {
            this.deviceSpecificPreferences = deviceSpecificPreferences;
            return this;
        }

        public ServiceLocator build() {
            return new ServiceLocator(this);
        }
    }
}
