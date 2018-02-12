package com.vitalityactive.va.utilities.date;

import android.support.annotation.NonNull;

import org.threeten.bp.format.DateTimeFormatter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class NonUserFacingDateFormatter {
    public static final String TAG = "NUFDateFormatter";
    private static final NonUserFacingDateFormatter allFieldsWithoutSeparatorsFormatter = new NonUserFacingDateFormatter("yyyyMMddHHmmssSSSS");
    private static final NonUserFacingDateFormatter yearMonthDayFormatter = new NonUserFacingDateFormatter("yyyy-MM-dd");
    private static final SimpleDateFormat javaUtilDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

    private final DateTimeFormatter dateTimeFormatter;

    public NonUserFacingDateFormatter(DateFormat dateFormat) {
        this(dateFormat instanceof SimpleDateFormat
                ? ((SimpleDateFormat) dateFormat).toLocalizedPattern()
                : "EEEE, dd MMMM yyyy");
    }

    public NonUserFacingDateFormatter(String pattern) {
        dateTimeFormatter = DateTimeFormatter.ofPattern(pattern);
    }

    @NonNull
    public static NonUserFacingDateFormatter getYearMonthDayFormatter() {
        return yearMonthDayFormatter;
    }

    @Deprecated
    public static String getYearMonthDayDateString(java.util.Date date) {
        return yearMonthDayFormatter.format(new Date(date.getTime()));
    }

    @Deprecated
    public static java.util.Date javaUtilDateFromYearMonthDayString(String yearMonthDayString) throws ParseException {
        return javaUtilDateFormat.parse(yearMonthDayString);
    }

    @NonNull
    public static NonUserFacingDateFormatter getAllFieldsWithoutSeparatorsFormatter() {
        return allFieldsWithoutSeparatorsFormatter;
    }

    public String format(Date date) {
        return dateTimeFormatter.format(date.getValue());
    }

    public String format(LocalDate localDate) {
        return dateTimeFormatter.format(localDate.getValue());
    }
}
