package com.vitalityactive.va.vitalitystatus.repository;

import com.vitalityactive.va.home.events.ProductFeaturePointsResponse;
import com.vitalityactive.va.persistence.Model;
import com.vitalityactive.va.utilities.InvalidModelLogger;
import com.vitalityactive.va.wellnessdevices.Condition;
import com.vitalityactive.va.wellnessdevices.pointsmonitor.service.EventType;

import io.realm.RealmList;
import io.realm.RealmObject;

public class PotentialPointsModel extends RealmObject implements Model {
    private int potentialPoints;
    public RealmList<ConditionsModel> conditionsModel;

    public PotentialPointsModel() {}

    public static PotentialPointsModel create(ProductFeaturePointsResponse.PotentialPointses response) {
        if (response.potentialPoints == null) {
            InvalidModelLogger.warn(response, 0, "required fields are null");
            return null;
        }

        PotentialPointsModel instance = new PotentialPointsModel();

        instance.potentialPoints = response.potentialPoints;

        instance.conditionsModel = new RealmList<>();

        if (response.conditions != null) {
            for(EventType.Conditions condition : response.conditions) {
                ConditionsModel productSubfeatureConditionsModel = ConditionsModel.create(condition);
                if (productSubfeatureConditionsModel != null) {
                instance.conditionsModel.add(productSubfeatureConditionsModel);
                }
            }
        }

        return instance;
    }

    public RealmList<ConditionsModel> getConditions() {
        return conditionsModel;
    }

    public int getPotentialPoints() {
        return potentialPoints;
    }
}
