package com.vitalityactive.va.cucumber.screens;

import android.support.annotation.NonNull;

import com.vitalityactive.va.R;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

public class VHCCaptureResultScreen extends BaseScreen {
    @Override
    public VHCCaptureResultScreen checkIsOnPage() {
        onView(withId(R.id.activity_vhc_capture_results)).check(matches(isDisplayed()));
        return this;
    }

    @NonNull
    public VHCCaptureResultScreen enterWeight(String userName) {
        onView(withId(R.id.login_email_address)).perform(clearText(), typeText(userName), closeSoftKeyboard());
        return this;
    }

    public VHCCaptureResultScreen enterHeight(String userName) {
        onView(withId(R.id.login_email_address)).perform(clearText(), typeText(userName), closeSoftKeyboard());
        return this;
    }

    public VHCCaptureResultScreen enterSystolic(String userName) {
        onView(withId(R.id.login_email_address)).perform(clearText(), typeText(userName), closeSoftKeyboard());
        return this;
    }

    public VHCCaptureResultScreen enterDiastolic(String userName) {
        onView(withId(R.id.login_email_address)).perform(clearText(), typeText(userName), closeSoftKeyboard());
        return this;
    }

    public VHCCaptureResultScreen enterFastGlucose(String userName) {
        onView(withId(R.id.login_email_address)).perform(clearText(), typeText(userName), closeSoftKeyboard());
        return this;
    }

    public VHCCaptureResultScreen enterWC(String userName) {
        onView(withId(R.id.login_email_address)).perform(clearText(), typeText(userName), closeSoftKeyboard());
        return this;
    }

    public VHCCaptureResultScreen enterRegularGlucose(String userName) {
        onView(withId(R.id.login_email_address)).perform(clearText(), typeText(userName), closeSoftKeyboard());
        return this;
    }

    public VHCCaptureResultScreen enterTotalCholesterol(String userName) {
        onView(withId(R.id.login_email_address)).perform(clearText(), typeText(userName), closeSoftKeyboard());
        return this;
    }

    public VHCCaptureResultScreen enterHDLC(String userName) {
        onView(withId(R.id.login_email_address)).perform(clearText(), typeText(userName), closeSoftKeyboard());
        return this;
    }

    public VHCCaptureResultScreen enterLDLC(String userName) {
        onView(withId(R.id.login_email_address)).perform(clearText(), typeText(userName), closeSoftKeyboard());
        return this;
    }

    public VHCCaptureResultScreen enterTriglycerides(String userName) {
        onView(withId(R.id.login_email_address)).perform(clearText(), typeText(userName), closeSoftKeyboard());
        return this;
    }

    public VHCCaptureResultScreen enterHbA1c(String userName) {
        onView(withId(R.id.login_email_address)).perform(clearText(), typeText(userName), closeSoftKeyboard());
        return this;
    }
}
