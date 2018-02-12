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

public class CommunicationPreferencesScreen extends BaseScreen {

    @Override
    public BaseScreen checkIsOnPage() {
        onView(withId(R.id.user_preferences_communications)).check(matches(isDisplayed()));
        return this;
    }

    @NonNull
    public CommunicationPreferencesScreen checkInfoIsDisplayed(@StringRes int matcher) {
        onView(withText(matcher))
                .check(matches(isDisplayed()));
        return this;
    }

    public CommunicationPreferencesScreen toggleOn(String text) {
       if (text == "email") {
            onView(withText(R.string.user_prefs_email_toggle_message_66)).check(matches(isEnabled()));
           return this;
        }
        return this;
    }

    public CommunicationPreferencesScreen toggleOff(String text) {
        if (text == "remember me") {
            onView(withText(R.string.user_prefs_remember_me_toggle_title_81)).check(matches(not(isEnabled())));
            return this;
        } else if (text == "email") {
            onView(withText(R.string.user_prefs_email_toggle_message_66)).check(matches(not(isEnabled())));
        } else if(text == "fingerprint") {
            onView(withText(R.string.user_prefs_fingerprint_title_92)).check(matches(not(isEnabled())));
        }
        return this;
    }

    public CommunicationPreferencesScreen toggle() {
        ViewInteraction switch_3 = onView(allOf(withId(R.id.preference_item_switch),
                    hasSibling(withChild(withText(R.string.user_prefs_email_toggle_message_66))),
                    isDisplayed()));
            switch_3.perform(click());
            return this;
    }

    public BaseScreen scroll() {
        onView(withId(R.id.recycler_view)).perform(scrollToPosition(2));
        return this;
    }

}