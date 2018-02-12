package com.vitalityactive.va.cucumber.screens;

import android.support.annotation.NonNull;
import android.view.View;

import com.vitalityactive.va.R;
import com.vitalityactive.va.cucumber.utils.MockNetworkHandler;
import com.vitalityactive.va.testutilities.VitalityActive;

import org.hamcrest.Matcher;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.not;

public class LoginScreen extends BaseScreen {
    private static final String LOG_IN_BUTTON_TEXT = "Login";
    private String userName;
    private String password;

    @Override
    public LoginScreen checkIsOnPage() {
        onView(withId(R.id.activity_login)).check(matches(isDisplayed()));
        return this;
    }

    @Override
    public void afterActivityLaunched() {
        skipOnboardingScreens();
    }

    public LoginScreen skipOnboardingScreens() {
        VitalityActive.Navigate.skipOnboardingScreen();
        return this;
    }

    @Override
    public void focusOnField(String field) {
        if (field.equals("Email")) {
            focusOnUsername();
        } else if (field.equals("password")) {
            focusOnPassword();
        } else {
            super.focusOnField(field);
        }
    }

    @NonNull
    @Override
    public BaseScreen clickOnButtonWithText(String buttonText) {
        if (buttonText.equals(LOG_IN_BUTTON_TEXT)) {
            MockNetworkHandler.enqueueLoginResponse(userName, password);
        }
        return super.clickOnButtonWithText(buttonText);
    }

    @NonNull
    public LoginScreen enterUsername(String userName) {
        onView(withId(R.id.login_email_address)).perform(clearText(), typeText(userName), closeSoftKeyboard());
        this.userName = userName;
        return this;
    }

    @NonNull
    public LoginScreen enterPassword(String password) {
        onView(withId(R.id.login_password)).perform(clearText(), typeText(password), closeSoftKeyboard());
        this.password = password;
        return this;
    }

    public LoginScreen focusOnUsername() {
        onView(withId(R.id.login_email_address)).perform(click());
        return this;
    }

    public LoginScreen focusOnPassword() {
        onView(withId(R.id.login_password)).perform(click());
        return this;
    }

    public IndeterminateScreen clickLogin() {
        clickOnButtonWithText(R.string.login_login_button_title_20);
        return IndeterminateScreen.after("login clicked", this);
    }

    public void checkUserValidationErrorMessageShown() {
        onView(withText(R.string.registration_invalid_email_footnote_error_35)).check(matches(isDisplayed()));
    }

    public void checkIconIsHighlighted(String icon) {
        if (icon.equals("Email")) {
            checkViewHasDrawable(R.id.login_email_icon, R.drawable.email_active);
        } else if (icon.equals("password")) {
            checkViewHasDrawable(R.id.login_password_icon, R.drawable.password_active);
        }
    }

    public LoginScreen checkButtonIsEnabled(String enabled) {
        if (enabled.equals("enabled")) {
            onView(withText(R.string.login_login_button_title_20)).check(matches(isEnabled()));
        } else {
            onView(withText(R.string.login_login_button_title_20)).check(matches(not(isEnabled())));
        }
        return this;
    }

    public LoginScreen checkEnteredUsername(String username) {
        return checkEnteredUsername(withText(username));
    }

    public LoginScreen checkEnteredUsername(Matcher<? super View> check) {
        onView(withId(R.id.login_email_address)).check(matches(check));
        return this;
    }

    public LoginScreen checkEnteredPassword(String password) {
        return checkEnteredPassword(withText(password));
    }

    public LoginScreen checkEnteredPassword(Matcher<? super View> check) {
        onView(withId(R.id.login_password)).check(matches(check));
        return this;
    }

    public ForgotPasswordScreen clickForgetPassword() throws InstantiationException, IllegalAccessException {
        onView(withId(R.id.login_forgot_password_button)).perform(click());
        return isOnScreen(ForgotPasswordScreen.class);
    }

    public RegistrationScreen clickRegister() throws InstantiationException, IllegalAccessException {
        onView(withText(R.string.login_register_button_title_23)).perform(click());
        return isOnScreen(RegistrationScreen.class);
    }
}
