package com.vitalityactive.va.myhealth.content;

import android.content.Context;

import com.vitalityactive.va.R;
import com.vitalityactive.va.myhealth.shared.MyHealthUtils;
import com.vitalityactive.va.myhealth.shared.VitalityAgeConstants;

import java.util.Arrays;

public class MyHealthContent {
    public static final int MAX_TIPS_TO_SHOW = 3;

    public static VitalityAgeData getVitalityAge(Context context, String vitalityAgeValue, String summary, int feedbackType, String variation) {
        final VitalityAgeData vitalityAgeDetail = new VitalityAgeData.Builder().build();
        String placeHolderData = null;
        int feedbackTypeKey = feedbackType != 0 ? feedbackType : VitalityAgeConstants.VA_UNKNOWN;
        if (feedbackType == VitalityAgeConstants.VA_NOT_ENOUGH_DATA) {
            vitalityAgeDetail.isUnknown = true;
            placeHolderData = context.getString(R.string.my_health_vitality_age_vhc_completed_vhr_not_completed_not_enough_data_title_617);
            summary = context.getString(R.string.my_health_vitality_age_after_vhc_submission_result_unknown_title_615);
            vitalityAgeValue = null;
        }
        vitalityAgeDetail.summaryIconResourceId = StatusIcon.getIconId(feedbackTypeKey);
        vitalityAgeDetail.itemFeedbackExplanation = VitalityAgeContent.getVitalityAgeFeedback(context, feedbackTypeKey, variation);
        vitalityAgeDetail.itemFeedbackSummary = summary != null ? summary : "";
        vitalityAgeDetail.itemVitalityAgeValuePlaceHolder = placeHolderData;
        vitalityAgeDetail.itemVitalityAgeValue = vitalityAgeValue != null ? MyHealthUtils.getDisplayVitalityAge(vitalityAgeValue) : null;
        vitalityAgeDetail.bgIconResource = VitalityAgeBackgroundIcon.getIconId(feedbackTypeKey);
        vitalityAgeDetail.itemFeedbackType = feedbackTypeKey;
        return vitalityAgeDetail;
    }

    public static VitalityAgeData getVitalityAgeSummary(Context context, String vitalityAgeValue, String summary, int feedbackType, String variation) {
        final VitalityAgeData vitalityAgeDetail = new VitalityAgeData.Builder().build();
        String placeHolderData = null;
        if (feedbackType == VitalityAgeConstants.VA_NOT_ENOUGH_DATA) {
            placeHolderData = context.getString(R.string.my_health_vitality_age_vhc_completed_vhr_not_completed_not_enough_data_title_617);
            summary = context.getString(R.string.my_health_vitality_age_after_vhc_submission_result_unknown_title_615);
        } else if (vitalityAgeValue == null && summary == null) {
            vitalityAgeDetail.isUnknown = true;
            placeHolderData = context.getString(R.string.my_health_vitality_age_unknown_627);
        }
        int feedbackTypeKey = feedbackType != 0 ? feedbackType : VitalityAgeConstants.VA_UNKNOWN;
        vitalityAgeDetail.summaryIconResourceId = MyHealthContent.StatusIcon.getIconId(feedbackTypeKey);
        vitalityAgeDetail.itemFeedbackExplanation = VitalityAgeContent.getVitalityAgeFeedbackSummary(context, feedbackTypeKey, variation);
        vitalityAgeDetail.itemFeedbackSummary = summary != null ? summary : "";
        vitalityAgeDetail.itemVitalityAgeValuePlaceHolder = placeHolderData;
        vitalityAgeDetail.itemVitalityAgeValue = vitalityAgeValue != null ? MyHealthUtils.getDisplayVitalityAge(vitalityAgeValue) : "";
        vitalityAgeDetail.bgIconResource = MyHealthContent.MyHealthBackgroundIcon.getIconId(feedbackTypeKey);
        vitalityAgeDetail.itemFeedbackType = feedbackTypeKey;
        return vitalityAgeDetail;
    }

