package com.vitalityactive.va.activerewards.rewards;

import android.content.Intent;

import com.vitalityactive.va.R;
import com.vitalityactive.va.cucumber.utils.MockNetworkHandler;
import com.vitalityactive.va.cucumber.utils.TestHarness;
import com.vitalityactive.va.testutilities.VitalityActive;
import com.vitalityactive.va.testutilities.annotations.UITestWithMockedNetwork;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.IsNot.not;

@UITestWithMockedNetwork
public class CinemaRewardSelectionActivityTests {

    @Rule
    public TestName name = new TestName();

    @Before
    public void setUp() throws IllegalAccessException, InstantiationException {
        TestHarness.Settings.useMockService = true;
        TestHarness.setup(name.getMethodName())
                .clearEverythingLikeAFreshInstall()
                .withLoggedInUser();
    }

    @After
    public void tearDown() {
        TestHarness.tearDown();
    }

    private void setupDataAndLaunchActivity() {
        MockNetworkHandler.enqueueResponseFromFile(200, "ar/get_awarded_reward_by_id_with_reward_selections.json");
        MockNetworkHandler.enqueueResponseFromFile(200, "ar/exchange_reward.json");
        MockNetworkHandler.enqueueResponseFromFile(200, "ar/get_awarded_reward_by_id_selected_reward.json");
        Intent intent = new Intent();
        intent.putExtra("REWARD_UNIQUE_ID", 300402L);
        TestHarness.launchActivity(CinemaRewardSelectionActivity.class, intent);
        TestHarness.waitForLoadingIndicatorToNotDisplay();

        VitalityActive.Assert.onScreen(R.id.choose_reward_container);
    }

    @Test
    public void should_display_cinema_list() {
        setupDataAndLaunchActivity();

        onView(withText("Cineworld")).check(matches(isDisplayed()));
        onView(withText("Vue")).check(matches(isDisplayed()));
    }

    @Test
    public void should_enable_choose_button_when_cinema_selected() {
        setupDataAndLaunchActivity();

        onView(withText(R.string.AR_rewards_choose_reward_button_title_686)).check(matches(not(isEnabled())));
        onView(withText("Vue")).perform(click());
        onView(withText(R.string.AR_rewards_choose_reward_button_title_686)).check(matches(isEnabled()));
    }

    @Test
    public void should_show_confirmation_alert_when_choose_button_tapped() {
        setupDataAndLaunchActivity();

        onView(withText("Vue")).perform(click());
        onView(withText(R.string.AR_rewards_choose_reward_button_title_686)).perform(click());
        onView(withText(R.string.AR_partners_cinema_reward_alert_title)).check(matches(isDisplayed()));
    }

    @Test
    public void should_navigate_after_cinema_selection_confirmed() {
        setupDataAndLaunchActivity();

        onView(withText("Vue")).perform(click());
        onView(withText(R.string.AR_rewards_choose_reward_button_title_686)).perform(click());
        onView(withText(R.string.AR_partners_cinema_reward_alert_title)).check(matches(isDisplayed()));
        onView(withText(R.string.confirm_title_button_182)).perform(click());

        TestHarness.waitForLoadingIndicatorToNotDisplay();
        VitalityActive.Assert.onScreen(R.id.reward_voucher);
    }

}