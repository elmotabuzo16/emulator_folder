package com.vitalityactive.va.vhc;

import android.app.Instrumentation;
import android.content.Intent;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.test.InstrumentationRegistry;

import com.vitalityactive.va.R;
import com.vitalityactive.va.RepositoryTestBase;
import com.vitalityactive.va.UnitsOfMeasure;
import com.vitalityactive.va.appconfig.AppConfigRepository;
import com.vitalityactive.va.constants.EventType;
import com.vitalityactive.va.cucumber.utils.MockNetworkHandler;
import com.vitalityactive.va.cucumber.utils.TestHarness;
import com.vitalityactive.va.login.LoginRepositoryImpl;
import com.vitalityactive.va.networking.model.LoginServiceResponse;
import com.vitalityactive.va.persistence.DataStore;
import com.vitalityactive.va.register.entity.Username;
import com.vitalityactive.va.testutilities.VitalityActive;
import com.vitalityactive.va.vhc.addproof.VHCAddProofActivity;
import com.vitalityactive.va.vhc.captureresults.models.CapturedGroup;
import com.vitalityactive.va.vhc.captureresults.models.GroupType;
import com.vitalityactive.va.vhc.landing.VHCLandingActivity;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import static android.support.test.InstrumentationRegistry.getContext;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.IntentMatchers.isInternal;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static android.support.v4.content.FileProvider.getUriForFile;
import static com.github.barteksc.pdfviewer.util.FileUtils.copy;
import static org.hamcrest.core.IsNot.not;

//import static android.support.v4.app.ActivityCompatJB.startActivityForResult;

public class VHCCompletedVHRPendingTest {

    public static final String PROVIDER = "com.vitalityactive.va.dev.provider";
    public static final String RESOURCE_FILE = "test_upload.png";
    public static final String TEST_FILE = "tmp.png";
    @Rule
    public TestName name = new TestName();

    @Before
    public void setUp() throws IOException {
        TestHarness.Settings.useMockService = true;
        TestHarness.setup(name.getMethodName())
                .clearEverythingLikeAFreshInstall()
                .withLoggedInUser();
        persistLoginResponse();

        setupDataAndStartVHCLandingActivity();
        setupCapturedBloodPressure();
    }

    private void setupDataAndStartVHCLandingActivity() {
        MockNetworkHandler.enqueueResponseFromFile(201, "vhc/landing_blood_pressure.json");
        TestHarness.launchActivity(VHCLandingActivity.class);
        TestHarness.waitForLoadingIndicatorToNotDisplay();
    }

    @Test
    @Ignore("Ignore failing test")
    public void should_show_vhr_pending() {
        onView(withText(R.string.onboarding_section_2_title_128)).perform(click());

        Instrumentation.ActivityResult result = new Instrumentation.ActivityResult(VHCAddProofActivity.RESULT_OK, pickImageIntent());

        intending(not(isInternal())).respondWith(result);
        onView(withText(R.string.next_button_title_84)).perform(click());
        onView(withText(R.string.proof_add_button_166)).perform(click());
        onView(withText(R.string.proof_action_choose_gallery_dialog_title_257)).perform(click());
        onView(withText(R.string.next_button_title_84)).perform(click());
        onView(withText(R.string.confirm_title_button_182)).perform(click());

        onView(withText(R.string.data_and_privacy_policy_title_123)).check(matches(isDisplayed()));
        onView(withText(R.string.agree_button_title_50)).perform(click());

        onView(withText(R.string.complete_screen_measurements_confirmed_message_189)).check(matches(isDisplayed()));
        onView(withText(R.string.great_button_title_120)).perform(click());

        onView(withText(R.string.my_health_vitality_age_screen_title_613)).check(matches(isDisplayed()));
        onView(VitalityActive.Matcher.withDrawable(R.drawable.close)).perform(click());
    }

    @NonNull
    private Intent pickImageIntent() {
        return new Intent(Intent.ACTION_OPEN_DOCUMENT)
            .setType("image/*")
            .putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            .putExtra(Intent.EXTRA_LOCAL_ONLY, true)
            .setData(getUriForFile(getContext(), PROVIDER, getFile()));
    }

    @NonNull
    private File getFile() {
        File file = new File(Environment.getExternalStorageDirectory(), TEST_FILE);
        try {
            InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(RESOURCE_FILE);
            copy(inputStream, file);
        }
        catch (Exception ex) { }
        return file;
    }

    private void persistLoginResponse() throws java.io.IOException {
        DataStore dataStore = TestHarness.setup().getDefaultDataStore();
        LoginServiceResponse response = RepositoryTestBase.getResponse(LoginServiceResponse.class, "login/Login_successful.json");
        new LoginRepositoryImpl(dataStore, new AppConfigRepository(dataStore, InstrumentationRegistry.getTargetContext()))
                .persistLoginResponse(response, new Username("Leyla_Richard@SITtest.com"));
    }

    private void setupCapturedBloodPressure() {
        CapturedGroup bloodPressure = new CapturedGroup(GroupType.BLOOD_PRESSURE, "Blood Pressure");
        bloodPressure.addCapturedField(String.valueOf(EventType._SYSTOLICPRESSURE))
                .setPrimaryValue(80f, true)
                .setSelectedUnitOfMeasure(UnitsOfMeasure.SYSTOLIC_MILLIMETER_OF_MERCURY)
                .setDateTested(123);
        bloodPressure.addCapturedField(String.valueOf(EventType._DIASTOLICPRESSURE))
                .setPrimaryValue(120f, true)
                .setSelectedUnitOfMeasure(UnitsOfMeasure.SYSTOLIC_MILLIMETER_OF_MERCURY)
                .setDateTested(123);

        TestHarness.MockData mockData = new TestHarness.MockData();
        mockData.datum.add(bloodPressure);
        TestHarness.setup().addData(mockData);
    }
}
