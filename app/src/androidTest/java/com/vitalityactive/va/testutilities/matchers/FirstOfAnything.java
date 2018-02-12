package com.vitalityactive.va.testutilities.matchers;

import android.view.View;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

public class FirstOfAnything extends TypeSafeMatcher<View> {
    private boolean found = false;

    @Override
    protected boolean matchesSafely(View item) {
        if (!found && matches(item)) {
            found = true;
            return true;
        }
        return false;
    }

    protected boolean matches(View item) {
        return item != null;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("Matcher.FirstOfAnything");
    }
}
