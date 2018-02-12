package com.vitalityactive.va.forgotpassword.ui;

import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;

import com.vitalityactive.va.R;
import com.vitalityactive.va.cucumber.screens.LoginScreen;
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
public class ForgotPasswordWorks {
    private static final String VALID_USERNAME = "random@glucode.com";
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
    @Ignore("Broken")
    public void to_login_screen_when_done_with_entered_username() throws IllegalAccessException, InstantiationException {
        TestNavigator.toForgotPasswordScreen()
                .enterUsername(VALID_USERNAME)
                .clickOnNext()
                .checkDialogWithTextIsDisplayed(R.string.forgot_password_confirmation_screen_email_sent_message_59)
                .clickOnButtonWithText(R.string.ok_button_title_40);
        TestHarness.isOnScreen(LoginScreen.class)
                .checkEnteredUsername(VALID_USERNAME);      // same username
    }
}
