package com.vitalityactive.va.myhealth.content;


import android.content.Context;

import com.vitalityactive.va.R;
import com.vitalityactive.va.myhealth.entity.VitalityAge;
import com.vitalityactive.va.myhealth.shared.MyHealthUtils;
import com.vitalityactive.va.myhealth.shared.VitalityAgeConstants;

import java.util.Arrays;

public class MyHealthLearnMoreContent {

    private static String getLearnMoreOutDated(Context context, VitalityAge vitalityAge) {
        if (vitalityAge != null && vitalityAge.getActualType() != 0) {
            String value = MyHealthUtils.getDisplayVitalityAge(vitalityAge.getAge());
            return context.getString(VitalityAgeOutdatedLearnMore.getContentResourceId(vitalityAge.getActualType()), value != null ? value : "");
        } else {
            return context.getString(VitalityAgeOutdatedLearnMore.getDefaultContentId());
        }
    }

    public static String getLearnMoreHeader(Context context, VitalityAge vitalityAge) {
        if (vitalityAge != null) {
            if (vitalityAge.getEffectiveType() == VitalityAgeConstants.VA_OUTDATED) {
                return getLearnMoreOutDated(context, vitalityAge);
            }
            String variance = vitalityAge.getVariance();
            if (typeCanSetVariance(vitalityAge.getEffectiveType())) {
                return context.getString(LearnMoreHeader.getContentResourceId(vitalityAge.getEffectiveType()), variance != null ? variance : "");
            }
        }
        return context.getString(LearnMoreHeader.getContentResourceId(VitalityAgeConstants.VA_UNKNOWN));
    }

    private static boolean typeCanSetVariance(int feedbackKey) {
        return Arrays.asList(VitalityAgeConstants.VA_ABOVE, VitalityAgeConstants.VA_BELOW, VitalityAgeConstants.VA_HEALTHY).contains(feedbackKey);
    }

    public enum LearnMoreHeader {
        VAgeNotEnoughData(VitalityAgeConstants.VA_NOT_ENOUGH_DATA, R.string.my_health_learn_more_title_744,
                R.string.my_health_learn_more_subtitle_745),
        VAgeAbove(VitalityAgeConstants.VA_ABOVE, R.string.my_health_learn_more_too_high_title_752, R.string.my_health_learn_more_too_high_content_753),
        VAgeHealthy(VitalityAgeConstants.VA_HEALTHY, R.string.my_health_learn_more_good_title_756, R.string.my_health__learn_more_good_content_757),
        VAUnknown(VitalityAgeConstants.VA_UNKNOWN, R.string.my_health_learn_more_title_744,
                R.string.my_health_learn_more_subtitle_745),
        VAgeBelow(VitalityAgeConstants.VA_BELOW, R.string.my_health_learn_more_good_title_756, R.string.my_health_vitality_age_younger_description_long_631),
        VAgeOutdated(VitalityAgeConstants.VA_OUTDATED, R.string.my_health_learn_more_your_vitality_age_is_outdated_1100, R.string.my_health_learn_more_vitality_age_outdated_description_1101);

        public int feedbackKey, titleResourceId, contentResourceId;

        LearnMoreHeader(int feedbackKey, int titleResourceId, int contentResourceId) {
            this.feedbackKey = feedbackKey;
            this.titleResourceId = titleResourceId;
            this.contentResourceId = contentResourceId;
        }

        public static int getTitleResourceId(int key) {
            for (LearnMoreHeader learnMoreTitle : LearnMoreHeader.values()) {
                if (key == learnMoreTitle.feedbackKey) {
                    return learnMoreTitle.titleResourceId;
                }
            }
            return VAUnknown.titleResourceId;
        }

        public static int getContentResourceId(int key) {
            for (LearnMoreHeader learnMoreContent : LearnMoreHeader.values()) {
                if (key == learnMoreContent.feedbackKey) {
                    return learnMoreContent.contentResourceId;
                }
            }
            return VAUnknown.contentResourceId;
        }
    }

    public enum LearnMoreFirstSection {
        VAgeNotEnoughData(VitalityAgeConstants.VA_NOT_ENOUGH_DATA,
                R.string.my_health_learn_more_how_is_this_calculated_title_746,
                R.string.my_health_learn_more_how_is_this_calculated_content_747,
                R.drawable.calculator_med
        ),
        VAgeAbove(VitalityAgeConstants.VA_ABOVE,
                R.string.my_health_learn_more_how_do_I_lower_title_754,
                R.string.my_health_learn_more_how_do_I_lower_content_755,
                R.drawable.know_your_health_med),
        VAgeHealthy(VitalityAgeConstants.VA_HEALTHY,
                R.string.my_health_learn_more_how_do_I_maintain_title_758,
                R.string.my_health_learn_more_how_do_I_maintain_content_759,
                R.drawable.lightbulb_med),
        VAUnknown(VitalityAgeConstants.VA_UNKNOWN,
                R.string.my_health_learn_more_how_is_this_calculated_title_746,
                R.string.my_health_learn_more_how_is_this_calculated_content_747,
                R.drawable.calculator_med),
        VAgeBelow(VitalityAgeConstants.VA_BELOW,
                R.string.my_health_learn_more_how_do_I_maintain_title_758,
                R.string.my_health_learn_more_how_do_I_maintain_content_759,
                R.drawable.lightbulb_med),
        VAgeOutdated(VitalityAgeConstants.VA_OUTDATED,
                R.string.my_health_learn_more_how_is_this_calculated_title_746,
                R.string.my_health_learn_more_how_is_this_calculated_content_747,
                R.drawable.calculator_med);

