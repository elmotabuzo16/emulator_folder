package com.vitalityactive.va.testutilities;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

public class AssertUtils {
    public static void assertStringMatches(String s, String regex) throws InterruptedException {
        assertNotNull(String.format("[null] does not match %s", regex), s);
        assertTrue(String.format("%s does not match %s", s, regex), s.matches(regex));
    }

    public static void assertUrlPathMatch(String s, String urlPath) throws InterruptedException {
        assertStringMatches(s, ".*/" + urlPath + "/?.*");
    }
}
