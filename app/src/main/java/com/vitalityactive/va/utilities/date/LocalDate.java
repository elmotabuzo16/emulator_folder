package com.vitalityactive.va.utilities.date;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.threeten.bp.DayOfWeek;
import org.threeten.bp.ZoneId;
import org.threeten.bp.format.DateTimeParseException;
import org.threeten.bp.temporal.WeekFields;

import java.util.Locale;
import java.util.Objects;

public class LocalDate implements Comparable<LocalDate> {
    private final org.threeten.bp.LocalDate value;

    public LocalDate(String dateString) {
        value = org.threeten.bp.LocalDate.parse(dateString);
    }

    public LocalDate(@NonNull org.threeten.bp.LocalDate value) {
        this.value = value;
    }

    public LocalDate(int year, int month, int dayOfMonth) {
        value = org.threeten.bp.LocalDate.of(year, month, dayOfMonth);
    }

    @Nullable
    public static LocalDate create(String dateString) {
        try {
            return new LocalDate(dateString);
        } catch (DateTimeParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        LocalDate date = (LocalDate) o;
        return this.value.isEqual(date.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    public int compareTo(@NonNull LocalDate other) {
        return value.compareTo(other.value);
    }

    public int getMonth() {
        return value.getMonthValue();
    }

    public int getYear() {
        return value.getYear();
    }

    public int getDayOfMonth() {
        return value.getDayOfMonth();
    }

    public org.threeten.bp.LocalDate getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value.toString();
    }

    public LocalDate toFirstDayOfMonth() {
        return new LocalDate(value.withDayOfMonth(1));
    }

    public LocalDate toLastDayOfMonth() {
        return new LocalDate(value.withDayOfMonth(value.lengthOfMonth()));
    }

    public LocalDate toFirstDayOfWeek() {
        return new LocalDate(value.with(WeekFields.of(Locale.getDefault()).getFirstDayOfWeek()));
    }

    public LocalDate minusMonths(int months) {
        return new LocalDate(value.minusMonths(months));
    }

    public static LocalDate now() {
        return new LocalDate(org.threeten.bp.LocalDate.now());
    }

    public LocalDate plusWeeks(long weeks) {
        return new LocalDate(value.plusWeeks(weeks));
    }

    public LocalDate toMonday() {
        return new LocalDate(value.with(DayOfWeek.MONDAY));
    }

    public long getMillisecondsSinceEpoch() {
        return value.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    public LocalDate minusDays(long days) {
        return new LocalDate(value.minusDays(days));
    }

    public boolean isBefore(LocalDate other) {
        return value.isBefore(other.getValue());
    }
}
