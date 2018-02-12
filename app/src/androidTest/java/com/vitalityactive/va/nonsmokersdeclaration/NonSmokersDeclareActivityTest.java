package com.vitalityactive.va.nonsmokersdeclaration;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.test.espresso.Espresso;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;

import com.vitalityactive.va.BaseAndroidTest;
import com.vitalityactive.va.MockJUnitAndCucumberRunner;
import com.vitalityactive.va.R;
import com.vitalityactive.va.cms.CMSContentFetchFailedEvent;
import com.vitalityactive.va.cms.CMSContentFetchSucceededEvent;
import com.vitalityactive.va.cucumber.utils.TestHarness;
import com.vitalityactive.va.testutilities.ServiceIdlingResource;
import com.vitalityactive.va.testutilities.dependencyinjection.PersistenceWithInMemoryRealmModule;

import org.junit.After;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@LargeTest
public class NonSmokersDeclareActivityTest extends BaseAndroidTest {
    @Rule
    public ActivityTestRule<NonSmokersDeclareActivity> activityTestRule = new ActivityTestRule<>(NonSmokersDeclareActivity.class, false, false);

    private ServiceIdlingResource<CMSContentFetchSucceededEvent, CMSContentFetchFailedEvent> cmsServiceIdlingResource;

    @After
    public void tearDown() {
        if (cmsServiceIdlingResource != null) {
            Espresso.unregisterIdlingResources(cmsServiceIdlingResource);
            cmsServiceIdlingResource.close();
        }
    }

    @SuppressLint("PrivateResource")
    @Test
    public void up_button_is_displayed() {
        setupPersistenceModule();
        setUpRealNetworkModuleWithResponseCode(201, "<p>This is the declaration</p>");
        cmsServiceIdlingResource = new ServiceIdlingResource<>(CMSContentFetchSucceededEvent.class, CMSContentFetchFailedEvent.class);

        Espresso.registerIdlingResources(cmsServiceIdlingResource);

        activityTestRule.launchActivity(new Intent());

        onView(withId(R.id.activity_terms_and_conditions)).check(matches(isDisplayed()));
        onView(withContentDescription(R.string.abc_action_bar_up_description)).check(matches(isDisplayed()));
    }

    private void setupPersistenceModule() {
        PersistenceWithInMemoryRealmModule realmModule = new PersistenceWithInMemoryRealmModule(MockJUnitAndCucumberRunner.getInstance().getTargetContext());
        persistenceModule = realmModule;
        realmModule.addData(TestHarness.dataBuilder().loggedInUser().datum);
    }

    @Override
    protected void launchActivity() {
    }
}
