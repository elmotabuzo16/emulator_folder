package com.vitalityactive.va.activerewards.onboarding;

import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.vitalityactive.va.R;
import com.vitalityactive.va.home.HomeActivity;
import com.vitalityactive.va.testutilities.VitalityActive;

import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.vitalityactive.va.testutilities.VitalityActive.Matcher.withDrawable;

@RunWith(AndroidJUnit4.class)
@LargeTest
@Ignore("java.lang.NullPointerException: Attempt to invoke virtual method 'java.lang.String com.vitalityactive.va.persistence.models.InsurerConfiguration.getTenantId()' on a null object reference")
public class ActiveRewardsOnboardingTest {
    @Rule
    public ActivityTestRule<HomeActivity> mActivityTestRule = new ActivityTestRule<>(HomeActivity.class);

    @Test
    @Ignore("fix me")
    public void probabilistic_content_is_shown_when_navigating_to_probabilistic_onboarding() {
        VitalityActive.Navigate.fromHomeScreenToProbabilisticOnboarding();

        onView(withId(R.id.active_rewards_onboarding_section_1_content)).check(matches(withText(R.string.AR_onboarding_common_item1_731)));
        onView(withId(R.id.active_rewards_onboarding_section_2_content)).check(matches(withText(R.string.AR_onboarding_common_heading_682)));
        onView(withId(R.id.active_rewards_onboarding_section_3_content)).check(matches(withText(R.string.AR_onboarding_common_item3_666)));
        onView(withId(R.id.active_rewards_onboarding_section_4_content)).check(matches(withText(R.string.AR_onboarding_probabilistic_item4_698)));
        onView(withId(R.id.active_rewards_onboarding_icon4)).check(matches(withDrawable(R.drawable.onboarding_spinner)));
    }

    @Test
    @Ignore("fix me")
    public void defined_content_is_shown_when_navigating_to_defined_onboarding() {
        VitalityActive.Navigate.fromHomeScreenToDefinedOnboarding();

        onView(withId(R.id.active_rewards_onboarding_section_1_content)).check(matches(withText(R.string.AR_onboarding_common_item1_731)));
        onView(withId(R.id.active_rewards_onboarding_section_2_content)).check(matches(withText(R.string.AR_onboarding_common_heading_682)));
        onView(withId(R.id.active_rewards_onboarding_section_3_content)).check(matches(withText(R.string.AR_onboarding_common_item3_666)));
        onView(withId(R.id.active_rewards_onboarding_section_4_content)).check(matches(withText(R.string.AR_onboarding_defined_item4_732)));
        onView(withId(R.id.active_rewards_onboarding_icon4)).check(matches(withDrawable(R.drawable.onboarding_reward)));
    }

    @Test
    @Ignore("fix me")
    public void pressing_get_started_navigates_to_terms_and_conditions() {
        VitalityActive.Navigate.fromHomeScreenToProbabilisticOnboarding();

        onView(withId(R.id.get_started_button)).perform(scrollTo());
        onView(withId(R.id.get_started_button)).perform(click());

        VitalityActive.Assert.onTermsAndConditions();
    }
}
