package com.vitalityactive.va.wellnessdevices.dto;

import com.vitalityactive.va.persistence.models.wellnessdevices.WellnessDevicesAssets;

public class AssetsDto {
    private String device;
    private String partnerLogoUrl;
    private String partnerDescription;
    private String partnerWebsiteUrl;
    private String aboutPartnerContentId;
    private String stepsToLinkContentId;

    public AssetsDto(WellnessDevicesAssets src){
        this.device = src.getDevice();
        this.partnerLogoUrl = src.getPartnerLogoUrl();
        this.partnerDescription = src.getPartnerDescription();
        this.partnerWebsiteUrl = src.getPartnerWebsiteUrl();
        this.aboutPartnerContentId = src.getAboutPartnerContentId();
        this.stepsToLinkContentId = src.getStepsToLinkContentId();
    }

    public String getDevice() {
        return device;
    }

    public String getPartnerLogoUrl() {
        return partnerLogoUrl;
    }

    public String getPartnerDescription() {
        return partnerDescription;
    }

    public String getPartnerWebsiteUrl() {
        return partnerWebsiteUrl == null ? "" : partnerWebsiteUrl;
    }

    public String getAboutPartnerContentId() {
        return aboutPartnerContentId == null ? "" : aboutPartnerContentId;
    }

    public String getStepsToLinkContentId() {
        return stepsToLinkContentId == null ? "" : stepsToLinkContentId;
    }
}
