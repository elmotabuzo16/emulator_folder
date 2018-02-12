package com.vitalityactive.va.login.ui;

import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;

import com.vitalityactive.va.cucumber.screens.HomeScreen;
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
public class NavigationFlowTests {
    @Rule
    public TestName testName;

    @Before
    public void setUp() {
        testName = new TestName();
        TestHarness.setup(testName.getMethodName())
                .clearEverythingLikeAFreshInstall();
        TestHarness.startVitalityActiveWithClearedData();
    }

    @After
    public void tearDown() {
        TestHarness.tearDown();
    }

    @Test
    @Ignore("navigation changed?")
    public void back_navigates_home() throws InstantiationException, IllegalAccessException {
        TestNavigator.toUserPreferenceScreen()
                .pressBack()
                .is(HomeScreen.class);
    }

    @Test
    @Ignore("navigation changed?")
    public void back_from_home() throws IllegalAccessException, InstantiationException {
        TestNavigator.toUserPreferenceScreen()
                .clickNext()
                .is(HomeScreen.class)
                .pressBackAndExpectAppToBeClosed();
    }
}
