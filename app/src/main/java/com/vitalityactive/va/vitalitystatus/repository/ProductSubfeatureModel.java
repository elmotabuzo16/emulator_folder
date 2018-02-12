package com.vitalityactive.va.vitalitystatus.repository;

import com.vitalityactive.va.home.events.ProductFeaturePointsResponse;
import com.vitalityactive.va.persistence.Model;
import com.vitalityactive.va.utilities.InvalidModelLogger;

import io.realm.RealmList;
import io.realm.RealmObject;

public class ProductSubfeatureModel extends RealmObject implements Model {
    private String code;
    private Integer key;
    private String name;
    private Integer pointsEarned;
    private String pointsEarningFlag;
    private Integer potentialPoints;
    private Integer potentialPoints1;
    private RealmList<ProductSubfeatureEventTypeModel> productSubfeatureEventTypesModel;
    private RealmList<ProductSubfeaturePointsEntryModel> productSubfeaturePointsEntriesModel;

    public ProductSubfeatureModel() {
    }

    public static ProductSubfeatureModel create(ProductFeaturePointsResponse.ProductSubfeature response) {
        if (response.key == null
                || response.pointsEarned == null
                || response.pointsEarningFlag == null
                || response.potentialPoints == null) {

            InvalidModelLogger.warn(response, response.key, "required fields are null");
            return null;
        }

        ProductSubfeatureModel instance = new ProductSubfeatureModel();

        instance.code = response.code;
        instance.key = response.key;
        instance.name = response.name;
        instance.pointsEarned = response.pointsEarned;
        instance.pointsEarningFlag = response.pointsEarningFlag;
        instance.potentialPoints = response.potentialPoints;
        instance.potentialPoints1 = response.potentialPoints;

        instance.productSubfeaturePointsEntriesModel = new RealmList<>();
        if (response.productSubfeaturePointsEntries != null) {
            for (ProductFeaturePointsResponse.ProductSubfeaturePointsEntry pointsEntry : response.productSubfeaturePointsEntries) {
                ProductSubfeaturePointsEntryModel productSubfeaturePointsEntriesModel = ProductSubfeaturePointsEntryModel.create(pointsEntry);
                if (productSubfeaturePointsEntriesModel != null) {
                    instance.productSubfeaturePointsEntriesModel.add(productSubfeaturePointsEntriesModel);
                }
            }
        }

        instance.productSubfeatureEventTypesModel = new RealmList<>();
        if (response.productSubfeatureEventTypes != null) {
            for (ProductFeaturePointsResponse.ProductSubfeatureEventType eventType : response.productSubfeatureEventTypes) {
                ProductSubfeatureEventTypeModel productSubfeatureEventTypeModel = ProductSubfeatureEventTypeModel.create(eventType);
                if (productSubfeatureEventTypeModel != null) {
                    instance.productSubfeatureEventTypesModel.add(productSubfeatureEventTypeModel);
                }
            }
        }

        return instance;
    }

    public String getName() {
        return name;
    }

    public Integer getPotentialPoints() {
        return potentialPoints;
    }

    public Integer getKey() {
        return key;
    }

    public String getPointsEarningFlag() {
        return pointsEarningFlag;
    }

    public RealmList<ProductSubfeaturePointsEntryModel> getPointsEntries() {
        return productSubfeaturePointsEntriesModel;
    }
}
