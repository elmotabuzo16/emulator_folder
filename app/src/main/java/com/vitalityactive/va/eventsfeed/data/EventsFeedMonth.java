package com.vitalityactive.va.eventsfeed.data;

public class EventsFeedMonth {
    private final String monthName;
    private final String year;

    public EventsFeedMonth(String monthName, String year) {
        this.monthName = monthName;
        this.year = year;
    }

    public String getMonthName() {
        return monthName;
    }

    public String getYear() {
        return year;
    }

}
