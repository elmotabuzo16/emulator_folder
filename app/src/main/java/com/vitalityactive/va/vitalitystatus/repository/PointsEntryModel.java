package com.vitalityactive.va.vitalitystatus.repository;

import com.vitalityactive.va.home.events.ProductFeaturePointsResponse;
import com.vitalityactive.va.persistence.Model;
import com.vitalityactive.va.utilities.InvalidModelLogger;

import io.realm.RealmList;
import io.realm.RealmObject;

public class PointsEntryModel extends RealmObject implements Model {
    public String categoryCode;
    public Integer categoryKey;
    public int categoryLimit;
    public String categoryName;
    public Integer frequencyLimit;
    public Integer maximumMembershipPeriodPoints;
    public Integer pointsEarned;
    public String pointsEntryLimit;
    public Integer potentialPoints;
    public RealmList<PotentialPointsModel> potentialPointsModel;
    public String typeCode;
    public Integer typeKey;
    public String typeName;

    public PointsEntryModel() {
    }

    public static PointsEntryModel create(ProductFeaturePointsResponse.PointsEntry response) {
        if (response.categoryKey == null
                || response.frequencyLimit == null
                || response.maximumMembershipPeriodPoints == null
                || response.pointsEarned == null
                || response.potentialPoints == null
                || response.typeKey == null) {
            InvalidModelLogger.warn(response, response.typeKey, "required fields are null");
            return null;
        }

        PointsEntryModel instance = new PointsEntryModel();

        instance.categoryCode = response.categoryCode;
        instance.categoryKey = response.categoryKey;
        instance.categoryLimit = response.categoryLimit;
        instance.categoryName = response.categoryName;
        instance.frequencyLimit = response.frequencyLimit;
        instance.maximumMembershipPeriodPoints = response.maximumMembershipPeriodPoints;
        instance.pointsEarned = response.pointsEarned;
        instance.pointsEntryLimit = response.pointsEntryLimit;
        instance.potentialPoints = response.potentialPoints;
        instance.typeCode = response.typeCode;
        instance.typeKey = response.typeKey;
        instance.typeName = response.typeName;

        instance.potentialPointsModel = new RealmList<>();
        if (response.productSubfeaturePotentialPointses != null) {
            for (ProductFeaturePointsResponse.PotentialPointses responsePoints : response.productSubfeaturePotentialPointses) {
                PotentialPointsModel object = PotentialPointsModel.create(responsePoints);
                if (object != null) {
                    instance.potentialPointsModel.add(object);
                }
            }
        }

        return instance;
    }

    public Integer getPotentialPoints() {
        return potentialPoints;
    }

    public Integer getTypeKey() {
        return typeKey;
    }

    public RealmList<PotentialPointsModel> getPotentialPointsModels() {
        return potentialPointsModel;
    }
}
