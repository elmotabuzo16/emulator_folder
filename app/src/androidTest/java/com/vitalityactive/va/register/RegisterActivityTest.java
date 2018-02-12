package com.vitalityactive.va.register;

import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.rule.ActivityTestRule;
import android.util.Log;

import com.google.gson.Gson;
import com.vitalityactive.va.AppConfigRepositoryWithFixedVersionNumber;
import com.vitalityactive.va.BaseAndroidTest;
import com.vitalityactive.va.BuildConfig;
import com.vitalityactive.va.DeviceSpecificPreferences;
import com.vitalityactive.va.LanguageProvider;
import com.vitalityactive.va.MockJUnitAndCucumberRunner;
import com.vitalityactive.va.MockJUnitRunner;
import com.vitalityactive.va.R;
import com.vitalityactive.va.dependencyinjection.DefaultModule;
import com.vitalityactive.va.dependencyinjection.NetworkModule;
import com.vitalityactive.va.networking.ServiceGenerator;
import com.vitalityactive.va.networking.model.RegistrationServiceRequest;
import com.vitalityactive.va.register.interactor.RegistrationInteractor;
import com.vitalityactive.va.register.view.RegisterActivity;
import com.vitalityactive.va.testutilities.ServiceIdlingResource;
import com.vitalityactive.va.testutilities.VitalityActive;
import com.vitalityactive.va.testutilities.dependencyinjection.PersistenceWithInMemoryRealmModule;
import com.vitalityactive.va.testutilities.servicesetup.DeleteUserService;

import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.UUID;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.mock.BehaviorDelegate;
import retrofit2.mock.Calls;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.vitalityactive.va.testutilities.VitalityActive.Matcher.withRecyclerView;
import static com.vitalityactive.va.testutilities.VitalityActive.Navigate.clickButton;
import static com.vitalityactive.va.testutilities.VitalityActive.Navigate.clickOKButton;

public class RegisterActivityTest extends BaseAndroidTest {
    private static final String USERNAME = "henri@glucode.com";
    private static final String PASSWORD = "Password1";
    private static final String INSURER_CODE = "vv1BcBResJEoIFfXR/QNOQ==:BftdCtzdKLTUWmiLlE13eg==";

    @Rule
    public ActivityTestRule<RegisterActivity> activityTestRule = new ActivityTestRule<>(RegisterActivity.class, false, false);

    @Override
    public void setUp() {
        super.setUp();
        persistenceModule = new PersistenceWithInMemoryRealmModule(MockJUnitAndCucumberRunner.getInstance().getTargetContext());
    }

    @Test
    @Ignore("NoMatchingViewException")
    public void user_is_navigated_to_the_login_screen_when_registration_completes_successfully() {
        setUpRealNetworkModuleWithResponseCode(201);

        register();

        VitalityActive.Assert.onLoginScreen();
    }

    @Test
    public void error_alert_is_shown_when_registration_fails_because_of_a_connection_error() {
        setUpMockNetworkModuleAndLaunchActivity(new UnknownHostException("Something went wrong"));

        register();

        onView(withText(resources.getString(R.string.connectivity_error_alert_title_44))).check(matches(isDisplayed()));
        onView(withText(resources.getString(R.string.connectivity_error_alert_message_45))).check(matches(isDisplayed()));
        onView(withText(resources.getString(R.string.cancel_button_title_24))).check(matches(isDisplayed()));
        onView(withText(resources.getString(R.string.try_again_button_title_43))).check(matches(isDisplayed()));
    }

    @Test
    public void error_alert_is_shown_when_registration_fails_because_of_a_generic_error() {
        setUpMockNetworkModuleAndLaunchActivity(new IOException("Something went wrong"));

        register();

        onView(withText(resources.getString(R.string.registration_unable_to_register_alert_title_41))).check(matches(isDisplayed()));
        onView(withText(resources.getString(R.string.registration_unable_to_register_alert_message_42))).check(matches(isDisplayed()));
        onView(withText(resources.getString(R.string.cancel_button_title_24))).check(matches(isDisplayed()));
        onView(withText(resources.getString(R.string.try_again_button_title_43))).check(matches(isDisplayed()));
    }

    @Test
    public void error_alert_is_shown_when_registration_fails_because_of_a_generic_error_response() {
        setUpRealNetworkModuleWithResponseCode(500);

        register();

        onView(withText(resources.getString(R.string.registration_unable_to_register_alert_title_41))).check(matches(isDisplayed()));
        onView(withText(resources.getString(R.string.registration_unable_to_register_alert_message_42))).check(matches(isDisplayed()));
        onView(withText(resources.getString(R.string.cancel_button_title_24))).check(matches(isDisplayed()));
        onView(withText(resources.getString(R.string.try_again_button_title_43))).check(matches(isDisplayed()));
    }

