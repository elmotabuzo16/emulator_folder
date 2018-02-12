package com.vitalityactive.va.utilities;


import com.vitalityactive.va.BaseTest;
import com.vitalityactive.va.utilities.date.LocalDate;
import com.vitalityactive.va.utilities.date.formatting.DateFormattingUtilities;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class DateUtilitiesTest extends BaseTest {

    @Override
    public void setUp() throws Exception {
        super.setUp();
        initialiseAndroidThreeTen();
    }

    @Test
    public void formatted_date_test_values() {
        String formattedDate = DateFormattingUtilities.getFormattedDate("E,d MMM", new LocalDate("2018-02-05"));

        assertEquals("Mon,5 Feb", formattedDate);
    }
}
