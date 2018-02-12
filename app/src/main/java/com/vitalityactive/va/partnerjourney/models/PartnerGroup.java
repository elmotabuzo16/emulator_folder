package com.vitalityactive.va.partnerjourney.models;

import com.vitalityactive.va.persistence.Model;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class PartnerGroup extends RealmObject implements Model {
    @PrimaryKey
    public int type;
    public String name;
    public RealmList<PartnerItem> items;

    public PartnerGroup() {
    }

    public PartnerGroup(int type, String name) {
        this.type = type;
        this.name = name;
        items = new RealmList<>();
    }
}
