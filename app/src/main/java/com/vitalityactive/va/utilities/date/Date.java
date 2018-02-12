package com.vitalityactive.va.utilities.date;

import android.support.annotation.NonNull;

import org.threeten.bp.Instant;
import org.threeten.bp.LocalTime;
import org.threeten.bp.ZoneId;
import org.threeten.bp.ZonedDateTime;

import java.util.Objects;

public class Date implements Comparable<Date> {
    private static final int NOT_SET = -1;
    private final ZonedDateTime value;
    private long millisecondsSinceEpoch = NOT_SET;

    public Date(String dateString) {
        value = ZonedDateTime.parse(dateString).withZoneSameInstant(ZoneId.systemDefault());
    }

    public Date(long millisecondsSinceEpoch) {
        this.millisecondsSinceEpoch = millisecondsSinceEpoch;
        value = ZonedDateTime.ofInstant(Instant.ofEpochMilli(millisecondsSinceEpoch), ZoneId.systemDefault());
    }

    public Date(ZonedDateTime value) {
        this.value = value;
    }

    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Date date = (Date) o;
        return this.value.isEqual(date.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    public int compareTo(@NonNull Date other) {
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

    public long getMillisecondsSinceEpoch() {
        if (millisecondsSinceEpoch == NOT_SET) {
            millisecondsSinceEpoch = value.toInstant().toEpochMilli();
        }
        return millisecondsSinceEpoch;
    }

    public ZonedDateTime getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value.toString();
    }

    public Date toFirstDayOfMonth() {
        return new Date(value.withDayOfMonth(1).with(LocalTime.of(0, 0, 0)));
    }

    public Date toLastDayOfMonth() {
        return new Date(value.withDayOfMonth(value.toLocalDate().lengthOfMonth()).with(LocalTime.of(23, 59, 59)));
    }

    public Date minusMonths(int months) {
        return new Date(value.minusMonths(months));
    }
}
