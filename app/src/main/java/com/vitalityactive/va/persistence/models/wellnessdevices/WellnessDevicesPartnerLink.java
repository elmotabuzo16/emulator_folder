package com.vitalityactive.va.persistence.models.wellnessdevices;

import com.vitalityactive.va.persistence.Model;
import com.vitalityactive.va.wellnessdevices.landing.service.GetFullListResponse;

import io.realm.RealmObject;

public class WellnessDevicesPartnerLink extends RealmObject implements Model {
    private String url;

    private String method;

    public WellnessDevicesPartnerLink() {
    }

    public WellnessDevicesPartnerLink(GetFullListResponse.PartnerLink src) {
        if(src != null) {
            this.url = src.url;
            this.method = src.method;
        }
    }

    public String getUrl() {
        return url;
    }

    public String getMethod() {
        return method;
    }
}
