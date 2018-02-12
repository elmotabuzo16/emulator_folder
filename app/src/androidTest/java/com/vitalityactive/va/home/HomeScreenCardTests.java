package com.vitalityactive.va.home;

import com.vitalityactive.va.R;
import com.vitalityactive.va.cucumber.utils.MockNetworkHandler;
import com.vitalityactive.va.cucumber.utils.TestHarness;
import com.vitalityactive.va.testutilities.annotations.UITestWithMockedNetwork;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.vitalityactive.va.testutilities.VitalityActive.Matcher.withDrawable;
import static com.vitalityactive.va.testutilities.VitalityActive.Matcher.withRegexText;

@UITestWithMockedNetwork
public class HomeScreenCardTests {

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

    private void setupDataAndLaunchHomeActivity(String responseFile) {
        MockNetworkHandler.enqueueResponseFromFile(200, responseFile);

        TestHarness.launchActivity(HomeActivity.class);
        TestHarness.waitForLoadingIndicatorToNotDisplay();
    }

    @Test
    public void active_rewards_learn_more_home_screen_card_is_displayed() {
        setupDataAndLaunchHomeActivity("home/ar_learn_more_home_screen_card.json");

        onView(withId(R.id.small_logo)).check(matches(withDrawable(R.drawable.rewards)));
        onView(withText(R.string.AR_homescreen_card_rewards_title_785)).check(matches(isDisplayed()));
        onView(withText(R.string.AR_homescreen_card_earn_rewards_title_786)).check(matches(isDisplayed()));
        onView(withText(R.string.learn_more_button_title_104)).check(matches(isDisplayed()));
    }

    @Test
    public void active_rewards_spin_now_home_screen_card_is_displayed() {
        setupDataAndLaunchHomeActivity("home/ar_spin_now_home_screen_card.json");

        onView(withId(R.id.small_logo)).check(matches(withDrawable(R.drawable.rewards)));
        onView(withText(R.string.AR_homescreen_card_rewards_title_785)).check(matches(isDisplayed()));
        onView(withText(R.string.AR_reward_earned_title)).check(matches(isDisplayed()));
        onView(withText(R.string.AR_rewards_spin_now_title_704)).check(matches(isDisplayed()));
    }

    @Test
    public void active_rewards_view_available_spins_home_screen_card_is_displayed() {
        setupDataAndLaunchHomeActivity("home/ar_view_available_spins_home_screen_card.json");

        onView(withId(R.id.small_logo)).check(matches(withDrawable(R.drawable.rewards)));
        onView(withText(R.string.AR_homescreen_card_rewards_title_785)).check(matches(isDisplayed()));
        onView(withRegexText(R.string.AR_rewards_earned_title_782)).check(matches(isDisplayed()));
        onView(withText(R.string.AR_homescreen_card_available_spins_button_title_787)).check(matches(isDisplayed()));
    }

    @Test
    public void active_rewards_choose_reward_home_screen_card_is_displayed() {
        setupDataAndLaunchHomeActivity("home/ar_choose_reward_home_screen_card.json");

        onView(withId(R.id.small_logo)).check(matches(withDrawable(R.drawable.rewards)));
        onView(withText(R.string.AR_homescreen_card_rewards_title_785)).check(matches(isDisplayed()));
        onView(withText(R.string.AR_reward_earned_title)).check(matches(isDisplayed()));
        onView(withText(R.string.AR_rewards_choose_reward_title_724)).check(matches(isDisplayed()));
    }

    @Test
    public void active_rewards_choose_rewards_home_screen_card_is_displayed() {
        setupDataAndLaunchHomeActivity("home/ar_choose_rewards_home_screen_card.json");

        onView(withId(R.id.small_logo)).check(matches(withDrawable(R.drawable.rewards)));
        onView(withText(R.string.AR_homescreen_card_rewards_title_785)).check(matches(isDisplayed()));
        onView(withRegexText(R.string.AR_rewards_earned_title_782)).check(matches(isDisplayed()));
        onView(withText(R.string.card_action_title_choose_rewards)).check(matches(isDisplayed()));
    }

    @Test
    public void active_rewards_starbucks_voucher_home_screen_card_is_displayed() {
        setupDataAndLaunchHomeActivity("home/ar_starbucks_voucher_home_screen_card.json");

        onView(withId(R.id.small_logo)).check(matches(withDrawable(R.drawable.activerewards)));
        onView(withText("Starbucks Voucher")).check(matches(isDisplayed()));
        onView(withText("ZAR 5 Voucher")).check(matches(isDisplayed()));
        onView(withText(R.string.card_action_title_view_reward1092)).check(matches(isDisplayed()));
    }

    @Test
    public void active_rewards_reward_selection_pending_home_screen_card_is_displayed() {
        setupDataAndLaunchHomeActivity("home/ar_cinema_reward_selection_pending_home_screen_card.json");

        onView(withId(R.id.small_logo)).check(matches(withDrawable(R.drawable.activerewards)));
        onView(withText("Cineworld or Vue")).check(matches(isDisplayed()));
        onView(withRegexText(R.string.AR_rewards_count)).check(matches(isDisplayed()));
        onView(withText(R.string.card_action_title_view_reward1092)).check(matches(isDisplayed()));
    }

    @Test
    public void active_rewards_reward_selection_available_home_screen_card_is_displayed() {
        setupDataAndLaunchHomeActivity("home/ar_cinema_reward_selection_available_home_screen_card.json");

        onView(withId(R.id.small_logo)).check(matches(withDrawable(R.drawable.activerewards)));
        onView(withText("Cineworld or Vue")).check(matches(isDisplayed()));
        onView(withRegexText(R.string.AR_rewards_count)).check(matches(isDisplayed()));
        onView(withText(R.string.card_action_title_redeem_reward1097)).check(matches(isDisplayed()));
    }

}
