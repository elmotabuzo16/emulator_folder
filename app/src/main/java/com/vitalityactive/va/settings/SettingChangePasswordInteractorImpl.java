package com.vitalityactive.va.settings;


import android.support.annotation.NonNull;

import com.vitalityactive.va.BuildConfig;
import com.vitalityactive.va.PartyInformationRepository;
import com.vitalityactive.va.authentication.AccessTokenAuthorizationProvider;
import com.vitalityactive.va.events.EventDispatcher;
import com.vitalityactive.va.networking.WebServiceResponseParser;
import com.vitalityactive.va.networking.model.ChangePasswordResponse;
import com.vitalityactive.va.register.entity.EmailAddress;
import com.vitalityactive.va.register.entity.Password;

public class SettingChangePasswordInteractorImpl implements SettingChangePasswordInteractor{

    private final PartyInformationRepository partyInfoRepository;
    private final ChangePasswordClient changePasswordClient;
    private final EventDispatcher eventDispatcher;
    private final AccessTokenAuthorizationProvider accessTokenAuthorizationProvider;

    public SettingChangePasswordInteractorImpl(@NonNull PartyInformationRepository partyInfoRepository, @NonNull ChangePasswordClient changePasswordClient, @NonNull EventDispatcher eventDispatcher, @NonNull AccessTokenAuthorizationProvider authorizationProvider) {
        this.partyInfoRepository = partyInfoRepository;
        this.changePasswordClient = changePasswordClient;
        this.eventDispatcher = eventDispatcher;
        this.accessTokenAuthorizationProvider = authorizationProvider;
    }

    @Override
    public void changePassword(@NonNull Password existingPassword, @NonNull Password newPassword, @NonNull EmailAddress userName, @NonNull AccessTokenAuthorizationProvider authorizationProvider) {
        changePasswordClient.changeUserPassword(existingPassword, newPassword, userName, new WebServiceResponseParser<ChangePasswordResponse>() {
            @Override
            public void parseResponse(ChangePasswordResponse response) {
                String newAccessToken = response.newAccessToken;
                String oldAccessToken = accessTokenAuthorizationProvider.getAuthorization().replace(BuildConfig.AUTH_BEARER_KEY_NAME, "");
                partyInfoRepository.setNewAccessToken(oldAccessToken, newAccessToken);

                eventDispatcher.dispatchEvent(new ChangePasswordEvent(ChangePasswordEvent.EventType.CHANGE_PASSWORD_SUCCESS));
            }

            @Override
            public void parseErrorResponse(String errorBody, int code) {
                eventDispatcher.dispatchEvent(new ChangePasswordEvent(ChangePasswordEvent.EventType.CHANGE_PASSWORD_AUTH_ERROR));
            }

            @Override
            public void handleGenericError(Exception exception) {
                eventDispatcher.dispatchEvent(new ChangePasswordEvent(ChangePasswordEvent.EventType.CHANGE_PASSWORD_FAILED));
            }

            @Override
            public void handleConnectionError() {
                eventDispatcher.dispatchEvent(new ChangePasswordEvent(ChangePasswordEvent.EventType.CHANGE_PASSWORD_FAILED));
            }
        });
    }
}
