package com.vitalityactive.va.vhc.captureresults.models;

import com.vitalityactive.va.persistence.Model;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class CapturedGroup extends RealmObject implements Model {
    @PrimaryKey
    private int typeKey;
    private RealmList<CapturedField> capturedFields = new RealmList<>();
    private String groupDescription;

    public CapturedGroup() {
    }

    public CapturedGroup(GroupType type, String groupDescription) {
        this.typeKey = type.getTypeKey();
        this.groupDescription = groupDescription;
    }

    public GroupType getGroupType() {
        return GroupType.fromValue(typeKey);
    }

    public CapturedField addCapturedField(String key) {
        CapturedField field = new CapturedField(key);
        capturedFields.add(field);
        return field;
    }

    public RealmList<CapturedField> getCapturedFields() {
        return capturedFields;
    }

    public CapturedField getCapturedField(String fieldKey) {
        for (CapturedField capturedField : capturedFields) {
            if (capturedField.key.equals(fieldKey)) {
                return capturedField;
            }
        }
        return null;
    }

    public String getGroupDescription() {
        return groupDescription;
    }
}
