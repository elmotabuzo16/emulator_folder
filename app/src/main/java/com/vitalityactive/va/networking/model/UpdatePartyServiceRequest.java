package com.vitalityactive.va.networking.model;

import com.google.gson.annotations.SerializedName;
import com.vitalityactive.va.constants.PreferenceType;
import com.vitalityactive.va.utilities.TimeUtilities;
import com.vitalityactive.va.utilities.date.NonUserFacingDateFormatter;
import com.vitalityactive.va.utilities.date.Date;

public class UpdatePartyServiceRequest {

    public enum GeneralPreferenceType {
        EMAIL(PreferenceType._EMAILCOMMPREF),
        STARBUCKS_EMAIL(PreferenceType._STARBUCKSEMAIL),
        SHARE_VITALITY_STATUS(PreferenceType._SHARESTATUSWITHEMP);

        private int typeKey;

        GeneralPreferenceType(int typeKey) {
            this.typeKey = typeKey;
        }

        public int getTypeKey() {
            return typeKey;
        }
    }

    @SerializedName("updatePartyRequest")
    public UpdatePartyRequestBody updatePartyRequest;

    private UpdatePartyServiceRequest(GeneralPreference generalPreference) {
        updatePartyRequest = new UpdatePartyRequestBody();
        updatePartyRequest.generalPreferences = new GeneralPreference[] {generalPreference};
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        public Builder() {

        }

        private GeneralPreference generalPreference;

        public Builder addGeneralPreference(GeneralPreferenceType preference, String value) {
            generalPreference = new UpdatePartyServiceRequest.GeneralPreference(preference, value);
            return this;
        }

        public UpdatePartyServiceRequest build() {
            return new UpdatePartyServiceRequest(generalPreference);
        }
    }

    public static class GeneralPreference {
        @SerializedName("effectiveFrom")
        public String effectiveFrom;
        @SerializedName("effectiveTo")
        public String effectiveTo;
        @SerializedName("typeKey")
        public int typeKey;
        @SerializedName("value")
        public String value;

        public GeneralPreference(GeneralPreferenceType preference, String value) {
            this.typeKey = preference.getTypeKey();
            this.value = value;
            effectiveFrom = NonUserFacingDateFormatter.getYearMonthDayFormatter().format(new TimeUtilities().now());
            effectiveTo = NonUserFacingDateFormatter.getYearMonthDayFormatter().format(new Date("9999-01-01T00:00:00.000+02:00[Africa/Johannesburg]"));
        }
    }

    public static class UpdatePartyRequestBody {
        @SerializedName("generalPreferences")
        public GeneralPreference[] generalPreferences;
    }
}
