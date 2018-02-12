package com.vitalityactive.va.snv.partners.dto;

import com.vitalityactive.va.snv.partners.service.GetPartnersByCategoryResponse;

import java.io.Serializable;

/**
 * Created by kerry.e.lawagan on 12/8/2017.
 */

public class ProductFeatureGroupDto implements Serializable {
    private String description;
    private String logoFileName;
    private String longDescription;
    private String name;
    private String typeCode;
    private int typeKey;
    private String typeName;

    public ProductFeatureGroupDto(GetPartnersByCategoryResponse.ProductFeatureGroup productFeatureGroup) {
        description = productFeatureGroup.getDescription();
        logoFileName = productFeatureGroup.getLogoFileName();
        longDescription = productFeatureGroup.getLongDescription();
        name = productFeatureGroup.getName();
        typeCode = productFeatureGroup.getTypeCode();
        typeKey = productFeatureGroup.getTypeKey();
        typeName = productFeatureGroup.getTypeName();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLogoFileName() {
        return logoFileName;
    }

    public void setLogoFileName(String logoFileName) {
        this.logoFileName = logoFileName;
    }

    public String getLongDescription() {
        return longDescription;
    }

    public void setLongDescription(String longDescription) {
        this.longDescription = longDescription;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
