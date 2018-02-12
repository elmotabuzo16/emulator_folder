package com.vitalityactive.va.persistence.models.wellnessdevices;

import com.vitalityactive.va.persistence.Model;
import com.vitalityactive.va.wellnessdevices.landing.service.GetFullListResponse;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class WellnessDevicesPartner extends RealmObject implements Model {
    private String partnerSystem;
    @PrimaryKey
    private String device;
    private WellnessDevicesPartnerLink partnerLink;
    private WellnessDevicesPartnerLink partnerDelink;
    private WellnessDevicesPartnerLink partnerSync;
    private String partnerLinkedStatus;
    private String partnerLastSync;
    private String partnerLastWorkout;

    public WellnessDevicesPartner(){}

    public WellnessDevicesPartner(GetFullListResponse.Partner partner){
        this.partnerSystem = partner.partnerSystem;
        this.device = partner.device;
        this.partnerLink = new WellnessDevicesPartnerLink(partner.partnerLink);
        this.partnerDelink = new WellnessDevicesPartnerLink(partner.partnerDelink);
        this.partnerSync = new WellnessDevicesPartnerLink(partner.partnerSync);
        this.partnerLinkedStatus = partner.partnerLinkedStatus;
        this.partnerLastSync = partner.partnerLastSync;
        this.partnerLastWorkout = partner.partnerLastWorkout;
    }

    public String getPartnerSystem() {
        return partnerSystem;
    }

    public String getDevice() {
        return device;
    }

    public WellnessDevicesPartnerLink getPartnerLink() {
        return partnerLink;
    }

    public WellnessDevicesPartnerLink getPartnerDelink() {
        return partnerDelink;
    }

    public WellnessDevicesPartnerLink getPartnerSync() {
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
}
