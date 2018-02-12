package com.vitalityactive.va.wellnessdevices.dto;

import android.os.Parcel;
import android.os.Parcelable;

import com.vitalityactive.va.persistence.models.wellnessdevices.WellnessDevicesPartnerLink;
import com.vitalityactive.va.wellnessdevices.landing.service.GetFullListResponse;

public class PartnerLinkDto implements Parcelable {
    private String url;
    private String method;

    public PartnerLinkDto() {}

    public PartnerLinkDto(GetFullListResponse.PartnerLink src) {
        if (src != null) {
            this.url = src.url;
            this.method = src.method;
        }
    }

    public PartnerLinkDto(WellnessDevicesPartnerLink src) {
        if (src != null) {
            this.url = src.getUrl();
            this.method = src.getMethod();
        }
    }

    public String getUrl() {
        return url;
    }

    public String getMethod() {
        return method;
    }

    public void setUrl(String url) {
        this.url = url;
    }


    public void setMethod(String method) {
        this.method = method;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.url);
        dest.writeString(this.method);
    }

    protected PartnerLinkDto(Parcel in) {
        this.url = in.readString();
        this.method = in.readString();
    }

    public static final Parcelable.Creator<PartnerLinkDto> CREATOR = new Parcelable.Creator<PartnerLinkDto>() {
        @Override
        public PartnerLinkDto createFromParcel(Parcel source) {
            return new PartnerLinkDto(source);
        }

        @Override
        public PartnerLinkDto[] newArray(int size) {
            return new PartnerLinkDto[size];
        }
    };
}
