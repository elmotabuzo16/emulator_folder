package com.vitalityactive.va.dto;

import com.vitalityactive.va.persistence.models.FeatureLink;

/**
 * Created by kerry.e.lawagan on 11/26/2017.
 */

public class FeatureLinkDto {
    private int typeKey;
    private int linkedKey;
    private ProductFeatureDto productFeature;

    public static FeatureLinkDto create(FeatureLink featureLink, ProductFeatureDto productFeature) {
        FeatureLinkDto featureLinkDto = new FeatureLinkDto();
        featureLinkDto.setTypeKey(featureLink.getTypeKey());
        featureLinkDto.setLinkedKey(featureLink.getLinkedKey());
        featureLinkDto.setProductFeature(productFeature);

        return  featureLinkDto;
    }

    public int getTypeKey() {
        return typeKey;
    }

    public void setTypeKey(int typeKey) {
        this.typeKey = typeKey;
    }

    public int getLinkedKey() {
        return linkedKey;
    }

    public void setLinkedKey(int linkedKey) {
        this.linkedKey = linkedKey;
    }

    public ProductFeatureDto getProductFeature() {
        return productFeature;
    }

    public void setProductFeature(ProductFeatureDto productFeature) {
        this.productFeature = productFeature;
    }
}
