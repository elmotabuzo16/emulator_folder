package com.vitalityactive.va.persistence.models.goalandprogress;

import com.vitalityactive.va.persistence.Model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by dloskot on 8/15/17.
 */

public class EntryMetadata extends RealmObject implements Model {
    private String typeCode;
    @PrimaryKey
    private int typeKey;
    private String typeName;
    private String unitOfMeasure;
    private String value;

    public EntryMetadata() {}

    public EntryMetadata(com.vitalityactive.va.networking.model.goalandprogress.EntryMetadata entryMetadata) {
        this.typeCode = entryMetadata.typeCode;
        this.typeKey = entryMetadata.typeKey;
        this.typeName = entryMetadata.typeName;
        this.unitOfMeasure = entryMetadata.unitOfMeasure;
        this.value = entryMetadata.value;
    }

    public String getTypeCode() {
        return typeCode;
    }

    public int getTypeKey() {
        return typeKey;
    }

    public String getTypeName() {
        return typeName;
    }

    public String getUnitOfMeasure() {
        return unitOfMeasure;
    }

    public String getValue() {
        return value;
    }
}
