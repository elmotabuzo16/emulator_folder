package com.vitalityactive.va.cucumber.screens;

import com.vitalityactive.va.R;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

public class VHCLandingScreen extends BaseScreen {
    @Override
    public VHCLandingScreen checkIsOnPage() {
        onView(withId(R.id.activity_vhc_landing)).check(matches(isDisplayed()));
        return this;
    }
}
