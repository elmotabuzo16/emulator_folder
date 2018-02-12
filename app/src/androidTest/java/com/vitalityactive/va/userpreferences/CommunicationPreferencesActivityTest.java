package com.vitalityactive.va.userpreferences;


import android.content.Intent;
import android.support.test.rule.ActivityTestRule;

import com.vitalityactive.va.BaseAndroidTest;
import com.vitalityactive.va.BuildConfig;
import com.vitalityactive.va.MockJUnitAndCucumberRunner;
import com.vitalityactive.va.R;
import com.vitalityactive.va.testutilities.dependencyinjection.PersistenceWithInMemoryRealmModule;

import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

public class CommunicationPreferencesActivityTest extends BaseAndroidTest {

    @Rule
    public ActivityTestRule<CommunicationPreferencesActivity> activityTestRule =
            new ActivityTestRule<>(CommunicationPreferencesActivity.class, false, false);

    @Override
    public void setUp() {
        super.setUp();
        persistenceModule = new PersistenceWithInMemoryRealmModule(MockJUnitAndCucumberRunner.getInstance().getTargetContext());
    }

    @Override
    protected void launchActivity() {
        activityTestRule.launchActivity(new Intent());
    }

    @Test
    public void should_show_communication_preferences() throws Exception {
        setUpNetworkModuleAndLaunchActivity(BuildConfig.TEST_BASE_URL);

        onView(withText(R.string.user_prefs_communication_group_header_message_65))
                .check(matches(isDisplayed()));
    }

}