package com.vitalityactive.va.activerewards.rewards.persistence;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.vitalityactive.va.persistence.Model;

import java.util.List;

import io.realm.RealmObject;

public class PartnerEmail extends RealmObject implements Model {
    @NonNull
    private String value = "";

    public PartnerEmail() {

    }

    public PartnerEmail(@NonNull String value) {
        this.value = value;
    }

    @NonNull
    public String getValue() {
        return value;
    }
}
