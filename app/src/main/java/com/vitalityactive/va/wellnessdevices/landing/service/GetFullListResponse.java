package com.vitalityactive.va.wellnessdevices.landing.service;

import com.google.gson.annotations.SerializedName;
import com.vitalityactive.va.wellnessdevices.dto.PartnerDto;
import com.vitalityactive.va.wellnessdevices.dto.PartnerLinkDto;

import java.util.List;

public class GetFullListResponse {
    @SerializedName("markets")
    public List<Market> markets;

    public static class Market {
        @SerializedName("partner")
        public Partner partner;
        @SerializedName("assets")
        public Assets assets;
    }

    public static class Assets {
        @SerializedName("partnerLogoUrl")
        public String partnerLogoUrl;
        @SerializedName("partnerDescription")
        public String partnerDescription;
        @SerializedName("partnerWebsiteUrl")
        public String partnerWebsiteUrl;
        @SerializedName("aboutPartnerContentId")
        public String aboutPartnerContentId;
        @SerializedName("stepsToLinkContentId")
        public String stepsToLinkContentId;
    }

    public static class Partner{
        @SerializedName("partnerSystem")
        public String partnerSystem;
        @SerializedName("device")
        public String device;
        @SerializedName("partnerLink")
        public PartnerLink partnerLink;
        @SerializedName("partnerDelink")
        public PartnerLink partnerDelink;
        @SerializedName("partnerSync")
        public PartnerLink partnerSync;
        @SerializedName("partnerLinkedStatus")
        public String partnerLinkedStatus;
        @SerializedName("partnerLastSync")
        public String partnerLastSync;
        @SerializedName("partnerLastWorkout")
        public String partnerLastWorkout;

        public Partner(){}

        public Partner(PartnerDto dto){
            this.partnerSystem = dto.getPartnerSystem();
            this.device = dto.getDevice();
            this.partnerLink = new PartnerLink(dto.getPartnerLink());
            this.partnerDelink = new PartnerLink(dto.getPartnerDelink());
            this.partnerSync = new PartnerLink(dto.getPartnerSync());
            this.partnerLinkedStatus = dto.getPartnerLinkedStatus();
            this.partnerLastSync = dto.getPartnerLastSync();
            this.partnerLastWorkout = dto.getPartnerLastWorkout();
        }
    }

    public static class PartnerLink{
        @SerializedName("url")
        public String url;
        @SerializedName("method")
        public String method;
        @SerializedName("redirectUrl")
        public String redirectUrl;

        public PartnerLink(){}

        public PartnerLink(PartnerLinkDto dto){
            this.url = dto.getUrl();
            this.method = dto.getMethod();
        }
    }
}
