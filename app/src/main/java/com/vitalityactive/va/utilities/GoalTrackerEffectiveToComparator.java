package com.vitalityactive.va.utilities;

import android.util.Log;

import com.vitalityactive.va.networking.model.goalandprogress.GetGoalProgressAndDetailsResponse;
import com.vitalityactive.va.utilities.date.NonUserFacingDateFormatter;

import java.text.ParseException;
import java.util.Comparator;
import java.util.Date;

public class GoalTrackerEffectiveToComparator implements Comparator<GetGoalProgressAndDetailsResponse.GoalTrackerOut> {
    @Override
    public int compare(GetGoalProgressAndDetailsResponse.GoalTrackerOut t0,
                       GetGoalProgressAndDetailsResponse.GoalTrackerOut t1) {
        try {
            Date d0 = NonUserFacingDateFormatter.javaUtilDateFromYearMonthDayString(t0.effectiveTo);
            Date d1 = NonUserFacingDateFormatter.javaUtilDateFromYearMonthDayString(t1.effectiveTo);
            return d1.compareTo(d0);
        } catch (ParseException e) {
            Log.e(NonUserFacingDateFormatter.TAG, "wrong date format: " + e.toString());
            return 0;
        }
    }
}
