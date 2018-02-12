package com.vitalityactive.va;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.vitalityactive.va.constants.BooleanString;
import com.vitalityactive.va.constants.GeneralPreferences;
import com.vitalityactive.va.dto.CurrentVitalityMembershipPeriodDTO;
import com.vitalityactive.va.dto.EmailAddressDto;
import com.vitalityactive.va.dto.EmailContactRoleDto;
import com.vitalityactive.va.dto.PersonalDetailsDTO;
import com.vitalityactive.va.dto.ProfilePhotoDTO;
import com.vitalityactive.va.dto.ReferenceDTO;
import com.vitalityactive.va.persistence.DataStore;
import com.vitalityactive.va.persistence.models.CurrentVitalityMembershipPeriod;
import com.vitalityactive.va.persistence.models.EmailAddress;
import com.vitalityactive.va.persistence.models.GeneralPreference;
import com.vitalityactive.va.persistence.models.InsurerConfiguration;
import com.vitalityactive.va.persistence.models.ProfilePhoto;
import com.vitalityactive.va.persistence.models.Reference;
import com.vitalityactive.va.persistence.models.Telephone;
import com.vitalityactive.va.persistence.models.User;
import com.vitalityactive.va.utilities.RepositoryUtilities;
import com.vitalityactive.va.utilities.TextUtilities;

import java.util.ArrayList;
import java.util.List;

public class PartyInformationRepositoryImpl implements PartyInformationRepository {
    private DataStore dataStore;
    private DeviceSpecificPreferences deviceSpecificPreferences;

    public PartyInformationRepositoryImpl(DataStore dataStore, DeviceSpecificPreferences deviceSpecificPreferences) {
        this.dataStore = dataStore;
        this.deviceSpecificPreferences = deviceSpecificPreferences;
    }

    @Override
    public long getTenantId() {
        return dataStore.getFieldValue(InsurerConfiguration.class, new DataStore.FieldAccessor<InsurerConfiguration, Long>() {
            @NonNull
            @Override
            public Long getField(@Nullable InsurerConfiguration model) {
                return longValueOrDefault(model == null ? null : model.getTenantId());
            }
        });
    }

    @Override
    public long getPartyId() {
        return dataStore.getFieldValue(User.class, new DataStore.FieldAccessor<User, Long>() {
            @NonNull
            @Override
            public Long getField(@Nullable User model) {
                return longValueOrDefault(model == null ? null : model.getPartyId());
            }
        });
    }

    private Long longValueOrDefault(@Nullable Long value) {
        return RepositoryUtilities.valueOrDefault(value, 0L);
    }

    @NonNull
    @Override
    public String getUsername() {
        return dataStore.getFieldValue(User.class, new DataStore.FieldAccessor<User, String>() {
            @NonNull
            @Override
            public String getField(@Nullable User model) {
                return stringValueOrDefault(model == null ? null : model.getUsername());
            }
        });
    }

    @NonNull
    @Override
    public String getVitalityMembershipId() {
        return dataStore.getFieldValue(User.class, new DataStore.FieldAccessor<User, String>() {
            @NonNull
            @Override
            public String getField(@Nullable User model) {
                return PartyInformationRepositoryImpl.this.stringValueOrDefault(model == null ? null : model.getVitalityMembershipId());
            }
        });
    }

    @NonNull
    @Override
    public String getEntityNumber() {
        List<ReferenceDTO> references = getReferences();
        String entityValue = "";
        if(references.size() > 0){
            for(ReferenceDTO referenceDTO : references){
                if(referenceDTO.getTypeCode().equals("EntityNumber")){
                    entityValue = referenceDTO.getValue();
                }
            }
        }
        return entityValue;
    }

    @NonNull
    @Override
    public String getEntityNumberFromDevicePreference() {
        return  deviceSpecificPreferences.getRememberedEntity();
    }

    @Override
    public void setNewEntityNumber(String oldEntityNumber, final String newEntityNumber) {
        dataStore.setFieldValue(Reference.class, "value", oldEntityNumber, new DataStore.FieldUpdater<Reference>(){
            @Override
            public void updateField(@Nullable Reference references) {
                if (references != null) {
                    if(references.getTypeCode().equals("EntityNumber")){
                        references.setValue(newEntityNumber);
                    }
                }
            }
        });
    }

