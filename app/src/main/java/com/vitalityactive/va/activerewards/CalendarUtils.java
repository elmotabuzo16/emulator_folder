package com.vitalityactive.va.activerewards;

import android.util.Log;

import com.vitalityactive.va.activerewards.landing.service.EffectiveDate;
import com.vitalityactive.va.utilities.date.NonUserFacingDateFormatter;
import com.vitalityactive.va.utilities.date.LocalDate;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

public class CalendarUtils {
    public static EffectiveDate getCurrentWeekRange() {
        LocalDate startDate = LocalDate.now().toMonday();
        String start = startDate.toString();
        LocalDate endDate = startDate.plusWeeks(2);
        String end = endDate.toString();
        return new EffectiveDate(start, end);
    }

    public static EffectiveDate getFirstDayOfMonth(String dayPlusOne) throws ParseException {
        Calendar cal = Calendar.getInstance();
        cal.setTime(NonUserFacingDateFormatter.javaUtilDateFromYearMonthDayString(dayPlusOne));
        cal.add(Calendar.DATE, -1); // previous day
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.clear(Calendar.MINUTE);
        cal.clear(Calendar.SECOND);
        cal.clear(Calendar.MILLISECOND);
        String end = NonUserFacingDateFormatter.getYearMonthDayDateString(cal.getTime());
        cal.set(Calendar.DATE, 1);
        String start = NonUserFacingDateFormatter.getYearMonthDayDateString(cal.getTime());
        EffectiveDate range = new EffectiveDate(start, end);
        Log.d(">>>", "range: " + range);
        return range;
    }

    /**
     * {"effectiveFrom":"2017-07-01","effectiveTo":"2017-09-20"}
     * {"effectiveFrom":"2017-04-01","effectiveTo":"2017-06-30"}
     * {"effectiveFrom":"2017-01-01","effectiveTo":"2017-03-31"}
     */
    public static EffectiveDate getRangeForRequest(String endDate) throws ParseException {
        Calendar cal = Calendar.getInstance();
        cal.setTime(NonUserFacingDateFormatter.javaUtilDateFromYearMonthDayString(endDate));
        cal.add(Calendar.DATE, -1);
        String effectiveTo = NonUserFacingDateFormatter.getYearMonthDayDateString(cal.getTime());
        cal.set(Calendar.DATE, 1);
        cal.add(Calendar.MONTH, -2);
        String effectiveFrom = NonUserFacingDateFormatter.getYearMonthDayDateString(cal.getTime());

        return new EffectiveDate(effectiveFrom, effectiveTo);
    }

    public static String getToday() {
        Calendar cal = Calendar.getInstance();
        return NonUserFacingDateFormatter.getYearMonthDayDateString(cal.getTime());
    }

    public static Date getLastSupportedDate() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, -2);
        return cal.getTime();
    }

    public static String getTomorrow() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, 1);
        return NonUserFacingDateFormatter.getYearMonthDayDateString(cal.getTime());
    }

}
