package com.vitalityactive.va.profile;

import android.net.Uri;
import android.support.annotation.NonNull;

import com.vitalityactive.va.register.entity.EmailAddress;
import com.vitalityactive.va.register.entity.Password;

import java.io.InputStream;

public interface PersonalDetailsInteractor {

    enum PersonalDetailsRequestResult {
        NONE,
        CONNECTION_ERROR,
        GENERIC_ERROR,
        EXISTING_EMAIL,
        SUCCESSFUL
    }

    void verifyEmail(@NonNull EmailAddress newEmailAddress);

    boolean verifyErrorCode(int errorCode);

    void changeEmail(@NonNull EmailAddress existingEmailAddress, @NonNull EmailAddress newEmailAddress, @NonNull Password password);

    void addProfilePhoto(Uri uri, InputStream inputStream, String fileDir, byte[] imageByte);
}
