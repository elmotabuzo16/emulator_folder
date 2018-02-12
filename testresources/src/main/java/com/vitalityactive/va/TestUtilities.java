package com.vitalityactive.va;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class TestUtilities {
    // This method is duplicated in FileUtilities because the cucumber tests blow up if I move it to a library and depend on it in both the app and the tests
    public static String readFile(InputStream inputStream) {
        final StringBuilder sb = new StringBuilder();
        String strLine;
        try (final BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"))) {
            while ((strLine = reader.readLine()) != null) {
                sb.append(strLine);
            }
        } catch (final IOException ignore) {
            //ignore
        }
        return sb.toString();
    }

    public String readFile(String path) {
        return readFile(getResourceAsStream(path));
    }

    public InputStream getResourceAsStream(String path) {
        return getClass().getClassLoader().getResourceAsStream(path);
    }

    public static final String ACCESS_TOKEN = "Authorization: Bearer 124ad3a4-82e2-3e9e-b4be-b8705550c4e5";
}
