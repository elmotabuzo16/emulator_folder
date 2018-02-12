package com.vitalityactive.va.activerewards.participatingpartners;

import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.vitalityactive.va.R;
import com.vitalityactive.va.testutilities.VitalityActive;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class ActiveRewardsParticipatingPartnersActivityTest {
    @Rule
    public ActivityTestRule<ActiveRewardsParticipatingPartnersActivity> mActivityTestRule = new ActivityTestRule<>(ActiveRewardsParticipatingPartnersActivity.class);

    @Test
    public void click_on_partner_loads_details() {
        onView(allOf(withId(R.id.title), withText(R.string.AR_partners_starbucks_name_734))).perform(click());

        VitalityActive.Assert.onScreen(R.id.activity_participating_partner_detail);
    }
}
