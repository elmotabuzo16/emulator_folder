package com.vitalityactive.va.cucumber.screens;

import com.vitalityactive.va.R;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created by edinel.k.f.degollado on 1/23/2018.
 */

public class PartnerScreen extends BaseScreen {
    @Override
    public PartnerScreen checkIsOnPage() {
        onView(withId(R.id.snvPartnerDetailsImageView)).check(matches(isDisplayed()));
        return this;
    }
}
