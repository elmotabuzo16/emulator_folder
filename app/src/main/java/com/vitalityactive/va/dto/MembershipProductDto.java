package com.vitalityactive.va.dto;

import com.vitalityactive.va.persistence.models.MembershipProduct;
import com.vitalityactive.va.persistence.models.ProductFeature;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kerry.e.lawagan on 11/26/2017.
 */

public class MembershipProductDto {
    public List<ProductFeatureDto> productFeatures;

    public MembershipProductDto(MembershipProduct membershipProduct) {
        productFeatures = new ArrayList<ProductFeatureDto>();
        for (ProductFeature productFeature: membershipProduct.productFeatures) {
            productFeatures.add(ProductFeatureDto.create(productFeature));
        }
    }
}
