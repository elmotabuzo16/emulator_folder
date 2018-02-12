package com.vitalityactive.va.login.ui;

import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;

import com.vitalityactive.va.cucumber.TestData;
import com.vitalityactive.va.cucumber.utils.TestHarness;
import com.vitalityactive.va.cucumber.utils.TestNavigator;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class RememberMeWorks {
    @Rule
    public TestName testName = new TestName();

    @Before
    public void setUp() {
        TestHarness.setup(testName.getMethodName())
                .clearEverythingLikeAFreshInstall();
        TestHarness.startVitalityActiveWithClearedData();
    }

    @After
    public void tearDown() {
        TestHarness.tearDown();
    }

    @Test
    @Ignore("NoMatchingViewException")
    public void when_remember_me_is_on_username_shown_on_login_screen() throws InstantiationException, IllegalAccessException {
        TestNavigator.toUserPreferenceScreen()
                .enableRememberMe(true)
                .clickNext()
                .clickOnLogout()
                .skipOnboardingScreens()
                .checkEnteredUsername(TestData.USERNAME);
    }

    @Test
    @Ignore("toggling not implemented")
    public void when_remember_me_is_off_username_is_not_shown_on_login_screen() throws InstantiationException, IllegalAccessException {
        TestNavigator.toUserPreferenceScreen()
                .enableRememberMe(false)
                .clickNext()
                .clickOnLogout()
                .skipOnboardingScreens()
                .checkEnteredUsername(TestData.EMPTY);
    }
}
