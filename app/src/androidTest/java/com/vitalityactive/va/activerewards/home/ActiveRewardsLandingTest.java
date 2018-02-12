package com.vitalityactive.va.activerewards.home;

import android.support.test.InstrumentationRegistry;
import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;

import com.vitalityactive.va.R;
import com.vitalityactive.va.RepositoryTestBase;
import com.vitalityactive.va.activerewards.landing.ActiveRewardsLandingActivity;
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
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;

import java.io.IOException;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.scrollToPosition;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.vitalityactive.va.testutilities.VitalityActive.Matcher.childAtPosition;
import static com.vitalityactive.va.testutilities.VitalityActive.Matcher.withRegexText;
import static org.hamcrest.Matchers.allOf;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class ActiveRewardsLandingTest {

    @Rule
    public TestName name = new TestName();

    @Before
    public void setUp() throws IOException {
        TestHarness.Settings.useMockService = true;
        TestHarness.setup(name.getMethodName())
                .clearEverythingLikeAFreshInstall()
                .withLoggedInUser();
        persistLoginResponse();
    }

    @After
    public void tearDown() {
        TestHarness.tearDown();
    }

    private void persistLoginResponse() throws IOException {
        DataStore dataStore = TestHarness.setup().getDefaultDataStore();
        LoginServiceResponse response = RepositoryTestBase.getResponse(LoginServiceResponse.class, "login/Login_successful_with_ar_choice.json");
        new LoginRepositoryImpl(dataStore, new AppConfigRepository(dataStore, InstrumentationRegistry.getTargetContext()))
                .persistLoginResponse(response, new Username("active_rewards@UItest.com"));
    }

    private void setupDataAndLaunchActivity() {
        MockNetworkHandler.enqueueResponseFromFile(200, "wellnessdevices/devices.json");
        MockNetworkHandler.enqueueResponseFromText(200, "{}");
        MockNetworkHandler.enqueueResponseFromText(200, "{}");
        launchActivity();
    }

    private void launchActivity() {
        TestHarness.launchActivity(ActiveRewardsLandingActivity.class);
        TestHarness.waitForLoadingIndicatorToNotDisplay();

        VitalityActive.Assert.onScreen(R.id.activity_active_rewards_landing);
    }

    @Test
    @Ignore("TODO: fix me")
    public void start_date_shows_correcly() {
        onView(withId(R.id.txtWillStart)).check(matches(withRegexText(R.string.AR_landing_first_goal_cell_title_677)));
    }

    @Test
    public void activity_menu_item_shows_correctly() {
        setupDataAndLaunchActivity();

        itemIsDisplayed(0, R.string.AR_landing_activity_cell_title_711);
    }

    @Test
    public void rewards_menu_item_shows_correctly() {
        setupDataAndLaunchActivity();

        itemIsDisplayed(1, R.string.AR_landing_rewards_cell_title_695);
    }

    @Test
    public void learn_more_menu_item_shows_correctly() {
        setupDataAndLaunchActivity();

        itemIsDisplayed(2, R.string.learn_more_button_title_104);
    }

    @Test
    @Ignore("TODO: fix me")
    public void help_menu_item_shows_correctly() {
        itemIsDisplayed(3, R.string.help_button_141);
    }

    @Test
    public void rewards_menu_item_navigates_to_rewards_list_activity() {
        setupDataAndLaunchActivity();

        onView(withId(R.id.main_recyclerview)).perform(scrollToPosition(1));
        onView(allOf(withId(R.id.label), withText(R.string.AR_landing_rewards_cell_title_695))).perform(click());

        VitalityActive.Assert.onScreen(R.id.activity_ar_rewards_list);
    }

    private void itemIsDisplayed(int position, int resourceId) {
        onView(withId(R.id.main_recyclerview)).perform(scrollToPosition(1));
        VitalityActive.Assert.itemWithLabelIsDisplayed(allOf(withId(R.id.recycler_view), childAtPosition(withId(R.id.card_view), 0)), position, resourceId);
    }
}