    @Override
    public List<ReferenceDTO> getReferences(){
        return dataStore.getModels(Reference.class, new DataStore.ModelListMapper<Reference, ReferenceDTO>() {
            @Override
            public List<ReferenceDTO> mapModels(List<Reference> models) {
                List<ReferenceDTO> mapModel = new ArrayList<>();
                for(Reference reference : models){
                    mapModel.add(new ReferenceDTO(reference));
                }
                return mapModel;
            }
        });
    }


    @Override
    public List<EmailAddressDto> getEmails() {
        return dataStore.getModels(EmailAddress.class,
                new DataStore.ModelListMapper<EmailAddress, EmailAddressDto>() {
                    @Override
                    public List<EmailAddressDto> mapModels(List<EmailAddress> models) {
                        List<EmailAddressDto> mappedModels = new ArrayList<>();
                        for (EmailAddress emailAddress : models) {
                            mappedModels.add(new EmailAddressDto(emailAddress));
                        }
                        return mappedModels;
                    }
                });
    }

    private String stringValueOrDefault(@Nullable String value) {
        return RepositoryUtilities.valueOrDefault(value, "0");
    }

    @Override
    public boolean isOptedInToEmailCommunication() {
        return dataStore.getFieldValue(GeneralPreference.class, "type", GeneralPreferences.Types.EMAIL_COMMUNICATION.value, new DataStore.FieldAccessor<GeneralPreference, Boolean>() {
            @NonNull
            @Override
            public Boolean getField(@Nullable GeneralPreference model) {
                return model != null && model.getValue().equals(BooleanString.TRUE.value);
            }
        });
    }

    public void setOptedInToEmailCommunication(final boolean optedInToEmailCommunication) {
        dataStore.setFieldValue(GeneralPreference.class, "type", GeneralPreferences.Types.EMAIL_COMMUNICATION.value, new DataStore.FieldUpdater<GeneralPreference>() {
            @Override
            public void updateField(@Nullable GeneralPreference model) {
                if (model != null) {
                    model.setValue(optedInToEmailCommunication ? BooleanString.TRUE.value : BooleanString.FALSE.value);
                }
            }
        });
    }

    @Override
    public boolean isOptedInShareVitalityStatus() {
        return dataStore.getFieldValue(GeneralPreference.class, "type", GeneralPreferences.Types.SHARE_STATUS_WITH_EMP.value, new DataStore.FieldAccessor<GeneralPreference, Boolean>() {
            @NonNull
            @Override
            public Boolean getField(@Nullable GeneralPreference model) {
                return model != null && model.getValue().equals(BooleanString.TRUE.value);
            }
        });
    }

    @Override
    public void setOptedInShareVitalityStatus(final boolean optedInShareVitalityStatus) {
        dataStore.setFieldValue(GeneralPreference.class, "type", GeneralPreferences.Types.SHARE_STATUS_WITH_EMP.value, new DataStore.FieldUpdater<GeneralPreference>() {
            @Override
            public void updateField(@Nullable GeneralPreference model) {
                if (model != null) {
                    model.setValue(optedInShareVitalityStatus ? BooleanString.TRUE.value : BooleanString.FALSE.value);
                }
            }
        });
    }

    @NonNull
    @Override
    public String getUserInitials() {
        return dataStore.getFieldValue(User.class, new DataStore.FieldAccessor<User, String>() {
            @NonNull
            @Override
            public String getField(@Nullable User model) {
                if (model == null) {
                    return "";
                }
                return getInitial(model.getGivenName());
            }

            private String getInitial(String name) {
                return TextUtilities.isNullOrWhitespace(name) ? "" : name.substring(0, 1);
            }
        });
    }

    @NonNull
    @Override
    public String getUserGivenName() {
        return dataStore.getFieldValue(User.class, new DataStore.FieldAccessor<User, String>() {
            @NonNull
            @Override
            public String getField(@Nullable User model) {
                return model == null ? "" : model.getGivenName();
            }
        });
    }

    @NonNull
    @Override
    public String getUserFamilyName() {
        return dataStore.getFieldValue(User.class, new DataStore.FieldAccessor<User, String>() {
            @NonNull
            @Override
            public String getField(@Nullable User model) {
                return model == null ? "" : model.getFamilyName();
            }
        });
    }

