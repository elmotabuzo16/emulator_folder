package com.vitalityactive.va.cucumber.screens;

import com.vitalityactive.va.R;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.isDialog;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.not;

public class ForgotPasswordScreen extends BaseScreen {
    private String email;

    @Override
    public ForgotPasswordScreen checkIsOnPage() {
        onView(withId(R.id.forgot_password_screen)).check(matches(isDisplayed()));
        return this;
    }

    @Override
    public void focusOnField(String field) {
        if (field.equals("Email")) {
            focusOnEmail();
        } else if (field.equals("password")) {
            focusOnPassword();
        } else {
            super.focusOnField(field);
        }
    }

    public ForgotPasswordScreen enterUsername(String username) {
        onView(withId(R.id.forgot_password_email)).perform(clearText(), typeText(username));
        return this;
    }

    public ForgotPasswordScreen clickOnNext() {
        onView(withId(R.id.forgot_password_next_button)).perform(click());
        return this;
    }

    public ForgotPasswordScreen enterEmail(String email) {
        onView(withId(R.id.forgot_password_email)).perform(clearText(), typeText(email), closeSoftKeyboard());
        this.email = email;
        return this;
    }

    public ForgotPasswordScreen focusOnPassword() {
        onView(withId(R.id.forgot_password_description)).perform(click());
        return this;
    }

    public ForgotPasswordScreen focusOnEmail() {
        onView(withId(R.id.forgot_password_email)).perform(click());
        return this;
    }

    public void checkUserValidationErrorMessageShown() {
        onView(withText(R.string.registration_invalid_email_footnote_error_35)).check(matches(isDisplayed()));
    }

    public void checkIconIsHighlighted() {
        checkViewHasDrawable(R.id.login_email_icon, R.drawable.email_active);
    }

    public ForgotPasswordScreen checkButtonIsEnabled(String enabled) {
        if (enabled.equals("enabled")) {
            onView(withText(R.string.next_button_title_84)).check(matches(isEnabled()));
        } else {
            onView(withText(R.string.next_button_title_84)).check(matches(not(isEnabled())));
        }
        return this;
    }

    public ForgotPasswordScreen checkDialogWithButtonIsDisplayed(int buttonText) {
        onView(withText(buttonText))
                .inRoot(isDialog())
                .check(matches(isDisplayed()));
        return this;
    }

}
