package com.vitalityactive.va.testutilities;

import android.content.Intent;
import android.content.res.Resources;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.util.Log;

import com.vitalityactive.va.AppConfigRepositoryWithFixedVersionNumber;
import com.vitalityactive.va.BuildConfig;
import com.vitalityactive.va.LanguageProvider;
import com.vitalityactive.va.MockJUnitRunner;
import com.vitalityactive.va.R;
import com.vitalityactive.va.activerewards.ActiveRewardsActivator;
import com.vitalityactive.va.cms.CMSContentFetchFailedEvent;
import com.vitalityactive.va.cms.CMSContentFetchSucceededEvent;
import com.vitalityactive.va.cucumber.utils.TestHarness;
import com.vitalityactive.va.dependencyinjection.DefaultModule;
import com.vitalityactive.va.dependencyinjection.NetworkModule;
import com.vitalityactive.va.launch.LaunchActivity;
import com.vitalityactive.va.login.AuthenticationFailedEvent;
import com.vitalityactive.va.login.AuthenticationSucceededEvent;
import com.vitalityactive.va.login.CreateUserInstructionService;
import com.vitalityactive.va.login.CreateUserInstructionServiceRequest;
import com.vitalityactive.va.register.interactor.RegistrationInteractor;
import com.vitalityactive.va.termsandconditions.TermsAndConditionsAgreeRequestCompletedEvent;
import com.vitalityactive.va.testutilities.servicesetup.DeleteUserService;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.rules.TestName;

import java.util.UUID;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static com.vitalityactive.va.testutilities.VitalityActive.Matcher.withRecyclerView;

public class BaseNavigationFlowTests {
    private static final String TAG = "BaseNavigationFlowTests";
    private static final String USERNAME = "henri@glucode.com";
    private static final String PASSWORD = "Password1";
    private static final String INSURER_CODE = "vv1BcBResJEoIFfXR/QNOQ==:BftdCtzdKLTUWmiLlE13eg==";
    @Rule
    public TestName testName = new TestName();
    protected boolean pressBackAfterTestIsDone = true;
    private Resources resources;
    private ServiceIdlingResource<RegistrationInteractor.RegistrationSucceededEvent, RegistrationInteractor.RegistrationFailedEvent> registrationIdlingResource;
    private ServiceIdlingResource<CMSContentFetchSucceededEvent, CMSContentFetchFailedEvent> termsContentIdlingResource;
    private ServiceIdlingResource<TermsAndConditionsAgreeRequestCompletedEvent, TermsAndConditionsAgreeRequestCompletedEvent> termsAcceptIdlingResource;
    private ServiceIdlingResource<CMSContentFetchSucceededEvent, CMSContentFetchFailedEvent> activeTermsIdlingResource;
    private ServiceIdlingResource<ActiveRewardsActivator.ActivationSucceededEvent, ActiveRewardsActivator.ActivationFailedEvent> activeRewardsActivatedIdlingResource;
    private ServiceIdlingResource<AuthenticationSucceededEvent, AuthenticationFailedEvent> loginIdlingResource;
    @Rule
    public ActivityTestRule<LaunchActivity> activityTestRule = new ActivityTestRule<LaunchActivity>(LaunchActivity.class) {
        @Override
        protected void beforeActivityLaunched() {
            super.beforeActivityLaunched();
            resources = InstrumentationRegistry.getTargetContext().getResources();
            setUpIdlingResources();
        }
    };

    @BeforeClass
    public static void resetNetworkModule() {

    }

