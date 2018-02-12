package com.vitalityactive.va.pointsmonitor;

import android.content.Context;
import android.support.annotation.NonNull;

import com.vitalityactive.va.R;
import com.vitalityactive.va.constants.PointsEntryCategory;

public class PointsMonitorContentImpl implements PointsMonitorContent {
    private Context context;

    public PointsMonitorContentImpl(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public String getPointsEntryCategoryTitle(int categoryTypeKey) {
        switch (categoryTypeKey) {
            case 999:
                return getString(R.string.PM_category_filter_all_points_title_515);
            case PointsEntryCategory._FITNESS:
                return getString(R.string.PM_category_filter_fitness_title_519);
            case PointsEntryCategory._ASSESSMENT:
                return getString(R.string.PM_category_filter_assessment_title_516);
            case PointsEntryCategory._SCREENING:
                return getString(R.string.PM_category_filter_screening_title_518);
            case PointsEntryCategory._ADJUSTMENT:
                return getString(R.string.PM_category_filter_points_adjustments_title_521);
            case PointsEntryCategory._BOOSTERPOINTS:
                return getString(R.string.PM_category_filter_booster_points_title_520);
            case PointsEntryCategory._HEALTHYFOOD:
                return getString(R.string.PM_category_filter_nutrition_title_517);
            default:
                return getString(R.string.PM_category_filter_pother_title_521);
        }
    }

    @NonNull
    private String getString(int resourceId) {
        return context.getString(resourceId);
    }
}
