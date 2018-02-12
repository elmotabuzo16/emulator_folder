package com.vitalityactive.va.dto;

import android.support.annotation.NonNull;

import com.vitalityactive.va.persistence.Model;
import com.vitalityactive.va.vhc.service.HealthAttributeResponse;

import io.realm.RealmObject;

public class HealthAttributePointsEntryReason extends RealmObject implements Model {
    private int reasonKey;
    private String reasonName = "";

    public HealthAttributePointsEntryReason(){}

    public HealthAttributePointsEntryReason(HealthAttributeResponse.Reason reason){
        reasonName = reason.reasonName;
        reasonKey = reason.reasonKey;
    }

    public String getReasonName() {
        return reasonName;
    }

    public int getReasonKey() {
        return reasonKey;
    }
}
