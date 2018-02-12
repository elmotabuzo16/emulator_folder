package com.vitalityactive.va.utilities;

import com.vitalityactive.va.BaseTest;
import com.vitalityactive.va.utilities.date.LocalDate;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TimeUtilitiesTest extends BaseTest {

    @Override
    public void setUp() throws Exception {
        super.setUp();
        initialiseAndroidThreeTen();
    }

    @Test
    public void can_parse_from_string_with_zone_offset_and_name() {
        String s = "2017-06-22T00:00:00.000000000+02:00[Africa/Johannesburg]";

        long date = TimeUtilities.getDateFromFormatterString(s);

        assertEquals(1498082400000L, date);
    }

    @Test
    public void can_parse_from_string_with_zone_name_only() {
        String s = "2017-07-04T09:14:02.820000000Z[UTC]";

        long date = TimeUtilities.getDateFromFormatterString(s);

        assertEquals(1499159642820L, date);
    }

    @Test
    public void parse_empty_string_as_0() {
        String s = "";

        long date = TimeUtilities.getDateFromFormatterString(s);

        assertEquals(0, date);
    }

    @Test
    public void parse_null_as_0() {
        String s = null;

        long date = TimeUtilities.getDateFromFormatterString(s);

        assertEquals(0, date);
    }

    @Test
    public void ageTest() {
        LocalDate localDateTestData_1 = new LocalDate("1993-03-20");
        LocalDate localDateTestData_2 = new LocalDate("1992-08-31");
        LocalDate localDateTestData_3 = new LocalDate("1997-06-21");
        LocalDate localDateTestData_4 = new LocalDate("2016-01-01");
        LocalDate localDateTestData_5 = new LocalDate("1993-01-01");

        LocalDate localDateTestData_6 = new LocalDate("1998-02-02");
        LocalDate localDateTestData_7 = new LocalDate("1998-02-03");
        LocalDate localDateTestData_8 = new LocalDate("1998-02-01");

        LocalDate localDateTestData_9 = new LocalDate("1998-02-02");
        LocalDate localDateTestData_10 = new LocalDate("1998-01-02");
        LocalDate localDateTestData_11 = new LocalDate("1998-03-02");

        assertEquals(24, TimeUtilities.getAge(localDateTestData_1));
        assertEquals(25, TimeUtilities.getAge(localDateTestData_2));
        assertEquals(20, TimeUtilities.getAge(localDateTestData_3));
        assertEquals(2, TimeUtilities.getAge(localDateTestData_4));
        assertEquals(25, TimeUtilities.getAge(localDateTestData_5));

        assertEquals(20, TimeUtilities.getAge(localDateTestData_6));
        assertEquals(19, TimeUtilities.getAge(localDateTestData_7));
        assertEquals(20, TimeUtilities.getAge(localDateTestData_8));

        assertEquals(20, TimeUtilities.getAge(localDateTestData_9));
        assertEquals(20, TimeUtilities.getAge(localDateTestData_10));
        assertEquals(19, TimeUtilities.getAge(localDateTestData_11));
    }
}
