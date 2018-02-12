package com.vitalityactive.va.cucumber.screens;

import android.support.annotation.NonNull;
import android.view.View;

import com.vitalityactive.va.R;
import com.vitalityactive.va.testutilities.matchers.DrawableMatcher;

import org.hamcrest.Matcher;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;


public class ChangePasswordScreen extends BaseScreen {

    @Override
    public BaseScreen checkIsOnPage() {
        onView(withId(R.id.change_password_container)).check(matches(isDisplayed()));
        return this;
    }

    @NonNull
    public ChangePasswordScreen enterCurrentPassword(String currentpassword) {
        onView(withId(R.id.current_password)).perform(clearText(), typeText(currentpassword), closeSoftKeyboard());
        return this;
    }

    @NonNull
    public ChangePasswordScreen enterNewPassword(String newpassword) {
        onView(withId(R.id.new_password)).perform(clearText(), typeText(newpassword), closeSoftKeyboard());
        return this;
    }

    @NonNull
    public ChangePasswordScreen enterConfirmPassword(String confirmpassword) {
        onView(withId(R.id.confirm_password)).perform(clearText(), typeText(confirmpassword), closeSoftKeyboard());
        return this;
    }

    public ChangePasswordScreen clickMenuDone() {
        onView(withText("Done")).perform(click());
        return this;
    }

}
