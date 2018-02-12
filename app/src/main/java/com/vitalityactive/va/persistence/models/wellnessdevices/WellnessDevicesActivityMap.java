package com.vitalityactive.va.persistence.models.wellnessdevices;

import com.vitalityactive.va.persistence.Model;
import com.vitalityactive.va.persistence.models.base.RealmInt;

import java.util.Map;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class WellnessDevicesActivityMap extends RealmObject implements Model {
    @PrimaryKey
    private String device;
    private RealmList<RealmInt> features;

    public WellnessDevicesActivityMap() {
    }

    public WellnessDevicesActivityMap(Map.Entry<String, int[]> src) {
        this.device = src.getKey();
        features = new RealmList<>();
        for (int feature : src.getValue()) {
            features.add(new RealmInt(feature));
        }
    }

    public String getDevice() {
        return device;
    }

    public RealmList<RealmInt> getFeatures() {
        return features;
    }
}
