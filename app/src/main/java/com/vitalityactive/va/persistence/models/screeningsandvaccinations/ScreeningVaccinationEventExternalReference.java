package com.vitalityactive.va.persistence.models.screeningsandvaccinations;

import com.vitalityactive.va.home.service.EventByPartyResponse;
import com.vitalityactive.va.persistence.Model;

import io.realm.RealmObject;

/**
 * Created by dharel.h.rosell on 12/8/2017.
 */

public class ScreeningVaccinationEventExternalReference extends RealmObject implements Model {

    public String typeCode;
    public int typeKey;
    public String typeName;
    public String value;

    public ScreeningVaccinationEventExternalReference() {
    }

    public ScreeningVaccinationEventExternalReference(EventByPartyResponse.EventExternalReference externalReference) {

        this.typeCode = externalReference.getTypeCode();
        this.typeKey = externalReference.getTypeKey();
        this.typeName = externalReference.getTypeName();
        this.value = externalReference.getValue();
    }

    public String getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

    public int getTypeKey() {
        return typeKey;
    }

    public void setTypeKey(int typeKey) {
        this.typeKey = typeKey;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
