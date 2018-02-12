package com.vitalityactive.va.profile;


import android.net.Uri;
import android.support.annotation.NonNull;

import com.vitalityactive.va.BuildConfig;
import com.vitalityactive.va.DeviceSpecificPreferences;
import com.vitalityactive.va.PartyInformationRepository;
import com.vitalityactive.va.authentication.AccessTokenAuthorizationProvider;
import com.vitalityactive.va.cms.CMSContentUploader;
import com.vitalityactive.va.events.EventDispatcher;
import com.vitalityactive.va.networking.WebServiceResponseParser;
import com.vitalityactive.va.networking.model.PersonalDetailsResponse;
import com.vitalityactive.va.register.entity.EmailAddress;
import com.vitalityactive.va.register.entity.Password;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;

import javax.inject.Inject;

public class PersonalDetailsInteractorImpl implements PersonalDetailsInteractor {

    private final PersonalDetailsClient personalDetailsClient;
    private final EventDispatcher eventDispatcher;
    private final PartyInformationRepository partyInformationRepository;
    private final CMSContentUploader cmsContentUploader;
    private final AccessTokenAuthorizationProvider accessTokenAuthorizationProvider;
    private final DeviceSpecificPreferences deviceSpecificPreferences;

    private static final int VERIFY_EMAIL_HTTP_BAD_REQUEST = 400;
    private static final int CA_VERIFY_EMAIL_OK_NOT_EXISTING = 30163;
    private static final int SLI_VERIFY_EMAIL_OK_NOT_EXISTING = 30164;
    private static final String PROFILE_IMAGE_NAME = "profile.jpg";

    @Inject
    public PersonalDetailsInteractorImpl(@NonNull PersonalDetailsClient personalDetailsClient, @NonNull EventDispatcher eventDispatcher, @NonNull PartyInformationRepository partyInformationRepository, @NonNull CMSContentUploader cmsContentUploader, @NonNull AccessTokenAuthorizationProvider authorizationProvider, @NonNull DeviceSpecificPreferences deviceSpecificPreferences) {
        this.personalDetailsClient = personalDetailsClient;
        this.eventDispatcher = eventDispatcher;
        this.partyInformationRepository = partyInformationRepository;
        this.cmsContentUploader = cmsContentUploader;
        this.accessTokenAuthorizationProvider = authorizationProvider;
        this.deviceSpecificPreferences = deviceSpecificPreferences;
    }

    @Override
    public void verifyEmail(@NonNull EmailAddress newEmailAddress) {
        personalDetailsClient.verifyNewEmailAddress(newEmailAddress, new WebServiceResponseParser<PersonalDetailsResponse.VerifyResponse>() {
            @Override
            public void parseResponse(PersonalDetailsResponse.VerifyResponse response) {
                eventDispatcher.dispatchEvent(new PersonalDetailsEvent(PersonalDetailsEvent.EventType.VERIFY_EMAIL_EXISTING));
            }

            @Override
            public void parseErrorResponse(String errorBody, int code) {
                int errorCode = 0;
                try {
                    JSONObject jsonError = new JSONObject(errorBody);
                    JSONArray jsonArray = jsonError.getJSONArray("errors");
                    JSONObject errorObj = jsonArray.getJSONObject(0);
                    errorCode = errorObj.optInt("code", 0);
                } catch (JSONException e) {
                    e.printStackTrace();
                    eventDispatcher.dispatchEvent(new PersonalDetailsEvent(PersonalDetailsEvent.EventType.CHANGE_EMAIL_FAILED));
                }
                if (code == VERIFY_EMAIL_HTTP_BAD_REQUEST && verifyErrorCode(errorCode)) {
                    eventDispatcher.dispatchEvent(new PersonalDetailsEvent(PersonalDetailsEvent.EventType.VERIFY_EMAIL_OK));
                } else {
                    eventDispatcher.dispatchEvent(new PersonalDetailsEvent(PersonalDetailsEvent.EventType.VERIFY_EMAIL_FAILED));
                }
            }

            @Override
            public void handleGenericError(Exception exception) {
                eventDispatcher.dispatchEvent(new PersonalDetailsEvent(PersonalDetailsEvent.EventType.VERIFY_EMAIL_FAILED));
            }

            @Override
            public void handleConnectionError() {
                eventDispatcher.dispatchEvent(new PersonalDetailsEvent(PersonalDetailsEvent.EventType.VERIFY_EMAIL_FAILED));
            }
        });
    }

    @Override
    public boolean verifyErrorCode(int errorCode){
        if(errorCode == CA_VERIFY_EMAIL_OK_NOT_EXISTING || errorCode == SLI_VERIFY_EMAIL_OK_NOT_EXISTING){
            return true;
        }
        return false;

    }

    @Override
    public void changeEmail(final @NonNull EmailAddress existingEmailAddress, @NonNull final EmailAddress newEmailAddress, @NonNull Password password) {
        personalDetailsClient.changeEmailAddress(existingEmailAddress, newEmailAddress, password, new WebServiceResponseParser<PersonalDetailsResponse.ChangeEmailResponse>() {
            @Override
            public void parseResponse(PersonalDetailsResponse.ChangeEmailResponse response) {

                String newAccessToken = response.newAccessToken;
                String oldAccessToken = accessTokenAuthorizationProvider.getAuthorization().replace(BuildConfig.AUTH_BEARER_KEY_NAME, "");
                partyInformationRepository.setNewAccessToken(oldAccessToken, newAccessToken);
                partyInformationRepository.setNewEmailAddress(existingEmailAddress.toString(), newEmailAddress.toString());
                deviceSpecificPreferences.setRememberedUsername(newEmailAddress.toString());
                eventDispatcher.dispatchEvent(new PersonalDetailsEvent(PersonalDetailsEvent.EventType.CHANGE_EMAIL_SUCCESS));
            }

            @Override
            public void parseErrorResponse(String errorBody, int code) {
                eventDispatcher.dispatchEvent(new PersonalDetailsEvent(PersonalDetailsEvent.EventType.CHANGE_EMAIL_AUTH_ERROR));
            }

            @Override
            public void handleGenericError(Exception exception) {
                eventDispatcher.dispatchEvent(new PersonalDetailsEvent(PersonalDetailsEvent.EventType.CHANGE_EMAIL_FAILED));
            }

            @Override
            public void handleConnectionError() {
                eventDispatcher.dispatchEvent(new PersonalDetailsEvent(PersonalDetailsEvent.EventType.CHANGE_EMAIL_FAILED));
            }
        });
    }

    @Override
    public void addProfilePhoto(Uri uri, InputStream inputStream, String fileDir, byte[] imageByte) {
        eventDispatcher.dispatchEvent(new ProfileImageAvailableEvent());

        /*
        // Do not use for now. Image photo is only saved locally
        //ProfilePhotoDTO profilePhotoDTO = partyInformationRepository.persistProfilePhotoUri(uri.toString());

        // Remove as of now. profile photo will be saved in the device only.
        //cmsContentUploader.uploadProfilePhoto(profilePhotoDTO, )imageByte;
        if (FileUtilities.writeFileToDisk(fileDir, inputStream, PROFILE_IMAGE_NAME )){
           eventDispatcher.dispatchEvent(new ProfileImageAvailableEvent());
       }*/
    }
}
