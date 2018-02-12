package com.vitalityactive.va.persistence.models.myhealth;


import com.vitalityactive.va.networking.model.HealthAttributeInformationResponse;
import com.vitalityactive.va.persistence.Model;

import io.realm.RealmList;
import io.realm.RealmObject;

public class HealthInformationSection extends RealmObject implements Model {

    private RealmList<Attribute> attributes = new RealmList<Attribute>();
    public Integer sortOrder;
    public String typeCode;
    public Integer typeKey;
    public String typeName;
    public Integer parentTypeKey;

    public HealthInformationSection() {
    }

    public HealthInformationSection(HealthAttributeInformationResponse.Section section, Integer parentTypeKey) {
        this.attributes = toAttributes(section);
        this.typeCode = section.typeCode;
        this.typeKey = section.typeKey;
        this.typeName = section.typeName;
        this.sortOrder = section.sortOrder;
        this.parentTypeKey = parentTypeKey;
    }

    public RealmList<Attribute> toAttributes(HealthAttributeInformationResponse.Section section) {
        RealmList<Attribute> attributes = new RealmList<>();
        if (section != null && section.attributes != null) {
            for (HealthAttributeInformationResponse.Attribute attribute : section.attributes) {
                attributes.add(new Attribute(attribute));
            }
        }
        return attributes;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public String getTypeCode() {
        return typeCode;
    }

    public Integer getTypeKey() {
        return typeKey;
    }

    public String getTypeName() {
        return typeName;
    }

    public RealmList<Attribute> getAttributes() {
        return attributes;
    }

    public Integer getParentTypeKey() {
        return parentTypeKey;
    }
}