    public static boolean vitalityAgeCalculated(int feedbackKey) {
        return Arrays.asList(VitalityAgeConstants.VA_ABOVE, VitalityAgeConstants.VA_HEALTHY, VitalityAgeConstants.VA_BELOW).contains(feedbackKey);
    }

    public enum StatusIcon {
        VAgeNotEnoughData(VitalityAgeConstants.VA_NOT_ENOUGH_DATA, R.drawable.result_unknown_24), VAgeAbove(VitalityAgeConstants.VA_ABOVE, R.drawable.value_too_high_small), VAgeHealthy(VitalityAgeConstants.VA_HEALTHY, R.drawable.value_looking_great_small), VAUnknown(VitalityAgeConstants.VA_UNKNOWN, R.drawable.value_result_unknown_small), VAgeBelow(VitalityAgeConstants.VA_BELOW, R.drawable.value_looking_great_small);

        public int feedbackKey, resourceId;

        StatusIcon(int feedbackKey, int resourceId) {
            this.feedbackKey = feedbackKey;
            this.resourceId = resourceId;
        }

        public static int getIconId(int feedbackKey) {
            for (StatusIcon iconKey : StatusIcon.values()) {
                if (iconKey.feedbackKey == feedbackKey) {
                    return iconKey.resourceId;
                }
            }
            return VAUnknown.resourceId;
        }
    }

    public enum MyHealthBackgroundIcon {
        VAgeNotEnoughData(VitalityAgeConstants.VA_NOT_ENOUGH_DATA, R.drawable.vitality_age_disabled_large), VAgeAbove(VitalityAgeConstants.VA_ABOVE, R.drawable.vitality_age_large), VAgeHealthy(VitalityAgeConstants.VA_HEALTHY, R.drawable.vitality_age_large), VAgeBelow(VitalityAgeConstants.VA_BELOW, R.drawable.vitality_age_large), VAUnknown(VitalityAgeConstants.VA_UNKNOWN, R.drawable.vitality_age_large);

        public int feedbackKey, resourceId;

        MyHealthBackgroundIcon(int feedbackKey, int resourceId) {
            this.feedbackKey = feedbackKey;
            this.resourceId = resourceId;
        }

        public static int getIconId(int feedbackKey) {
            for (MyHealthBackgroundIcon iconKey : MyHealthBackgroundIcon.values()) {
                if (iconKey.feedbackKey == feedbackKey) {
                    return iconKey.resourceId;
                }
            }
            return VAUnknown.resourceId;
        }
    }

    public enum VitalityAgeBackgroundIcon {
        VAgeNotEnoughData(VitalityAgeConstants.VA_NOT_ENOUGH_DATA, R.drawable.vitality_age_disabled_xlarge), VAgeAbove(VitalityAgeConstants.VA_ABOVE, R.drawable.vitality_age_xlarge), VAgeHealthy(VitalityAgeConstants.VA_HEALTHY, R.drawable.vitality_age_xlarge), VAgeBelow(VitalityAgeConstants.VA_BELOW, R.drawable.vitality_age_xlarge), VAUnknown(VitalityAgeConstants.VA_UNKNOWN, R.drawable.vitality_age_xlarge);

        public int feedbackKey, resourceId;

        VitalityAgeBackgroundIcon(int feedbackKey, int resourceId) {
            this.feedbackKey = feedbackKey;
            this.resourceId = resourceId;
        }

        public static int getIconId(int feedbackKey) {
            for (VitalityAgeBackgroundIcon iconKey : VitalityAgeBackgroundIcon.values()) {
                if (iconKey.feedbackKey == feedbackKey) {
                    return iconKey.resourceId;
                }
            }
            return VAUnknown.resourceId;
        }
    }


