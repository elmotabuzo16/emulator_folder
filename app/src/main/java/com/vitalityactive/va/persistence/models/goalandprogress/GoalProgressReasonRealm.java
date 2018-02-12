package com.vitalityactive.va.persistence.models.goalandprogress;

import com.vitalityactive.va.networking.model.goalandprogress.Reason;
import com.vitalityactive.va.persistence.Model;

import io.realm.RealmObject;

public class GoalProgressReasonRealm extends RealmObject implements Model {
    private String categoryCode;
    private int categoryKey;
    private int reasonKey;
    private String reasonName;
    private String reasonCode;
    private String categoryName;

    public GoalProgressReasonRealm(){}

    public GoalProgressReasonRealm(Reason reason){
        this.categoryCode = reason.categoryCode;
        this.categoryKey = reason.categoryKey;
        this.reasonKey = reason.reasonKey;
        this.reasonName = reason.reasonName;
        this.reasonCode = reason.reasonCode;
        this.categoryName = reason.categoryName;
    }

    public String getCategoryCode() {
        return categoryCode;
    }

    public int getCategoryKey() {
        return categoryKey;
    }

    public int getReasonKey() {
        return reasonKey;
    }

    public String getReasonName() {
        return reasonName;
    }

    public String getReasonCode() {
        return reasonCode;
    }

    public String getCategoryName() {
        return categoryName;
    }
}
