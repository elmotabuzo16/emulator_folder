package com.vitalityactive.va.utilities;

import android.support.test.InstrumentationRegistry;

import com.jakewharton.threetenabp.AndroidThreeTen;
import com.vitalityactive.va.testutilities.annotations.UnitTestRequiringAndroidContext;

import org.junit.Test;
import org.threeten.bp.ZoneId;
import org.threeten.bp.ZonedDateTime;

import static org.junit.Assert.assertEquals;

@UnitTestRequiringAndroidContext
public class TimeUtilitiesTest {
    @Test
    public void the_time_is_in_the_correct_format() {
        AndroidThreeTen.init(InstrumentationRegistry.getTargetContext());
        ZonedDateTime date = ZonedDateTime.of(2017, 1, 12, 8, 43, 33, 0, ZoneId.of("Africa/Johannesburg"));
        assertEquals("2017-01-12T08:43:33+02:00[Africa/Johannesburg]", new TimeUtilities().getStringOfISODateWithZoneId(date));
    }
}
