package com.vitalityactive.va;

import android.app.Activity;

import com.vitalityactive.va.appconfig.AppConfigRepository;
import com.vitalityactive.va.events.EventDispatcher;
import com.vitalityactive.va.launch.Navigator;
import com.vitalityactive.va.login.LoginCredentials;
import com.vitalityactive.va.login.LoginPresenterImpl;
import com.vitalityactive.va.pushnotification.InAppPreferences;
import com.vitalityactive.va.register.interactor.RegistrationInteractorImpl;
import com.vitalityactive.va.termsandconditions.GeneralTermsAndConditionsInstructionRepository;
import com.vitalityactive.va.wellnessdevices.landing.repository.DeviceListRepository;

import org.junit.Before;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class NavigationTestBase {
    protected NavigationCoordinator navigationCoordinator;

    @Mock
    protected GeneralTermsAndConditionsInstructionRepository mockTermsAndConditionsInstructionRepository;

    @Mock
    private RegistrationInteractorImpl mockRegistrationInteractor;

    @Mock
    private LoginPresenterImpl mockLoginPresenter;

    @Mock
    protected LoginCredentials mockLoginCredentials;

    @Mock
    protected Navigator mockNavigator;

    @Mock
    protected Activity mockActivity;

    @Mock
    private InsurerConfigurationRepository mockInsurerConfigurationRepository;

    @Mock
    protected DeviceSpecificPreferences mockPreferences;

    @Mock
    private EventDispatcher mockEventDispatcher;

    @Mock
    private VitalityActiveApplication mockVitalityActiveApplication;

    @Mock
    private AppConfigRepository mockAppConfigRepository;

    @Mock
    private DeviceListRepository mockDeviceListRepository;

    @Mock
    private InAppPreferences inAppPreferences;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        navigationCoordinator = new NavigationCoordinator(mockNavigator,
                mockTermsAndConditionsInstructionRepository,
                mockRegistrationInteractor,
                mockLoginPresenter,
                mockInsurerConfigurationRepository,
                mockPreferences,
                mockEventDispatcher,
                mockVitalityActiveApplication,
                mockAppConfigRepository,
                mockDeviceListRepository,
                inAppPreferences
                );
    }
}
