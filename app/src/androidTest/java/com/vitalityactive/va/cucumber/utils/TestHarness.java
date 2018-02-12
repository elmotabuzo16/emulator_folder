package com.vitalityactive.va.cucumber.utils;

import android.app.Activity;
import android.content.Intent;
import android.preference.PreferenceActivity;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.action.ViewActions;
import android.support.test.rule.ActivityTestRule;
import android.util.Log;
import android.view.View;

import com.vitalityactive.va.MockJUnitAndCucumberRunner;
import com.vitalityactive.va.R;
import com.vitalityactive.va.activerewards.onboarding.ActiveRewardsOnboardingActivity;
import com.vitalityactive.va.cucumber.TestData;
import com.vitalityactive.va.cucumber.exception.TimeoutException;
import com.vitalityactive.va.cucumber.exception.UnknownScreenName;
import com.vitalityactive.va.cucumber.screens.BaseScreen;
import com.vitalityactive.va.cucumber.screens.ForgotPasswordScreen;
import com.vitalityactive.va.cucumber.screens.HomeScreen;
import com.vitalityactive.va.cucumber.screens.LoginScreen;
import com.vitalityactive.va.cucumber.screens.NonSmokersAssessmentCompleteScreen;
import com.vitalityactive.va.cucumber.screens.NonSmokersDeclarationScreen;
import com.vitalityactive.va.cucumber.screens.NonSmokersLearnMoreScreen;
import com.vitalityactive.va.cucumber.screens.NonSmokersOnboardingScreen;
import com.vitalityactive.va.cucumber.screens.NonSmokersPrivacyPolicyScreen;
import com.vitalityactive.va.cucumber.screens.OnboardingScreen;
import com.vitalityactive.va.cucumber.screens.PartnerScreen;
import com.vitalityactive.va.cucumber.screens.RegistrationScreen;
import com.vitalityactive.va.cucumber.screens.ScreeningsAndVaccinationScreen;
import com.vitalityactive.va.cucumber.screens.ScreeningLearnMoreScreen;
import com.vitalityactive.va.cucumber.screens.TermsAndConditionsScreen;
import com.vitalityactive.va.cucumber.screens.UserPreferencesScreen;
import com.vitalityactive.va.cucumber.screens.VHCCaptureResultScreen;
import com.vitalityactive.va.cucumber.screens.VHCLandingScreen;
import com.vitalityactive.va.cucumber.screens.VHCLearnMoreScreen;
import com.vitalityactive.va.cucumber.screens.VHCOnboardingScreen;
import com.vitalityactive.va.cucumber.screens.VHRLandingScreen;
import com.vitalityactive.va.cucumber.screens.VHRLearnMoreScreen;
import com.vitalityactive.va.cucumber.screens.VHROnboardingScreen;
import com.vitalityactive.va.dependencyinjection.ModuleCollection;
import com.vitalityactive.va.dependencyinjection.NetworkModule;
import com.vitalityactive.va.forgotpassword.ForgotPasswordActivity;
import com.vitalityactive.va.home.HomeActivity;
import com.vitalityactive.va.launch.LaunchActivity;
import com.vitalityactive.va.networking.model.Application;
import com.vitalityactive.va.networking.model.LoginServiceResponse;
import com.vitalityactive.va.nonsmokersdeclaration.NonSmokersAssessmentCompleteActivity;
import com.vitalityactive.va.nonsmokersdeclaration.NonSmokersDeclareActivity;
import com.vitalityactive.va.nonsmokersdeclaration.NonSmokersPrivacyPolicyActivity;
import com.vitalityactive.va.nonsmokersdeclaration.onboarding.NonSmokersDeclarationLearnMoreActivity;
import com.vitalityactive.va.nonsmokersdeclaration.onboarding.NonSmokersDeclarationOnboardingActivity;
import com.vitalityactive.va.persistence.DataStore;
import com.vitalityactive.va.persistence.Model;
import com.vitalityactive.va.persistence.models.AppConfigFeatureParameter;
import com.vitalityactive.va.persistence.models.InsurerConfiguration;
import com.vitalityactive.va.persistence.models.User;
import com.vitalityactive.va.register.view.RegisterActivity;
import com.vitalityactive.va.snv.onboarding.ScreeningsAndVaccinationsOnboardingActivity;
import com.vitalityactive.va.snv.learnmore.ScreeningsLearnMoreActivity;
import com.vitalityactive.va.snv.partners.SnvParticipatingPartnersDetailActivity;
import com.vitalityactive.va.termsandconditions.TermsAndConditionsActivity;
import com.vitalityactive.va.testutilities.ScreenshotAction;
import com.vitalityactive.va.testutilities.dependencyinjection.PersistenceWithInMemoryRealmModule;
import com.vitalityactive.va.vhc.VHCOnboardingActivity;
import com.vitalityactive.va.vhc.captureresults.VHCCaptureResultsActivity;
import com.vitalityactive.va.vhc.landing.VHCLandingActivity;
import com.vitalityactive.va.vhc.learnmore.VHCLearnMoreActivity;
import com.vitalityactive.va.vhr.VHRLearnMoreActivity;
import com.vitalityactive.va.vhr.VHROnBoardingActivity;
import com.vitalityactive.va.vhr.landing.VHRLandingActivity;