    @Test
    public void error_alert_is_shown_when_registration_fails_because_of_an_incorrect_email_address_or_insurer_code() {
        setUpRealNetworkModuleWithResponseCode(400);

        register();

        onView(withText(resources.getString(R.string.registration_invalid_email_or_registration_code_alert_message_39))).check(matches(isDisplayed()));
        onView(withText(resources.getString(R.string.ok_button_title_40))).check(matches(isDisplayed()));
    }

    @Test
    public void error_alert_is_shown_when_registration_fails_because_the_user_is_already_registered() {
        setUpRealNetworkModuleWithResponseCode(409);

        register();

        onView(withText(resources.getString(R.string.registration_unable_to_register_alert_message_46))).check(matches(isDisplayed()));
        onView(withText(resources.getString(R.string.ok_button_title_40))).check(matches(isDisplayed()));
    }

    @Test
    @Ignore("NoMatchingViewException")
    public void registration_service_integration_works() throws Exception {
        setUpNetworkModuleAndLaunchActivity(BuildConfig.TEST_BASE_URL);

        ServiceIdlingResource<RegistrationInteractor.RegistrationSucceededEvent, RegistrationInteractor.RegistrationFailedEvent> registrationServiceIdlingResource = new ServiceIdlingResource<>(RegistrationInteractor.RegistrationSucceededEvent.class, RegistrationInteractor.RegistrationFailedEvent.class);

        deleteUser(USERNAME);

        register();

        Espresso.registerIdlingResources(registrationServiceIdlingResource);

        VitalityActive.Assert.onLoginScreen();

        deleteUser(USERNAME);

        Espresso.unregisterIdlingResources(registrationServiceIdlingResource);
        registrationServiceIdlingResource.close();
    }

    @Test
    public void tapping_ok_on_alerts_without_try_again_buttons_does_not_attempt_to_register_again() {
        setUpRealNetworkModuleWithResponseCode(400);

        register();

        clickOKButton();

        VitalityActive.Assert.isNotRegistering();
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

    private void setUpMockNetworkModuleAndLaunchActivity(IOException exception) {
        setUpNetworkModuleAndLaunchActivity(getMockNetworkModule(exception), new DefaultModule(MockJUnitRunner.getInstance().getApplication()));
    }

    private NetworkModule getMockNetworkModule(IOException exception) {
        return new MockNetworkModule("http://www.nosuchluck.com/", exception);
    }

    private void register() {
        fillFields();
        clickRegisterButton();
    }

    private void clickRegisterButton() {
        clickButton(R.id.register_button);
    }

    private void fillFields() {
        onView((withRecyclerView(R.id.main_recyclerview).atPositionOnView(0, R.id.field))).perform(replaceText(USERNAME), closeSoftKeyboard());
        onView((withRecyclerView(R.id.main_recyclerview).atPositionOnView(1, R.id.field))).perform(replaceText(PASSWORD), closeSoftKeyboard());
        onView((withRecyclerView(R.id.main_recyclerview).atPositionOnView(2, R.id.field))).perform(replaceText(PASSWORD), closeSoftKeyboard());
        onView((withRecyclerView(R.id.main_recyclerview).atPositionOnView(3, R.id.field))).perform(replaceText(INSURER_CODE), closeSoftKeyboard());
    }

    @Override
    protected void launchActivity() {
        activityTestRule.launchActivity(new Intent());
    }

    private class MockServiceThrowingAnException implements RegistrationService {
        final BehaviorDelegate<RegistrationService> delegate = mockRetrofit.create(RegistrationService.class);
        private final IOException exception;

        MockServiceThrowingAnException(IOException exception) {
            this.exception = exception;
        }

        @Override
        public Call<String> getRegistrationRequest(RegistrationServiceRequest registrationServiceRequest, String authorization) {
            return delegate.returning(Calls.failure(exception)).getRegistrationRequest(registrationServiceRequest, authorization);
        }
    }

    private class MockNetworkModule extends NetworkModule {

        String baseYouAreEl;
        private IOException exception;

        MockNetworkModule(String baseUrl, IOException exception) {
            super(baseUrl, UUID.randomUUID().toString());
            baseYouAreEl = baseUrl;
            this.exception = exception;
        }

        @Override
        @SuppressWarnings("unchecked")
        public ServiceGenerator provideServiceGenerator(OkHttpClient httpClient, Gson gson, DeviceSpecificPreferences preferences) {
            return new ServiceGenerator(baseYouAreEl, httpClient, gson, preferences) {
                @Override
                public <ServiceClass> ServiceClass create(Class<ServiceClass> serviceClass) {
                    return (ServiceClass) new MockServiceThrowingAnException(exception);
                }
            };
        }
    }
}
