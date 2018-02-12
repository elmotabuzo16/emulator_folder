package com.vitalityactive.va.activerewards.termsandconditions;

import android.content.Intent;
import android.support.test.espresso.Espresso;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.vitalityactive.va.MockJUnitRunner;
import com.vitalityactive.va.R;
import com.vitalityactive.va.ServiceLocator;
import com.vitalityactive.va.cms.CMSContentFetchFailedEvent;
import com.vitalityactive.va.cms.CMSContentFetchSucceededEvent;
import com.vitalityactive.va.cucumber.utils.TestHarness;
import com.vitalityactive.va.dependencyinjection.ModuleCollection;
import com.vitalityactive.va.testutilities.ServiceIdlingResource;
import com.vitalityactive.va.testutilities.dependencyinjection.PersistenceWithInMemoryRealmModule;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class ActiveRewardsTermsAndConditionsTest {
    @Rule
    public ActivityTestRule<ActiveRewardsMedicallyFitAgreementActivity> activityTestRule = new ActivityTestRule<>(ActiveRewardsMedicallyFitAgreementActivity.class, true, false);

    private ServiceIdlingResource<CMSContentFetchSucceededEvent, CMSContentFetchFailedEvent> cmsServiceIdlingResource;

    @Before
    public void setUp() {
        setupLoggedInUser();
        cmsServiceIdlingResource = new ServiceIdlingResource<>(ServiceLocator.getInstance().eventDispatcher, CMSContentFetchSucceededEvent.class, CMSContentFetchFailedEvent.class);
        Espresso.registerIdlingResources(cmsServiceIdlingResource);
    }

    private void setupLoggedInUser() {
        PersistenceWithInMemoryRealmModule realmModule = new PersistenceWithInMemoryRealmModule(MockJUnitRunner.getInstance().getContext());
        realmModule.addData(TestHarness.dataBuilder().loggedInUser().liferayGroupId("83709").datum);
        ModuleCollection moduleCollection = MockJUnitRunner.getModuleCollection();
        moduleCollection.persistenceModule = realmModule;
        MockJUnitRunner.initialiseTestObjectGraph(moduleCollection);
    }

    @After
    public void tearDown() {
        Espresso.unregisterIdlingResources(cmsServiceIdlingResource);
        cmsServiceIdlingResource.close();
    }

    @Test
    @Ignore
    public void agree_button_is_disabled_until_terms_and_conditions_content_finishes_downloading() {
        activityTestRule.launchActivity(new Intent());

        onView(withId(R.id.terms_and_conditions_agree_button)).check(matches(isEnabled()));
    }
}
