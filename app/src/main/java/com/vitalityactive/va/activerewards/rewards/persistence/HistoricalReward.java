package com.vitalityactive.va.activerewards.rewards.persistence;

import com.vitalityactive.va.persistence.Model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class HistoricalReward extends RealmObject implements Model {
    @PrimaryKey
    int id;
}
