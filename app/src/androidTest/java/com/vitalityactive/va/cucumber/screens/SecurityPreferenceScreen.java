package com.vitalityactive.va.cucumber.screens;

import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.ViewInteraction;
import android.widget.Toast;

import com.vitalityactive.va.R;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.contrib.RecyclerViewActions.scrollToPosition;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.hasSibling;
import static android.support.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.withChild;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.not;

/**
 * Created by dharel.h.rosell on 11/21/2017.
 */

public class SecurityPreferenceScreen extends BaseScreen {

    @Override
    public BaseScreen checkIsOnPage() {
        onView(withId(R.id.user_preferences_security)).check(matches(isDisplayed()));
        return this;
    }

    @NonNull
    public SecurityPreferenceScreen checkInfoIsDisplayed(@StringRes int matcher) {
        onView(withText(matcher))
                .check(matches(isDisplayed()));
        return this;
    }

    public SecurityPreferenceScreen toggleOn(String text) {
        if(text == "fingerprint") {
            ViewInteraction switch_3 = onView(allOf(withId(R.id.preference_item_switch),
                    hasSibling(withChild(withText(R.string.user_prefs_fingerprint_message_93))),
                    isDisplayed()));

            switch_3.check(matches(isEnabled()));
        } else if (text == "remember me") {
            ViewInteraction switch_3 = onView(allOf(withId(R.id.preference_item_switch),
                    hasSibling(withChild(withText(R.string.user_prefs_remember_me_toggle_message_82))),
                    isDisplayed()));
            switch_3.check(matches(isEnabled()));
        }

        return this;
    }

    public SecurityPreferenceScreen toggleOff(String text) {
        if(text == "fingerprint") {
            ViewInteraction switch_3 = onView(allOf(withId(R.id.preference_item_switch),
                    hasSibling(withChild(withText(R.string.user_prefs_fingerprint_message_93))),
                    isDisplayed()));

            switch_3.check(matches(not(isEnabled())));
        } else if (text == "remember me") {
            ViewInteraction switch_3 = onView(allOf(withId(R.id.preference_item_switch),
                    hasSibling(withChild(withText(R.string.user_prefs_remember_me_toggle_message_82))),
                    isDisplayed()));
            switch_3.check(matches(not(isEnabled())));
        }
        return this;
    }

    public SecurityPreferenceScreen toggle(String id) {
        if(id == "fingerprint"){
            ViewInteraction switch_3 = onView(allOf(withId(R.id.preference_item_switch),
                                        hasSibling(withChild(withText(R.string.user_prefs_fingerprint_message_93))),
                                        isDisplayed()));
            switch_3.perform(click());
            return this;
        } else if(id == "remember me") {
            ViewInteraction switch_3 = onView(allOf(withId(R.id.preference_item_switch),
                            hasSibling(withChild(withText(R.string.user_prefs_remember_me_toggle_message_82))),
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