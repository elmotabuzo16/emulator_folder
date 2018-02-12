package com.vitalityactive.va.cucumber.screens;

import com.vitalityactive.va.R;
import com.vitalityactive.va.testutilities.VitalityActive;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

public class TermsAndConditionsScreen extends BaseScreen {
    @Override
    public TermsAndConditionsScreen checkIsOnPage() {
        onView(withId(R.id.webview)).check(matches(isDisplayed()));
        return this;
    }

    public IndeterminateScreen clickOnMoreAndThenOnAgreeToAcceptTerms() {
        VitalityActive.Navigate.clickMoreThenAgree();
        return IndeterminateScreen.after("click on more then agree", this);
    }
}
