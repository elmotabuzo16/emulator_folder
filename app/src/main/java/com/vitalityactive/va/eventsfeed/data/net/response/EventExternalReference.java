package com.vitalityactive.va.eventsfeed.data.net.response;

/**
 * Created by jayellos on 11/17/17.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EventExternalReference {

    @SerializedName("typeCode")
    @Expose
    private String typeCode;
    @SerializedName("typeKey")
    @Expose
    private Integer typeKey;
    @SerializedName("typeName")
    @Expose
    private String typeName;
    @SerializedName("value")
    @Expose
    private String value;

    public String getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

    public Integer getTypeKey() {
        return typeKey;
    }

    public void setTypeKey(Integer typeKey) {
        this.typeKey = typeKey;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
