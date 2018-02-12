package com.vitalityactive.va.cucumber.screens;

import com.vitalityactive.va.R;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

public class SplashScreen extends BaseScreen {
    @Override
    public BaseScreen checkIsOnPage() {
        onView(withId(R.id.activity_splash_screen)).check(matches(isDisplayed()));
        return this;
    }

    public TermsAndConditionsScreen clickOnBackground() throws InstantiationException, IllegalAccessException {
        onView(withId(R.id.activity_splash_screen)).perform(click());
        return isOnScreen(TermsAndConditionsScreen.class);
    }

    public IndeterminateScreen clickOnBackgroundAndIgnoreNextScreen() throws InstantiationException, IllegalAccessException {
        onView(withId(R.id.activity_splash_screen)).perform(click());
        return IndeterminateScreen.after("splash screen clicked", this);
    }
}
