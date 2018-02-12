package com.vitalityactive.va.vitalitystatus;

import android.content.Context;

import com.vitalityactive.va.R;
import com.vitalityactive.va.constants.ProductFeature;
import com.vitalityactive.va.constants.ProductFeatureCategory;

enum PointsEarningFlag {
    EARNING_POINTS(R.string.Status_points_indication_message_829),
    EARN_UP_TO_POINTS(R.string.Status_points_indication_message_up_to_828),
    EARN_UP_TO_POINTS_LIMITS(R.string.Status_points_indication_message_limit_830),
    UNKNOWN(0);

    private final int stringId;

    PointsEarningFlag(int stringId) {
        this.stringId = stringId;
    }

    static PointsEarningFlag fromFlag(String flag) {
        switch (flag) {
            case "EarnPoints":
                return EARNING_POINTS;
            case "EarnUpToPoints":
                return EARN_UP_TO_POINTS;
            case "EarnUpToPointsLimits":
                return EARN_UP_TO_POINTS_LIMITS;
            default:
                return UNKNOWN;
        }
    }

    int getStringId() {
        return stringId;
    }
}

public class ProductPointsContentImpl implements ProductPointsContent {
    private Context context;

    public ProductPointsContentImpl(Context context) {
        this.context = context;
    }

    @Override
    public String getSubtitleString(String pointsEarningFlag, int potentialPoints, boolean pointsLimitReached) {
        int placeholderId;

        if (pointsLimitReached) {
            placeholderId = R.string.Status_points_earned_indication_message_896;
        } else {
            placeholderId = PointsEarningFlag.fromFlag(pointsEarningFlag).getStringId();
        }

        return placeholderId == 0 ? "" : String.format(getString(placeholderId), potentialPoints);
    }

    private String getString(int placeholderId) {
        return context.getString(placeholderId);
    }


    @Override
    public String getCategoryTitle(int key) {
        int categoryTitleResourceId = getCategoryTitleResourceId(key);
        return categoryTitleResourceId == 0 ? "" : getString(categoryTitleResourceId);
    }

    private int getCategoryTitleResourceId(int key) {
        switch (key) {
            case ProductFeatureCategory._ASSESSMENTSMOBILE:
                return R.string.PM_category_filter_assessment_title_516;
            case ProductFeatureCategory._NUTRITIONMOBILE:
                return R.string.PM_category_filter_nutrition_title_517;
            case ProductFeatureCategory._SCREENINGSMOBILE:
                return R.string.PM_category_filter_screening_title_518;
            case ProductFeatureCategory._GETACTIVEMOBILE:
                return R.string.PM_category_filter_fitness_title_519;
            default:
                return 0;
        }
    }

    @Override
    public int getIconResourceId(int key) {
        switch (key) {
            case ProductFeatureCategory._ASSESSMENTSMOBILE:
                return R.drawable.assessments_24;
            case ProductFeatureCategory._NUTRITIONMOBILE:
                return R.drawable.nutrition_24;
            case ProductFeatureCategory._SCREENINGSMOBILE:
                return R.drawable.screenings_24;
            case ProductFeatureCategory._GETACTIVEMOBILE:
                return R.drawable.get_active_24;
            default:
                return R.drawable.img_placeholder;
        }
    }

    @Override
    public String getFeatureTitle(int key) {
        int featureTitleResourceId = getFeatureTitleResourceId(key);
        return featureTitleResourceId == 0 ? "" : getString(featureTitleResourceId);
    }

    @Override
    public String getPointsStatusMessage(int pointsToMaintainStatus, String currentStatusName, int pointsToNextLevel, String nextStatusLevelName) {
        int pointsNeeded;
        int pointsPlaceHolderId;
        String level;
        String pointsStatus;

        if (pointsToMaintainStatus > 0) {
            pointsNeeded = pointsToMaintainStatus;
            level = currentStatusName;
            pointsPlaceHolderId = R.string.Status_landing_points_maintain_message_819;
        } else {
            pointsNeeded = pointsToNextLevel;
            level = nextStatusLevelName;
            pointsPlaceHolderId = R.string.Status_landing_points_target_message_797;
        }

        if (pointsToNextLevel != 0) {
            pointsStatus = String.format(getString(pointsPlaceHolderId), pointsNeeded, level);
        } else {
            pointsStatus = getString(R.string.Status_landing_points_final_message_824);
        }

        return pointsStatus;
    }

    private int getFeatureTitleResourceId(int key) {
        switch (key) {
            case ProductFeature._VHC:
                return R.string.assessment_source_vhc_571;
            case ProductFeature._VHR:
                return R.string.assessment_source_vhr_572;
            case ProductFeature._VNA:
                return R.string.assessment_source_vna_573;
        }
        return 0;
    }
}
