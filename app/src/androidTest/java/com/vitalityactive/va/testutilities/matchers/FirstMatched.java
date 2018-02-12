package com.vitalityactive.va.testutilities.matchers;

import android.view.View;

import org.hamcrest.Description;
import org.hamcrest.Matcher;

public class FirstMatched extends FirstOfAnything {
    private final Matcher<View> expected;

    public FirstMatched(Matcher<View> expected) {
        this.expected = expected;
    }

    @Override
    protected boolean matches(View item) {
        return super.matches(item) && expected.matches(item);
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("Matcher.first( " + expected.toString() + " )");
    }
}
