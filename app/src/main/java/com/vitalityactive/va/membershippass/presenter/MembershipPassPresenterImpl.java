package com.vitalityactive.va.membershippass.presenter;


import android.util.Log;

import com.vitalityactive.va.MainThreadScheduler;
import com.vitalityactive.va.PartyInformationRepository;
import com.vitalityactive.va.dto.HomeCardDTO;
import com.vitalityactive.va.dto.MembershipPassDTO;
import com.vitalityactive.va.dto.PersonalDetailsDTO;
import com.vitalityactive.va.events.EventDispatcher;
import com.vitalityactive.va.events.EventListener;
import com.vitalityactive.va.home.repository.HomeScreenCardSectionRepository;
import com.vitalityactive.va.membershippass.MembershipPassEvent;
import com.vitalityactive.va.membershippass.dto.MembershipPassRepository;
import com.vitalityactive.va.membershippass.interactor.MembershipPassInteractor;
import com.vitalityactive.va.membershippass.model.MembershipPassResponse;
import com.vitalityactive.va.profile.ProfileImageAvailableEvent;
import com.vitalityactive.va.profile.ProfileImageProvider;
import com.vitalityactive.va.utilities.date.LocalDate;
import com.vitalityactive.va.utilities.date.formatting.DateFormattingUtilities;
import com.vitalityactive.va.vitalitystatus.VitalityStatusDTO;
import com.vitalityactive.va.vitalitystatus.repository.VitalityStatusRepository;

import java.util.List;

/**
 * Created by christian.j.p.capin on 11/14/2017.
 */

public class MembershipPassPresenterImpl
        implements MembershipPassPresenter, EventListener<ProfileImageAvailableEvent> {

    private final EventDispatcher eventDispatcher;
    private final ProfileImageProvider profileImageProvider;
    private final PartyInformationRepository partyInfoRepo;

    private UI userInterface;
    private MembershipPassInteractor interactor;
    private final VitalityStatusRepository statusRepository;
    private DateFormattingUtilities dateFormatUtil;
    private MembershipPassRepository membershipPassRepository;
    private HomeScreenCardSectionRepository homeScreenCardSectionRepository;
    private PersonalDetailsDTO personalDetails;
    private MembershipPassDTO membershipDetails;
    private List<HomeCardDTO> homeCardDTO;
    private VitalityStatusDTO vitalityStatusDTO;
    private MainThreadScheduler scheduler;
    private MembershipPassResponse membershipPassResponse;

    public MembershipPassPresenterImpl(MembershipPassInteractor interactor,
                                       EventDispatcher eventDispatcher,
                                       ProfileImageProvider profileImageProvider,
                                       PartyInformationRepository partyInfoRepo,
                                       VitalityStatusRepository statusRepository,
                                       HomeScreenCardSectionRepository homeScreenCardSectionRepository,
                                       DateFormattingUtilities dateFormatUtil,
                                       MembershipPassRepository membershipPassRepository,
                                       MainThreadScheduler scheduler) {
        this.interactor = interactor;
        this.eventDispatcher = eventDispatcher;
        this.profileImageProvider = profileImageProvider;
        this.partyInfoRepo = partyInfoRepo;
        this.statusRepository = statusRepository;
        this.dateFormatUtil = dateFormatUtil;
        this.membershipPassRepository = membershipPassRepository;
        this.homeScreenCardSectionRepository = homeScreenCardSectionRepository;
        this.scheduler= scheduler;
    }

    @Override
    public void onUserInterfaceCreated(boolean isNewNavigation) {
        eventDispatcher.addEventListener(ProfileImageAvailableEvent.class, this);
        eventDispatcher.addEventListener(MembershipPassEvent.class, membershipPassEventEventListener);

        interactor.initialize();

    }

    @Override
    public void onUserInterfaceAppeared() {


        personalDetails = partyInfoRepo.getPersonalDetails();
        homeCardDTO = homeScreenCardSectionRepository.getHomeCards();
        vitalityStatusDTO = statusRepository.getVitalityStatus();
        membershipDetails = membershipPassRepository.getMembershipPass();


        initMembershipPassInitial();

        if (profileImageProvider.isProfileImageAvailable()) {
            userInterface.showProfileImage(profileImageProvider.getProfileImagePath());
        } else {
            profileImageProvider.fetchProfileImage();
           // userInterface.showProfileInitials(personalDetails.getInitials());
            userInterface.showProfileInitials("D");
        }

    }

    @Override
    public void onUserInterfaceDisappeared(boolean isFinishing) {
        eventDispatcher.removeEventListener(ProfileImageAvailableEvent.class, this);
        eventDispatcher.removeEventListener(MembershipPassEvent.class, membershipPassEventEventListener);
    }

    @Override
    public void onUserInterfaceDestroyed() {
        userInterface.activityDestroyed();
    }

    @Override
    public void setUserInterface(UI userInterface) {
        this.userInterface = userInterface;
    }

    @Override
    public void onEvent(ProfileImageAvailableEvent event) {
        if (profileImageProvider.isProfileImageAvailable()) {
            userInterface.showProfileImage(profileImageProvider.getProfileImagePath());
        }
    }

    public void loadMembershipPassDetails(MembershipPassEvent event) {

        if (event.getMembershipPassRequestResult() == MembershipPassInteractor.MembershipPassRequestResult.SUCCESSFUL) {

            membershipPassResponse=event.getResponseBody();
            scheduler.schedule(new Runnable() {
                @Override
                public void run() {
                    userInterface.showMembershipInfo(partyInfoRepo.getPartyId(), "",partyInfoRepo.getVitalityMembershipId(),
                            String.valueOf(partyInfoRepo.getPartyId()), vitalityStatusDTO.getCurrentStatusLevelName(),
                            dateFormatUtil.formatDateMonthYear(LocalDate.create(membershipPassResponse.sections.get(0).currentVitalityMembershipPeriod.get(0).effectiveFrom)),
                            membershipPassResponse.sections.get(0).stateCategories.get(0).stateList.get(0).statusTypeName);

                }
            });
        }
    }

    private void initMembershipPassInitial() {
        if (hasMembershipDetails(membershipDetails) && hasHomeDetails(homeCardDTO)) {
            userInterface.showMembershipInfo(partyInfoRepo.getPartyId(), "",partyInfoRepo.getVitalityMembershipId() ,
                    String.valueOf(partyInfoRepo.getPartyId()), vitalityStatusDTO.getCurrentStatusLevelName(),
                    dateFormatUtil.formatDateMonthYear(LocalDate.create(membershipDetails.getMembershipStartDate())), membershipDetails.getVitalityStatus());
        } else {
            userInterface.showMembershipInfo( 0,"", "",
                    "", "",
                    "", "");
        }
    }

    private boolean hasMembershipDetails(MembershipPassDTO membershipPassDTO) {
        return (membershipPassDTO != null && membershipPassDTO.getMembershipStartDate() != null && !membershipPassDTO.getMembershipStartDate().isEmpty());
    }
    private boolean hasHomeDetails(List<HomeCardDTO>  homeCardDTO){
        return (homeCardDTO != null && homeCardDTO.size() !=0 && !homeCardDTO.isEmpty());
    }
    public final EventListener<MembershipPassEvent> membershipPassEventEventListener = new EventListener<MembershipPassEvent>() {
        @Override
        public void onEvent(MembershipPassEvent membershipPassEventEventListener) {
            loadMembershipPassDetails(membershipPassEventEventListener);

        }
    };

}
