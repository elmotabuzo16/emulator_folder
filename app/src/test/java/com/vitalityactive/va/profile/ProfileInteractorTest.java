package com.vitalityactive.va.profile;

import com.vitalityactive.va.PartyInformationRepository;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;

public class ProfileInteractorTest {

    @Mock
    private ProfileImageProvider profileImageProvider;
    @Mock
    private PartyInformationRepository partyInfoRepo;

    private ProfileInteractor interactor;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        interactor = new ProfileInteractorImpl(profileImageProvider, partyInfoRepo);
    }

    @Test
    public void should_return_user_given_name() throws Exception {
        interactor.getUserGivenName();
        verify(partyInfoRepo).getUserGivenName();
    }

    @Test
    public void should_return_user_family_name() throws Exception {
        interactor.getUserFamilyName();
        verify(partyInfoRepo).getUserFamilyName();
    }

    @Test
    public void should_return_user_initials() throws Exception {
        interactor.getUserInitials();
        verify(partyInfoRepo).getUserInitials();
    }

    @Test
    public void should_return_profile_image_availability() throws Exception {
        interactor.isProfileImageAvailable();
        verify(profileImageProvider).isProfileImageAvailable();
    }

    @Test
    public void should_return_profile_image_path() throws Exception {
        interactor.getProfileImagePath();
        verify(profileImageProvider).getProfileImagePath();
    }

    @Test
    public void should_fetch_profile_image() throws Exception {
        interactor.fetchProfileImage();
        verify(profileImageProvider).fetchProfileImage();
    }

}