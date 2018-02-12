package com.vitalityactive.va.wellnessdevices.dto;

import android.os.Parcel;
import android.os.Parcelable;

import com.vitalityactive.va.persistence.models.wellnessdevices.WellnessDevicesPartner;
import com.vitalityactive.va.wellnessdevices.landing.service.GetFullListResponse;

import java.util.HashSet;
import java.util.Set;

public class PartnerDto implements Parcelable {
    private String partnerSystem;
    private String device;
    private PartnerLinkDto partnerLink;
    private PartnerLinkDto partnerDelink;
    private PartnerLinkDto partnerSync;
    private String partnerLinkedStatus;
    private String partnerLastSync;
    private String partnerLastWorkout;
    private Set<Integer> availableOptions;

    public PartnerDto(){}

    public PartnerDto(GetFullListResponse.Partner partner){
        this.partnerSystem = partner.partnerSystem;
        this.device = partner.device;
        this.partnerLink = new PartnerLinkDto(partner.partnerLink);
        this.partnerDelink = new PartnerLinkDto(partner.partnerDelink);
        this.partnerSync = new PartnerLinkDto(partner.partnerSync);
        this.partnerLinkedStatus = partner.partnerLinkedStatus;
        this.partnerLastSync = partner.partnerLastSync;
        this.partnerLastWorkout = partner.partnerLastWorkout;
        this.availableOptions = new HashSet<>();
    }

    public PartnerDto(WellnessDevicesPartner partner){
        this.partnerSystem = partner.getPartnerSystem();
        this.device = partner.getDevice();
        this.partnerLink = new PartnerLinkDto(partner.getPartnerLink());
        this.partnerDelink = new PartnerLinkDto(partner.getPartnerDelink());
        this.partnerSync = new PartnerLinkDto(partner.getPartnerSync());
        this.partnerLinkedStatus = partner.getPartnerLinkedStatus();
        this.partnerLastSync = partner.getPartnerLastSync();
        this.partnerLastWorkout = partner.getPartnerLastWorkout();
        this.availableOptions = new HashSet<>();
    }


    public String getPartnerSystem() {
        return partnerSystem;
    }

    public String getDevice() {
        return device;
    }

    public PartnerLinkDto getPartnerLink() {
        return partnerLink;
    }

    public PartnerLinkDto getPartnerDelink() {
        return partnerDelink;
    }

    public PartnerLinkDto getPartnerSync() {
        return partnerSync;
    }

    public String getPartnerLinkedStatus() {
        return partnerLinkedStatus;
    }

    public String getPartnerLastSync() {
        return partnerLastSync;
    }

    public String getPartnerLastWorkout() {
        return partnerLastWorkout;
    }

    public Set<Integer> getAvailableOptions() {
        return availableOptions;
    }

    public void setPartnerDelink(PartnerLinkDto delinkUrl) {
        partnerDelink = delinkUrl;
    }

    public void setAvailableOptions(Set<Integer> availableOptions) {
        this.availableOptions = availableOptions;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.partnerSystem);
        dest.writeString(this.device);
        dest.writeParcelable(this.partnerLink, flags);
        dest.writeParcelable(this.partnerDelink, flags);
        dest.writeParcelable(this.partnerSync, flags);
        dest.writeString(this.partnerLinkedStatus);
        dest.writeString(this.partnerLastSync);
        dest.writeString(this.partnerLastWorkout);
    }

    protected PartnerDto(Parcel in) {
        this.partnerSystem = in.readString();
        this.device = in.readString();
        this.partnerLink = in.readParcelable(PartnerLinkDto.class.getClassLoader());
        this.partnerDelink = in.readParcelable(PartnerLinkDto.class.getClassLoader());
        this.partnerSync = in.readParcelable(PartnerLinkDto.class.getClassLoader());
        this.partnerLinkedStatus = in.readString();
        this.partnerLastSync = in.readString();
        this.partnerLastWorkout = in.readString();
        this.availableOptions = new HashSet<>();
    }

    public static final Parcelable.Creator<PartnerDto> CREATOR = new Parcelable.Creator<PartnerDto>() {
        @Override
        public PartnerDto createFromParcel(Parcel source) {
            return new PartnerDto(source);
        }

        @Override
        public PartnerDto[] newArray(int size) {
            return new PartnerDto[size];
        }
    };
}
