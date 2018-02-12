package com.vitalityactive.va.utilities;

public class RepositoryUtilities {
    public static <T> T valueOrDefault(T value, T defaultValue) {
        return value == null ? defaultValue : value;
    }
}
