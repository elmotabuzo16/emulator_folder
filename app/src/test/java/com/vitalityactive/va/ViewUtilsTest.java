package com.vitalityactive.va;

import com.vitalityactive.va.utilities.ViewUtilities;

import org.junit.Test;
import org.junit.runner.RunWith;

import junitparams.JUnitParamsRunner;

import static junit.framework.Assert.assertEquals;

@RunWith(JUnitParamsRunner.class)
public class ViewUtilsTest {
    @Test
    public void decimal_formatter_formats_correctly() {
        assertEquals("100", ViewUtilities.roundToTwoDecimalsRemoveTrailingZeros("100"));
        assertEquals("100", ViewUtilities.roundToTwoDecimalsRemoveTrailingZeros("100.00"));
        assertEquals("123", ViewUtilities.roundToTwoDecimalsRemoveTrailingZeros("123"));
        assertEquals("100.1", ViewUtilities.roundToTwoDecimalsRemoveTrailingZeros("100.1"));
        assertEquals("100.12", ViewUtilities.roundToTwoDecimalsRemoveTrailingZeros("100.12"));
        assertEquals("100", ViewUtilities.roundToTwoDecimalsRemoveTrailingZeros("100.000000001"));
    }
}
