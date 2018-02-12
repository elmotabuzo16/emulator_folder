package com.vitalityactive.va.testutilities;

import android.content.Intent;
import android.support.annotation.StringRes;
import android.support.test.espresso.NoMatchingViewException;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.view.View;

import com.vitalityactive.va.R;
import com.vitalityactive.va.activerewards.onboarding.ActiveRewardsOnboardingActivity;
import com.vitalityactive.va.testutilities.matchers.ChildAtPositionMatcher;
import com.vitalityactive.va.testutilities.matchers.DrawableMatcher;
import com.vitalityactive.va.testutilities.matchers.FirstMatched;
import com.vitalityactive.va.testutilities.matchers.FirstOfAnything;
import com.vitalityactive.va.testutilities.matchers.RecyclerViewMatcher;
import com.vitalityactive.va.testutilities.matchers.RegexTextMatcher;
import com.vitalityactive.va.wellnessdevices.WellnessDevicesLearnMoreActivity;
import com.vitalityactive.va.wellnessdevices.dto.PartnerDto;
import com.vitalityactive.va.wellnessdevices.linking.WellnessDevicesDetailsActivity;
import com.vitalityactive.va.wellnessdevices.onboarding.WellnessDevicesOnboardingActivity;

import junit.framework.AssertionFailedError;

import org.hamcrest.Matchers;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.scrollToPosition;
import static android.support.test.espresso.matcher.RootMatchers.isDialog;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.vitalityactive.va.activerewards.onboarding.ActiveRewardsOnboardingActivity.EXTRA_CONTENT_TYPE;
import static org.hamcrest.Matchers.allOf;

public class VitalityActive {
    private static boolean viewIsDisplayed(ViewInteraction viewInteraction) {
        try {
            viewInteraction.check(matches(ViewMatchers.isDisplayed()));
            return true;
        } catch (AssertionFailedError t) {
            return false;
        }
    }

    public static class Assert {
        public static void onScreen(int id) {
            onView(withId(id)).check(matches(isDisplayed()));
        }

        public static void onHomeScreen() {
            onScreen(R.id.activity_home);
        }

        public static void onTermsAndConditions() {
            onScreen(R.id.activity_terms_and_conditions);
        }

        public static void onActiveRewardsLanding() {
            onScreen(R.id.activity_active_rewards_landing);
        }

        public static void onLinkDeviceAfterActiveRewardsOnboarding() {
            onScreen(R.id.sv_wd_onboarding);
        }

        public static void onLoginScreen() {
            onScreen(R.id.activity_login);
        }

        public static void onLoginFragment() {
            onScreen(R.id.login_container);
        }

        public static void onLinkWellnessDeviceScreen() {
            onScreen(R.id.activity_link_wellness_devices);
        }

        public static void onSplashScreen() {
            onScreen(R.id.activity_splash_screen);
        }

        public static void isNotRegistering() {
            onView(withId(R.id.activity_splash_screen)).check(doesNotExist());
        }

        public static void itemWithLabelIsDisplayed(org.hamcrest.Matcher<View> recyclerViewMatcher, int position, int resourceId) {
            onView(recyclerViewMatcher).perform(scrollToPosition(position));
            onView(allOf(withId(R.id.label), withText(resourceId))).check(matches(isDisplayed()));
        }

        //        Wellness Devices Asserts
        public static void onWellnessDevicesLanding() {
            onScreen(R.id.activity_link_wellness_devices);
        }

        public static void onWellnessDevicesLinking() {
            onScreen(R.id.activity_link_wellness_devices_detail);
        }

        public static void onWellnessDevicesLearnMore() {
            onScreen(R.id.activity_learn_more);
        }
    }

    public static class Navigate {
        public static void clickButton(int buttonId) {
            onView(withId(buttonId)).perform(click());
        }

        public static void clickMoreThenAgree() {
            clickMoreThen(R.string.agree_button_title_50);
        }

