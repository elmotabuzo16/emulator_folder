package com.vitalityactive.va.vitalitystatus.repository;

import com.vitalityactive.va.home.events.ProductFeaturePointsResponse;
import com.vitalityactive.va.persistence.Model;
import com.vitalityactive.va.utilities.InvalidModelLogger;

import io.realm.RealmList;
import io.realm.RealmObject;

public class ProductFeatureAndPointsInformationModel extends RealmObject implements Model {
    private int key;
    private String name;
    private int pointsEarned;
    private int potentialPoints;
    private String code;
    private String pointsEarningFlag;
    private RealmList<EventTypeModel> eventTypes;
    private RealmList<PointsEntryModel> pointsEntries;
    private RealmList<ProductSubfeatureModel> productSubfeatures;

    public ProductFeatureAndPointsInformationModel() {
    }

    public static ProductFeatureAndPointsInformationModel create(ProductFeaturePointsResponse.ProductFeatureAndPointsInformation response) {
        if (response.key == null
                || response.pointsEarned == null
                || response.pointsEarningFlag == null
                || response.potentialPoints == null) {

            InvalidModelLogger.warn(response, response.key, "required fields are null");
            return null;
        }

        ProductFeatureAndPointsInformationModel instance = new ProductFeatureAndPointsInformationModel();

        instance.code = response.code;
        instance.key = response.key;
        instance.name = response.name;
        instance.pointsEarned = response.pointsEarned;
        instance.pointsEarningFlag = response.pointsEarningFlag;
        instance.potentialPoints = response.potentialPoints;

        instance.productSubfeatures = new RealmList<>();
        if (response.productSubfeatures != null) {
            for (ProductFeaturePointsResponse.ProductSubfeature productSubfeature : response.productSubfeatures) {
                ProductSubfeatureModel productSubfeatureModel = ProductSubfeatureModel.create(productSubfeature);
                if (productSubfeatureModel != null) {
                    instance.productSubfeatures.add(productSubfeatureModel);
                }
            }
        }

        instance.eventTypes = new RealmList<>();
        if (response.eventTypes != null) {
            for (ProductFeaturePointsResponse.EventType eventType : response.eventTypes) {
                EventTypeModel eventTypeModel = EventTypeModel.create(eventType);
                if (eventTypeModel != null) {
                    instance.eventTypes.add(eventTypeModel);
                }
            }

        }

        instance.pointsEntries = new RealmList<>();
        if (response.pointsEntries != null) {
            for (ProductFeaturePointsResponse.PointsEntry pointsEntry : response.pointsEntries) {
                PointsEntryModel pointsEntriesModel = PointsEntryModel.create(pointsEntry);
                if (pointsEntriesModel != null) {
                    instance.pointsEntries.add(pointsEntriesModel);
                }
            }
        }

        return instance;
    }

    public int getKey() {
        return key;
    }

    public String getName() {
        return name;
    }

    public int getPotentialPoints() {
        return potentialPoints;
    }

    public String getPointsEarningFlag() {
        return pointsEarningFlag;
    }

    public RealmList<ProductSubfeatureModel> getSubfeatures() {
        return productSubfeatures;
    }

    public RealmList<PointsEntryModel> getPointsEntries() {
        return pointsEntries;
    }
}