import org.hamcrest.Matcher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.UUID;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isRoot;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

public class TestHarness {
    private static final String TAG = "TestHarness";
    public static BaseScreen currentScreen;

    private static HashMap<String, ScreenMapping> screenMapping = new HashMap<>();
    private static Instance instance;

    static {
        screenMapping.put("Login", new ScreenMapping(LaunchActivity.class, LoginScreen.class));
        screenMapping.put("Get started with Vitality Active Rewards", new ScreenMapping(ActiveRewardsOnboardingActivity.class, OnboardingScreen.class));
        screenMapping.put("Registration", new ScreenMapping(RegisterActivity.class, RegistrationScreen.class));
        screenMapping.put("terms and conditions", new ScreenMapping(TermsAndConditionsActivity.class, TermsAndConditionsScreen.class));
        screenMapping.put("home", new ScreenMapping(HomeActivity.class, HomeScreen.class));
        screenMapping.put("NonSmokers", new ScreenMapping(NonSmokersDeclarationOnboardingActivity.class, NonSmokersOnboardingScreen.class));
        screenMapping.put("NonSmokersDeclaration", new ScreenMapping(NonSmokersDeclareActivity.class, NonSmokersDeclarationScreen.class));
        screenMapping.put("learn more", new ScreenMapping(NonSmokersDeclarationLearnMoreActivity.class, NonSmokersLearnMoreScreen.class));
        screenMapping.put("privacy policy", new ScreenMapping(NonSmokersPrivacyPolicyActivity.class, NonSmokersPrivacyPolicyScreen.class));
        screenMapping.put("nonSmoking completion", new ScreenMapping(NonSmokersAssessmentCompleteActivity.class, NonSmokersAssessmentCompleteScreen.class));
        screenMapping.put("Forgot Password", new ScreenMapping(ForgotPasswordActivity.class, ForgotPasswordScreen.class));
        screenMapping.put("preference", new ScreenMapping(PreferenceActivity.class, UserPreferencesScreen.class));
        screenMapping.put("VHC Onboarding", new ScreenMapping(VHCOnboardingActivity.class, VHCOnboardingScreen.class));
        screenMapping.put("VHC Learn More", new ScreenMapping(VHCLearnMoreActivity.class, VHCLearnMoreScreen.class));
        screenMapping.put("VHC Landing", new ScreenMapping(VHCLandingActivity.class, VHCLandingScreen.class));
        screenMapping.put("VHR Onboarding", new ScreenMapping(VHROnBoardingActivity.class, VHROnboardingScreen.class));
        screenMapping.put("VHR Learn More", new ScreenMapping(VHRLearnMoreActivity.class, VHRLearnMoreScreen.class));
        screenMapping.put("VHR Landing", new ScreenMapping(VHRLandingActivity.class, VHRLandingScreen.class));
        screenMapping.put("VHC Capture", new ScreenMapping(VHCCaptureResultsActivity.class, VHCCaptureResultScreen.class));
        screenMapping.put("SNV Learn More", new ScreenMapping(ScreeningsLearnMoreActivity.class, ScreeningLearnMoreScreen.class));
//        screenMapping.put("help", new ScreenMapping(Help.class, .class));
//        screenMapping.put("Disclaimer", new ScreenMapping(.class, VHCLandingScreen.class));
        screenMapping.put("SNV Landing", new ScreenMapping(ScreeningsAndVaccinationsOnboardingActivity.class, ScreeningsAndVaccinationScreen.class));
        screenMapping.put("SNV Landing", new ScreenMapping(SnvParticipatingPartnersDetailActivity.class, PartnerScreen.class));
    }

    public static ScreenMapping getScreenMapping(String screenName) {
        if (!screenMapping.containsKey(screenName)) {
            throw new UnknownScreenName(screenName);
        }
        return screenMapping.get(screenName);
    }

    public static Instance setup(String scenarioId) {
        if (instance != null && scenarioId.equals(instance.scenarioId)) {
            Log.d(TAG, "already setup for scenario scenarioId " + scenarioId);
            return instance;
        }
        instance = new Instance(scenarioId);
        instance.setup();
        return instance;
    }

    public static Instance setup() {
        return instance;
    }

    public static void tearDown() {
        if (instance != null) {
            instance.tearDown();
        }
        instance = null;
    }

