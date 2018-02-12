package com.vitalityactive.va.dto;


import android.os.Parcel;
import android.os.Parcelable;

import com.vitalityactive.va.cms.CMSServiceClient;
import com.vitalityactive.va.persistence.models.ProfilePhoto;

public class ProfilePhotoDTO implements Parcelable, CMSServiceClient.UploadableFile {
    private final String id;
    private final String uri;
    private final String referenceId;

    public ProfilePhotoDTO(ProfilePhoto profilePhoto) {
        id = profilePhoto.getId();
        uri = profilePhoto.getUri();
        referenceId = profilePhoto.getReferenceId();
    }

    protected ProfilePhotoDTO(Parcel in) {
        id = in.readString();
        uri = in.readString();
        referenceId = in.readString();
    }

    public static final Creator<ProfilePhotoDTO> CREATOR = new Creator<ProfilePhotoDTO>() {
        @Override
        public ProfilePhotoDTO createFromParcel(Parcel in) {
            return new ProfilePhotoDTO(in);
        }

        @Override
        public ProfilePhotoDTO[] newArray(int size) {
            return new ProfilePhotoDTO[size];
        }
    };

    @Override
    public String getUri() {
        return null;
    }

    @Override
    public String getId() {
        return null;
    }

    public String getReferenceId() {
        return referenceId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(uri);
        parcel.writeString(referenceId);
    }


}
