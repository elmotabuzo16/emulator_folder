package com.vitalityactive.va.networking.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Application {
    @SerializedName("configurationVersion")
    public ConfigurationVersion configurationVersion;
    @SerializedName("typeKey")
    public Integer typeKey;
    @SerializedName("typeName")
    public String typeName;
    @SerializedName("name")
    public String name;
    @SerializedName("typeCode")
    public String typeCode;

    public static class ConfigurationVersion {
        @SerializedName("effectiveTo")
        public String effectiveTo;
        @SerializedName("applicationFeatures")
        public List<ApplicationFeature> applicationFeatures;
        @SerializedName("releaseVersion")
        public String releaseVersion;
        @SerializedName("effectiveFrom")
        public String effectiveFrom;
    }

    public static class ApplicationFeature {
        @SerializedName("applicationFeatureParameters")
        public List<ApplicationFeatureParameter> applicationFeatureParameters;
        @SerializedName("typeKey")
        public String type;
        @SerializedName("typeName")
        public String typeName;
        @SerializedName("toggle")
        public boolean toggle;
        @SerializedName("name")
        public String name;
        @SerializedName("typeCode")
        public String typeCode;
    }

    public static class ApplicationFeatureParameter {
        @SerializedName("name")
        public String name;
        @SerializedName("value")
        public String value;
    }
}
