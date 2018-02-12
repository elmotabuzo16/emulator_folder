package com.vitalityactive.va.cucumber.screens;

import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.test.espresso.ViewInteraction;

import com.vitalityactive.va.R;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.scrollToPosition;
import static android.support.test.espresso.matcher.ViewMatchers.hasSibling;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.withChild;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.not;

/**
 * Created by dharel.h.rosell on 11/21/2017.
 */

public class PrivacyPreferenceScreen extends BaseScreen {

    @Override
    public BaseScreen checkIsOnPage() {
        onView(withId(R.id.user_preferences_privacy)).check(matches(isDisplayed()));
        return this;
    }

    @NonNull
    public PrivacyPreferenceScreen checkInfoIsDisplayed(@StringRes int matcher) {
        onView(withText(matcher))
                .check(matches(isDisplayed()));
        return this;
    }

    public PrivacyPreferenceScreen toggleOn(String text) {
        if (text == "analytics") {
            onView(withText(R.string.user_prefs_analytics_toggle_title_73)).check(matches(isEnabled()));
            return this;
        } else if (text == "crash reports") {
            onView(withText(R.string.user_prefs_crash_reports_toggle_title_75)).check(matches(isEnabled()));
            return this;
        }

        return this;
    }

    public PrivacyPreferenceScreen toggleOff(String text) {
        if (text == "analytics") {
            onView(withText(R.string.user_prefs_analytics_toggle_title_73)).check(matches(isEnabled()));
            return this;
        } else if (text == "crash reports") {
            onView(withText(R.string.user_prefs_crash_reports_toggle_title_75)).check(matches(isEnabled()));
            return this;
        }
        return this;
    }

    public PrivacyPreferenceScreen toggle(String id) {
        if (id == "analytics") {
            ViewInteraction switch_3 = onView(allOf(withId(R.id.preference_item_switch),
                    hasSibling(withChild(withText(R.string.user_prefs_analytics_toggle_message_74))),
                    isDisplayed()));
            switch_3.perform(click());
            return this;
        } else if (id == "crash report") {
            ViewInteraction switch_3 = onView(allOf(withId(R.id.preference_item_switch),
                    hasSibling(withChild(withText(R.string.user_prefs_crash_reports_toggle_message_76))),
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