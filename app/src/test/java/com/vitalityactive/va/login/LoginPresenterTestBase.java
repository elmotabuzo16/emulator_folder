package com.vitalityactive.va.login;

import com.vitalityactive.va.DeviceSpecificPreferences;
import com.vitalityactive.va.MainThreadScheduler;
import com.vitalityactive.va.SameThreadScheduler;
import com.vitalityactive.va.events.EventDispatcher;

import org.junit.Before;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.when;

class LoginPresenterTestBase {
    LoginPresenterImpl loginPresenter;
    protected EventDispatcher eventDispatcher;

    @Mock
    LoginInteractor mockLoginInteractor;

    @Mock
    LoginPresenter.UserInterface mockUserInterface;
    @Mock
    MainThreadScheduler mockMainThreadScheduler;
    @Mock
    DeviceSpecificPreferences mockDeviceSpecificPreferences;

    SameThreadScheduler scheduler;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        eventDispatcher = new EventDispatcher();
        scheduler = new SameThreadScheduler();
        when(mockDeviceSpecificPreferences.getRememberedUsername()).thenReturn("");
        loginPresenter = new LoginPresenterImpl(eventDispatcher, mockLoginInteractor, scheduler, mockDeviceSpecificPreferences);
        loginPresenter.setUserInterface(mockUserInterface);
        when(mockLoginInteractor.getLoginRequestResult()).thenReturn(LoginInteractor.LoginRequestResult.NONE);
    }
}
