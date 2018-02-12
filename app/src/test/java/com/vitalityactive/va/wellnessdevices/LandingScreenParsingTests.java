package com.vitalityactive.va.wellnessdevices;

import com.vitalityactive.va.TestUtilities;

import org.junit.Before;
import org.junit.Test;

public class LandingScreenParsingTests {
    private String json;

    @Before
    public void setUp() {
        json = new TestUtilities().readFile("wellnessdevices/landing_screen.json");
    }

    @Test
    public void can_be_parsed() {
        Utils.parseDeviceActivityResponse(json);
        // no exceptions
    }
}
