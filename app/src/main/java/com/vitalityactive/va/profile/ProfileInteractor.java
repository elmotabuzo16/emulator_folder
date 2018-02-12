package com.vitalityactive.va.profile;

public interface ProfileInteractor {
    String getUserGivenName();

    String getUserFamilyName();

    String getUserInitials();

    boolean isProfileImageAvailable();

    String getProfileImagePath();

    void fetchProfileImage();
}