    public enum VitalityAgeTip {
        Tip_doing_well(VitalityAgeConstants.TIP_WHAT_DOING_WELL, R.drawable.value_looking_great_small, "#2bca6d"), Tip_what_to_improve(VitalityAgeConstants.TIP_WHAT_TO_IMPROVE, R.drawable.value_letswork_24, "#f34358"), Tip_not_provided(VitalityAgeConstants.TIP_WHAT_NOT_PROVIDED, R.drawable.result_unknown_24, "#89000000");
        public int feedbackTipSortOrder, iconResourceId;
        public String tintColor;

        VitalityAgeTip(int feedbackTipSortOrder, int iconResourceId, String tintColor) {
            this.feedbackTipSortOrder = feedbackTipSortOrder;
            this.iconResourceId = iconResourceId;
            this.tintColor = tintColor;
        }

        public static int getIconResource(int feedbackTipType) {
            for (VitalityAgeTip vitalityAgeTip : VitalityAgeTip.values()) {
                if (feedbackTipType == vitalityAgeTip.feedbackTipSortOrder) {
                    return vitalityAgeTip.iconResourceId;
                }
            }
            return Tip_not_provided.iconResourceId;
        }

        public static String getIconTintColor(int feedbackTipType) {
            for (VitalityAgeTip vitalityAgeTip : VitalityAgeTip.values()) {
                if (feedbackTipType == vitalityAgeTip.feedbackTipSortOrder) {
                    return vitalityAgeTip.tintColor;
                }
            }
            return Tip_not_provided.tintColor;
        }
    }

    public enum VitalityAgeTipsFeedbackPlaceholder {
        doingWell(VitalityAgeConstants.V_AGE_TYPE_KEY_RESULT_GOOD, R.string.feedback_tips_whatdoingwell_missing), whatYouCanImprove(VitalityAgeConstants.V_AGE_TYPE_KEY_RESULT_BAD, R.string.feedback_tips_whattoimprove_missing), whatNotProvided(VitalityAgeConstants.V_AGE_TYPE_KEY_RESULT_UNKNOWN, R.string.feedback_tips_whatnotprovided_missing);

        public long feebackTypeKey;
        public int placeholderResourceId;

        VitalityAgeTipsFeedbackPlaceholder(long feebackTypeKey, int placeholderResourceId) {
            this.feebackTypeKey = feebackTypeKey;
            this.placeholderResourceId = placeholderResourceId;
        }

        public static int getPlaceholder(long feedbackTypeKey) {
            for (VitalityAgeTipsFeedbackPlaceholder vitalityAgeTip : VitalityAgeTipsFeedbackPlaceholder.values()) {
                if (feedbackTypeKey == vitalityAgeTip.feebackTypeKey) {
                    return vitalityAgeTip.placeholderResourceId;
                }
            }
            return whatNotProvided.placeholderResourceId;
        }
    }

    public enum FeedbackAttributeTitle {
        doingWell(VitalityAgeConstants.V_AGE_TYPE_KEY_RESULT_GOOD, R.string.my_health_metric_detail_tips_to_maintain_1122), whatYouCanImprove(VitalityAgeConstants.V_AGE_TYPE_KEY_RESULT_BAD, R.string.my_health_metric_detail_tips_to_improve_1121), whatNotProvided(VitalityAgeConstants.V_AGE_TYPE_KEY_RESULT_UNKNOWN, R.string.my_health_metric_detail_tips_to_importance_1123);
        public long key;
        public int contentResourceId;

        FeedbackAttributeTitle(long key, int contentKey) {
            this.key = key;
            this.contentResourceId = contentKey;
        }

        public static int getContentId(int key) {
            for (FeedbackAttributeTitle feedbackTitle : FeedbackAttributeTitle.values()) {
                if (key == feedbackTitle.key) {
                    return feedbackTitle.contentResourceId;
                }
            }
            return getDefaultContentId();
        }

        public static int getDefaultContentId() {
            return R.string.feedback_attribute_section_title_result_whatnotprovided;
        }
    }

}
