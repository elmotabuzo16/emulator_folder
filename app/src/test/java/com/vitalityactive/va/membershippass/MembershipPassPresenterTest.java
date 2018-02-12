package com.vitalityactive.va.membershippass;

import android.util.Log;

import com.vitalityactive.va.MainThreadScheduler;
import com.vitalityactive.va.PartyInformationRepository;
import com.vitalityactive.va.dto.MembershipPassDTO;
import com.vitalityactive.va.dto.PersonalDetailsDTO;
import com.vitalityactive.va.events.EventDispatcher;
import com.vitalityactive.va.home.repository.HomeScreenCardSectionRepository;
import com.vitalityactive.va.membershippass.dto.MembershipPassRepository;
import com.vitalityactive.va.membershippass.interactor.MembershipPassInteractor;
import com.vitalityactive.va.membershippass.presenter.MembershipPassPresenter;
import com.vitalityactive.va.membershippass.presenter.MembershipPassPresenterImpl;
import com.vitalityactive.va.persistence.models.Membership;
import com.vitalityactive.va.profile.ProfileImageAvailableEvent;
import com.vitalityactive.va.profile.ProfileImageProvider;
import com.vitalityactive.va.utilities.date.LocalDate;
import com.vitalityactive.va.utilities.date.formatting.DateFormattingUtilities;
import com.vitalityactive.va.vitalitystatus.repository.VitalityStatusRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by christian.j.p.capin on 11/27/2017.
 */
public class MembershipPassPresenterTest {

    @Mock
    private EventDispatcher eventDispatcher;
    @Mock
    private DateFormattingUtilities dateFormatUtil;
    @Mock
    private ProfileImageProvider profileImageProvider;
    @Mock
    private MembershipPassPresenter.UI userInterface;
    @Mock
    private MembershipPassInteractor interactor;
    @Mock
    private VitalityStatusRepository statusRepository;
    @Mock
    private HomeScreenCardSectionRepository homeScreenCardSectionRepository;
    @Mock
    private PartyInformationRepository partyInfoRepo;
    @Mock
    private MembershipPassRepository membershipPassRepository;
    @Mock
    private MainThreadScheduler scheduler;

    private MembershipPassPresenterImpl presenter;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        presenter = new MembershipPassPresenterImpl(interactor,eventDispatcher, profileImageProvider,
                                            partyInfoRepo,statusRepository,homeScreenCardSectionRepository,
                                            dateFormatUtil,membershipPassRepository,scheduler);
        presenter.setUserInterface(userInterface);
    }

    @Test
    public void should_show_membership_details_as_created() throws Exception {
                presenter.onUserInterfaceCreated(true);
        InOrder inOrder = Mockito.inOrder(eventDispatcher,interactor);
        inOrder.verify(eventDispatcher).addEventListener(ProfileImageAvailableEvent.class, presenter);
        //inOrder.verify(eventDispatcher).addEventListener(MembershipPassEvent.class, presenter);
        inOrder.verify(interactor).initialize();
    }

    @Test
    public void should_show_membership_pass_and_profile_image_when_available_as_ui_appears() throws Exception {

        when(profileImageProvider.isProfileImageAvailable()).thenReturn(true);
        String imagePath = "/profile/image/path";
        when(profileImageProvider.getProfileImagePath()).thenReturn(imagePath);

        presenter.onUserInterfaceAppeared();
        InOrder inOrder = Mockito.inOrder(partyInfoRepo, homeScreenCardSectionRepository, statusRepository,membershipPassRepository,userInterface);
        inOrder.verify(partyInfoRepo).getPersonalDetails();
        inOrder.verify(homeScreenCardSectionRepository).getHomeCards();
        inOrder.verify(statusRepository).getVitalityStatus();
        inOrder.verify(membershipPassRepository).getMembershipPass();
        inOrder.verify(userInterface).showProfileImage(imagePath);

    }


    @Test
    public void should_remove_event_listener_when_ui_disappears() throws Exception {
        presenter.onUserInterfaceDisappeared(true);
        verify(eventDispatcher).removeEventListener(ProfileImageAvailableEvent.class, presenter);
        verify(eventDispatcher).removeEventListener(MembershipPassEvent.class, presenter.membershipPassEventEventListener);
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
