package com.vitalityactive.va.activerewards.rewards;

import android.content.Intent;
import android.support.test.InstrumentationRegistry;

import com.vitalityactive.va.R;
import com.vitalityactive.va.RepositoryTestBase;
import com.vitalityactive.va.appconfig.AppConfigRepository;
import com.vitalityactive.va.cucumber.utils.MockNetworkHandler;
import com.vitalityactive.va.cucumber.utils.TestHarness;
import com.vitalityactive.va.login.LoginRepositoryImpl;
import com.vitalityactive.va.networking.model.LoginServiceResponse;
import com.vitalityactive.va.persistence.DataStore;
import com.vitalityactive.va.register.entity.Username;
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
public class ChooseRewardActivityTests {

    @Rule
    public TestName name = new TestName();

    @Before
    public void setUp() throws Exception {
        TestHarness.Settings.useMockService = true;
        TestHarness.setup(name.getMethodName())
                .clearEverythingLikeAFreshInstall()
                .withLoggedInUser();
    }

    @After
    public void tearDown() {
        TestHarness.tearDown();
    }

    private void persistLoginResponse(String responsePath) throws Exception {
        DataStore dataStore = TestHarness.setup().getDefaultDataStore();
        LoginServiceResponse response = RepositoryTestBase.getResponse(LoginServiceResponse.class, responsePath);
        new LoginRepositoryImpl(dataStore, new AppConfigRepository(dataStore, InstrumentationRegistry.getTargetContext()))
                .persistLoginResponse(response, new Username("active_rewards@UItest.com"));
    }

    private void setupDataAndLaunchActivity() {
        MockNetworkHandler.enqueueResponseFromFile(200, "ar/get_awarded_reward_by_id_with_reward_choices.json");
        Intent intent = new Intent();
        intent.putExtra(ChooseRewardActivity.CHOOSE_REWARD_UNCLAIMED_REWARD_UNIQUE_ID, 300402L);
        TestHarness.launchActivity(ChooseRewardActivity.class, intent);
        TestHarness.waitForLoadingIndicatorToNotDisplay();

        VitalityActive.Assert.onScreen(R.id.choose_reward_container);
    }

    @Test
    public void should_display_cinema_list() throws Exception {
        setupDataAndLaunchActivity();

        onView(withText("Amazon")).check(matches(isDisplayed()));
        onView(withText("Zappos")).check(matches(isDisplayed()));
    }

    @Test
    public void should_enable_choose_button_when_cinema_selected() throws Exception {
        setupDataAndLaunchActivity();

        onView(withText(R.string.AR_rewards_choose_reward_button_title_686)).check(matches(not(isEnabled())));
        onView(withText("Amazon")).perform(click());
        onView(withText(R.string.AR_rewards_choose_reward_button_title_686)).check(matches(isEnabled()));
    }

    @Test
    public void should_show_data_sharing_consent_when_choose_button_tapped() throws Exception {
        persistLoginResponse("login/Login_successful_with_data_privacy.json");
        setupDataAndLaunchActivity();
        MockNetworkHandler.enqueueResponseFromText(200, "Data Privacy");

        onView(withText("Zappos")).perform(click());
        onView(withText(R.string.AR_rewards_choose_reward_button_title_686)).perform(click());

        onView(withText("Data Privacy")).check(matches(isDisplayed()));
    }

    @Test
    public void should_navigate_after_cinema_selection_confirmed() throws Exception {
        setupDataAndLaunchActivity();
        MockNetworkHandler.enqueueResponseFromFile(200, "ar/exchange_reward.json");
        MockNetworkHandler.enqueueResponseFromFile(200, "ar/get_awarded_reward_by_id_chosen_reward_response.json");

        onView(withText("Amazon")).perform(click());
        onView(withText(R.string.AR_rewards_choose_reward_button_title_686)).perform(click());

        TestHarness.waitForLoadingIndicatorToNotDisplay();
        VitalityActive.Assert.onScreen(R.id.reward_voucher);
    }

}