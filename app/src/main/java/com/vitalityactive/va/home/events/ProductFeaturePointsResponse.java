package com.vitalityactive.va.home.events;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import io.realm.annotations.Required;

public class ProductFeaturePointsResponse {
    @SerializedName("productFeatureCategoryAndPointsInformations")
    public List<ProductFeatureCategoryAndPointsInformation> productFeatureCategoryAndPointsInformations;

    public class ProductFeatureCategoryAndPointsInformation {
        @SerializedName("code")
        public String code;
        @Required
        @SerializedName("key")
        public Integer key;
        @SerializedName("name")
        public String name;
        @SerializedName("pointsCategoryLimit")
        public int pointsCategoryLimit;
        @Required
        @SerializedName("pointsEarned")
        public Integer pointsEarned;
        @Required
        @SerializedName("pointsEarningFlag")
        public String pointsEarningFlag;
        @Required
        @SerializedName("potentialPoints")
        public Integer potentialPoints;
        @SerializedName("typeCode")
        public String typeCode;
        @Required
        @SerializedName("typeKey")
        public int typeKey;
        @SerializedName("typeName")
        public String typeName;
        @SerializedName("productFeatureAndPointsInformations")
        public List<ProductFeatureAndPointsInformation> productFeatureAndPointsInformations;
    }

    public class ProductFeatureAndPointsInformation {
        @SerializedName("code")
        public String code;
        @SerializedName("eventTypes")
        public List<EventType> eventTypes;
        @Required
        @SerializedName("key")
        public Integer key;
        @SerializedName("name")
        public String name;
        @Required
        @SerializedName("pointsEarned")
        public Integer pointsEarned;
        @Required
        @SerializedName("pointsEarningFlag")
        public String pointsEarningFlag;
        @SerializedName("pointsEntries")
        public List<PointsEntry> pointsEntries;
        @Required
        @SerializedName("potentialPoints")
        public Integer potentialPoints;
        @SerializedName("productSubfeatures")
        public List<ProductSubfeature> productSubfeatures;
    }

    public class EventType {
        @SerializedName("code")
        public String code;
        @Required
        @SerializedName("key")
        public Integer key;
        @SerializedName("name")
        public String name;
    }

    public class PointsEntry {
        @SerializedName("categoryCode")
        public String categoryCode;
        @Required
        @SerializedName("categoryKey")
        public Integer categoryKey;
        @SerializedName("categoryLimit")
        public int categoryLimit;
        @SerializedName("categoryName")
        public String categoryName;
        @Required
        @SerializedName("frequencyLimit")
        public Integer frequencyLimit;
        @Required
        @SerializedName("maximumMembershipPeriodPoints")
        public Integer maximumMembershipPeriodPoints;
        @Required
        @SerializedName("pointsEarned")
        public Integer pointsEarned;
        @SerializedName("pointsEntryLimit")
        public String pointsEntryLimit;
        @Required
        @SerializedName("potentialPoints")
        public Integer potentialPoints;
        @SerializedName("potentialPointses")
        public List<PotentialPointses> productSubfeaturePotentialPointses;
        @SerializedName("typeCode")
        public String typeCode;
        @Required
        @SerializedName("typeKey")
        public Integer typeKey;
        @SerializedName("typeName")
        public String typeName;
    }

    public class PotentialPointses {
        @Required
        @SerializedName("potentialPoints")
        public Integer potentialPoints;
        @SerializedName("conditions")
        public List<com.vitalityactive.va.wellnessdevices.pointsmonitor.service.EventType.Conditions> conditions;
    }

    public class ProductSubfeaturePotentialPointses {
        @Required
        @SerializedName("potentialPoints")
        public Integer potentialPoints;
        @SerializedName("productSubfeatureConditions")
        public ProductSubfeatureConditions productSubfeatureConditions;
    }

    public class ProductSubfeatureConditions {
        @SerializedName("greaterThan")
        public int greaterThan;
        @SerializedName("greaterThanOrEqualTo")
        public int greaterThanOrEqualTo;
        @SerializedName("lessThan")
        public int lessThan;
        @SerializedName("lessThanOrEqualTo")
        public int lessThanOrEqualTo;
        @SerializedName("metadataTypeCode")
        public String metadataTypeCode;
        @Required
        @SerializedName("metadataTypeKey")
        public Integer metadataTypeKey;
        @SerializedName("metadataTypeName")
        public String metadataTypeName;
        @SerializedName("unitOfMeasure")
        public String unitOfMeasure;
    }

    public class ProductSubfeature {
        @SerializedName("code")
        public String code;
        @Required
        @SerializedName("key")
        public Integer key;
        @SerializedName("name")
        public String name;
        @Required
        @SerializedName("pointsEarned")
        public Integer pointsEarned;
        @Required
        @SerializedName("pointsEarningFlag")
        public String pointsEarningFlag;
        @Required
        @SerializedName("potentialPoints")
        public Integer potentialPoints;
        @SerializedName("productSubfeatureEventTypes")
        public List<ProductSubfeatureEventType> productSubfeatureEventTypes;
        @SerializedName("productSubfeaturePointsEntries")
        public List<ProductSubfeaturePointsEntry> productSubfeaturePointsEntries;
    }

    public class ProductSubfeatureEventType {
        @SerializedName("code")
        public String code;
        @Required
        @SerializedName("key")
        public Integer key;
        @SerializedName("name")
        public String name;
    }

    public class ProductSubfeaturePointsEntry {
        @SerializedName("categoryCode")
        public String categoryCode;
        @Required
        @SerializedName("categoryKey")
        public Integer categoryKey;
        @SerializedName("categoryLimit")
        public int categoryLimit;
        @SerializedName("categoryName")
        public String categoryName;
        @Required
        @SerializedName("frequencyLimit")
        public Integer frequencyLimit;
        @Required
        @SerializedName("maximumMembershipPeriodPoints")
        public Integer maximumMembershipPeriodPoints;
        @Required
        @SerializedName("pointsEarned")
        public Integer pointsEarned;
        @SerializedName("pointsEntryLimit")
        public int pointsEntryLimit;
        @Required
        @SerializedName("potentialPoints")
        public Integer potentialPoints;
        @SerializedName("productSubfeaturePotentialPointses")
        public ProductSubfeaturePotentialPointses productSubfeaturePotentialPointses;
        @SerializedName("typeCode")
        public String typeCode;
        @Required
        @SerializedName("typeKey")
        public Integer typeKey;
        @SerializedName("typeName")
        public String typeName;
    }
}
