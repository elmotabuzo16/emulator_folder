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

@UITestWithMockedNetwork
public class UkeHomeScreenCardTests {

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
    public void active_rewards_cineworld_voucher_home_screen_card_is_displayed() {
        setupDataAndLaunchHomeActivity("home/ar_cinema_cineworld_voucher_home_screen_card.json");

        onView(withId(R.id.small_logo)).check(matches(withDrawable(R.drawable.activerewards)));
        onView(withText("Cinema Reward")).check(matches(isDisplayed()));
        onView(withText("1 x Movie Ticket")).check(matches(isDisplayed()));
        onView(withText(R.string.card_action_title_use_reward1093)).check(matches(isDisplayed()));
    }

    @Test
    public void active_rewards_vue_voucher_home_screen_card_is_displayed() {
        setupDataAndLaunchHomeActivity("home/ar_cinema_vue_voucher_home_screen_card.json");

        onView(withId(R.id.small_logo)).check(matches(withDrawable(R.drawable.activerewards)));
        onView(withText("Cinema Reward")).check(matches(isDisplayed()));
        onView(withText("1 x Movie Ticket")).check(matches(isDisplayed()));
        onView(withText(R.string.card_action_title_use_reward1093)).check(matches(isDisplayed()));
    }

    @Test
    public void active_rewards_starbucks_confirm_details_home_screen_card_is_displayed() {
        setupDataAndLaunchHomeActivity("home/ar_starbucks_confirm_details_home_screen_card.json");

        onView(withId(R.id.small_logo)).check(matches(withDrawable(R.drawable.activerewards)));
        onView(withText("Starbucks Reward")).check(matches(isDisplayed()));
        onView(withText(R.string.card_title_confirm_details1091)).check(matches(isDisplayed()));
        onView(withText(R.string.card_action_title_view_reward1092)).check(matches(isDisplayed()));
    }

    @Test
    public void active_rewards_starbucks_free_drink_available_home_screen_card_is_displayed() {
        setupDataAndLaunchHomeActivity("home/ar_starbucks_free_drink_available_home_screen_card.json");

        onView(withId(R.id.small_logo)).check(matches(withDrawable(R.drawable.activerewards)));
        onView(withText("Starbucks Reward")).check(matches(isDisplayed()));
        onView(withText("Free Drink")).check(matches(isDisplayed()));
        onView(withText(R.string.card_action_title_use_reward1093)).check(matches(isDisplayed()));
    }

    @Test
    public void active_rewards_starbucks_free_drink_not_awarded_home_screen_card_is_displayed() {
        setupDataAndLaunchHomeActivity("home/ar_starbucks_free_drink_not_awarded_home_screen_card.json");

        onView(withId(R.id.small_logo)).check(matches(withDrawable(R.drawable.activerewards)));
        onView(withText("Starbucks Reward")).check(matches(isDisplayed()));
        onView(withText(R.string.AR_rewards_starbucks_free_drink_not_awarded)).check(matches(isDisplayed()));
        onView(withText(R.string.card_action_title_view_reward1092)).check(matches(isDisplayed()));
    }

    @Test
    public void active_rewards_starbucks_free_drink_pending_home_screen_card_is_displayed() {
        setupDataAndLaunchHomeActivity("home/ar_starbucks_free_drink_pending_home_screen_card.json");

        onView(withId(R.id.small_logo)).check(matches(withDrawable(R.drawable.activerewards)));
        onView(withText("Starbucks Reward")).check(matches(isDisplayed()));
        onView(withText("Free Drink")).check(matches(isDisplayed()));
        onView(withText(R.string.card_action_title_view_reward1092)).check(matches(isDisplayed()));
    }

}
