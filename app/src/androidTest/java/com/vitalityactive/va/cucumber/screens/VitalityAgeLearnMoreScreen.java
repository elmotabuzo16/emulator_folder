package com.vitalityactive.va.cucumber.screens;

import android.view.View;

import com.vitalityactive.va.R;

import org.hamcrest.Matcher;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

public class VitalityAgeLearnMoreScreen extends BaseScreen{

    @Override
    public VitalityAgeLearnMoreScreen checkIsOnPage() {
        onView(withId(R.id.activity_learn_more)).check(matches(isDisplayed()));
        return this;
    }

    public VitalityAgeLearnMoreScreen checkTextIsDisplayed(String text) {
        onView(withText(text))
                .check(matches(isDisplayed()));
        return this;
    }

}
