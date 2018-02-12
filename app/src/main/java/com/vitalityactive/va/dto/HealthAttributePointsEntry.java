package com.vitalityactive.va.dto;

import com.vitalityactive.va.persistence.Model;
import com.vitalityactive.va.vhc.service.HealthAttributeResponse;

import io.realm.RealmList;
import io.realm.RealmObject;

public class HealthAttributePointsEntry extends RealmObject implements Model {
    private RealmList<HealthAttributePointsEntryReason> reasons;

    public HealthAttributePointsEntry() {}

    public HealthAttributePointsEntry(HealthAttributeResponse.PointsEntry pointsEntry) {
        reasons = new RealmList<>();
        if (pointsEntry.reasons != null) {
            for (HealthAttributeResponse.Reason reason : pointsEntry.reasons) {
                reasons.add(new HealthAttributePointsEntryReason(reason));
            }
        }
    }

    public RealmList<HealthAttributePointsEntryReason> getReasons() {
        return reasons;
    }
}
