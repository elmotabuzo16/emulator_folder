package com.vitalityactive.va.cucumber.screens;

import com.vitalityactive.va.R;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

public class NonSmokersAssessmentCompleteScreen extends BaseScreen {
    @Override
    public NonSmokersAssessmentCompleteScreen checkIsOnPage() {
        onView(withId(R.id.activity_non_smokers_assesment_complete)).check(matches(isDisplayed()));
        return this;
    }
}