        public static void clickMoreThen(int resourceId) {
            boolean busy = true;
            while (busy) {
                try {
                    onView(allOf(withId(R.id.terms_and_conditions_agree_button), withText(R.string.terms_and_conditions_more_button_title_95))).perform(click());
                } catch (Throwable ignored) {
                    busy = false;
                }
            }
            onView(allOf(withId(R.id.terms_and_conditions_agree_button), withText(resourceId))).perform(click());
        }

        public static void fromHomeScreenToProbabilisticOnboarding() {
        }

        public static void fromHomeScreenToDefinedOnboarding() {
        }

        public static void clickOKButton() {
            clickButtonWithText(R.string.ok_button_title_40);
        }

        public static void clickTryAgainButton() {
            clickButtonWithText(R.string.try_again_button_title_43);
        }

        public static void clickButtonWithText(int textResourceId) {
            onView(withText(textResourceId)).perform(click());
        }

        public static void skipOnboardingScreen() {
            VitalityActive.Assert.onLoginScreen();
            ViewInteraction appCompatButton2 = onView(
                    allOf(withId(R.id.login_onboarding_skip_button), withText("Skip")));
            if (viewIsDisplayed(appCompatButton2)) {
                appCompatButton2.perform(click());
            } else {
                // login text must be visible
                onView(withId(R.id.login_email_address)).check(matches(ViewMatchers.isDisplayed()));
            }
        }

        public static void launchActiveRewardsOnboardingActivity(String contentTypeProbabilistic) {
            Intent intent = new Intent();
            intent.putExtra(EXTRA_CONTENT_TYPE, contentTypeProbabilistic);
            new ActivityTestRule<>(ActiveRewardsOnboardingActivity.class).launchActivity(intent);
        }

        //        Wellness Devices Tests
        public static void launchWellnessDeviceOnboardingActivity() {
            Intent intent = new Intent();
            new ActivityTestRule<>(WellnessDevicesOnboardingActivity.class).launchActivity(intent);
        }

        public static void launchLandingActivityFromOnboarding() {
            clickButton(R.id.btn_wd_got_it);
        }

        public static void launchLinkingActivityFromLanding(PartnerDto partner) {
//            onView(withId(R.id.main_recyclerview)).perform(scrollToPosition(1));
//            onView(allOf(withId(R.id.title), withText("Fitbit"))).perform(click());
            Intent intent = new Intent();
            intent.putExtra(WellnessDevicesDetailsActivity.KEY_DEVICE, partner);
            new ActivityTestRule<>(WellnessDevicesDetailsActivity.class).launchActivity(intent);
        }

        public static void launchWellnessDevicesLearnMoreActivityFromLanding() {
            Intent intent = new Intent();
            new ActivityTestRule<>(WellnessDevicesLearnMoreActivity.class).launchActivity(intent);
        }

        public static void clickOnDialogButtonWithText(@StringRes int text) {
            onView(withText(text))
                    .inRoot(isDialog())
                    .perform(click());
        }
    }

    public static class Matcher {
        public static org.hamcrest.Matcher<View> withDrawable(final int resourceId) {
            return new DrawableMatcher(resourceId);
        }

        public static org.hamcrest.Matcher<View> withRegexText(final int resourceId) {
            return new RegexTextMatcher(resourceId);
        }

        public static org.hamcrest.Matcher<View> childAtPosition(final org.hamcrest.Matcher<View> parentMatcher, final int position) {
            return new ChildAtPositionMatcher(parentMatcher, position);
        }

        public static org.hamcrest.Matcher<View> firstMatched(final org.hamcrest.Matcher<View> viewMatcher) {
            return new FirstMatched(viewMatcher);
        }

        public static org.hamcrest.Matcher<View> firstOfAnything() {
            return new FirstOfAnything();
        }

        public static RecyclerViewMatcher withRecyclerView(final int recyclerViewId) {
            return new RecyclerViewMatcher(recyclerViewId);
        }

        public static void checkViewIsNotDisplayed(org.hamcrest.Matcher<View> matcher) {
            try {
                onView(matcher).check(matches(Matchers.anything()));
                throw new RuntimeException("View was found!");
            } catch (NoMatchingViewException qwe) {
                // not found, which is good
            }
        }
    }
}
