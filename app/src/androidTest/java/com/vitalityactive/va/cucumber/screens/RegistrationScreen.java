package com.vitalityactive.va.cucumber.screens;

import android.support.annotation.NonNull;
import android.support.test.espresso.ViewInteraction;

import com.vitalityactive.va.R;
import com.vitalityactive.va.cucumber.utils.MockNetworkHandler;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.vitalityactive.va.testutilities.VitalityActive.Matcher.withRecyclerView;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.not;

public class RegistrationScreen extends BaseScreen {
    private static final int EMAIL_POSITION = 0;
    private static final int PASSWORD_POSITION = 1;
    private static final int CONFIRM_PASSWORD_POSITION = 2;
    private static final int INSURER_CODE_POSITION = 3;
    private String insurerCode;
    private String email;
    private String password;

    @Override
    public RegistrationScreen checkIsOnPage() {
        onView(withId(R.id.activity_register)).check(matches(isDisplayed()));
        return this;
    }

    @NonNull
    @Override
    public BaseScreen clickOnButtonWithText(String buttonText) {
        if (buttonText.equals("Register") || buttonText.equals("Try again")) {
            MockNetworkHandler.enqueueRegisterResponse(email, insurerCode);
        }
        return super.clickOnButtonWithText(buttonText);
    }

    public RegistrationScreen enterAllDetails(String email, String password, String insurerCode) {
        enterEmail(email);
        enterPasswordAndConfirm(password);
        enterInsurerCode(insurerCode);
        return this;
    }

    public RegistrationScreen enterEmail(String email) {
        this.email = email;
        fillInField(EMAIL_POSITION, email);
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

    public RegistrationScreen focusOnUsername() {
        onView((withRecyclerView(R.id.main_recyclerview).atPositionOnView(0, R.id.field))).perform(click());
        return this;
    }

    public RegistrationScreen focusOnPassword() {
        onView((withRecyclerView(R.id.main_recyclerview).atPositionOnView(1, R.id.field))).perform(click());
        return this;
    }

    public RegistrationScreen enterPasswordAndConfirm(String password) {
        enterPassword(password);
        enterConfirmPassword(password);
        return this;
    }

    public RegistrationScreen enterPassword(String password) {
        fillInField(PASSWORD_POSITION, password);
        return this;
    }

    public RegistrationScreen enterNumericPassword(String password) {
        fillInField(PASSWORD_POSITION, password);
        return this;
    }

    public RegistrationScreen enterAlphaPassword(String password) {
        fillInField(PASSWORD_POSITION, password);
        return this;
    }

    public RegistrationScreen checkButtonIsEnabled(String enabled) {
        ViewInteraction registerButton = onView(
                allOf(withId(R.id.register_button), withText(R.string.login_register_button_title_23),
                        withParent(allOf(withId(R.id.register_button_bar),
                                withParent(withId(R.id.register_content)))),
                        isDisplayed()));
        if (enabled.equals("enabled")) {
            registerButton.check(matches(isEnabled()));
        } else {
            registerButton.check(matches(not(isEnabled())));
        }
        return this;
    }

    public RegistrationScreen enterConfirmPassword(String password) {
        fillInField(CONFIRM_PASSWORD_POSITION, password);
        return this;
    }

    public RegistrationScreen enterInsurerCode(String insurerCode) {
        this.insurerCode = insurerCode;
        fillInField(INSURER_CODE_POSITION, insurerCode);
        return this;
    }

    public void checkUserValidationErrorMessageShown() {
        onView(withText(R.string.registration_password_field_footnote_29)).check(matches(isDisplayed()));
    }

    private void fillInField(int position, String username) {
        onView((withRecyclerView(R.id.main_recyclerview).atPositionOnView(position, R.id.field)))
                .perform(typeText(username), closeSoftKeyboard());
    }

    public RegistrationScreen checkInsurerCode(String InsurerCode) {
        if (InsurerCode.length() < 50)
            return this;

        return this;
    }

    public RegistrationScreen checkRegisteredUser() {
        /// still to implement
        return null;
    }

    public IndeterminateScreen clickOnRegister() {
        clickOnButtonWithText("Register");
        return IndeterminateScreen.after("register clicked", this);
    }

    public void assertEnteredUsername(String username) {
        onView(allOf(withRecyclerView(R.id.main_recyclerview).atPositionOnView(EMAIL_POSITION, R.id.field), withText(username)))
                .check(matches(isDisplayed()));
    }
}
