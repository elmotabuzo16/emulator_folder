package com.vitalityactive.va.persistence.models;

import com.vitalityactive.va.networking.model.LoginServiceResponse;
import com.vitalityactive.va.persistence.Model;

import io.realm.RealmObject;

public class CurrentVitalityMembershipPeriod extends RealmObject implements Model{
    private String effectiveFrom;
    private String effectiveTo;

    public CurrentVitalityMembershipPeriod() {}

    public CurrentVitalityMembershipPeriod(LoginServiceResponse.CurrentVitalityMembershipPeriod currentVitalityMembershipPeriod) {
        this.effectiveFrom = currentVitalityMembershipPeriod.effectiveFrom;
        this.effectiveTo = currentVitalityMembershipPeriod.effectiveTo;
    }

    public String getEffectiveTo() {
        return effectiveTo;
    }

    public String getEffectiveFrom() {
        return effectiveFrom;
    }
}
