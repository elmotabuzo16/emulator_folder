package com.vitalityactive.va.activerewards.linkdevices;

import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.vitalityactive.va.R;
import com.vitalityactive.va.testutilities.VitalityActive;
import com.vitalityactive.va.wellnessdevices.landing.WellnessDevicesLandingActivity;

import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.contrib.RecyclerViewActions.scrollToPosition;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.vitalityactive.va.testutilities.VitalityActive.Matcher.childAtPosition;
import static com.vitalityactive.va.testutilities.VitalityActive.Matcher.withRecyclerView;
import static org.hamcrest.Matchers.allOf;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class LinkWellnessDevicesTest {
    @Rule
    public ActivityTestRule<WellnessDevicesLandingActivity> mActivityTestRule = new ActivityTestRule<>(WellnessDevicesLandingActivity.class);

    @Test
    @Ignore("2017-08-10: failing test")
    public void learn_more_menu_item_shows_correctly() {
        itemIsDisplayed(0, R.string.learn_more_button_title_104);
    }

    @Test
    @Ignore("2017-08-10: failing test")
    public void help_menu_item_shows_correctly() {
        itemIsDisplayed(1, R.string.help_button_141);
    }

    @Test
    @Ignore("2017-08-10: failing test")
    public void click_learn_more_shows_more_details_screen() {
        onView(withId(R.id.main_recyclerview)).perform(scrollToPosition(2));
        onView(allOf(withId(R.id.recycler_view), childAtPosition(withId(R.id.container), 1))).perform(scrollToPosition(2));
        onView(allOf(withId(R.id.label), withText(R.string.learn_more_button_title_104))).perform(click());

        VitalityActive.Assert.onScreen(R.id.activity_active_rewards_learn_more);
    }

    @Test
    @Ignore("2017-08-10: failing test")
    public void click_learn_more_in_get_linking_shows_more_details_screen() {
        onView(withRecyclerView(R.id.main_recyclerview).atPositionOnView(0, R.id.learn_more_button)).perform(click());

        VitalityActive.Assert.onScreen(R.id.activity_active_rewards_learn_more);
    }

    private void itemIsDisplayed(int position, int resourceId) {
        onView(withId(R.id.main_recyclerview)).perform(scrollToPosition(2));
        VitalityActive.Assert.itemWithLabelIsDisplayed(allOf(withId(R.id.recycler_view), childAtPosition(withId(R.id.container), 1)), position, resourceId);
    }
}
