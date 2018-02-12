package com.vitalityactive.va.activerewards.rewards;

import android.support.test.InstrumentationRegistry;
import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;

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

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class RewardsListActivityTests {

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

    private void setupDataAndLaunchActivity() throws Exception {
        MockNetworkHandler.enqueueResponseFromText(200, "{}");
        TestHarness.launchActivity(RewardsListActivity.class);
        TestHarness.waitForLoadingIndicatorToNotDisplay();

        VitalityActive.Assert.onScreen(R.id.activity_ar_rewards_list);
    }

    @Test
    public void should_show_ar_defined_related_titles() throws Exception {
        persistLoginResponse("login/Login_successful_with_ar_choice.json");
        setupDataAndLaunchActivity();

        onView(withText(R.string.card_action_title_choose_rewards)).check(matches(isDisplayed()));
    }

    @Test
    public void should_show_ar_probabilistic_related_titles() throws Exception {
        persistLoginResponse("login/Login_successful_with_ar_probabilistic.json");
        setupDataAndLaunchActivity();

        onView(withText(R.string.AR_rewards_available_spins_section_title_721)).check(matches(isDisplayed()));
    }
}
