package com.vitalityactive.va.cucumber.screens;

import com.vitalityactive.va.R;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

public class NonSmokersLearnMoreScreen extends BaseScreen {
    @Override
    public NonSmokersLearnMoreScreen checkIsOnPage() {
        onView(withId(R.id.activity_learn_more)).check(matches(isDisplayed()));
        return this;
    }
}
