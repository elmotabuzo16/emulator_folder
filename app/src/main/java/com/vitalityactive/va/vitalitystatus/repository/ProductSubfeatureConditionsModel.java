package com.vitalityactive.va.vitalitystatus.repository;

import com.vitalityactive.va.home.events.ProductFeaturePointsResponse;
import com.vitalityactive.va.persistence.Model;
import com.vitalityactive.va.utilities.InvalidModelLogger;

import io.realm.RealmObject;

public class ProductSubfeatureConditionsModel extends RealmObject implements Model{
    private int greaterThan;
    private int greaterThanOrEqualTo;
    private int lessThan;
    private int lessThanOrEqualTo;
    private String metadataTypeCode;
    private Integer metadataTypeKey;
    private String metadataTypeName;
    private String unitOfMeasure;

    public ProductSubfeatureConditionsModel() {}

    public static ProductSubfeatureConditionsModel create(ProductFeaturePointsResponse.ProductSubfeatureConditions response) {
        if (response.metadataTypeKey == null) {
            InvalidModelLogger.warn(response, 0, "required fields are null");
            return null;
        }

        ProductSubfeatureConditionsModel instance = new ProductSubfeatureConditionsModel();

        instance.greaterThan = response.greaterThan;
        instance.greaterThanOrEqualTo = response.greaterThanOrEqualTo;
        instance.lessThan = response.lessThan;
        instance.lessThanOrEqualTo = response.lessThanOrEqualTo;
        instance.metadataTypeCode = response.metadataTypeCode;
        instance.metadataTypeKey = response.metadataTypeKey;
        instance.metadataTypeName = response.metadataTypeName;
        instance.unitOfMeasure = response.unitOfMeasure;

        return instance;
    }

    public Integer getMetadataTypeKey() {
        return metadataTypeKey;
    }

    public String getMetadataTypeCode() {
        return metadataTypeCode;
    }

    public String getMetadataTypeName() {
        return metadataTypeName;
    }

    public int getGreaterThanOrEqualTo() {
        return greaterThanOrEqualTo;
    }

    public int getGreaterThan() {
        return greaterThan;
    }

    public int getLessThan() {
        return lessThan;
    }

    public int getLessThanOrEqualTo() {
        return lessThanOrEqualTo;
    }

    public String getUnitOfMeasure() {
        return unitOfMeasure;
    }
}
