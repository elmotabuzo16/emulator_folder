package com.vitalityactive.va.cucumber.utils;

public class ScreenMapping {
    public final Class activity;
    public final Class screen;

    public ScreenMapping(Class activity, Class screen) {
        this.activity = activity;
        this.screen = screen;
    }
}
