package com.vitalityactive.va.screening;

import android.support.test.espresso.contrib.RecyclerViewActions;

import com.vitalityactive.va.R;
import com.vitalityactive.va.cucumber.screens.ScreeningLearnMoreScreen;
import com.vitalityactive.va.cucumber.screens.ScreeningsAndVaccinationScreen;
import com.vitalityactive.va.cucumber.utils.MockNetworkHandler;
import com.vitalityactive.va.cucumber.utils.TestHarness;
import com.vitalityactive.va.home.HomeActivity;
import com.vitalityactive.va.snv.learnmore.ScreeningsLearnMoreActivity;
import com.vitalityactive.va.snv.onboarding.healthactions.HealthActionsActivity;
import com.vitalityactive.va.testutilities.VitalityActive;
import com.vitalityactive.va.testutilities.annotations.UITestWithMockedNetwork;

import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.pressBack;
import static android.support.test.espresso.action.ViewActions.swipeDown;
import static android.support.test.espresso.action.ViewActions.swipeLeft;
import static android.support.test.espresso.action.ViewActions.swipeUp;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.NavigationViewActions.navigateTo;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static android.support.test.espresso.action.ViewActions.click;
import static com.vitalityactive.va.cucumber.utils.TestHarness.shortDelay;
import static com.vitalityactive.va.testutilities.VitalityActive.Matcher.withDrawable;

/**
 * Created by stephen.rey.w.avila on 12/8/2017.
 */
@UITestWithMockedNetwork
public class ScreeningLearnMoreActivityTest extends BaseTests {

    public static final String USERNAME = "Donette_Cason@SITtest.com";
    public static final String PASSWORD = "TestPass123";

//    @Test
//    public void can_load_landing_activity() {
//        setupSnvLearnMoreDataAndLaunchLandingActivity();
//    }
//
//    @Test
//    public void checkSnvLearnMoreStringStaticContents() throws Exception {
//
//        MockNetworkHandler.enqueueResponseFromFile(200, "snv/screening.json");
//        TestHarness.launchActivity(ScreeningsLearnMoreActivity.class);
//
//        onView(withId(R.id.activity_learn_more)).check(matches(isDisplayed()));
//        onView(withId(R.id.main_recyclerview)).perform(RecyclerViewActions.scrollToPosition(0));
//
//        onView(withText(R.string.SV_learn_more_main_title_1025)).check(matches(isDisplayed()));
//        onView(withText(R.string.SV_learn_more_main_message_1026)).check(matches(isDisplayed()));
//
//        onView(withText(R.string.SV_learn_more_section_1_title_1027)).check(matches(isDisplayed()));
//        onView(withText(R.string.SV_learn_more_section1_message_1028)).check(matches(isDisplayed()));
//        onView(withText(R.string.SV_partner_title_1029)).check(matches(isDisplayed()));
//        onView(withText(R.string.SV_partner_title_1029)).perform(click());
//
//        onView(withText(R.string.SV_learn_more_section_2_title_1030)).check(matches(isDisplayed()));
//        onView(withText(R.string.SV_onboarding_section_2_message_1005)).check(matches(isDisplayed()));
//
//        onView(withId(R.id.activity_learn_more)).perform(swipeUp());
//
//        onView(withText(R.string.SV_learn_more_section_3_title_1032)).check(matches(isDisplayed()));
//        onView(withText(R.string.SV_learn_more_section_3_message_1033)).check(matches(isDisplayed()));
//
//        onView(withText(R.string.home_card_card_title_15106)).check(matches(isDisplayed()));
//        onView(withText(R.string.home_card_card_title_15107)).check(matches(isDisplayed()));
//
//    }
//
////    @Test
////    public void checkSnvLearnMoreListItems() throws Exception {
////
////        MockNetworkHandler.enqueueResponseFromFile(200, "snv/screening.json");
////        TestHarness.launchActivity(ScreeningsLearnMoreActivity.class);
////        onView(withId(R.id.activity_learn_more)).perform(swipeUp());
////
////        onView(withText(R.string.home_card_card_title_15106)).perform(click());
////        onView(withText("HIV")).check(matches(isDisplayed()));
////
////        onView(withId(R.id.activity_learn_more)).perform(pressBack());
////        onView(withText(R.string.home_card_card_title_15107)).perform(click());
////        onView(withId(R.id.activity_learn_more)).perform(swipeUp());
////
////        onView(withText("HPV")).check(matches(isDisplayed()));
////    }
//
//    public void launchingActivity() throws Exception {
//        TestHarness.waitForLoadingIndicatorToNotDisplay();
//        toHomeScreen();
//    }

}


