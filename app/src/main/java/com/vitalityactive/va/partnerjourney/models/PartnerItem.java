package com.vitalityactive.va.partnerjourney.models;

import com.vitalityactive.va.persistence.Model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class PartnerItem extends RealmObject implements Model {
    @PrimaryKey
    public long id;
    public int category;
    public String title;
    public String details;
    public String logoFileName;
    public String longDescription;

    public PartnerItem() {
    }

    public PartnerItem(int category, long id, String title, String details, String longDescription, String logoFileName) {
        this.category = category;
        this.id = id;
        this.title = title;
        this.details = details;
        this.longDescription = longDescription;
        this.logoFileName = logoFileName;
    }
}
