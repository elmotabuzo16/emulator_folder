package com.vitalityactive.va.cucumber.screens;

import com.vitalityactive.va.R;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

public class NonSmokersPrivacyPolicyScreen extends BaseScreen {
    @Override
    public NonSmokersPrivacyPolicyScreen checkIsOnPage() {
        onView(withId(R.id.activity_terms_and_conditions)).check(matches(isDisplayed()));
        return this;
    }

//    @NonNull
//    @Override
//    public BaseScreen clickOnButtonWithText(String buttonText) {
    //        MockNetworkHandler.enqueueResponseWithEmptyJsonObject(200);
//        return super.clickOnButtonWithText(buttonText);
//    }
}
