package com.vitalityactive.va.partnerjourney.models;

import com.vitalityactive.va.persistence.Model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class PartnerItemDetails extends RealmObject implements Model {
    @PrimaryKey
    public long id;
    private String mainContent;
    private String url;

    public PartnerItemDetails() {
    }

    public PartnerItemDetails(long id, String mainContent, String url) {
        this.id = id;
        this.mainContent = mainContent;
        this.url = url;
    }

    public String getMainContent() {
        return mainContent;
    }

    public String getUrl() {
        return url;
    }

    public static PartnerItemDetails copy(PartnerItemDetails model) {
        return new PartnerItemDetails(model.id, model.mainContent, model.url);
    }
}
