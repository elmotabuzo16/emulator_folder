package com.vitalityactive.va.profile;

import android.net.Uri;

import com.vitalityactive.va.Presenter;

import java.io.InputStream;

public interface PersonalDetailsPresenter extends Presenter<PersonalDetailsPresenter.UI> {

    void addProfilePhoto(Uri uri, InputStream inputStream, String filesDir, byte[] imageByte);

    interface UI {
        void showProfileInfo(String givenName, String familyName, String dateOfBirth, String gender, String mobileNumber, String emailAddress, String insurerEmail, String entityNumber);

        void showProfileInitials(String initials);

        void showProfileImage(String imagePath);

        void activityDestroyed();
    }
}
