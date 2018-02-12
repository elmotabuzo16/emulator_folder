package com.vitalityactive.va.dto;

import android.os.Parcel;
import android.os.Parcelable;

import com.vitalityactive.va.cms.CMSServiceClient;
import com.vitalityactive.va.persistence.models.ProofItem;

public class ProofItemDTO implements Parcelable, CMSServiceClient.UploadableFile {
    private final String id;
    private final String uri;
    private final String referenceId;

    public ProofItemDTO(ProofItem proofItem) {
        id = proofItem.getId();
        uri = proofItem.getUri();
        referenceId = proofItem.getReferenceId();
    }

    protected ProofItemDTO(Parcel in) {
        id = in.readString();
        uri = in.readString();
        referenceId = in.readString();
    }

    public static final Creator<ProofItemDTO> CREATOR = new Creator<ProofItemDTO>() {
        @Override
        public ProofItemDTO createFromParcel(Parcel in) {
            return new ProofItemDTO(in);
        }

        @Override
        public ProofItemDTO[] newArray(int size) {
            return new ProofItemDTO[size];
        }
    };

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getUri() {
        return uri;
    }

    public String getReferenceId() {
        return referenceId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(uri);
        dest.writeString(referenceId);
    }
}
