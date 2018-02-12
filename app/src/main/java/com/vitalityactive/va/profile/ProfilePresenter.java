package com.vitalityactive.va.profile;

import com.vitalityactive.va.Presenter;

public interface ProfilePresenter extends Presenter<ProfilePresenter.UI> {

    interface UI {
        void showProfileName(String givenName, String familyName);
        void showProfileImage(String profileImagePath);
        void showProfileInitials(String userInitials);
    }
}
