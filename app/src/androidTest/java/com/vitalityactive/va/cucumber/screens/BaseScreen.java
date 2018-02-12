package com.vitalityactive.va.cucumber.screens;

import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.NoActivityResumedException;
import android.support.test.espresso.NoMatchingRootException;
import android.support.test.espresso.NoMatchingViewException;
import android.view.View;

import com.vitalityactive.va.R;
import com.vitalityactive.va.cucumber.exception.DescriptiveViewMarchException;
import com.vitalityactive.va.cucumber.exception.UnexpectedScreenException;
import com.vitalityactive.va.cucumber.utils.TestHarness;
import com.vitalityactive.va.testutilities.VitalityActive;

import junit.framework.AssertionFailedError;

import org.hamcrest.Matcher;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.isDialog;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.isRoot;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.vitalityactive.va.testutilities.VitalityActive.Matcher.withDrawable;
import static org.hamcrest.Matchers.not;

public abstract class BaseScreen {
    public static <T extends BaseScreen> T isOnScreen(Class<T> clazz) throws IllegalAccessException, InstantiationException {
        T object = clazz.newInstance();
        object.checkIsOnPage();
        return object;
    }

    public <T extends BaseScreen> T is(Class<T> clazz) {
        if (isOfType(clazz)) {
            T cast = clazz.cast(this);
            cast.checkIsOnPage();
            return cast;
        } else {
            throw new UnexpectedScreenException(clazz, this.getScreenDetails());
        }
    }

    public <T extends BaseScreen> boolean isOfType(Class<T> clazz) {
        return clazz.isInstance(this);
    }

    public abstract BaseScreen checkIsOnPage();

    @NonNull
    public BaseScreen clickOnButtonWithText(String buttonText) {
        return clickOnButton(withText(buttonText));
    }

    @NonNull
    public BaseScreen clickOnButtonWithText(int buttonText) {
        return clickOnButton(withText(buttonText));
    }

    @NonNull
    public BaseScreen clickOnButton(Matcher<View> matcher) {
        checkButtonIsDisplayed(matcher);
        try {
            onView(matcher).perform(click());
        } catch (Throwable e) {
            throw new RuntimeException("Click failed, but the button is present", e);
        }
        return this;
    }

    @NonNull
    public BaseScreen clickOnDialogButtonWithText(String text) {
        onView(withText(text))
                .inRoot(isDialog())
                .perform(click());
        return this;
    }

    public BaseScreen clickOnDialogButtonWithText(@StringRes int text) {
        VitalityActive.Navigate.clickOnDialogButtonWithText(text);
        return this;
    }

    @NonNull
    public BaseScreen checkButtonIsDisplayed(Matcher<View> matcher) {
        onView(matcher).check(matches(isDisplayed()));
        return this;
    }

    public BaseScreen checkButtonIsDisplayed(int Res) {
        onView(withText(Res)).check(matches(isDisplayed()));
        return this;
    }

    @NonNull
    public BaseScreen checkButtonIsEnabled(String buttonText, boolean enabled) {
        if (enabled) {
            onView(withText(buttonText)).check(matches(isEnabled()));
        } else {
            onView(withText(buttonText)).check(matches(not(isEnabled())));
        }
        return this;
    }

    public void afterActivityLaunched() {
        // no-op
    }

    public void focusOnField(String field) {
        onView(withText(field)).perform(click());
    }

    public void checkViewHasDrawable(int viewId, int drawableResourceId) {
        onView(withId(viewId)).check(matches(withDrawable(drawableResourceId)));
    }

    public BaseScreen checkUnexpectedDialogWithTextIsNotDisplayed(@StringRes int textResourceId) {
        try {
            onView(withText(textResourceId))
                    .inRoot(isDialog())
                    .check(matches(not(isDisplayed())));
        } catch (Throwable t) {
            String resourceName = DescriptiveViewMarchException.getResourceName(textResourceId);
            String value = DescriptiveViewMarchException.getStringValue(textResourceId);
            TestHarness.takeScreenshot("unexpected dialog displayed");
            throw new DescriptiveViewMarchException(String.format("An unexpected dialog with text id=%s (%s) is displayed", resourceName, value), t);
        }
        return this;
    }

    public BaseScreen checkDialogWithTextIsDisplayed(@StringRes int textResourceId) {
        onView(withText(textResourceId))
                .inRoot(isDialog())
                .check(matches(isDisplayed()));
        return this;
    }

    public BaseScreen checkDialogWithButtonIsDisplayed(String buttonText) {
        onView(withText(buttonText))
                .inRoot(isDialog())
                .check(matches(isDisplayed()));
        return this;
    }

    public BaseScreen checkDialogWithButtonIsDisplayed(@StringRes int buttonTextId) {
        onView(withText(buttonTextId))
                .inRoot(isDialog())
                .check(matches(isDisplayed()));
        return this;
    }

    public boolean isLoadingIndicatorDisplayed() {
        try {
            onView(withId(R.id.loading_indicator))
                    .check(matches(isDisplayed()));
            return true;
        } catch (NoMatchingViewException ignored) {
            return false;
        }
    }

    public BaseScreen checkNoDialogIsDisplayed() {
        if (isLoadingIndicatorDisplayed()) {
            // the loading indicator is sometimes wrongly seen as a dialog
            return this;
        }

        try {
            onView(isRoot())
                    .inRoot(isDialog())
                    .check(matches(isDisplayed()));
//            TestHarness.takeScreenshot("unexpected dialog displayed");
            throw new DescriptiveViewMarchException("Expected no dialog to be displayed, but a dialog is displayed", null);
        } catch (NoMatchingRootException e) {
            // nothing matched, so good
            return this;
        }
    }

    public BaseScreen checkDialogWithButtonIsDisplayed(@StringRes int button1TextId, @StringRes int button2TextId) {
        return checkDialogWithButtonIsDisplayed(button1TextId).checkDialogWithButtonIsDisplayed(button2TextId);
    }

    public BaseScreen checkDialogWithButtonIsDisplayed(String button1Text, String button2Text) {

        return checkDialogWithButtonIsDisplayed(button1Text).checkDialogWithButtonIsDisplayed(button2Text);
    }

    @Deprecated // use checkNoDialogIsDisplayed
    public BaseScreen checkErrorOccurredDialogIsNotDisplayed() {
        onView(withText("An error occurred"))
                .check(doesNotExist());
        return this;
    }

    public void checkSnackbarWithTextIsDisplayed(int resourceId) {
        onView(withText(resourceId)).check(matches(isDisplayed()));
    }

    public String getScreenDetails() {
        return this.getClass().getSimpleName().replaceAll("Screen$", "");
    }

    public IndeterminateScreen pressBack() {
        Espresso.pressBack();
        return IndeterminateScreen.after("back pressed", this);
    }

    public void pressBackAndExpectAppToBeClosed() {
        try {
            pressBack();
            throw new AssertionFailedError("Expected application to be closed after back pressed");
        } catch (NoActivityResumedException ignore) {
        }
    }
}
