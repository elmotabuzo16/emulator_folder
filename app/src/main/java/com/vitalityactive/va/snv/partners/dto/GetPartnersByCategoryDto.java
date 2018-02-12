package com.vitalityactive.va.snv.partners.dto;

import java.util.List;

/**
 * Created by kerry.e.lawagan on 12/8/2017.
 */

public class GetPartnersByCategoryDto {
    private List<ProductFeatureGroupDto> productFeatureGroups;
    private String typeCode;
    private int typeKey;
    private String typeName;

    public List<ProductFeatureGroupDto> getProductFeatureGroups() {
        return productFeatureGroups;
    }

    public void setProductFeatureGroups(List<ProductFeatureGroupDto> productFeatureGroups) {
        this.productFeatureGroups = productFeatureGroups;
    }

    public String getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

    public int getTypeKey() {
        return typeKey;
    }

    public void setTypeKey(int typeKey) {
        this.typeKey = typeKey;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
}
