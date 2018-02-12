package com.vitalityactive.va.settings;


import android.support.annotation.NonNull;

import com.vitalityactive.va.authentication.AccessTokenAuthorizationProvider;
import com.vitalityactive.va.register.entity.EmailAddress;
import com.vitalityactive.va.register.entity.Password;

public interface SettingChangePasswordInteractor {

    void changePassword(@NonNull Password existingPassword, @NonNull Password newPassword, @NonNull EmailAddress userName,  @NonNull AccessTokenAuthorizationProvider authorizationProvider);

}
