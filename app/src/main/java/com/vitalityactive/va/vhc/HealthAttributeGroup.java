package com.vitalityactive.va.vhc;

import com.vitalityactive.va.dto.HealthAttributeEventType;
import com.vitalityactive.va.persistence.Model;

import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;

public class HealthAttributeGroup extends RealmObject implements Model {
    private int featureType;
    private RealmList<HealthAttributeEventType> healthAttributeEventTypes = new RealmList<>();
    private String groupDescription;
    private String groupSubTitle;

    public HealthAttributeGroup() {
    }

    public HealthAttributeGroup(int featureType,
                                String groupDescription,
                                String groupSubTitle) {
        this.featureType = featureType;
        this.groupDescription = groupDescription;
        this.groupSubTitle = groupSubTitle;
    }

    public List<HealthAttributeEventType> getAllHealthAttributeEventTypes() {
        return healthAttributeEventTypes;
    }


    public void addHealthAttributeEventType(HealthAttributeEventType healthAttributeEventType) {
        healthAttributeEventTypes.add(healthAttributeEventType);
    }

    public String getGroupDescription() {
        return groupDescription;
    }

    public int getFeatureType() {
        return featureType;
    }

    public String getGroupSubTitle() {
        return groupSubTitle;
    }
}
