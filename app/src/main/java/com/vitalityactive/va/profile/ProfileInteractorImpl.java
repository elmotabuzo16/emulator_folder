package com.vitalityactive.va.profile;

import com.vitalityactive.va.PartyInformationRepository;

public class ProfileInteractorImpl implements ProfileInteractor {

    private final ProfileImageProvider profileImageProvider;
    private final PartyInformationRepository partyInformationRepository;

    public ProfileInteractorImpl(ProfileImageProvider profileImageProvider, PartyInformationRepository partyInformationRepository) {
        this.profileImageProvider = profileImageProvider;
        this.partyInformationRepository = partyInformationRepository;
    }

    @Override
    public String getUserGivenName() {
        return partyInformationRepository.getUserGivenName();
    }

    @Override
    public String getUserFamilyName() {
        return partyInformationRepository.getUserFamilyName();
    }

    @Override
    public String getUserInitials() {
        return partyInformationRepository.getUserInitials();
    }

    @Override
    public boolean isProfileImageAvailable() {
        return profileImageProvider.isProfileImageAvailable();
    }

    @Override
    public String getProfileImagePath() {
        return profileImageProvider.getProfileImagePath();
    }

    @Override
    public void fetchProfileImage() {
        profileImageProvider.fetchProfileImage();
    }
}
