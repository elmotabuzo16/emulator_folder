package com.vitalityactive.va.profile;

import com.vitalityactive.va.PartyInformationRepository;
import com.vitalityactive.va.appconfig.AppConfigRepository;
import com.vitalityactive.va.dto.PersonalDetailsDTO;
import com.vitalityactive.va.events.EventDispatcher;
import com.vitalityactive.va.utilities.date.LocalDate;
import com.vitalityactive.va.utilities.date.formatting.DateFormattingUtilities;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class PersonalDetailsPresenterTest {

    private static final String USER_GIVEN_NAME = "Sam";
    private static final String USER_FAMILY_NAME = "Cooper";
    private static final String USER_GENDER = "0";
    private static final String USER_INITIALS = "S";
    private static final String USER_DOB = "1997/6/5";
    private static final int AGE = 20;

    @Mock
    private EventDispatcher eventDispatcher;
    @Mock
    private DateFormattingUtilities dateFormatUtil;
    @Mock
    private ProfileImageProvider profileImageProvider;
    @Mock
    private PartyInformationRepository partyInfoRepo;
    @Mock
    private AppConfigRepository appConfigRepo;
    @Mock
    private PersonalDetailsPresenter.UI userInterface;
    @Mock
    private PersonalDetailsInteractor interactor;
    @Mock
    private AppConfigRepository mockAppConfigRepository;
  
    private PersonalDetailsPresenterImpl presenter;


    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        presenter = new PersonalDetailsPresenterImpl(eventDispatcher, dateFormatUtil, profileImageProvider, partyInfoRepo, interactor, mockAppConfigRepository);
        presenter.setUserInterface(userInterface);
    }

    @Test
    public void should_show_personal_details_as_ui_appears() throws Exception {
        LocalDate dateOfBirth = mock(LocalDate.class);
        PersonalDetailsDTO details = new PersonalDetailsDTO(USER_GIVEN_NAME, USER_FAMILY_NAME, dateOfBirth, USER_GENDER, USER_INITIALS, USER_DOB, 0);
        when(partyInfoRepo.getPersonalDetails()).thenReturn(details);
        when(profileImageProvider.isProfileImageAvailable()).thenReturn(false);

        presenter.onUserInterfaceAppeared();

        InOrder inOrder = Mockito.inOrder(eventDispatcher, dateFormatUtil, partyInfoRepo, userInterface, profileImageProvider);
        inOrder.verify(eventDispatcher).addEventListener(ProfileImageAvailableEvent.class, presenter);
        inOrder.verify(partyInfoRepo).getPersonalDetails();
        inOrder.verify(partyInfoRepo).getMobileNumber();
        inOrder.verify(partyInfoRepo).getEmailAddress();
        inOrder.verify(dateFormatUtil).formatDateMonthYear(dateOfBirth);
        inOrder.verify(profileImageProvider).isProfileImageAvailable();
        inOrder.verify(profileImageProvider).fetchProfileImage();
        inOrder.verify(userInterface).showProfileInitials(USER_INITIALS);
    }

    @Test
    public void should_show_personal_details_and_profile_image_when_available_as_ui_appears() throws Exception {
        LocalDate dateOfBirth = mock(LocalDate.class);
        PersonalDetailsDTO details = new PersonalDetailsDTO(USER_GIVEN_NAME, USER_FAMILY_NAME, dateOfBirth, USER_GENDER, USER_INITIALS, USER_DOB, AGE);
        when(partyInfoRepo.getPersonalDetails()).thenReturn(details);
        when(profileImageProvider.isProfileImageAvailable()).thenReturn(true);
        String imagePath = "/profile/image/path";
        when(profileImageProvider.getProfileImagePath()).thenReturn(imagePath);

        presenter.onUserInterfaceAppeared();

        InOrder inOrder = Mockito.inOrder(eventDispatcher, dateFormatUtil, partyInfoRepo, userInterface, profileImageProvider);
        inOrder.verify(eventDispatcher).addEventListener(ProfileImageAvailableEvent.class, presenter);
        inOrder.verify(partyInfoRepo).getPersonalDetails();
        inOrder.verify(partyInfoRepo).getMobileNumber();
        inOrder.verify(partyInfoRepo).getEmailAddress();
        inOrder.verify(dateFormatUtil).formatDateMonthYear(dateOfBirth);
        inOrder.verify(profileImageProvider).isProfileImageAvailable();
        inOrder.verify(userInterface).showProfileImage(imagePath);
    }

    @Test
    public void should_remove_event_listener_when_ui_disappears() throws Exception {
        presenter.onUserInterfaceDisappeared(true);
        verify(eventDispatcher).removeEventListener(ProfileImageAvailableEvent.class, presenter);
    }

    @Test
    public void should_show_profile_image_when_it_becomes_available() throws Exception {
        when(profileImageProvider.isProfileImageAvailable()).thenReturn(true);
        String imagePath = "/profile/image/path";
        when(profileImageProvider.getProfileImagePath()).thenReturn(imagePath);

        presenter.onEvent(mock(ProfileImageAvailableEvent.class));

        verify(userInterface).showProfileImage(imagePath);
    }

}