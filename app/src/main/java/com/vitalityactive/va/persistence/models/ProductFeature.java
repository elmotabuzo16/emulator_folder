package com.vitalityactive.va.persistence.models;

import android.support.annotation.Nullable;

import com.vitalityactive.va.networking.model.LoginServiceResponse;
import com.vitalityactive.va.persistence.Model;

import io.realm.RealmList;
import io.realm.RealmObject;

public class ProductFeature extends RealmObject implements Model {
    private int type;
    private int featureType;
    private String typeCode;
    private String typeName;
    private String categoryName;
    private String effectiveFrom;
    private String effectiveTo;

    private RealmList<FeatureLink> featureLinks;

    @Nullable
    public static ProductFeature create(LoginServiceResponse.ProductFeature response) {
        if (response.type == null || response.featureType == null || response.effectiveFrom == null || response.effectiveTo == null) {
            return null;
        }

        ProductFeature productFeature = new ProductFeature();
        productFeature.type = response.type;
        productFeature.featureType = response.featureType;
        productFeature.typeCode = response.typeCode;
        productFeature.typeName = response.typeName;
        productFeature.categoryName = response.categoryName;
        productFeature.effectiveFrom = response.effectiveFrom;
        productFeature.effectiveTo = response.effectiveTo;
        productFeature.featureLinks = new RealmList<>();
        if (response.featureLinks != null) {
            for (LoginServiceResponse.FeatureLink featureLink : response.featureLinks) {
                addFeatureLink(productFeature, featureLink);
            }
        }
        return productFeature;
    }

    private static void addFeatureLink(ProductFeature productFeature, LoginServiceResponse.FeatureLink featureLink) {
        FeatureLink model = FeatureLink.create(featureLink, productFeature);
        if (model != null) {
            productFeature.featureLinks.add(model);
        }
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getFeatureType() {
        return featureType;
    }

    public void setFeatureType(Integer featureType) {
        this.featureType = featureType;
    }

    public String getEffectiveFrom() {
        return effectiveFrom;
    }

    public void setEffectiveFrom(String effectiveFrom) {
        this.effectiveFrom = effectiveFrom;
    }

    public String getEffectiveTo() {
        return effectiveTo;
    }

    public void setEffectiveTo(String effectiveTo) {
        this.effectiveTo = effectiveTo;
    }

    public RealmList<FeatureLink> getFeatureLinks() {
        return featureLinks;
    }

    public String getTypeCode() {
        return typeCode;
    }

    public String getTypeName() {
        return typeName;
    }

    public String getCategoryName() {
        return categoryName;
    }
}
