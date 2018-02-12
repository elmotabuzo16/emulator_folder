package com.vitalityactive.va.cucumber.screens;

import android.util.Log;

import com.vitalityactive.va.cucumber.exception.UnexpectedScreenException;

public class IndeterminateScreen extends BaseScreen {
    private static final String TAG = "IndeterminateScreen";
    private final BaseScreen source;
    private final String actionPerformed;

    private IndeterminateScreen(BaseScreen source, String actionPerformed) {
        this.source = source;
        this.actionPerformed = actionPerformed;
    }

    public static IndeterminateScreen after(String actionPerformed, BaseScreen screen) {
        return new IndeterminateScreen(screen, actionPerformed);
    }

    @Override
    public BaseScreen checkIsOnPage() {
        String message = String.format("Currently on an Indeterminate screen %s. Use `is` or `isOnScreen` to check the current screen",
                getNavigationDetails());
        throw new RuntimeException(message);
    }

    @Override
    public <T extends BaseScreen> T is(Class<T> clazz) {
        if (clazz.isInstance(source)) {
            return getSourceScreen(clazz);
        } else {
            return getScreen(clazz);
        }
    }

    private <T extends BaseScreen> T getSourceScreen(Class<T> clazz) {
        try {
            source.checkIsOnPage();
            Log.i(TAG, "is still on the source screen, returning same instance: " + clazz.getSimpleName() + " " + getNavigationDetails());
            return clazz.cast(source);
        } catch (Exception ignored) {
            throw new UnexpectedScreenException(clazz, getScreenDetails());
        }
    }

    private <T extends BaseScreen> T getScreen(Class<T> clazz) {
        try {
            T screen = isOnScreen(clazz);
            Log.i(TAG, "is currently on " + clazz.getSimpleName() + " " + getNavigationDetails());
            return screen;
        } catch (Exception ignored) {
            throw new UnexpectedScreenException(clazz, getScreenDetails());
        }
    }

    private String getNavigationDetails() {
        return String.format("after '%s' from '%s'", actionPerformed, source.getClass().getSimpleName());
    }

    @Override
    public String getScreenDetails() {
        return "Indeterminate " + getNavigationDetails();
    }
}
