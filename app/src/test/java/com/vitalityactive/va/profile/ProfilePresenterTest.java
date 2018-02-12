package com.vitalityactive.va.profile;

import com.vitalityactive.va.events.EventDispatcher;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ProfilePresenterTest {

    @Mock
    private ProfileInteractor interactor;
    @Mock
    private EventDispatcher eventDispatcher;
    @Mock
    private ProfilePresenter.UI userInterface;

    private ProfilePresenterImpl presenter;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        presenter = new ProfilePresenterImpl(interactor, eventDispatcher);
        presenter.setUserInterface(userInterface);
    }

    @Test
    public void when_ui_appears_then_should_show_profile_image_if_available() throws Exception {
        String givenName = "Unit";
        String familyName = "Test";
        String imagePath = "/profile/image/path";
        when(interactor.getUserGivenName()).thenReturn(givenName);
        when(interactor.getUserFamilyName()).thenReturn(familyName);
        when(interactor.isProfileImageAvailable()).thenReturn(true);
        when(interactor.getProfileImagePath()).thenReturn(imagePath);

        presenter.onUserInterfaceAppeared();

        InOrder inOrder = Mockito.inOrder(eventDispatcher, interactor, userInterface);
        inOrder.verify(eventDispatcher).addEventListener(ProfileImageAvailableEvent.class, presenter);
        inOrder.verify(userInterface).showProfileName(givenName, familyName);
        inOrder.verify(interactor).isProfileImageAvailable();
        inOrder.verify(interactor).getProfileImagePath();
        inOrder.verify(userInterface).showProfileImage(imagePath);
    }

    @Test
    public void when_ui_appears_then_should_show_user_initials_if_profile_image_not_available() throws Exception {
        String givenName = "Unit";
        String familyName = "Test";
        String userInitials = "U";
        when(interactor.getUserGivenName()).thenReturn(givenName);
        when(interactor.getUserFamilyName()).thenReturn(familyName);
        when(interactor.isProfileImageAvailable()).thenReturn(false);
        when(interactor.getUserInitials()).thenReturn(userInitials);

        presenter.onUserInterfaceAppeared();

        InOrder inOrder = Mockito.inOrder(eventDispatcher, interactor, userInterface);
        inOrder.verify(eventDispatcher).addEventListener(ProfileImageAvailableEvent.class, presenter);
        inOrder.verify(userInterface).showProfileName(givenName, familyName);
        inOrder.verify(interactor).isProfileImageAvailable();
        inOrder.verify(interactor).getUserInitials();
        inOrder.verify(userInterface).showProfileInitials(userInitials);
    }

    @Test
    public void when_ui_disappears_then_should_remove_event_listener() throws Exception {
        presenter.onUserInterfaceDisappeared(true);
        verify(eventDispatcher).removeEventListener(ProfileImageAvailableEvent.class, presenter);
    }

    @Test
    public void should_show_profile_image_when_it_becomes_available() throws Exception {
        String imagePath = "/profile/image/path";
        when(interactor.isProfileImageAvailable()).thenReturn(true);
        when(interactor.getProfileImagePath()).thenReturn(imagePath);

        presenter.onEvent(mock(ProfileImageAvailableEvent.class));

        InOrder inOrder = Mockito.inOrder(interactor, userInterface);
        inOrder.verify(interactor).isProfileImageAvailable();
        inOrder.verify(interactor).getProfileImagePath();
        inOrder.verify(userInterface).showProfileImage(imagePath);
    }
}