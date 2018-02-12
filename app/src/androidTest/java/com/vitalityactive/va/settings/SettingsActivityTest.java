package com.vitalityactive.va.settings;

import android.support.annotation.CallSuper;
import android.view.View;

import com.vitalityactive.va.R;
import com.vitalityactive.va.cucumber.screens.BaseScreen;
import com.vitalityactive.va.cucumber.screens.ChangePasswordScreen;
import com.vitalityactive.va.cucumber.screens.CommunicationPreferencesScreen;
import com.vitalityactive.va.cucumber.screens.HomeScreen;
import com.vitalityactive.va.cucumber.screens.LoginScreen;
import com.vitalityactive.va.cucumber.screens.PrivacyPreferenceScreen;
import com.vitalityactive.va.cucumber.screens.SecurityPreferenceScreen;
import com.vitalityactive.va.cucumber.screens.SettingsScreen;
import com.vitalityactive.va.cucumber.screens.SplashScreen;
import com.vitalityactive.va.cucumber.screens.TermsAndConditionsScreen;
import com.vitalityactive.va.cucumber.screens.UserPreferencesScreen;
import com.vitalityactive.va.cucumber.utils.MockNetworkHandler;
import com.vitalityactive.va.cucumber.utils.TestHarness;
import com.vitalityactive.va.home.HomeActivity;
import com.vitalityactive.va.cucumber.utils.TestHarness;
import com.vitalityactive.va.testutilities.matchers.DrawableMatcher;
import com.vitalityactive.va.vhr.BaseTests;

import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.DrawerActions.open;
import static android.support.test.espresso.contrib.NavigationViewActions.navigateTo;
import static android.support.test.espresso.matcher.RootMatchers.isDialog;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withChild;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.vitalityactive.va.cucumber.utils.TestHarness.isOnScreen;
import static com.vitalityactive.va.cucumber.utils.TestHarness.shortDelay;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.not;

/**
 * Created by dharel.h.rosell on 11/21/2017.
 */

public class SettingsActivityTest extends BaseTests {

    public static final String USERNAME = "Donette_Cason@SITtest.com";
    public static final String PASSWORD = "TestPass123";
    public static final String CURRENT_PASSWORD = "passwordTest123";
    public static final String NEW_PASSWORD = "passwordTest456";
    public static final String ERROR_CONFIRM_PASSWORD = "passwordTest457";

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
    public void show_communication_preference_page() throws Exception {
        launchingActivity();
        TestHarness.isOnScreen(SettingsScreen.class);
        onView(withId(R.id.communication_preferences)).perform(click());
        TestHarness.isOnScreen(CommunicationPreferencesScreen.class);
        onView(withId(R.id.recycler_view)).check(matches(isDisplayed()));

        TestHarness.currentScreen.is(CommunicationPreferencesScreen.class).checkInfoIsDisplayed(R.string.user_prefs_email_toggle_message_66);
        TestHarness.currentScreen.is(CommunicationPreferencesScreen.class).checkInfoIsDisplayed(R.string.user_prefs_notifications_message_90);

        TestHarness.currentScreen.is(CommunicationPreferencesScreen.class).toggle();
        shortDelay();
        TestHarness.currentScreen.is(CommunicationPreferencesScreen.class).toggle();

    }

    @Test
    public void show_privacy_preference_page() throws Exception {
        launchingActivity();
        TestHarness.isOnScreen(SettingsScreen.class);
        onView(withId(R.id.privacy_preferences)).perform(click());
        TestHarness.isOnScreen(PrivacyPreferenceScreen.class);
        onView(withId(R.id.recycler_view)).check(matches(isDisplayed()));

        TestHarness.currentScreen.is(PrivacyPreferenceScreen.class).checkInfoIsDisplayed(R.string.user_prefs_analytics_toggle_title_73);
        TestHarness.currentScreen.is(PrivacyPreferenceScreen.class).checkInfoIsDisplayed(R.string.user_prefs_analytics_toggle_message_74);
        TestHarness.currentScreen.is(PrivacyPreferenceScreen.class).checkInfoIsDisplayed(R.string.user_prefs_crash_reports_toggle_title_75);
        TestHarness.currentScreen.is(PrivacyPreferenceScreen.class).checkInfoIsDisplayed(R.string.user_prefs_crash_reports_toggle_message_76);

        TestHarness.currentScreen.is(PrivacyPreferenceScreen.class).toggle("analytics");
        shortDelay();
        TestHarness.currentScreen.is(PrivacyPreferenceScreen.class).toggle("analytics");
        shortDelay();
        TestHarness.currentScreen.is(PrivacyPreferenceScreen.class).toggle("crash report");
        shortDelay();
        TestHarness.currentScreen.is(PrivacyPreferenceScreen.class).toggle("crash report");

    }

