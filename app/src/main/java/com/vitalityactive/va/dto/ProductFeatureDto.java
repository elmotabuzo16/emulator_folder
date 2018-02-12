package com.vitalityactive.va.dto;

import com.vitalityactive.va.persistence.models.FeatureLink;
import com.vitalityactive.va.persistence.models.ProductFeature;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kerry.e.lawagan on 11/26/2017.
 */

public class ProductFeatureDto {
    private int type;
    private int featureType;
    private String effectiveFrom;
    private String effectiveTo;
    private List<FeatureLinkDto> featureLinks;

    public static ProductFeatureDto create(ProductFeature productFeature) {
        ProductFeatureDto productFeatureDto = new ProductFeatureDto();
        productFeatureDto.setType(productFeature.getType());
        productFeatureDto.setFeatureType(productFeature.getFeatureType());
        productFeatureDto.setEffectiveFrom(productFeature.getEffectiveFrom());
        productFeatureDto.setEffectiveTo(productFeature.getEffectiveTo());

        List<FeatureLinkDto> featureLinkDtos = new ArrayList<FeatureLinkDto>();

        for (FeatureLink featureLink: productFeature.getFeatureLinks()) {
            featureLinkDtos.add(FeatureLinkDto.create(featureLink, productFeatureDto));
        }
        productFeatureDto.setFeatureLinks(featureLinkDtos);

        return productFeatureDto;
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

    public void setFeatureType(int featureType) {
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

    public List<FeatureLinkDto> getFeatureLinks() {
        return featureLinks;
    }

    public void setFeatureLinks(List<FeatureLinkDto> featureLinks) {
        this.featureLinks = featureLinks;
    }
}
