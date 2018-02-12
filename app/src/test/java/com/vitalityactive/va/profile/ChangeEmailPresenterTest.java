package com.vitalityactive.va.profile;


import com.vitalityactive.va.DeviceSpecificPreferences;
import com.vitalityactive.va.PartyInformationRepository;
import com.vitalityactive.va.events.EventDispatcher;
import com.vitalityactive.va.register.entity.EmailAddress;
import com.vitalityactive.va.register.entity.Password;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ChangeEmailPresenterTest {
    private static final String EMAIL_TEST = "Donette_Cason@SITtest.com";
    private static final String NEW_EMAIL = "Donette_Cason_New@SITtest.com";
    private static final String PASSWORD = "TestPass123";

    private static final String INVALID_LENGTH_EMAIL = "12345678901234567890123456789012345678901234567890" +
            "12345678901234567890123456789012345678901234567890@test.com";
    private static final String INVALID_EMAIL = "123jflk67890@t@es@t.com";

    @Mock
    private EventDispatcher eventDispatcher;
    @Mock
    private ChangeEmailPresenter.UI userInterface;
    @Mock
    private PersonalDetailsInteractor interactor;
    @Mock
    private PartyInformationRepository partyInfoRepo;

    private EmailAddress newEmailAddress = new EmailAddress(EMAIL_TEST);
    private Password authPassword = new Password(PASSWORD);

    private ChangeEmailPresenterImpl presenter;
    @Mock
    private DeviceSpecificPreferences preferences;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        presenter = new ChangeEmailPresenterImpl(eventDispatcher, partyInfoRepo, interactor, preferences);
        presenter.setUserInterface(userInterface);
        presenter.setNewEmailAddress(newEmailAddress);
        presenter.setAuthPassword(authPassword);
    }

    @Test
    public void show_change_email_screen_details_when_ui_appears() {
        when(partyInfoRepo.getEmailAddress()).thenReturn(EMAIL_TEST);
        presenter.onUserInterfaceAppeared();
        InOrder inOrder = Mockito.inOrder(eventDispatcher, partyInfoRepo, userInterface);
        inOrder.verify(eventDispatcher).addEventListener(PersonalDetailsEvent.class, presenter);
        inOrder.verify(userInterface).hideLoadingIndicator();
        inOrder.verify(partyInfoRepo).getEmailAddress();
        inOrder.verify(userInterface).showChangeEmailInfo(EMAIL_TEST);
    }

    @Test
    public void should_remove_event_listeners_when_ui_disappears() {
        presenter.onUserInterfaceDisappeared(true);
        verify(eventDispatcher).removeEventListener(PersonalDetailsEvent.class, presenter);
    }

    @Test
    public void should_attempt_to_change_email() {
        presenter.onUserTriesToChangeEmail(NEW_EMAIL);
        verify(userInterface).showLoadingIndicator();
        assertEquals(presenter.getNewEmailText(), NEW_EMAIL);
        verify(interactor).verifyEmail(newEmailAddress);
    }

    @Test
    @Ignore("Fix me")
    public void should_confirm_to_change_email() {
//        presenter.onUserConfirmsChangeEmail(NEW_EMAIL, PASSWORD);
//        verify(userInterface).showLoadingIndicator();
//        assertEquals(presenter.getNewEmailText(), NEW_EMAIL);
//        verify(interactor).changeEmail(newEmailAddress, authPassword);
    }

    @Test
    public void on_valid_email_changed() {
        presenter.onEmailTextChanged(NEW_EMAIL);
        verify(userInterface).updateDoneEnabled(presenter.isDoneEnabled());
        verify(userInterface).hideInvalidEmailAddressMessage();
    }

    @Test
    public void on_invalid_length_email_changed() {
        presenter.onEmailTextChanged(INVALID_LENGTH_EMAIL);
        verify(userInterface).updateDoneEnabled(presenter.isDoneEnabled());
        verify(userInterface).showInvalidLengthEmailAddressMessage();
    }

    @Test
    public void on_invalid_email_changed() {
        presenter.onEmailTextChanged(INVALID_EMAIL);
        verify(userInterface).updateDoneEnabled(presenter.isDoneEnabled());
        verify(userInterface).showInvalidEmailAddressMessage();
    }

    @Test
    public void on_valid_email_entered() {
        presenter.setNewEmailAddress(new EmailAddress(NEW_EMAIL));
        presenter.onNewEmailEntered();
        verify(userInterface).hideInvalidEmailAddressMessage();
    }

    @Test
    public void on_invalid_length_email_entered() {
        presenter.setNewEmailAddress(new EmailAddress(INVALID_LENGTH_EMAIL));
        presenter.onNewEmailEntered();
        verify(userInterface).showInvalidLengthEmailAddressMessage();
    }

    @Test
    public void on_invalid_email_entered() {
        presenter.setNewEmailAddress(new EmailAddress(INVALID_EMAIL));
        presenter.onNewEmailEntered();
        verify(userInterface).showInvalidEmailAddressMessage();
    }

    @Test
    public void on_event_change_email_failed(){
        presenter.onEvent(new PersonalDetailsEvent(PersonalDetailsEvent.EventType.CHANGE_EMAIL_FAILED));
        verify(userInterface).hideLoadingIndicator();
        verify(userInterface).showChangeEmailExistingError();
    }

    @Test
    public void on_event_change_email_error(){
        presenter.onEvent(new PersonalDetailsEvent(PersonalDetailsEvent.EventType.CHANGE_EMAIL_AUTH_ERROR));
        verify(userInterface).hideLoadingIndicator();
        verify(userInterface).showIncorrectPasswordError();
    }

    @Test
    public void on_event_verify_email_existing(){
        presenter.onEvent(new PersonalDetailsEvent(PersonalDetailsEvent.EventType.VERIFY_EMAIL_EXISTING));
        verify(userInterface).hideLoadingIndicator();
        verify(userInterface).showChangeEmailExistingError();
    }

    @Test
    public void on_event_verify_email_failed(){
        presenter.onEvent(new PersonalDetailsEvent(PersonalDetailsEvent.EventType.VERIFY_EMAIL_FAILED));
        verify(userInterface).hideLoadingIndicator();
        verify(userInterface).showChangeEmailExistingError();
    }

    @Test
    public void on_event_verify_email_ok(){
        presenter.onEvent(new PersonalDetailsEvent(PersonalDetailsEvent.EventType.VERIFY_EMAIL_OK));
        verify(userInterface).hideLoadingIndicator();
        verify(userInterface).showChangeEmailConfirmation();
    }
}
