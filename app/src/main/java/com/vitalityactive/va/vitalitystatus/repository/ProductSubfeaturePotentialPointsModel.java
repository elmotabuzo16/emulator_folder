package com.vitalityactive.va.vitalitystatus.repository;

import com.vitalityactive.va.home.events.ProductFeaturePointsResponse;
import com.vitalityactive.va.persistence.Model;
import com.vitalityactive.va.utilities.InvalidModelLogger;

import io.realm.RealmObject;

public class ProductSubfeaturePotentialPointsModel extends RealmObject implements Model{
    private int potentialPoints;
    public ProductSubfeatureConditionsModel productSubfeatureConditionsModel;

    public ProductSubfeaturePotentialPointsModel() {}

    public static ProductSubfeaturePotentialPointsModel create(ProductFeaturePointsResponse.ProductSubfeaturePotentialPointses response) {
        if (response.potentialPoints == null) {
            InvalidModelLogger.warn(response, 0, "required fields are null");
            return null;
        }

        ProductSubfeaturePotentialPointsModel instance = new ProductSubfeaturePotentialPointsModel();

        instance.potentialPoints = response.potentialPoints;
        instance.productSubfeatureConditionsModel = ProductSubfeatureConditionsModel.create(response.productSubfeatureConditions);

        return instance;
    }
}
