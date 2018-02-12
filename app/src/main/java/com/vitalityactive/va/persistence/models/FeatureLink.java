package com.vitalityactive.va.persistence.models;

import android.support.annotation.Nullable;

import com.vitalityactive.va.networking.model.LoginServiceResponse;
import com.vitalityactive.va.persistence.Model;

import io.realm.RealmObject;

public class FeatureLink extends RealmObject implements Model {
    private int typeKey;
    private int linkedKey;
    private ProductFeature productFeature;

    @Nullable
    public static FeatureLink create(@Nullable LoginServiceResponse.FeatureLink featureLink, ProductFeature productFeature) {
        if (featureLink == null
                || featureLink.typeKey == null
                || featureLink.linkedKey == null) {
            return null;
        }

        return new FeatureLink(featureLink, productFeature);
    }

    public FeatureLink() {

    }

    private FeatureLink(LoginServiceResponse.FeatureLink featureLink, ProductFeature productFeature) {
        typeKey = featureLink.typeKey;
        linkedKey = featureLink.linkedKey;
        this.productFeature = productFeature;
    }

    public int getLinkedKey() {
        return linkedKey;
    }

    public int getTypeKey() {
        return typeKey;
    }

    public ProductFeature getProductFeature() {
        return productFeature;
    }
}
