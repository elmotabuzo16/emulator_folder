package com.vitalityactive.va.networking.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LoginServiceResponse {
    @SerializedName("application")
    public Application application;
    @SerializedName("partyDetails")
    public PartyDetails partyDetails;
    @SerializedName("userInstructions")
    public List<UserInstruction> userInstructions;
    @SerializedName("vitalityMembership")
    public List<VitalityMembership> vitalityMembership;

    public static class PartyDetails {
        @SerializedName("person")
        public Person person;
        @SerializedName("partyId")
        public Long partyId;
        @SerializedName("tenantId")
        public Long tenantId;
        @SerializedName("references")
        public List<Reference> references;
        @SerializedName("generalPreferences")
        public List<GeneralPreference> generalPreferences;
        @SerializedName("measurementSystemPreference")
        public MeasurementSystemPreference measurementSystemPreference;
        @SerializedName("telephones")
        public List<Telephone> telephones;
        @SerializedName("timeZonePreference")
        public TimeZonePreference timeZonePreference;
        @SerializedName("webAddresses")
        public List<WebAddress> webAddresses;
        @SerializedName("geographicalAreaPreference")
        public GeographicalAreaPreference geographicalAreaPreference;
        @SerializedName("languagePreference")
        public LanguagePreference languagePreference;
        @SerializedName("emailAddresses")
        public List<EmailAddress> emailAddresses;
        @SerializedName("physicalAddresses")
        public List<PhysicalAddress> physicalAddresses;
        @SerializedName("accessToken")
        public String accessToken;
        @SerializedName("partnerRefreshToken")
        public String partnerRefreshToken;
    }

    public static class UserInstruction {
        @SerializedName("id")
        public Long id;
        @SerializedName("typeKey")
        public String typeKey;
        @SerializedName("effectiveFrom")
        public String effectiveFrom;
    }

    public static class Person {
        @SerializedName("middleNames")
        public String middleNames;
        @SerializedName("bornOn")
        public String bornOn;
        @SerializedName("givenName")
        public String givenName;
        @SerializedName("familyName")
        public String familyName;
        @SerializedName("language")
        public String language;
        @SerializedName("preferredName")
        public String preferredName;
        @SerializedName("suffix")
        public String suffix;
        @SerializedName("username")
        public String username;

        @SerializedName("titleTypeName")
        public String titleTypeName;
        @SerializedName("genderTypeKey")
        public String genderTypeKey;
        @SerializedName("titleTypeCode")
        public String titleTypeCode;
        @SerializedName("titleTypeKey")
        public String titleTypeKey;
        @SerializedName("genderTypeCode")
        public String genderTypeCode;
        @SerializedName("genderTypeName")
        public String genderTypeName;
    }

    private static class StringEffectiveDateModel {
        @SerializedName("effectiveFrom")
        public String effectiveFrom;
        @SerializedName("effectiveTo")
        public String effectiveTo;
    }

    public static class GeneralPreference extends StringEffectiveDateModel {
        @SerializedName("typeKey")
        public String typeKey;
        @SerializedName("value")
        public String value;
        @SerializedName("typeName")
        public String typeName;

    }

    public static class Reference extends StringEffectiveDateModel {
        @SerializedName("type")
        public String type;
        @SerializedName("value")
        public String value;
        @SerializedName("issuedBy")
        public Long issuedBy;
        @SerializedName("typeName")
        public String typeName;
        @SerializedName("typeCode")
        public String typeCode;
        @SerializedName("typeKey")
        public String typeKey;
    }

    public static class MeasurementSystemPreference {
        @SerializedName("typeKey")
        public String typeKey;
        @SerializedName("name")
        public String name;
        @SerializedName("contactRoles")
        public List<ContactRole> contactRoles;
    }

    public static class Telephone extends StringEffectiveDateModel {
        @SerializedName("extension")
        public String extension;
        @SerializedName("contactNumber")
        public String contactNumber;
        @SerializedName("contactRole")
        public List<ContactRole> contactRole;
        @SerializedName("countryDialCode")
        public String countryDialCode;
        @SerializedName("areaDialCode")
        public String areaDialCode;
    }

    public static class ContactRole {
        @SerializedName("availabilityFrom")
        public String availabilityFrom;
        @SerializedName("availabilityTo")
        public String availabilityTo;
        @SerializedName("rolePurposeType")
        public String rolePurposeType;
        @SerializedName("roleType")
        public String roleType;
        @SerializedName("roleTypeName")
        public String roleTypeName;
        @SerializedName("rolePurposeTypeName")
        public String rolePurposeTypeName;
        @SerializedName("effectiveTo")
        public String effectiveTo;
        @SerializedName("effectiveFrom")
        public String effectiveFrom;
    }

    public static class TimeZonePreference {
        @SerializedName("code")
        public String code;
        @SerializedName("daylightSavings")
        public Long daylightSavings;
        @SerializedName("value")
        public String value;
        @SerializedName("typeName")
        public String typeName;
    }

    public static class WebAddress extends StringEffectiveDateModel {
        @SerializedName("contactRole")
        public List<ContactRole> contactRole;
        @SerializedName("uRL")
        public String uRL;
    }

    public static class GeographicalAreaPreference extends StringEffectiveDateModel {
        @SerializedName("type")
        public String type;
        @SerializedName("value")
        public String value;
        @SerializedName("typeName")
        public String typeName;
    }

    public static class LanguagePreference {
        @SerializedName("iSOCode")
        public String iSOCode;
        @SerializedName("value")
        public String value;
    }

    public static class EmailAddress extends StringEffectiveDateModel {
        @SerializedName("contactRoles")
        public List<ContactRole> contactRoles;
        @SerializedName("value")
        public String value;
    }

    public static class PhysicalAddress extends StringEffectiveDateModel {
        @SerializedName("country")
        public String country;
        @SerializedName("pOBox")
        public Boolean pOBox;
        @SerializedName("complex")
        public String complex;
        @SerializedName("postalCode")
        public String postalCode;
        @SerializedName("streetAddress1")
        public String streetAddress1;
        @SerializedName("streetAddress2")
        public String streetAddress2;
        @SerializedName("streetAddress3")
        public String streetAddress3;
        @SerializedName("unitNumber")
        public String unitNumber;
        @SerializedName("place")
        public String place;
        @SerializedName("contactRole")
        public List<ContactRole> contactRole;
    }

    public static class VitalityMembership {
        @SerializedName("id")
        public Long id;
        @SerializedName("membershipProducts")
        public List<MembershipProduct> membershipProducts;
        @SerializedName("currentVitalityMembershipPeriod")
        public CurrentVitalityMembershipPeriod currentVitalityMembershipPeriod;
    }

    public static class ProductFeature {
        @SerializedName("typeKey")
        public Integer type;
        @SerializedName("typeName")
        public String typeName;
        @SerializedName("categoryName")
        public String categoryName;
        @SerializedName("featureTypeKey")
        public Integer featureType;
        @SerializedName("typeCode")
        public String typeCode;
        @SerializedName("effectiveFrom")
        public String effectiveFrom;
        @SerializedName("effectiveTo")
        public String effectiveTo;
        @SerializedName("featureLinks")
        public List<FeatureLink> featureLinks;
    }

    public static class MembershipProduct {
        @SerializedName("productFeatureApplicabilities")
        public List<ProductFeature> productFeatures;
    }

    public static class CurrentVitalityMembershipPeriod {
        @SerializedName("effectiveTo")
        public String effectiveTo;
        @SerializedName("effectiveFrom")
        public String effectiveFrom;
    }

    public static class FeatureLink {
        @SerializedName("typeKey")
        public Integer typeKey;
        @SerializedName("linkedKey")
        public Integer linkedKey;
    }
}
