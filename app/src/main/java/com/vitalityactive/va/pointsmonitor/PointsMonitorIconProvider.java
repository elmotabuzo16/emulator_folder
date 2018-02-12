package com.vitalityactive.va.pointsmonitor;

import com.vitalityactive.va.R;
import com.vitalityactive.va.constants.PointsEntryCategory;

import javax.inject.Inject;

class PointsMonitorIconProvider {

    @Inject
    public PointsMonitorIconProvider() {

    }

    public int getIconResourceId(int categoryTypeKey) {
        switch (categoryTypeKey) {
            case 999:
                return R.drawable.pmfilter_all;
            case PointsEntryCategory._FITNESS:
            case PointsEntryCategory._GETACTIVE:
                return R.drawable.pmfilter_getactive;
            case PointsEntryCategory._ASSESSMENT:
                return R.drawable.pmfilter_assessments;
            case PointsEntryCategory._SCREENING:
                return R.drawable.pmfilter_screening;
            case PointsEntryCategory._ADJUSTMENT:
                return R.drawable.pointsadjustment;
            case PointsEntryCategory._BOOSTERPOINTS:
                return R.drawable.booster_points;
            case PointsEntryCategory._HEALTHYFOOD:
                return R.drawable.pmfilter_nutrition;
            default:
                return R.drawable.pmfilter_other;
        }
    }
}
