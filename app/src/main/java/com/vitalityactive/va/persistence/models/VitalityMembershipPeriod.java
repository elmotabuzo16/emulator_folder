package com.vitalityactive.va.persistence.models;

import com.vitalityactive.va.networking.model.PointsHistoryServiceResponse;
import com.vitalityactive.va.persistence.Model;

import io.realm.RealmObject;

public class VitalityMembershipPeriod extends RealmObject implements Model {

    private String effectiveFrom = "";
    private String effectiveTo = "";
    private int carryOverPoints;
    private int pointsTotal;

    public VitalityMembershipPeriod() {

    }

    public VitalityMembershipPeriod(PointsHistoryServiceResponse.PointsAccount pointsAccount) {
        effectiveFrom = pointsAccount.effectiveFrom;
        effectiveTo = pointsAccount.effectiveTo;
        carryOverPoints = pointsAccount.carryOverPoints;
        pointsTotal = pointsAccount.pointsTotal;
    }

    public String getEffectiveFrom() {
        return effectiveFrom;
    }

    public String getEffectiveTo() {
        return effectiveTo;
    }

    public int getCarryOverPoints() {
        return carryOverPoints;
    }

    public int getPointsTotal() {
        return pointsTotal;
    }
}
