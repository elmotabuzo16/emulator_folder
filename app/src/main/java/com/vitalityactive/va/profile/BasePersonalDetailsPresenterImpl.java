package com.vitalityactive.va.profile;

import android.net.Uri;

import com.vitalityactive.va.PartyInformationRepository;
import com.vitalityactive.va.appconfig.AppConfigRepository;
import com.vitalityactive.va.dto.PersonalDetailsDTO;
import com.vitalityactive.va.events.EventDispatcher;
import com.vitalityactive.va.events.EventListener;
import com.vitalityactive.va.utilities.TextUtilities;
import com.vitalityactive.va.utilities.date.LocalDate;
import com.vitalityactive.va.utilities.date.formatting.DateFormattingUtilities;

import java.io.InputStream;

/**
 * Created by peter.ian.t.betos on 15/01/2018.
 */

public abstract class BasePersonalDetailsPresenterImpl  implements PersonalDetailsPresenter, EventListener<ProfileImageAvailableEvent> {

    private final EventDispatcher eventDispatcher;
    protected final DateFormattingUtilities dateFormatUtil;
    private final ProfileImageProvider profileImageProvider;
    private final PartyInformationRepository partyInfoRepo;
    private final PersonalDetailsInteractor personalDetailsInteractor;
    private final AppConfigRepository appConfigRepository;

    private UI userInterface;

    public BasePersonalDetailsPresenterImpl(EventDispatcher eventDispatcher, DateFormattingUtilities dateFormatUtil, ProfileImageProvider profileImageProvider,
                                        PartyInformationRepository partyInfoRepo, PersonalDetailsInteractor personalDetailsInteractor, AppConfigRepository appConfigRepository) {
        this.eventDispatcher = eventDispatcher;
        this.dateFormatUtil = dateFormatUtil;
        this.profileImageProvider = profileImageProvider;
        this.partyInfoRepo = partyInfoRepo;
        this.personalDetailsInteractor = personalDetailsInteractor;
        this.appConfigRepository = appConfigRepository;
    }

    @Override
    public void onUserInterfaceCreated(boolean isNewNavigation) {

    }

    @Override
    public void onUserInterfaceAppeared() {
        eventDispatcher.addEventListener(ProfileImageAvailableEvent.class, this);
        showProfileInfo();
    }

    @Override
    public void onUserInterfaceDisappeared(boolean isFinishing) {
        eventDispatcher.removeEventListener(ProfileImageAvailableEvent.class, this);
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
    public void addProfilePhoto(Uri uri, InputStream inputStream, String fileDir, byte[] imageByte) {
        personalDetailsInteractor.addProfilePhoto(uri, inputStream, fileDir, imageByte);
    }

    private void showProfileInfo() {

        PersonalDetailsDTO personalDetails = partyInfoRepo.getPersonalDetails();
        String mobileNumber = partyInfoRepo.getMobileNumber();
        String emailAddress = partyInfoRepo.getEmailFromDevicePreference();
        String entityNumber;
        if(TextUtilities.isNullOrEmpty(partyInfoRepo.getEntityNumber())){
            entityNumber = partyInfoRepo.getEntityNumberFromDevicePreference();
        } else {
            entityNumber = partyInfoRepo.getEntityNumber();
        }

        String dateOfBirthStr = getDateOfBirthWithFormat(getDateOfBirth(personalDetails));

        userInterface.showProfileInfo(personalDetails.getGivenName(), personalDetails.getFamilyName(),
                dateOfBirthStr,
                personalDetails.getGender(),
                mobileNumber, emailAddress, emailAddress, entityNumber
        );
        if (profileImageProvider.isProfileImageAvailable()) {
            userInterface.showProfileImage(profileImageProvider.getProfileImagePath());
        } else {
            profileImageProvider.fetchProfileImage();
            userInterface.showProfileInitials(personalDetails.getInitials());
        }
    }

    @Override
    public void onEvent(ProfileImageAvailableEvent event) {
        if (profileImageProvider.isProfileImageAvailable()) {
            userInterface.showProfileImage(profileImageProvider.getProfileImagePath());
        }
    }

    protected LocalDate getDateOfBirth(PersonalDetailsDTO personalDetails){
        return personalDetails.getDateOfBirth();
    }

    protected String getDateOfBirthWithFormat(LocalDate dateOfBirth){
        return dateFormatUtil.formatDateMonthYear(dateOfBirth);
    }
}
