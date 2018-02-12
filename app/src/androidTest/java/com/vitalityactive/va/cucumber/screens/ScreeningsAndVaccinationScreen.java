package com.vitalityactive.va.cucumber.screens;

import com.vitalityactive.va.R;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created by kerry.e.lawagan on 11/30/2017.
 */

public class ScreeningsAndVaccinationScreen extends BaseScreen {
    @Override
    public ScreeningsAndVaccinationScreen checkIsOnPage() {
        onView(withId(R.id.activity_screenings_and_vaccinations)).check(matches(isDisplayed()));
        return this;
    }
}
