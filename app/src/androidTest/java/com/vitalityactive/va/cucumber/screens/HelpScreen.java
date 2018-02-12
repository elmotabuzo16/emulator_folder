package com.vitalityactive.va.cucumber.screens;

import com.vitalityactive.va.R;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created by christian.j.p.capin on 12/6/2017.
 */

public class HelpScreen extends BaseScreen {
    @Override
    public HelpScreen checkIsOnPage() {
        onView(withId(R.id.fragment_help_parent)).check(matches(isDisplayed()));
        return this;
    }
}