    @Test
    public void show_security_preference_page() throws Exception {
        launchingActivity();
        TestHarness.isOnScreen(SettingsScreen.class);
        onView(withId(R.id.security_preferences)).perform(click());

        TestHarness.isOnScreen(SecurityPreferenceScreen.class);
        onView(withId(R.id.security_change_password)).check(matches(isDisplayed()));

        TestHarness.currentScreen.is(SecurityPreferenceScreen.class).checkInfoIsDisplayed(R.string.user_prefs_fingerprint_title_92);
        TestHarness.currentScreen.is(SecurityPreferenceScreen.class).checkInfoIsDisplayed(R.string.user_prefs_remember_me_toggle_title_81);

        TestHarness.currentScreen.is(SecurityPreferenceScreen.class).toggle("remember me");
        shortDelay();
        TestHarness.currentScreen.is(SecurityPreferenceScreen.class).toggle("remember me");
        shortDelay();
        TestHarness.currentScreen.is(SecurityPreferenceScreen.class).toggle("fingerprint");
        shortDelay();

        //onView(withText(R.string.user_your_fingerprint_for_faster)).check(matches(isDisplayed()));
        onView(withText(R.string.settings_security_finger_print_prompt_confirm_1157)).check(matches(isDisplayed()));
        onView(withText(R.string.login_touchId_fingerprint_sensor_area_9999)).check(matches(isDisplayed()));
        onView(withId(android.R.id.button2)).check(matches(isDisplayed()));
        onView(withId(android.R.id.button2)).perform(click());
        shortDelay();

        onView(allOf(withId(R.id.security_change_password), withChild(withText(R.string.Settings_security_change_password_title_827)), isDisplayed()));
        onView(allOf(withId(R.id.security_change_password), withChild(withText(R.string.Settings_security_change_password_title_827)))).perform(click());
        TestHarness.isOnScreen(ChangePasswordScreen.class);
        onView(withId(R.id.current_password_icon)).check(matches(isDisplayed()));
        onView(withId(R.id.current_password)).check(matches(isDisplayed()));
        onView(withId(R.id.new_password_icon)).check(matches(isDisplayed()));
        onView(withId(R.id.new_password)).check(matches(isDisplayed()));
        onView(withId(R.id.confirm_password_icon)).check(matches(isDisplayed()));
        onView(withId(R.id.confirm_password)).check(matches(isDisplayed()));
        onView(withText("At least 7 characters")).check(matches(isDisplayed()));
        onView(withId(R.id.confirm_password_icon)).check(matches(withDrawable(R.drawable.password_active)));

        //Test for fail
        TestHarness.currentScreen = TestHarness.isOnScreen(ChangePasswordScreen.class)
                .enterCurrentPassword(CURRENT_PASSWORD)
                .enterNewPassword(NEW_PASSWORD)
                .enterConfirmPassword(ERROR_CONFIRM_PASSWORD)
                .clickMenuDone();
        shortDelay();
        onView(allOf(withId(R.id.not_match_warning), withText("Confirm password does not match"))).check(matches(isDisplayed()));
        onView(withId(R.id.confirm_password_icon)).check(matches(withDrawable(R.drawable.confirm_password_24)));

        //Test for success
        TestHarness.currentScreen = TestHarness.isOnScreen(ChangePasswordScreen.class)
                .enterConfirmPassword(NEW_PASSWORD)
                .clickMenuDone();
        onView(withId(R.id.confirm_password_icon)).check(matches(withDrawable(R.drawable.password_active)));
        onView(allOf(withId(R.id.not_match_warning), withText("Confirm password does not match"))).check(matches(not(isDisplayed())));

    }

    public void launchingActivity() throws Exception {
        MockNetworkHandler.enqueueResponseFromFile(200, "status/gold_level_status.json");

        TestHarness.launchActivity(HomeActivity.class);
        TestHarness.waitForLoadingIndicatorToNotDisplay();


        onView(withId(R.id.activity_home)).perform(open());
        onView(withId(R.id.navigation_view)).perform(navigateTo(R.id.navigation_item_settings));
        shortDelay();
    }

//    private static HomeScreen toHomeScreen() throws InstantiationException, IllegalAccessException {
//        TestHarness.startVitalityActiveWithClearedData();
//        TestHarness.currentScreen = TestHarness.isOnScreen(LoginScreen.class)
//                .skipOnboardingScreens()
//                .enterUsername(USERNAME)
//                .enterPassword(PASSWORD)
//                .clickLogin();
//        TestHarness.waitForScreen(SplashScreen.class)
//                .clickOnBackgroundAndIgnoreNextScreen();
//        BaseScreen baseScreen = TestHarness.waitForAnyScreen(HomeScreen.class, UserPreferencesScreen.class);
//
//        if (baseScreen.isOfType(UserPreferencesScreen.class)){
//            TestHarness.currentScreen.is(UserPreferencesScreen.class).clickNext();
//        }
//        return TestHarness.waitForScreen(HomeScreen.class);
//    }

    public static Matcher<View> withDrawable(final int resourceId) {
        return new DrawableMatcher(resourceId);
    }

    public static Matcher<View> noDrawable() {
        return new DrawableMatcher(-1);
    }

}

