package com.vitalityactive.va.activerewards.onboarding;

import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;

import com.vitalityactive.va.MockJUnitRunner;
import com.vitalityactive.va.R;
import com.vitalityactive.va.cucumber.utils.TestHarness;
import com.vitalityactive.va.dependencyinjection.ModuleCollection;
import com.vitalityactive.va.testutilities.ForceLocaleTestRule;
import com.vitalityactive.va.testutilities.VitalityActive;
import com.vitalityactive.va.testutilities.dependencyinjection.PersistenceWithInMemoryRealmModule;

import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Locale;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.vitalityactive.va.activerewards.onboarding.ActiveRewardsOnboardingActivity.CONTENT_TYPE_DEFINED;
import static com.vitalityactive.va.activerewards.onboarding.ActiveRewardsOnboardingActivity.CONTENT_TYPE_PROBABILISTIC;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class ActiveRewardsOnboardingTranslationsTest {
    @ClassRule
    public static final ForceLocaleTestRule localeTestRule = new ForceLocaleTestRule(Locale.JAPAN);
    
    @Before
    public void setUp() {
        PersistenceWithInMemoryRealmModule realmModule = new PersistenceWithInMemoryRealmModule(MockJUnitRunner.getInstance().getContext());
        realmModule.addData(TestHarness.dataBuilder().loggedInUser().datum);
        ModuleCollection moduleCollection = MockJUnitRunner.getModuleCollection();
        moduleCollection.persistenceModule = realmModule;
        MockJUnitRunner.initialiseTestObjectGraph(moduleCollection);
    }

    @Test
    public void locale_changes_defined_content() {
        VitalityActive.Navigate.launchActiveRewardsOnboardingActivity(CONTENT_TYPE_DEFINED);

        onView(withId(R.id.active_rewards_onboarding_section_1_content)).check(matches(withText(R.string.AR_onboarding_common_item1_731)));
        onView(withId(R.id.active_rewards_onboarding_section_2_content)).check(matches(withText(R.string.AR_onboarding_common_item2_683)));
        onView(withId(R.id.active_rewards_onboarding_section_3_content)).check(matches(withText(R.string.AR_learn_more_claim_reward_content_713)));
        onView(withId(R.id.active_rewards_onboarding_section_4_title)).check(matches(withText(R.string.AR_onboarding_defined_item4_heading_733)));
        onView(withId(R.id.active_rewards_onboarding_section_4_content)).check(matches(withText(R.string.AR_onboarding_defined_item4_732)));
    }

    @Test
    public void locale_changes_probabilistic_content() {
        VitalityActive.Navigate.launchActiveRewardsOnboardingActivity(CONTENT_TYPE_PROBABILISTIC);

        onView(withId(R.id.active_rewards_onboarding_section_1_content)).check(matches(withText(R.string.AR_onboarding_common_item1_731)));
        onView(withId(R.id.active_rewards_onboarding_section_2_content)).check(matches(withText(R.string.AR_onboarding_common_item2_683)));
        onView(withId(R.id.active_rewards_onboarding_section_3_content)).check(matches(withText(R.string.AR_onboarding_common_item3_666)));
        onView(withId(R.id.active_rewards_onboarding_section_4_title)).check(matches(withText(R.string.AR_onboarding_probabilistic_item4_heading_699)));
        onView(withId(R.id.active_rewards_onboarding_section_4_content)).check(matches(withText(R.string.AR_onboarding_probabilistic_item4_698)));
    }

}
