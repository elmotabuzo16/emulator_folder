package com.vitalityactive.va.vitalitystatus.repository;

import com.vitalityactive.va.home.events.ProductFeaturePointsResponse;
import com.vitalityactive.va.persistence.Model;
import com.vitalityactive.va.utilities.InvalidModelLogger;

import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;

public class ProductFeatureCategoryAndPointsInformationModel extends RealmObject implements Model {
    public String code;
    private String name;
    private int key;
    private Integer pointsCategoryLimit;
    private int pointsEarned;
    private String pointsEarningFlag;
    private int potentialPoints;
    private String typeCode;
    private int typeKey;
    private String typeName;
    private RealmList<ProductFeatureAndPointsInformationModel> productFeatureAndPointsInformations;

    public ProductFeatureCategoryAndPointsInformationModel() {
    }

    public static ProductFeatureCategoryAndPointsInformationModel create(ProductFeaturePointsResponse.ProductFeatureCategoryAndPointsInformation response) {
        if (response.key == null
                || response.pointsEarned == null
                || response.pointsEarningFlag == null
                || response.potentialPoints == null) {
            InvalidModelLogger.warn(response, response.key, "required fields are null");
            return null;
        }

        ProductFeatureCategoryAndPointsInformationModel instance = new ProductFeatureCategoryAndPointsInformationModel();

        instance.code = response.code;
        instance.key = response.key;
        instance.name = response.name;
        instance.pointsCategoryLimit = response.pointsCategoryLimit;
        instance.pointsEarned = response.pointsEarned;
        instance.pointsEarningFlag = response.pointsEarningFlag;
        instance.potentialPoints = response.potentialPoints;
        instance.typeCode = response.typeCode;
        instance.typeKey = response.typeKey;
        instance.typeName = response.typeName;

        instance.productFeatureAndPointsInformations = new RealmList<>();
        if (response.productFeatureAndPointsInformations != null) {
            for (ProductFeaturePointsResponse.ProductFeatureAndPointsInformation productFeatureAndPointsInformation : response.productFeatureAndPointsInformations) {
                ProductFeatureAndPointsInformationModel pointsInformationModel = ProductFeatureAndPointsInformationModel.create(productFeatureAndPointsInformation);
                if (pointsInformationModel != null) {
                    instance.productFeatureAndPointsInformations.add(pointsInformationModel);
                }
            }
        }

        return instance;
    }

    public int getKey() {
        return key;
    }

    public String getPointsEarningFlag() {
        return pointsEarningFlag;
    }

    public int getPotentialPoints() {
        return potentialPoints;
    }

    public RealmList<ProductFeatureAndPointsInformationModel> getProductFeatureAndPointsInformations() {
        return productFeatureAndPointsInformations;
    }

    public String getName() {
        return name;
    }

    public int getPointsEarned() {
        return pointsEarned;
    }

    public Integer getPointsCategoryLimit() {
        return pointsCategoryLimit;
    }
}