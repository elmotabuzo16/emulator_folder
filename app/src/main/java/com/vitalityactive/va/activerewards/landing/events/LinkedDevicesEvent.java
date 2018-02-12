package com.vitalityactive.va.activerewards.landing.events;

public class LinkedDevicesEvent {
    public static final LinkedDevicesEvent UNKNOWN = new LinkedDevicesEvent();
    public static final LinkedDevicesEvent HAS_LINKED_DEVICES = new LinkedDevicesEvent();
    public static final LinkedDevicesEvent DOES_NOT_HAVE_LINKED_DEVICES = new LinkedDevicesEvent();
}
