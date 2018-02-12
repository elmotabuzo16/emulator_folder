package com.vitalityactive.va.cucumber.screens;

import com.vitalityactive.va.R;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

public class HomeScreen extends BaseScreen {
    @Override
    public HomeScreen checkIsOnPage() {
        onView(withId(R.id.activity_home)).check(matches(isDisplayed()));
        return this;
    }

    public LoginScreen clickOnLogout() throws InstantiationException, IllegalAccessException {
        clickOnButtonWithText("Log out");
        return isOnScreen(LoginScreen.class);
    }
}
