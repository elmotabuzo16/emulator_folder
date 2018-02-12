package com.vitalityactive.va.cucumber.screens;

import com.vitalityactive.va.R;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

public class VitalityAgeScreen extends BaseScreen{

    @Override
    public VitalityAgeScreen checkIsOnPage() {
        onView(withId(R.id.vitality_age)).check(matches(isDisplayed()));
        return this;
    }
}
