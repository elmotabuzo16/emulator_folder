package com.vitalityactive.va.home;

import android.support.annotation.CallSuper;
import android.support.test.runner.AndroidJUnit4;

import com.vitalityactive.va.R;
import com.vitalityactive.va.cucumber.utils.MockNetworkHandler;
import com.vitalityactive.va.cucumber.utils.TestHarness;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.vitalityactive.va.testutilities.VitalityActive.Matcher.withDrawable;

@RunWith(AndroidJUnit4.class)
public class HomeStatusBarTests {
    @Rule
    public TestName name = new TestName();

    @Before
    public void setUp() {
        TestHarness.Settings.useMockService = true;
        TestHarness.setup(name.getMethodName())
                .clearEverythingLikeAFreshInstall()
                .withLoggedInUser();
    }

    @After
    @CallSuper
    public void tearDown() {
        TestHarness.tearDown();
    }

    @Test
    public void correct_status_bar_content_is_displayed() {
        MockNetworkHandler.enqueueResponseFromFile(200, "status/gold_level_status.json");

        TestHarness.launchActivity(HomeActivity.class);
        TestHarness.waitForLoadingIndicatorToNotDisplay();

        onView(withText(R.string.Status_level_gold_title_836)).check(matches(isDisplayed()));
        onView(withId(R.id.home_status_icon)).check(matches(withDrawable(R.drawable.status_badge_gold_small)));
        onView(withText("7000 Points to Reach Platinum")).check(matches(isDisplayed()));
    }
}
