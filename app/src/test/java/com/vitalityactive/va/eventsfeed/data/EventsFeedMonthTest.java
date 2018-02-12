package com.vitalityactive.va.eventsfeed.data;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

/**
 * Created by jayellos on 11/24/17.
 */
public class EventsFeedMonthTest {


    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetters() throws Exception {
        String month = "Jan";
        String year = "2017";

        EventsFeedMonth eventsFeedMonth = new EventsFeedMonth(month, year);

        Assert.assertEquals(eventsFeedMonth.getMonthName(), month);
        Assert.assertEquals(eventsFeedMonth.getYear(), year);
    }
}