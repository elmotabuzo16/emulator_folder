package com.vitalityactive.va.userpreferences;

import com.vitalityactive.va.MainThreadScheduler;
import com.vitalityactive.va.PartyInformationRepository;
import com.vitalityactive.va.events.EventDispatcher;
import com.vitalityactive.va.networking.RequestResult;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class EmailPreferencePresenterTest {

    @Mock
    private EmailPreferenceServiceClient emailPreferenceSvc;
    @Mock
    private PartyInformationRepository partyInfoRepo;
    @Mock
    private EventDispatcher eventDispatcher;
    @Mock
    private MainThreadScheduler scheduler;
    @Mock
    private UserPreferencePresenter.UserInterface userInterface;

    private EmailPreferencePresenter presenter;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        presenter = new EmailPreferencePresenter(0, 0, false, 0,
                UserPreferencePresenter.Type.Email, true, false,
                emailPreferenceSvc, eventDispatcher, scheduler, partyInfoRepo);
        presenter.setUserInterface(userInterface);
    }

    @Test
    public void when_ui_appears_then_should_add_event_listener_and_synchronise_state() throws Exception {
        when(emailPreferenceSvc.isEmailToggleRequestInProgress()).thenReturn(false);

        presenter.onUserInterfaceAppeared();

        InOrder inOrder = Mockito.inOrder(eventDispatcher, emailPreferenceSvc, userInterface);
        inOrder.verify(eventDispatcher).addEventListener(EmailPreferenceToggleCompletedEvent.class, presenter);
        inOrder.verify(userInterface).synchroniseOptedInState();
        inOrder.verify(emailPreferenceSvc).isEmailToggleRequestInProgress();
        inOrder.verify(userInterface).enableSwitch();
    }

    @Test
    public void when_ui_appears_then_should_add_event_listener_and_synchronise_state_when_toggle_in_progress() throws Exception {
        when(emailPreferenceSvc.isEmailToggleRequestInProgress()).thenReturn(true);

        presenter.onUserInterfaceAppeared();

        InOrder inOrder = Mockito.inOrder(eventDispatcher, emailPreferenceSvc, userInterface);
        inOrder.verify(eventDispatcher).addEventListener(EmailPreferenceToggleCompletedEvent.class, presenter);
        inOrder.verify(userInterface).synchroniseOptedInState();
        inOrder.verify(emailPreferenceSvc).isEmailToggleRequestInProgress();
        inOrder.verify(userInterface).disableSwitch();
    }

    @Test
    public void should_remove_event_listener_when_ui_disappears() throws Exception {
        presenter.onUserInterfaceDisappeared(false);
        verify(eventDispatcher).removeEventListener(EmailPreferenceToggleCompletedEvent.class, presenter);
    }

    @Test
    public void should_opt_out_when_toggled_off() throws Exception {
        presenter.onToggle(false);
        verify(userInterface).disableSwitch();
        verify(emailPreferenceSvc).optOutOfEmailCommunication();
    }

    @Test
    public void should_opt_in_when_toggled_in() throws Exception {
        presenter.onToggle(true);
        verify(userInterface).disableSwitch();
        verify(emailPreferenceSvc).optInToEmailCommunication();
    }

    @Test
    public void should_schedule_preference_toggle_completion_for_handling() throws Exception {
        presenter.onEvent(mock(EmailPreferenceToggleCompletedEvent.class));
        verify(scheduler).schedule(any(Runnable.class));
    }

    @Test
    public void should_persist_status_when_preference_toggle_completes_successfully() throws Exception {
        when(emailPreferenceSvc.getEmailPreferenceRequestResult()).thenReturn(RequestResult.SUCCESSFUL);
        when(partyInfoRepo.isOptedInToEmailCommunication()).thenReturn(false);

        presenter.handleToggleCompletion();

        InOrder inOrder = Mockito.inOrder(emailPreferenceSvc, partyInfoRepo, userInterface);
        inOrder.verify(emailPreferenceSvc).getEmailPreferenceRequestResult();
        inOrder.verify(partyInfoRepo).isOptedInToEmailCommunication();
        inOrder.verify(partyInfoRepo).setOptedInToEmailCommunication(true);
        inOrder.verify(userInterface).synchroniseOptedInState();
        inOrder.verify(userInterface).enableSwitch();
    }

    @Test
    public void should_show_error_when_preference_toggle_fails() throws Exception {
        when(emailPreferenceSvc.getEmailPreferenceRequestResult()).thenReturn(RequestResult.GENERIC_ERROR);

        presenter.handleToggleCompletion();

        InOrder inOrder = Mockito.inOrder(emailPreferenceSvc, partyInfoRepo, userInterface);
        inOrder.verify(emailPreferenceSvc).getEmailPreferenceRequestResult();
        inOrder.verify(userInterface).showErrorMessage();
        inOrder.verify(userInterface).synchroniseOptedInState();
        inOrder.verify(userInterface).enableSwitch();
    }

    @Test
    public void should_return_toggle_status() throws Exception {
        when(partyInfoRepo.isOptedInToEmailCommunication()).thenReturn(true);
        assertTrue(presenter.isOptedIn());
        verify(partyInfoRepo).isOptedInToEmailCommunication();
    }

}