    public static MockData dataBuilder() {
        return new MockData();
    }

    public static <T extends BaseScreen> T isOnScreen(Class<T> clazz) throws IllegalAccessException, InstantiationException {
        T screen = BaseScreen.isOnScreen(clazz);
        currentScreen = screen;     // is on this screen, so save it
        return screen;
    }

    public static <T extends BaseScreen> T waitForScreen(Class<T> screen) throws InstantiationException, IllegalAccessException {
        final int SECONDS = 30;
        return waitForScreen(screen, SECONDS);
    }

    public static <T extends BaseScreen> T waitForScreen(Class<T> screen, int maxSecondsToWait) {
        final int SCREENSHOT_SECONDS = 1;

        for (int i = 0; i < maxSecondsToWait * 100; i++) {
            try {
                shortDelay();
                return isOnScreen(screen);
            } catch (Throwable ignored) {
                if (i % (SCREENSHOT_SECONDS * 10) == 0) {
                    TestHarness.takeScreenshot("waiting for " + screen.getSimpleName());
                }
            }
            noDialogIsDisplayed();
        }

        throw new TimeoutException(screen.getSimpleName() + " to show", maxSecondsToWait * 1000);
    }

    @SafeVarargs
    public static BaseScreen waitForAnyScreen(Class<? extends BaseScreen>... screens) throws InstantiationException, IllegalAccessException {
        final int SECONDS = 30;
        final int SCREENSHOT_SECONDS = 1;
        String message = String.format(Locale.getDefault(), "waiting for any one of %d screens", screens.length);

        for (int i = 0; i < SECONDS * 10; i++) {
            shortDelay();
            for (Class<? extends BaseScreen> screen : screens) {
                try {
                    return isOnScreen(screen);
                } catch (Throwable ignored) {
                }
            }

            if (i % (SCREENSHOT_SECONDS * 10) == 0) {
                TestHarness.takeScreenshot(message);
            }
            noDialogIsDisplayed();
        }

        throw new TimeoutException(message, SECONDS * 1000);
    }

    public static void waitForLoadingIndicatorToNotDisplay() {
        final int SECONDS = 30;
        for (int i = 0; i < SECONDS; i++) {
            try {
                onView(withId(R.id.loading_indicator)).check(matches(isDisplayed()));
                shortDelay();
            } catch (Throwable ignored) {
                return;
            }
        }
    }

    public static void noDialogIsDisplayed() {
        currentScreen.checkNoDialogIsDisplayed();
    }

    public static void shortDelay() {
        shortDelay(1500);
    }

    public static void shortDelay(int msec) {
        try {
            Thread.sleep(msec);
        } catch (InterruptedException ignored) {
        }
    }

    public static void delay() {
        shortDelay(5000);
    }

    public static <T extends Activity> void launchActivity(Class<T> activityClass) {
        new ActivityTestRule<>(activityClass).launchActivity(new Intent());
    }

    @Deprecated
    /*
     * @deprecated rather call startVitalityActiveAsAFreshInstall or startVitalityActiveWithClearedData
    */
    public static void launchDefaultStartingActivity() {
        launchActivity(LaunchActivity.class);
    }

    public static void startVitalityActiveAsAFreshInstall() {
        instance.clearEverythingLikeAFreshInstall();
        launchActivity(LaunchActivity.class);
    }

    public static void startVitalityActiveWithClearedData() {
        instance.clearAllData();
        launchActivity(LaunchActivity.class);
    }

    public static <TActivity extends Activity, TScreen extends BaseScreen> void launchActivity(Class<TActivity> activityClass, Class<TScreen> screenClass)
            throws IllegalAccessException, InstantiationException {
        Log.d(TAG, String.format("launching activity %s for screen %s", activityClass.getSimpleName(), screenClass.getSimpleName()));
        launchActivity(activityClass);
        isOnScreen(screenClass).afterActivityLaunched();
    }

    public static <T extends Activity> void launchActivity(Class<T> activityClass,Intent intent) {
        new ActivityTestRule<>(activityClass).launchActivity(intent);
    }

    public static <TActivity extends Activity, TScreen extends BaseScreen> void givenIAmOnActivity(Class<TActivity> activityClass, Class<TScreen> screenClass)
            throws IllegalAccessException, InstantiationException {
        launchActivity(activityClass, screenClass);
    }

    public static void takeScreenshot(String details) {
        String testName = "test";
        if (instance != null) {
            testName = instance.scenarioId;
        }
        onView(isRoot()).perform(new ScreenshotAction(String.format("%s-%s", testName, details)));
    }

    public static void pressHardwareBack() {
        Espresso.pressBack();
    }

    public static void swipeDown(Matcher<View> viewMatcher) {
        onView(viewMatcher).perform(ViewActions.swipeDown());
    }

