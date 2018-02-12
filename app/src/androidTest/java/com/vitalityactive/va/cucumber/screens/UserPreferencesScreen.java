package com.vitalityactive.va.cucumber.screens;

import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.test.espresso.ViewInteraction;
import android.support.v7.widget.LinearLayoutManager;

import com.vitalityactive.va.R;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.scrollToPosition;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.not;

public class UserPreferencesScreen extends BaseScreen {
    private LinearLayoutManager mLinearLayoutManager;

    @Override
    public UserPreferencesScreen checkIsOnPage() {
        onView(withId(R.id.activity_user_preferences)).check(matches(isDisplayed()));
        return this;
    }

    public HomeScreen clickNext() throws InstantiationException, IllegalAccessException {
        clickOnButton(withText(R.string.next_button_title_84));
        return isOnScreen(HomeScreen.class);
    }

    public UserPreferencesScreen enableRememberMe(boolean enabled) {
        if (enabled) {
            return this;
        }
        throw new UnsupportedOperationException("disabling not implemented");
    }

    @NonNull
    public UserPreferencesScreen checkInfoIsDisplayed(@StringRes int matcher) {
        onView(withText(matcher))
                .check(matches(isDisplayed()));
        return this;
    }

    public UserPreferencesScreen toggleOn(String text) {
        if (text == "remember me") {
            onView(withText(R.string.user_prefs_remember_me_toggle_title_81)).check(matches(isEnabled()));
        } else if (text == "email") {
            onView(withText(R.string.user_prefs_email_toggle_message_66)).check(matches(isEnabled()));
        }

        return this;
    }

    public UserPreferencesScreen toggleOff(String text) {
        if (text == "remember me") {
            onView(withText(R.string.user_prefs_remember_me_toggle_title_81)).check(matches(not(isEnabled())));
            return this;
        } else if (text == "email") {
            onView(withText(R.string.user_prefs_email_toggle_message_66)).check(matches(not(isEnabled())));
        }
        return this;
    }

    public UserPreferencesScreen toggle(String id) {
        if (id == "email") {
            ViewInteraction switch_4 = onView(
                    allOf(withId(R.id.preference_item_switch),
                            withParent(withText(R.string.user_prefs_email_toggle_message_66)),
                            isDisplayed()));
            switch_4.perform(click());
        } else if (id == "analytics") {
            ViewInteraction switch_3 = onView(
                    allOf(withId(R.id.preference_item_switch),
                            withParent(withText(R.string.user_prefs_analytics_toggle_message_74)),
                            isDisplayed()));
            switch_3.perform(click());
        } else if (id == "crash report") {
            ViewInteraction switch_3 = onView(
                    allOf(withId(R.id.preference_item_switch),
                            withParent(withText(R.string.user_prefs_crash_reports_toggle_message_76)),
                            isDisplayed()));
            switch_3.perform(click());
        } else if (id == "remember me") {
            ViewInteraction switch_3 = onView(
                    allOf(withId(R.id.preference_item_switch),
                            withParent(withText(R.string.user_prefs_remember_me_toggle_message_82)),
                            isDisplayed()));
            switch_3.perform(click());
            return this;
        }
        return this;
    }

    public BaseScreen scroll() {
        onView(withId(R.id.recycler_view)).perform(scrollToPosition(2));
        return this;
    }
}
