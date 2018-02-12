package com.vitalityactive.va.userpreferences;

import com.vitalityactive.va.DeviceSpecificPreferences;
import com.vitalityactive.va.PartyInformationRepository;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class RememberMePreferencePresenterTest {

    @Mock
    private PartyInformationRepository partyInfoRepo;
    @Mock
    private DeviceSpecificPreferences deviceSpecificPreferences;
    @Mock
    private UserPreferencePresenter.UserInterface userInterface;

    private RememberMePreferencePresenter presenter;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        presenter = new RememberMePreferencePresenter(0, 0, false, 0,
                UserPreferencePresenter.Type.RememberMe, false, false, partyInfoRepo, deviceSpecificPreferences);
        presenter.setUserInterface(userInterface);
    }

    @Test
    public void should_synchronize_state_when_ui_appears() throws Exception {
        presenter.onUserInterfaceAppeared();
        verify(userInterface).synchroniseOptedInState();
    }

    @Test
    public void should_remember_username_when_toggled_on() throws Exception {
        String username = "user@mail.com";
        when(partyInfoRepo.getUsername()).thenReturn(username);

        presenter.onToggle(true);

        InOrder inOrder = Mockito.inOrder(deviceSpecificPreferences, partyInfoRepo);
        inOrder.verify(deviceSpecificPreferences).setRememberMe(true);
        inOrder.verify(partyInfoRepo).getUsername();
        inOrder.verify(deviceSpecificPreferences).setRememberedUsername(username);
    }

    @Test
    public void should_forget_username_when_toggled_off() throws Exception {
        presenter.onToggle(false);
        verify(deviceSpecificPreferences).setRememberMe(false);
        verify(deviceSpecificPreferences).clearRememberedUsername();
    }

    @Test
    public void should_return_remember_status() throws Exception {
        when(deviceSpecificPreferences.isRememberMeOn()).thenReturn(true);
        assertTrue(presenter.isOptedIn());
        verify(deviceSpecificPreferences).isRememberMeOn();
    }

}