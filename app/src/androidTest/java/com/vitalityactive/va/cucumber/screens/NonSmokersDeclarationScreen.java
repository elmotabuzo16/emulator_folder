package com.vitalityactive.va.cucumber.screens;

import android.support.annotation.NonNull;

import com.vitalityactive.va.R;
import com.vitalityactive.va.cucumber.utils.MockNetworkHandler;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

public class NonSmokersDeclarationScreen extends BaseScreen {
    @Override
    public NonSmokersDeclarationScreen checkIsOnPage() {
        onView(withId(R.id.activity_terms_and_conditions)).check(matches(isDisplayed()));
        return this;

    }

    @NonNull
    @Override
    public BaseScreen clickOnButtonWithText(String buttonText) {
        MockNetworkHandler.enqueueResponseWithoutBody(200);
        if (buttonText.equals("I Declare")) {
            MockNetworkHandler.enqueueResponseWithoutBody(200);
        }
        return super.clickOnButtonWithText(buttonText);
    }
}
