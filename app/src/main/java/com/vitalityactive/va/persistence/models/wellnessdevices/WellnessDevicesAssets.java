package com.vitalityactive.va.persistence.models.wellnessdevices;

import android.support.annotation.NonNull;

import com.vitalityactive.va.persistence.Model;
import com.vitalityactive.va.wellnessdevices.landing.service.GetFullListResponse;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class WellnessDevicesAssets extends RealmObject implements Model {
    @PrimaryKey
    private String device;
    private String partnerLogoUrl;
    private String partnerDescription;
    private String partnerWebsiteUrl;
    private String aboutPartnerContentId;
    private String stepsToLinkContentId;

    public WellnessDevicesAssets(){}

    public WellnessDevicesAssets(@NonNull String device,
                                 GetFullListResponse.Assets assets){
        this.device = device;
        this.partnerLogoUrl = assets.partnerLogoUrl;
        this.partnerDescription = assets.partnerDescription;
        this.partnerWebsiteUrl = assets.partnerWebsiteUrl;
        this.aboutPartnerContentId = assets.aboutPartnerContentId;
        this.stepsToLinkContentId = assets.stepsToLinkContentId;
    }

    public WellnessDevicesAssets(GetFullListResponse.Market market){
        this.device = market.partner.device;
        this.partnerLogoUrl = market.assets.partnerLogoUrl;
        this.partnerDescription = market.assets.partnerDescription;
        this.partnerWebsiteUrl = market.assets.partnerWebsiteUrl;
        this.aboutPartnerContentId = market.assets.aboutPartnerContentId;
        this.stepsToLinkContentId = market.assets.stepsToLinkContentId;
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
        return partnerWebsiteUrl;
    }

    public String getAboutPartnerContentId() {
        return aboutPartnerContentId;
    }

    public String getStepsToLinkContentId() {
        return stepsToLinkContentId;
    }
}
