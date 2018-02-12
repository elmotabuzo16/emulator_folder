package com.vitalityactive.va.cucumber.screens;

import com.vitalityactive.va.R;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static com.vitalityactive.va.testutilities.VitalityActive.Matcher.withDrawable;

public class OnboardingScreen extends BaseScreen {
    @Override
    public OnboardingScreen checkIsOnPage() {
        onView(withId(R.id.active_rewards_onboarding_icon4)).check(matches(withDrawable(R.drawable.onboarding_spinner)));
        return this;
    }
}
