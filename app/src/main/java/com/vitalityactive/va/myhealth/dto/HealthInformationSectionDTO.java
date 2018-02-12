package com.vitalityactive.va.myhealth.dto;


import com.vitalityactive.va.persistence.models.myhealth.Attribute;
import com.vitalityactive.va.persistence.models.myhealth.HealthInformationSection;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;

public class HealthInformationSectionDTO {


    private List<AttributeDTO> attributeDTOS = null;
    private Integer sortOrder;
    private String typeCode;
    private Integer typeKey;
    private String typeName;

    public HealthInformationSectionDTO(HealthInformationSection healthInformationSection) {
        this.sortOrder = healthInformationSection.getSortOrder();
        this.typeCode = healthInformationSection.getTypeCode();
        this.typeKey = healthInformationSection.getTypeKey();
        this.typeName = healthInformationSection.getTypeName();
        this.attributeDTOS = new ArrayList<>();
        for (Attribute attribute : healthInformationSection.getAttributes()) {
            this.attributeDTOS.add(new AttributeDTO(attribute));
        }
    }

    public HealthInformationSectionDTO(List<AttributeDTO> attributeDTOS, Integer sortOrder, String typeCode, Integer typeKey, String typeName) {
        this.attributeDTOS = attributeDTOS;
        this.sortOrder = sortOrder;
        this.typeCode = typeCode;
        this.typeKey = typeKey;
        this.typeName = typeName;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public String getTypeCode() {
        return typeCode;
    }

    public Integer getTypeKey() {
        return typeKey;
    }

    public String getTypeName() {
        return typeName;
    }

    private <T, R extends RealmObject> List<T> toItemDto(RealmList<R> dbModels, Class<R> fromClass, Class<T> toClass) {
        List<T> dtoModels = new ArrayList<>();
        for (R model : dbModels) {
            try {
                dtoModels.add(toClass.getConstructor(fromClass).newInstance(model));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return dtoModels;
    }

    public List<AttributeDTO> getAttributeDTOS() {
        return attributeDTOS;
    }

}
