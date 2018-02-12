package com.vitalityactive.va;

import android.support.annotation.NonNull;

import com.vitalityactive.va.dto.CurrentVitalityMembershipPeriodDTO;
import com.vitalityactive.va.dto.EmailAddressDto;
import com.vitalityactive.va.dto.PersonalDetailsDTO;
import com.vitalityactive.va.dto.ProfilePhotoDTO;
import com.vitalityactive.va.dto.ReferenceDTO;

import java.util.List;

public interface PartyInformationRepository {
    long getTenantId();

    long getPartyId();

    @NonNull
    String getUsername();

    @NonNull
    String getVitalityMembershipId();

    @NonNull
    String getEntityNumber();

    @NonNull
    String getEntityNumberFromDevicePreference();

    void setNewEntityNumber(String oldEntityNumber, String newEntityNumber);

    List<ReferenceDTO> getReferences();

    List<EmailAddressDto> getEmails();

    boolean isOptedInToEmailCommunication();

    void setOptedInToEmailCommunication(boolean optedInToEmailCommunication);

    boolean isOptedInShareVitalityStatus();

    void setOptedInShareVitalityStatus(boolean optedInShareVitalityStatus);

    @NonNull
    String getUserInitials();

    @NonNull
    String getUserGivenName();

    @NonNull
    String getUserFamilyName();

    @NonNull
    String getMobileNumber();

    @NonNull
    String getEmailAddress();

    @NonNull
    String getLatestEmailAddress();

    @NonNull
    String getEmailFromDevicePreference();

    @NonNull
    PersonalDetailsDTO getPersonalDetails();

    @NonNull
    ProfilePhotoDTO persistProfilePhotoUri(String uri);

    CurrentVitalityMembershipPeriodDTO getCurrentVitalityMembershipPeriod();

    @NonNull
    void setNewAccessToken(String oldAccessToken, final String newAccessToken);

    @NonNull
    void setNewEmailAddress(String oldEmail, final String newEmail);
}