    public static LoginServiceResponse.PartyDetails getPartyDetails(Long partyId) {
        LoginServiceResponse.PartyDetails partyDetails = new LoginServiceResponse.PartyDetails();
        partyDetails.accessToken = "asdf@#$R#R@#";
        partyDetails.partyId = partyId;
        partyDetails.partnerRefreshToken = "asdfe33f4sadf";
        partyDetails.person = new LoginServiceResponse.Person();
        return partyDetails;
    }

    public static class Settings {
        public static boolean useMockService;

        public Settings() {
            useMockService = com.vitalityactive.va.BuildConfig.GHERKIN_USE_MOCK_SERVER;
            Log.d(TAG, "initialized TestHarness.Settings");
        }
    }

    public static class Instance {
        private final String scenarioId;
        private PersistenceWithInMemoryRealmModule persistenceWithInMemoryRealmModule;
        private ModuleCollection moduleCollection;

        public Instance(String scenarioId) {
            this.scenarioId = scenarioId;
        }

        void setup() {
            Log.d(TAG, "setup TestHarness.instance");
            MockNetworkHandler.start();
            setupDependencyInjection();
        }

        void tearDown() {
            Log.d(TAG, "tearDown TestHarness.instance");
            ActivityFinisher.finishOpenActivities();
            MockNetworkHandler.shutdownAndDeallocateInstance();
            persistenceWithInMemoryRealmModule.closeRealm();
        }

        private void setupDependencyInjection() {
            persistenceWithInMemoryRealmModule = new PersistenceWithInMemoryRealmModule(MockJUnitAndCucumberRunner.getInstance().getTargetContext());
            moduleCollection = MockJUnitAndCucumberRunner.getModuleCollection();
            setupNetworkModule(moduleCollection);
            moduleCollection.persistenceModule = persistenceWithInMemoryRealmModule;
            MockJUnitAndCucumberRunner.initialiseTestObjectGraph(moduleCollection);
        }

        private void setupNetworkModule(ModuleCollection moduleCollection) {
            if (Settings.useMockService) {
                Log.i(TAG, String.format("Using a mock network server, running on the device itself (%s)", MockNetworkHandler.getBaseUrl()));
                moduleCollection.networkModule = new NetworkModule(MockNetworkHandler.getBaseUrl(), UUID.randomUUID().toString());
            } else {
                Log.i(TAG, "Using the default network module (ie, not the mocked network server)");
            }
        }

        public Instance clearEverythingLikeAFreshInstall() {
            return clearAllData().clearAllPreferences();
        }

        public Instance clearAllData() {
            persistenceWithInMemoryRealmModule.clearAllDataStores();
            return this;
        }

        public Instance clearAllPreferences() {
            return clearDeviceSpecificPreferences().clearUserSpecificPreferences();
        }

        public Instance clearUserSpecificPreferences() {
            // nothing here yet
            return this;
        }

        public Instance clearDeviceSpecificPreferences() {
            moduleCollection.defaultModule.provideDeviceSpecificPreferences().clearAll();
            return this;
        }

        public Instance addData(MockData data) {
            DataStore dataStore = persistenceWithInMemoryRealmModule.getDataStore();
            dataStore.add(data.datum);
            return this;
        }

        public Instance addVHRData(MockData data) {
            DataStore dataStore = persistenceWithInMemoryRealmModule.vhrRealm.dataStore;
            dataStore.add(data.datum);
            return this;
        }

        public Instance addVNAData(MockData data) {
            DataStore dataStore = persistenceWithInMemoryRealmModule.vnaRealm.dataStore;
            dataStore.add(data.datum);
            return this;
        }

        public DataStore getDefaultDataStore() {
            return persistenceWithInMemoryRealmModule.defaultRealm.dataStore;
        }

        @Deprecated
        /*
          call withLoggedInUser if you want a logged in user
          if you want to start the activity without data, call startVitalityActiveWithClearedData as the first line in the method that navigates to the expected screen
         */
        public void withDefaults() {
            withLoggedInUser();
        }

        public void withLoggedInUser() {
            addData(TestHarness.dataBuilder()
                    .loggedInUser());
        }
    }

    public static class MockData {
        public ArrayList<Model> datum = new ArrayList<>();

        public MockData loggedInUser() {
            datum.add(new InsurerConfiguration(1L));
            datum.add(new User(getPartyDetails(100001L), 2L, TestData.USERNAME));
            return this;
        }

        public MockData liferayGroupId(String value) {
            Application.ApplicationFeatureParameter parameter = new Application.ApplicationFeatureParameter();
            parameter.name = "liferayGroupId";
            parameter.value = value;
            datum.add(new AppConfigFeatureParameter(parameter, "string"));
            return this;
        }
    }
}