        public int feedbackKey, titleResourceId, contentResourceId, iconResourceId;

        LearnMoreFirstSection(int feedbackKey, int titleResourceId, int contentResourceId, int iconResourceId) {
            this.feedbackKey = feedbackKey;
            this.titleResourceId = titleResourceId;
            this.contentResourceId = contentResourceId;
            this.iconResourceId = iconResourceId;
        }

        public static int getTitleResourceId(int key) {
            for (LearnMoreFirstSection learnMoreTitle : LearnMoreFirstSection.values()) {
                if (key == learnMoreTitle.feedbackKey) {
                    return learnMoreTitle.titleResourceId;
                }
            }
            return VAUnknown.titleResourceId;
        }

        public static int getContentResourceId(int key) {
            for (LearnMoreFirstSection learnMoreContent : LearnMoreFirstSection.values()) {
                if (key == learnMoreContent.feedbackKey) {
                    return learnMoreContent.contentResourceId;
                }
            }
            return VAUnknown.contentResourceId;
        }

        public static int getIconResourceId(int key) {
            for (LearnMoreFirstSection learnMoreContent : LearnMoreFirstSection.values()) {
                if (key == learnMoreContent.feedbackKey) {
                    return learnMoreContent.iconResourceId;
                }
            }
            return VAUnknown.iconResourceId;
        }
    }

    public enum LearnMoreSecondSection {
        VAgeNotEnoughData(VitalityAgeConstants.VA_NOT_ENOUGH_DATA,
                R.string.my_health_learn_more_find_your_vitality_age_title_748,
                R.string.my_health_learn_more_find_your_vitality_age_content_749,
                R.drawable.know_your_health_med
        ),
        VAgeAbove(VitalityAgeConstants.VA_ABOVE,
                R.string.my_health_learn_more_how_is_this_calculated_title_746,
                R.string.my_health_learn_more_how_is_this_calculated_content_747,
                R.drawable.calculator_med),
        VAgeHealthy(VitalityAgeConstants.VA_HEALTHY,
                R.string.my_health_learn_more_how_is_this_calculated_title_746,
                R.string.my_health_learn_more_how_is_this_calculated_content_747,
                R.drawable.calculator_med),
        VAUnknown(VitalityAgeConstants.VA_UNKNOWN,
                R.string.my_health_learn_more_find_your_vitality_age_title_748,
                R.string.my_health_learn_more_find_your_vitality_age_content_749,
                R.drawable.know_your_health_med),
        VAgeBelow(VitalityAgeConstants.VA_BELOW,
                R.string.my_health_learn_more_how_is_this_calculated_title_746,
                R.string.my_health_learn_more_how_is_this_calculated_content_747,
                R.drawable.calculator_med),
        VAgeOutdated(VitalityAgeConstants.VA_OUTDATED,
                R.string.my_health_learn_more_find_your_vitality_age_title_748,
                R.string.my_health_learn_more_find_your_vitality_age_content_749,
                R.drawable.know_your_health_med);

        public int feedbackKey, titleResourceId, contentResourceId, iconResourceId;

        LearnMoreSecondSection(int feedbackKey, int titleResourceId, int contentResourceId, int iconResourceId) {
            this.feedbackKey = feedbackKey;
            this.titleResourceId = titleResourceId;
            this.contentResourceId = contentResourceId;
            this.iconResourceId = iconResourceId;
        }

        public static int getTitleResourceId(int key) {
            for (LearnMoreSecondSection learnMoreTitle : LearnMoreSecondSection.values()) {
                if (key == learnMoreTitle.feedbackKey) {
                    return learnMoreTitle.titleResourceId;
                }
            }
            return VAUnknown.titleResourceId;
        }

        public static int getContentResourceId(int key) {
            for (LearnMoreSecondSection learnMoreContent : LearnMoreSecondSection.values()) {
                if (key == learnMoreContent.feedbackKey) {
                    return learnMoreContent.contentResourceId;
                }
            }
            return VAUnknown.contentResourceId;
        }

        public static int getIconResourceId(int key) {
            for (LearnMoreSecondSection learnMoreContent : LearnMoreSecondSection.values()) {
                if (key == learnMoreContent.feedbackKey) {
                    return learnMoreContent.iconResourceId;
                }
            }
            return VAUnknown.iconResourceId;
        }
    }

    public enum VitalityAgeOutdatedLearnMore {
        VAgeAbove(VitalityAgeConstants.VA_ABOVE, R.string.my_health_learn_more_vitality_age_above_outdated_description), VAgeHealthy(VitalityAgeConstants.VA_HEALTHY, R.string.my_health_learn_more_vitality_age_outdated_description_1101), VAgeBelow(VitalityAgeConstants.VA_BELOW, R.string.my_health_learn_more_vitality_age_outdated_description_1101);
        public int key, contentResourceId;

        VitalityAgeOutdatedLearnMore(int key, int contentKey) {
            this.key = key;
            this.contentResourceId = contentKey;
        }

        public static int getContentResourceId(int key) {
            for (VitalityAgeOutdatedLearnMore feedbackContent : VitalityAgeOutdatedLearnMore.values()) {
                if (key == feedbackContent.key) {
                    return feedbackContent.contentResourceId;
                }
            }
            return getDefaultContentId();
        }

        public static int getDefaultContentId() {
            return R.string.my_health_vitality_age_outdated_default;
        }
    }

}
