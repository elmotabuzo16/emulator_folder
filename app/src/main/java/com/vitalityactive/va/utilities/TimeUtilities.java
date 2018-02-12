package com.vitalityactive.va.utilities;

import com.vitalityactive.va.utilities.date.Date;
import com.vitalityactive.va.utilities.date.LocalDate;

import org.threeten.bp.ZonedDateTime;
import org.threeten.bp.format.DateTimeFormatter;
import org.threeten.bp.format.DateTimeParseException;

import java.util.Calendar;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class TimeUtilities {
    public static final String TAG = "TimeUtilities";

    @Inject
    public TimeUtilities() {

    }

    public String getNowAsISODateTimeWithZoneId() {
        return getStringOfISODateWithZoneId(ZonedDateTime.now());
    }

    public String getStringOfISODateWithZoneId(ZonedDateTime date) {
        return DateTimeFormatter.ISO_DATE_TIME.format(date);
    }

    public int getCurrentYear() {
        return ZonedDateTime.now().getYear();
    }

    public int getCurrentMonth() {
        return ZonedDateTime.now().getMonthValue();
    }

    public Date now() {
        return new Date(ZonedDateTime.now());
    }

    public static boolean isDateInPeriod(java.util.Date dateToCheck, java.util.Date periodStart, java.util.Date periodEnd) {
        return periodStart.compareTo(dateToCheck) <= 0 && periodEnd.compareTo(dateToCheck) >= 0;
    }

    public static int getAge(LocalDate birthdate) {
        int age;
        int yearDifference = ZonedDateTime.now().getYear() - birthdate.getYear();
        int monthDifference = ZonedDateTime.now().getMonthValue() - birthdate.getMonth();
        int dayDifference = ZonedDateTime.now().getDayOfMonth() - birthdate.getDayOfMonth();

        if (monthDifference < 0 ||
                (monthDifference == 0 && dayDifference < 0)) {
            age = yearDifference - 1;
        } else {
            age = yearDifference;
        }
        return age;
    }

    public static long getCurrentTimestamp() {
        return Calendar.getInstance().getTimeInMillis();
    }

    public static java.util.Date getAdjustedTime(int type, int value) {
        Calendar instance = Calendar.getInstance();
        instance.add(type, value);
        return instance.getTime();
    }

    public static long getDateFromFormatterString(String date) {
        if (date == null || date.isEmpty()) {
            return 0;
        }

        try {
            return new Date(date).getMillisecondsSinceEpoch();
        } catch (DateTimeParseException ignored) {
            return new Date(date.substring(0, date.indexOf("["))).getMillisecondsSinceEpoch();
        }
    }

    public static boolean isTodayInPeriod(LocalDate effectiveFrom, LocalDate effectiveTo) {
        LocalDate now = LocalDate.now();
        return now.compareTo(effectiveFrom) >= 0 && now.compareTo(effectiveTo) <= 0;
    }

}
