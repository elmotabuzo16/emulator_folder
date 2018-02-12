package com.vitalityactive.va.cucumber.screens;

import com.vitalityactive.va.R;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

public class VHCOnboardingScreen extends BaseScreen {
    @Override
    public VHCOnboardingScreen checkIsOnPage() {
        onView(withId(R.id.vitality_health_check)).check(matches(isDisplayed()));
        return this;
    }
}
