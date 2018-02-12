package com.vitalityactive.va.cucumber.screens;

import com.vitalityactive.va.R;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created by stephen.rey.w.avila on 12/8/2017.
 */
public class ScreeningLearnMoreScreen extends BaseScreen {
    @Override
    public ScreeningLearnMoreScreen checkIsOnPage() {
        onView(withId(R.id.sv_activity_learn_more)).check(matches(isDisplayed()));
        return this;
    }
}
