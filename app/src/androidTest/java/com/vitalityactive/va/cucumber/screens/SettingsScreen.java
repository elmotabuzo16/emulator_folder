package com.vitalityactive.va.cucumber.screens;


import com.vitalityactive.va.R;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created by dharel.h.rosell on 11/21/2017.
 */

public class SettingsScreen extends BaseScreen {

    @Override
    public BaseScreen checkIsOnPage() {
        onView(withId(R.id.settings_page)).check(matches(isDisplayed()));
        return null;
    }
}
