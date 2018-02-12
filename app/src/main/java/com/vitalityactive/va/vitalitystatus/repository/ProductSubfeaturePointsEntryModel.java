package com.vitalityactive.va.vitalitystatus.repository;

import com.vitalityactive.va.home.events.ProductFeaturePointsResponse;
import com.vitalityactive.va.persistence.Model;
import com.vitalityactive.va.utilities.InvalidModelLogger;

import io.realm.RealmObject;

public class ProductSubfeaturePointsEntryModel extends RealmObject implements Model{
    private String categoryCode;
    private Integer categoryKey;
    private int categoryLimit;
    private String categoryName;
    private Integer frequencyLimit;
    private Integer maximumMembershipPeriodPoints;
    private Integer pointsEarned;
    private int pointsEntryLimit;
    private Integer potentialPoints;
    private String typeCode;
    private Integer typeKey;
    private String typeName;
    private ProductSubfeaturePotentialPointsModel productSubfeaturePotentialPointsModel;

    public ProductSubfeaturePointsEntryModel() {}

    public static ProductSubfeaturePointsEntryModel create(ProductFeaturePointsResponse.ProductSubfeaturePointsEntry response) {
        if (response.categoryKey == null
                || response.frequencyLimit == null
                || response.maximumMembershipPeriodPoints == null
                || response.pointsEarned == null
                || response.potentialPoints == null
                || response.typeKey == null) {
            InvalidModelLogger.warn(response, response.typeKey, "required fields are null");
            return null;
        }

        ProductSubfeaturePointsEntryModel instance = new ProductSubfeaturePointsEntryModel();

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

        if (response.productSubfeaturePotentialPointses != null) {
            instance.productSubfeaturePotentialPointsModel = ProductSubfeaturePotentialPointsModel.create(response.productSubfeaturePotentialPointses);
        }

        return instance;
    }

    public int getTypeKey() {
        return typeKey;
    }

    public Integer getPotentialPoints() {
        return potentialPoints;
    }
}
