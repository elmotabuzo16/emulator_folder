package com.vitalityactive.va.pointsmonitor;

class PointsHistoryMonth {
    private final String monthName;
    private final String year;

    PointsHistoryMonth(String monthName, String year) {
        this.monthName = monthName;
        this.year = year;
    }

    String getMonthName() {
        return monthName;
    }

    String getYear() {
        return year;
    }

}