    private void setUpIdlingResources() {
        registrationIdlingResource = new ServiceIdlingResource<>(RegistrationInteractor.RegistrationSucceededEvent.class, RegistrationInteractor.RegistrationFailedEvent.class);
        termsContentIdlingResource = new ServiceIdlingResource<>("ApplicationTerms", CMSContentFetchSucceededEvent.class, CMSContentFetchFailedEvent.class);
        activeTermsIdlingResource = new ServiceIdlingResource<>("ActiveRewardsTerms", CMSContentFetchSucceededEvent.class, CMSContentFetchFailedEvent.class);
        activeRewardsActivatedIdlingResource = new ServiceIdlingResource<>(ActiveRewardsActivator.ActivationSucceededEvent.class, ActiveRewardsActivator.ActivationFailedEvent.class);
        loginIdlingResource = new ServiceIdlingResource<>(AuthenticationSucceededEvent.class, AuthenticationFailedEvent.class);
        termsAcceptIdlingResource = new ServiceIdlingResource<>(TermsAndConditionsAgreeRequestCompletedEvent.class, TermsAndConditionsAgreeRequestCompletedEvent.class);
    }

    @Before
    public void setUp() {
        TestHarness.Settings.useMockService = false;
        TestHarness.setup(testName.getMethodName()).clearEverythingLikeAFreshInstall();
    }

    @After
    public void tearDown() {
        unregisterAndCloseIdlingResource(registrationIdlingResource);
        unregisterAndCloseIdlingResource(termsContentIdlingResource);
        unregisterAndCloseIdlingResource(activeTermsIdlingResource);
        unregisterAndCloseIdlingResource(activeRewardsActivatedIdlingResource);
        unregisterAndCloseIdlingResource(loginIdlingResource);
        unregisterAndCloseIdlingResource(termsAcceptIdlingResource);
        if (pressBackAfterTestIsDone)
            Espresso.pressBack();
        TestHarness.tearDown();
    }

    private void unregisterAndCloseIdlingResource(ServiceIdlingResource idlingResource) {
        Espresso.unregisterIdlingResources(idlingResource);
        idlingResource.close();
    }

    protected void launchActivity() {
        Log.d(TAG, String.format("launching for test %s", testName.getMethodName()));
        activityTestRule.launchActivity(new Intent());
    }

    protected void clickOnSplashScreen() {
        Log.d(TAG, "clickOnSplashScreen()");
        ViewInteraction splashScreen = onView(withId(R.id.activity_splash_screen));
        splashScreen.check(matches(isDisplayed()));
        splashScreen.perform(click());
    }

    protected void acceptApplicationTermsAndConditions() {
        Log.d(TAG, "acceptApplicationTermsAndConditions()");
        Espresso.registerIdlingResources(termsContentIdlingResource);
        VitalityActive.Navigate.clickMoreThenAgree();
        Espresso.registerIdlingResources(termsAcceptIdlingResource);
    }

    protected void registerAndAcceptApplicationTermsAndConditions() {
        createGeneralTermsAndConditionsInstruction();
        // TODO: when the login service is fully integrated with the profile service, add instruction here so that we actually show the application terms and conditions
        deleteUser(USERNAME);
        VitalityActive.Navigate.skipOnboardingScreen();
        register();
        logIn();
        clickOnSplashScreen();
        acceptApplicationTermsAndConditions();
        clickNextOnUserPreferencesScreen();
        VitalityActive.Assert.onHomeScreen();
        deleteUser(USERNAME);
    }

    private void createGeneralTermsAndConditionsInstruction() {
        NetworkModule networkModule = new NetworkModule(BuildConfig.TEST_BASE_URL, UUID.randomUUID().toString());
        Retrofit retrofit = networkModule.provideServiceGenerator(networkModule.provideHTTPClient(new LanguageProvider(resources),
                new AppConfigRepositoryWithFixedVersionNumber(InstrumentationRegistry.getTargetContext())), networkModule.provideGson(), new DefaultModule(MockJUnitRunner.getInstance().getApplication()).provideDeviceSpecificPreferences()).getRetrofit();
        CreateUserInstructionService service = retrofit.create(CreateUserInstructionService.class);
        Call<String> call = service.getCreateUserInstructionRequest(new CreateUserInstructionServiceRequest(USERNAME, "LOGIN_T_AND_C", "2016-12-12T00:00:00+02:00[Africa/Johannesburg]"));
        try {
            Response<String> response = call.execute();
            Log.d("", response.body() == null ? "" : response.body());
        } catch (Exception ignored) {

        }
    }

