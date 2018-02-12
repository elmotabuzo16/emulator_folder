package com.vitalityactive.va.myhealth.content;

import android.content.Context;

import com.vitalityactive.va.R;
import com.vitalityactive.va.myhealth.shared.VitalityAgeConstants;
import com.vitalityactive.va.utilities.TextUtilities;

import java.util.Arrays;

public class VitalityAgeContent {

    public static String getVitalityAgeFeedback(Context context, int typeKey, String variance) {
        variance = !TextUtilities.isNullOrWhitespace(variance) ? variance : "";
        if (typeCanSetVariance(typeKey)) {
            return String.format(context.getString(FeedbackContentWithVarianceComplete.getFeedbackContentId(typeKey)), variance);
        } else {
            return context.getString(FeedbackContentNoVarianceComplete.getFeedbackContentId(typeKey));
        }
    }

    public static String getVitalityAgeFeedbackSummary(Context context, int typeKey, String variance) {
        variance = !TextUtilities.isNullOrWhitespace(variance) ? variance : "";
        if (typeCanSetVariance(typeKey)) {
            return String.format(context.getString(FeedbackContentWithVarianceSummary.getFeedbackContentId(typeKey)), variance);
        } else {
            return context.getString(FeedbackContentNoVarianceSummary.getFeedbackContentId(typeKey));
        }
    }

    private static boolean typeCanSetVariance(int feedbackKey) {
        return Arrays.asList(VitalityAgeConstants.VA_ABOVE, VitalityAgeConstants.VA_BELOW).contains(feedbackKey);
    }

    private enum FeedbackContentWithVarianceComplete {
        VAgeNotEnoughData(VitalityAgeConstants.VA_NOT_ENOUGH_DATA, R.string.my_health_vitality_age_vhc_completed_vhr_not_completed_not_enough_data_description_long_623), VAgeAbove(VitalityAgeConstants.VA_ABOVE, R.string.my_health_vitality_age_older_description_long_622), VAgeHealthy(VitalityAgeConstants.VA_HEALTHY, R.string.my_health_vitality_age_healthy_description_long_625), VAUnknown(VitalityAgeConstants.VA_UNKNOWN, R.string.my_health_vitality_age_vhc_completed_vhr_not_completed_not_enough_data_description_short_618), VAgeBelow(VitalityAgeConstants.VA_BELOW, R.string.my_health_vitality_age_younger_description_long_631);
        public int key, contentResourceId;

        FeedbackContentWithVarianceComplete(int key, int contentKey) {
            this.key = key;
            this.contentResourceId = contentKey;
        }

        public static int getFeedbackContentId(int key) {
            for (FeedbackContentWithVarianceComplete feedbackContent : FeedbackContentWithVarianceComplete.values()) {
                if (key == feedbackContent.key) {
                    return feedbackContent.contentResourceId;
                }
            }
            return VAUnknown.contentResourceId;
        }
    }

    private enum FeedbackContentNoVarianceComplete {
        VAgeNotEnoughData(VitalityAgeConstants.VA_NOT_ENOUGH_DATA, R.string.my_health_vitality_age_vhc_completed_vhr_not_completed_not_enough_data_description_long_623), VAgeAbove(VitalityAgeConstants.VA_ABOVE, R.string.my_health_vitality_age_older_description_long_622), VAgeHealthy(VitalityAgeConstants.VA_HEALTHY, R.string.my_health_vitality_age_healthy_description_long_625), VAUnknown(VitalityAgeConstants.VA_UNKNOWN, R.string.my_health_vitality_age_vhc_completed_vhr_not_completed_not_enough_data_description_short_618), VAgeBelow(VitalityAgeConstants.VA_BELOW, R.string.my_health_vitality_age_younger_description_long_631);
        public int key, contentResourceId;

        FeedbackContentNoVarianceComplete(int key, int contentKey) {
            this.key = key;
            this.contentResourceId = contentKey;
        }

        public static int getFeedbackContentId(int key) {
            for (FeedbackContentNoVarianceComplete feedbackContent : FeedbackContentNoVarianceComplete.values()) {
                if (key == feedbackContent.key) {
                    return feedbackContent.contentResourceId;
                }
            }
            return VAUnknown.contentResourceId;
        }
    }

    private enum FeedbackContentWithVarianceSummary {
        VAgeNotEnoughData(VitalityAgeConstants.VA_NOT_ENOUGH_DATA, R.string.my_health_vitality_age_vhc_completed_vhr_not_completed_not_enough_data_description_short_618), VAgeAbove(VitalityAgeConstants.VA_ABOVE, R.string.my_health_vitality_age_older_description_short_621), VAgeHealthy(VitalityAgeConstants.VA_HEALTHY, R.string.my_health_vitality_age_healthy_description_short_624), VAUnknown(VitalityAgeConstants.VA_UNKNOWN, R.string.my_health_vitality_age_vhc_completed_vhr_not_completed_not_enough_data_description_short_618), VAgeBelow(VitalityAgeConstants.VA_BELOW, R.string.my_health_vitality_age_younger_description_short_630);
        public int key, contentResourceId;

        FeedbackContentWithVarianceSummary(int key, int contentKey) {
            this.key = key;
            this.contentResourceId = contentKey;
        }

        public static int getFeedbackContentId(int key) {
            for (FeedbackContentWithVarianceSummary feedbackContent : FeedbackContentWithVarianceSummary.values()) {
                if (key == feedbackContent.key) {
                    return feedbackContent.contentResourceId;
                }
            }
            return VAUnknown.contentResourceId;
        }
    }

    private enum FeedbackContentNoVarianceSummary {
        VAgeNotEnoughData(VitalityAgeConstants.VA_NOT_ENOUGH_DATA, R.string.my_health_vitality_age_vhc_completed_vhr_not_completed_not_enough_data_description_short_618), VAgeAbove(VitalityAgeConstants.VA_ABOVE, R.string.my_health_vitality_age_older_description_short_621), VAgeHealthy(VitalityAgeConstants.VA_HEALTHY, R.string.my_health_vitality_age_healthy_description_short_624), VAUnknown(VitalityAgeConstants.VA_UNKNOWN, R.string.my_health_vitality_age_vhc_completed_vhr_not_completed_not_enough_data_description_short_618), VAgeBelow(VitalityAgeConstants.VA_BELOW, R.string.my_health_vitality_age_younger_description_short_630);
        public int key, contentResourceId;

        FeedbackContentNoVarianceSummary(int key, int contentKey) {
            this.key = key;
            this.contentResourceId = contentKey;
        }

        public static int getFeedbackContentId(int key) {
            for (FeedbackContentNoVarianceSummary feedbackContent : FeedbackContentNoVarianceSummary.values()) {
                if (key == feedbackContent.key) {
                    return feedbackContent.contentResourceId;
                }
            }
            return VAUnknown.contentResourceId;
        }
    }
}
