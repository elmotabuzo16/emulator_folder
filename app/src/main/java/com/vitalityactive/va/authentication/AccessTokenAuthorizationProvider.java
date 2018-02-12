package com.vitalityactive.va.authentication;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.vitalityactive.va.BuildConfig;
import com.vitalityactive.va.persistence.DataStore;
import com.vitalityactive.va.persistence.models.User;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class AccessTokenAuthorizationProvider {

    private DataStore dataStore;

    @Inject
    public AccessTokenAuthorizationProvider(DataStore dataStore) {
        this.dataStore = dataStore;
    }

    public String getAuthorization() {
        return BuildConfig.AUTH_BEARER_KEY_NAME + dataStore.getFieldValue(User.class, new DataStore.FieldAccessor<User, String>() {
            @NonNull
            @Override
            public String getField(@Nullable User model) {
                if (model == null) {
                    return "";
                }
                String accessToken = model.getAccessToken();
                return accessToken == null ? "" : accessToken;
            }
        });
    }
}
