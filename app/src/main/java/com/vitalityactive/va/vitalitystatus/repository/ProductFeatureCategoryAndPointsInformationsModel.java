package com.vitalityactive.va.vitalitystatus.repository;

import com.vitalityactive.va.home.events.ProductFeaturePointsResponse;
import com.vitalityactive.va.persistence.Model;

import io.realm.RealmList;
import io.realm.RealmObject;

public class ProductFeatureCategoryAndPointsInformationsModel extends RealmObject implements Model {
    public RealmList<ProductFeatureCategoryAndPointsInformationModel> productFeatureCategoryAndPointsInformationsModel;

    public ProductFeatureCategoryAndPointsInformationsModel() {
    }

    public static Model create(ProductFeaturePointsResponse response) {
        ProductFeatureCategoryAndPointsInformationsModel instance = new ProductFeatureCategoryAndPointsInformationsModel();

        instance.productFeatureCategoryAndPointsInformationsModel = new RealmList<>();
        for (ProductFeaturePointsResponse.ProductFeatureCategoryAndPointsInformation informationModel : response.productFeatureCategoryAndPointsInformations) {
            ProductFeatureCategoryAndPointsInformationModel model = ProductFeatureCategoryAndPointsInformationModel.create(informationModel);
            if (model != null) {
                instance.productFeatureCategoryAndPointsInformationsModel.add(model);
            }
        }

        return instance;
    }
}
