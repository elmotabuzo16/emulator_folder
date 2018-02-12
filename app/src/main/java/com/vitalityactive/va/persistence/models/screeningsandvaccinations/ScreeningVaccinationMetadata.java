package com.vitalityactive.va.persistence.models.screeningsandvaccinations;

import com.vitalityactive.va.home.service.EventByPartyResponse;
import com.vitalityactive.va.persistence.Model;

import io.realm.RealmObject;

/**
 * Created by dharel.h.rosell on 12/8/2017.
 */

public class ScreeningVaccinationMetadata extends RealmObject implements Model {

        public String typeCode;
        public int typeKey;
        public String typeName;
        public String unitOfMeasure;
        public String value;

    public ScreeningVaccinationMetadata() {
    }

    public ScreeningVaccinationMetadata(EventByPartyResponse.EventMetaData metaData) {

        this.typeCode = metaData.getTypeCode();
        this.typeKey = metaData.getTypeKey();
        this.typeName = metaData.getTypeName();
        this.unitOfMeasure = metaData.getUnitOfMeasure();
        this.value = metaData.getValue();
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

    public String getUnitOfMeasure() {
        return unitOfMeasure;
    }

    public void setUnitOfMeasure(String unitOfMeasure) {
        this.unitOfMeasure = unitOfMeasure;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