    @NonNull
    @Override
    public String getMobileNumber() {
        return dataStore.getFieldValue(Telephone.class, new DataStore.FieldAccessor<Telephone, String>() {
            @NonNull
            @Override
            public String getField(@Nullable Telephone model) {
                String mobileNumber = "Unknown";
                if (model != null) {
                    mobileNumber = model.getCountryDialCode() + model.getAreaDialCode() + model.getContactNumber();
                }

                return mobileNumber;
            }
        });
    }

    @NonNull
    @Override
    public String getEmailAddress() {
        return dataStore.getFieldValue(EmailAddress.class, new DataStore.FieldAccessor<EmailAddress, String>() {
            @NonNull
            @Override
            public String getField(@Nullable EmailAddress model) {
                return model == null ? "Unknown" : model.getValue();
            }
        });
    }

    @NonNull
    @Override
    public String getLatestEmailAddress() {
        List<EmailAddressDto> emails = getEmails();
        String latestEmail;
        if (emails != null && !emails.isEmpty()) {
            if (emails.size() > 0) {
                latestEmail = emails.get(emails.size() - 1).getValue();
                for (EmailAddressDto email : emails) {
                    for (EmailContactRoleDto contactRole : email.getContactRoles()) {
                        String effectiveTo = contactRole.getEffectiveTo();
                        if (effectiveTo != null && effectiveTo.contains("9999")) {
                            latestEmail = email.getValue();
                            break;
                        }
                    }
                }
            } else {
                latestEmail = emails.get(0).getValue();
            }
            return latestEmail;
        }
        return getEmailAddress();
    }

    @NonNull
    @Override
    public String getEmailFromDevicePreference() {
        return  deviceSpecificPreferences.getRememberedUsername();
    }

    @NonNull
    @Override
    public PersonalDetailsDTO getPersonalDetails() {
        return dataStore.getFirstModelInstance(User.class, new PersonalDetailsDTO.Mapper());
    }

    @Override
    public void setNewAccessToken(String oldAccessToken, final String newAccessToken) {
        dataStore.setFieldValue(User.class, "accessToken", oldAccessToken, new DataStore.FieldUpdater<User>() {
            @Override
            public void updateField(@Nullable User user) {
                if (user != null) {
                    user.setAccessToken(newAccessToken);
                }
            }
        });
    }

    @Override
    public void setNewEmailAddress(String oldEmail, final String newEmail) {
        dataStore.setFieldValue(EmailAddress.class, "value", oldEmail, new DataStore.FieldUpdater<EmailAddress>() {
            @Override
            public void updateField(@Nullable EmailAddress emailAddress) {
                if (emailAddress != null) {
                    emailAddress.setValue(newEmail);
                }
            }
        });
    }

    @NonNull
    @Override
    public ProfilePhotoDTO persistProfilePhotoUri(String uri) {
        ProfilePhoto profilePhoto = new ProfilePhoto(uri);
        dataStore.add(profilePhoto);
        return dataStore.getModelInstance(ProfilePhoto.class, new DataStore.ModelMapper<ProfilePhoto, ProfilePhotoDTO>() {
            @Override
            public ProfilePhotoDTO mapModel(ProfilePhoto model) {
                return new ProfilePhotoDTO(model);
            }
        }, "id", profilePhoto.getId());
    }

    @Override
    public CurrentVitalityMembershipPeriodDTO getCurrentVitalityMembershipPeriod() {

        List<CurrentVitalityMembershipPeriodDTO> list = dataStore.getModels(CurrentVitalityMembershipPeriod.class, new DataStore.ModelListMapper<CurrentVitalityMembershipPeriod, CurrentVitalityMembershipPeriodDTO>() {
            @Override
            public List<CurrentVitalityMembershipPeriodDTO> mapModels(List<CurrentVitalityMembershipPeriod> models) {
                List<CurrentVitalityMembershipPeriodDTO> mappedModels = new ArrayList<>();
                for (CurrentVitalityMembershipPeriod membershipPeriod : models) {
                    mappedModels.add(new CurrentVitalityMembershipPeriodDTO(membershipPeriod));
                    break;
                }
                return mappedModels;
            }
        });


        return list != null && list.size() > 0 ? list.get(0) : null;


    }
}
