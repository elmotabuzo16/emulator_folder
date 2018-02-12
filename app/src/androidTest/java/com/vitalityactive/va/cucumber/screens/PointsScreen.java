package com.vitalityactive.va.cucumber.screens;

import com.vitalityactive.va.R;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

public class PointsScreen extends BaseScreen {
    @Override
    public PointsScreen checkIsOnPage() {
        onView(withId(R.id.points)).check(matches(isDisplayed()));
        return this;
    }
}
