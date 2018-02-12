package com.vitalityactive.va.utilities.date.formatting;

import android.content.Context;
import android.text.format.DateUtils;

import com.vitalityactive.va.utilities.date.Date;
import com.vitalityactive.va.utilities.date.LocalDate;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class DateFormattingUtilities {
    private final Context context;

    public DateFormattingUtilities(Context context) {
        this.context = context;
    }

    /**
     * @return "12/31/2017"
     */
    public String formatDateYearInNumericFormat(LocalDate localDate) {
        return DateFormattingUtilities.formatDateYearInNumericFormat(this.context, localDate.getMillisecondsSinceEpoch());
    }

    public static String formatDateYearInNumericFormat(Context context, long timeMs) {
        return DateUtils.formatDateTime(context, timeMs,
                DateUtils.FORMAT_NUMERIC_DATE | DateUtils.FORMAT_SHOW_YEAR);
    }

    /**
     * @return "Oct 23, 2017"
     */
    public String formatDateMonthAbbreviatedYear(LocalDate localDate) {
        return DateFormattingUtilities.formatDateMonthAbbreviatedYear(this.context, localDate.getMillisecondsSinceEpoch());
    }

    public static String formatDateMonthAbbreviatedYear(Context context, LocalDate localDate) {
        return DateFormattingUtilities.formatDateMonthAbbreviatedYear(context, localDate.getMillisecondsSinceEpoch());
    }

    public String formatDateMonthAbbreviatedYear(com.vitalityactive.va.utilities.date.Date date) {
        return DateFormattingUtilities.formatDateMonthAbbreviatedYear(this.context, date.getMillisecondsSinceEpoch());
    }

    public static String formatDateMonthAbbreviatedYear(Context context, long timeMs) {
        return DateUtils.formatDateTime(context, timeMs,
                DateUtils.FORMAT_ABBREV_MONTH | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR);
    }



    /**
     * @return "October 2017"
     */
    public String formatMonthYear(LocalDate localDate) {
        return DateFormattingUtilities.formatMonthYear(this.context, localDate.getMillisecondsSinceEpoch());
    }

    public String formatMonthYear(com.vitalityactive.va.utilities.date.Date date) {
        return DateFormattingUtilities.formatMonthYear(this.context, date.getMillisecondsSinceEpoch());
    }

    public static String formatMonthYear(Context context, long timeMs) {
        return DateUtils.formatDateTime(context, timeMs,
                DateUtils.FORMAT_NO_MONTH_DAY | DateUtils.FORMAT_SHOW_YEAR);
    }

    /**
     * @return "Mon, Oct 23"
     */
    public String formatDateAbbreviatedWeekdayAbbreviatedMonth(LocalDate localDate) {
        return DateFormattingUtilities.formatDateAbbreviatedWeekdayAbbreviatedMonth(this.context, localDate);
    }

    public static String formatDateAbbreviatedWeekdayAbbreviatedMonth(Context context, LocalDate localDate) {
        return DateUtils.formatDateTime(context, localDate.getMillisecondsSinceEpoch(),
                DateUtils.FORMAT_ABBREV_WEEKDAY |
                        DateUtils.FORMAT_SHOW_WEEKDAY |
                        DateUtils.FORMAT_SHOW_DATE |
                        DateUtils.FORMAT_ABBREV_MONTH);
    }

    /**
     * @return "Tuesday, 01 January 2030"
     */
    public String formatWeekdayDateMonthYear(LocalDate localDate) {
        return DateFormattingUtilities.formatWeekdayDateMonthYear(this.context, localDate.getMillisecondsSinceEpoch());
    }

    public static String formatWeekdayDateMonthYear(Context context, LocalDate localDate) {
        return DateFormattingUtilities.formatWeekdayDateMonthYear(context, localDate.getMillisecondsSinceEpoch());
    }

    public String formatWeekdayDateMonthYear(long timeMs) {
        return DateFormattingUtilities.formatWeekdayDateMonthYear(this.context, timeMs);
    }

    public static String formatWeekdayDateMonthYear(Context context, long timeMs) {
        return DateUtils.formatDateTime(context, timeMs,
                DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR | DateUtils.FORMAT_SHOW_WEEKDAY);
    }

    public static String formatWeekdayDateMonthYear(Context context, Date date) {
        return formatWeekdayDateMonthYear(context, date.getMillisecondsSinceEpoch());
    }

    public static String formatWeekdayDateMonthAbbreviatedYear(Context context, long timeMs) {
        return DateUtils.formatDateTime(context, timeMs,
                DateUtils.FORMAT_SHOW_DATE |
                        DateUtils.FORMAT_SHOW_YEAR |
                        DateUtils.FORMAT_SHOW_WEEKDAY |
                        DateUtils.FORMAT_ABBREV_MONTH);
    }

    /**
     * @return "Oct 23, 2017 – Nov 4, 2018"
     */
    public String formatRangeDateMonthAbbreviatedYear(LocalDate startDate, LocalDate endDate) {
        return DateFormattingUtilities.formatRangeDateMonthAbbreviatedYear(this.context, startDate, endDate);
    }

    public static String formatRangeDateMonthAbbreviatedYear(Context context, LocalDate startDate, LocalDate endDate) {       return DateUtils.formatDateRange(context, startDate.getMillisecondsSinceEpoch(), getAlignedEndDate(endDate),
             DateUtils.FORMAT_ABBREV_MONTH | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR);

    }

    /**
     * @return "Mon, 23 Oct  – Wed,4 Nov "
     */
    public String formatRangeDateMonthAbbreviatedYearDay(LocalDate startDate, LocalDate endDate) {
        return DateFormattingUtilities.formatRangeDateMonthAbbreviatedYearDay(this.context, startDate, endDate);
    }

    public static String formatRangeDateMonthAbbreviatedYearDay(Context context, LocalDate startDate, LocalDate endDate) {
        String sDate = getFormattedDate("E, dd MMM", startDate);
        String eDate = getFormattedDate("E, dd MMM", endDate);
        return sDate + " - " +eDate;
    }

    /**
     * @return "Mon, 23-Oct – Wed, 4-Nov "
     */
    public static String formatRangeDateMonthAbbreviatedYearDayDashed(Context context, LocalDate startDate, LocalDate endDate) {
        String sDate = getFormattedDate("E, dd-MMM", startDate);
        String eDate = getFormattedDate("E, dd-MMM", endDate);
        return sDate + " - " +eDate;
    }
    /**
     * @return "12 August 2016"
     */
    public String formatDateMonthYear(LocalDate localDate) {
        return DateFormattingUtilities.formatDateMonthYear(this.context, localDate);
    }

    public static String formatDateMonthYear(Context context, LocalDate localDate) {
        return DateUtils.formatDateTime(context, localDate.getMillisecondsSinceEpoch(),
                DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR);
    }

    /**
     * @return Oct 23 – 29, 2017      yearMandatory == true
     * Oct 23 – 29            yearMandatory == false
     * Oct 23 – Nov 4, 2017   yearMandatory == true
     * Oct 23 – Nov 4         yearMandatory == false
     * Oct 23, 2017 – Feb 19, 2018
     */
    public String formatRangeDateMonthAbbreviatedYearOptional(LocalDate startDate,
                                                              LocalDate endDate,
                                                              boolean yearMandatory) {
        return DateFormattingUtilities.formatRangeDateMonthAbbreviatedYearOptional(this.context,
                startDate,
                endDate,
                yearMandatory);
    }

    public static String formatRangeDateMonthAbbreviatedYearOptional(Context context,
                                                                     LocalDate startDate,
                                                                     LocalDate endDate,
                                                                     boolean yearMandatory) {
        int flags = DateUtils.FORMAT_ABBREV_MONTH | DateUtils.FORMAT_SHOW_DATE |
                ((yearMandatory || startDate.getYear() != endDate.getYear()) ? DateUtils.FORMAT_SHOW_YEAR : 0);
        return DateUtils.formatDateRange(context, startDate.getMillisecondsSinceEpoch(), getAlignedEndDate(endDate), flags);
    }

    /**
     * @return Oct 23 – 29, 2017      yearMandatory == true
     * Oct 23 – 29            yearMandatory == false
     * Oct 23 – Nov 4, 2017   yearMandatory == true
     * Oct 23 – Nov 4         yearMandatory == false
     * Oct 23, 2017 – Feb 19, 2018
     */
    public String formatRangeWeekdayDateMonthAbbreviatedYearOptional(LocalDate startDate,
                                                                     LocalDate endDate,
                                                                     boolean yearMandatory) {
        return DateFormattingUtilities.formatRangeWeekdayDateMonthAbbreviatedYearOptional(this.context,
                startDate,
                endDate,
                yearMandatory);
    }

    public static String formatRangeWeekdayDateMonthAbbreviatedYearOptional(Context context,
                                                                            LocalDate startDate,
                                                                            LocalDate endDate,
                                                                            boolean yearMandatory) {
        int flags = DateUtils.FORMAT_ABBREV_MONTH | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_WEEKDAY |
                ((yearMandatory || startDate.getYear() != endDate.getYear()) ? DateUtils.FORMAT_SHOW_YEAR : 0);
        return DateUtils.formatDateRange(context, startDate.getMillisecondsSinceEpoch(), getAlignedEndDate(endDate), flags);
    }

    /**
     * @return "2:35 PM", "14:35"
     */
    public static String formatHoursMinutes(Context context, com.vitalityactive.va.utilities.date.Date date) {
        return DateFormattingUtilities.formatHoursMinutes(context, date.getMillisecondsSinceEpoch());
    }

    public static String formatHoursMinutes(Context context, long timeMs) {
        return DateUtils.formatDateTime(context, timeMs, DateUtils.FORMAT_SHOW_TIME);
    }

    /**
     * @return "31 October 2017, 2:35 PM"
     */
    public String formatDateMonthYearHoursMinutes(com.vitalityactive.va.utilities.date.Date date) {
        return DateFormattingUtilities.formatDateMonthYearHoursMinutes(this.context, date.getMillisecondsSinceEpoch());
    }

    public static String formatDateMonthYearHoursMinutes(Context context, com.vitalityactive.va.utilities.date.Date date) {
        return DateFormattingUtilities.formatDateMonthYearHoursMinutes(context, date.getMillisecondsSinceEpoch());
    }

    public static String formatDateMonthYearHoursMinutes(Context context, long millisecondsSinceEpoch) {
        return DateUtils.formatDateTime(context, millisecondsSinceEpoch,
                DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_YEAR | DateUtils.FORMAT_SHOW_DATE);
    }

    /**
     * formatDateRange() is cutting off the end day of period. That's because it cuts off at midnight
     * Adding 1 millisecond solves this issue
     * See this article for more details:
     * http://blog.danlew.net/2013/06/27/how_to_correctly_format_date_time_strings_on_android
     */
    private static long getAlignedEndDate(LocalDate endDate) {
        return endDate.getMillisecondsSinceEpoch() + 1;
    }



    public static String getFormattedDate(String format, LocalDate date){
        return new SimpleDateFormat(format, Locale.getDefault()).format(new java.util.Date(date.getMillisecondsSinceEpoch()));
    }
}
