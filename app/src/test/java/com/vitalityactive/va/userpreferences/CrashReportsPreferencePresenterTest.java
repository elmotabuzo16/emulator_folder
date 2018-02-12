package com.vitalityactive.va.userpreferences;

import com.vitalityactive.va.DeviceSpecificPreferences;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.verify;

public class CrashReportsPreferencePresenterTest {

    @Mock
    private DeviceSpecificPreferences deviceSpecificPreferences;
    @Mock
    private UserPreferencePresenter.UserInterface userInterface;

    private CrashReportsPreferencePresenter presenter;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        presenter = new CrashReportsPreferencePresenter(0, 0, false, 0,
                UserPreferencePresenter.Type.CrashReports, false, false, deviceSpecificPreferences);
        presenter.setUserInterface(userInterface);
    }

    @Test
    public void should_synchronize_state_when_ui_appears() throws Exception {
        presenter.onUserInterfaceAppeared();
        verify(userInterface).synchroniseOptedInState();
    }

    @Test
    public void should_enable_crash_reports_when_toggled_on() throws Exception {
        presenter.onToggle(true);
        verify(deviceSpecificPreferences).setEnableCrashReports(true);
    }

    @Test
    public void should_disable_crash_reports_when_toggled_off() throws Exception {
        presenter.onToggle(false);
        verify(deviceSpecificPreferences).setEnableCrashReports(false);
    }

    @Test
    public void should_return_crash_reports_status() throws Exception {
        presenter.isOptedIn();
        verify(deviceSpecificPreferences).isCrashReportsEnabled();
    }

}