    private void register() {
        Log.d(TAG, "register()");
        VitalityActive.Assert.onLoginScreen();
        VitalityActive.Navigate.clickButton(R.id.login_register_button);
        onView((withRecyclerView(R.id.main_recyclerview).atPositionOnView(0, R.id.field))).perform(replaceText(USERNAME), closeSoftKeyboard());
        onView((withRecyclerView(R.id.main_recyclerview).atPositionOnView(1, R.id.field))).perform(replaceText(PASSWORD), closeSoftKeyboard());
        onView((withRecyclerView(R.id.main_recyclerview).atPositionOnView(2, R.id.field))).perform(replaceText(PASSWORD), closeSoftKeyboard());
        onView((withRecyclerView(R.id.main_recyclerview).atPositionOnView(3, R.id.field))).perform(replaceText(INSURER_CODE), closeSoftKeyboard());
        VitalityActive.Navigate.clickButton(R.id.register_button);
    }

    private void logIn() {
        Log.d(TAG, "logIn()");
        Espresso.registerIdlingResources(loginIdlingResource);
    }

    private void deleteUser(String userId) {
        userId = userId.replace("@", "%40");
        NetworkModule networkModule = new NetworkModule(BuildConfig.TEST_BASE_URL, UUID.randomUUID().toString());
        Retrofit retrofit = networkModule.provideServiceGenerator(networkModule.provideHTTPClient(new LanguageProvider(resources), new AppConfigRepositoryWithFixedVersionNumber(InstrumentationRegistry.getTargetContext())),
                networkModule.provideGson(), new DefaultModule(MockJUnitRunner.getInstance().getApplication()).provideDeviceSpecificPreferences()).getRetrofit();
        DeleteUserService service = retrofit.create(DeleteUserService.class);
        Call<String> deleteCall = service.getDeleteRequest(userId);
        try {
            Response<String> response = deleteCall.execute();
            Log.d("", response.body() == null ? "" : response.body());
        } catch (Exception ignored) {

        }
    }

    protected void clickGetStartedOnOnboardingScreen() {
        Log.d(TAG, "clickGetStartedOnOnboardingScreen()");
        onView(withId(R.id.get_started_button)).perform(scrollTo()).perform(click());
    }

    protected void acceptActiveRewardsTermsAndConditions() {
        Log.d(TAG, "acceptActiveRewardsTermsAndConditions()");
        Espresso.registerIdlingResources(activeTermsIdlingResource);
        VitalityActive.Navigate.clickMoreThenAgree();
        Espresso.registerIdlingResources(activeRewardsActivatedIdlingResource);
    }

    protected void skipLinkingDevice() {
        Log.d(TAG, "skipLinkingDevice()");
        VitalityActive.Assert.onLinkDeviceAfterActiveRewardsOnboarding();
        onView(withId(R.id.btn_wd_onboarding_secondary_option)).perform(scrollTo());
        VitalityActive.Navigate.clickButton(R.id.btn_wd_onboarding_secondary_option);
    }

    protected void linkDevicesAfterActiveRewardsOnboarding() {
        Log.d(TAG, "skipLinkingDevice()");
        VitalityActive.Assert.onLinkDeviceAfterActiveRewardsOnboarding();
        VitalityActive.Navigate.clickButton(R.id.link_now_button);
    }

    protected void clickGotItOnActivatedScreen() {
        Log.d(TAG, "clickGotItOnActivatedScreen()");
        VitalityActive.Assert.onScreen(R.id.onboarding_content);
        onView(withId(R.id.onboarding_got_it_button)).perform(click());
    }

    private void clickNextOnUserPreferencesScreen() {
        Log.d(TAG, "clickNextOnUserPreferencesScreen()");
        onView(withId(R.id.button_bar_forward_button)).perform(click());
    }
}
