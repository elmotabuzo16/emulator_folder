package com.vitalityactive.va.userpreferences;

import com.vitalityactive.va.DeviceSpecificPreferences;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.verify;

public class AnalyticsPreferencePresenterTest {

    @Mock
    private DeviceSpecificPreferences deviceSpecificPreferences;
    @Mock
    private UserPreferencePresenter.UserInterface userInterface;

    private AnalyticsPreferencePresenter presenter;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        presenter = new AnalyticsPreferencePresenter(0, 0, false, 0,
                UserPreferencePresenter.Type.Analytics, false, false, deviceSpecificPreferences);
        presenter.setUserInterface(userInterface);
    }

    @Test
    public void should_synchronize_state_when_ui_appears() throws Exception {
        presenter.onUserInterfaceAppeared();
        verify(userInterface).synchroniseOptedInState();
    }

    @Test
    public void should_enable_analytics_when_toggled_on() throws Exception {
        presenter.onToggle(true);
        verify(deviceSpecificPreferences).setAnalytics(true);
    }

    @Test
    public void should_disable_analytics_when_toggled_off() throws Exception {
        presenter.onToggle(false);
        verify(deviceSpecificPreferences).setAnalytics(false);
    }

    @Test
    public void should_return_analytics_status() throws Exception {
        presenter.isOptedIn();
        verify(deviceSpecificPreferences).isAnalyticsEnabled();
    }

}