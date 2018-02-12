package com.vitalityactive.va.cucumber.screens;

import android.support.annotation.NonNull;

import com.vitalityactive.va.R;
import com.vitalityactive.va.cucumber.utils.MockNetworkHandler;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

public class NonSmokersOnboardingScreen extends BaseScreen {
    @Override
    public NonSmokersOnboardingScreen checkIsOnPage() {
        onView(withId(R.id.non_smoker_declaration)).check(matches(isDisplayed()));
        return this;
    }

    @NonNull
    @Override
    public BaseScreen clickOnButtonWithText(String buttonText) {
        MockNetworkHandler.enqueueResponseWithoutBody(200);
        return super.clickOnButtonWithText(buttonText);
    }
}
