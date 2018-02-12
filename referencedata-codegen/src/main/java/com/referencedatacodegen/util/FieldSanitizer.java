package com.referencedatacodegen.util;

public class FieldSanitizer {
    public static String getCleanFieldName(String dirtyFieldName) {
        return '_' + dirtyFieldName.replaceAll("[^a-zA-Z0-9_]", "").trim().toUpperCase();
    }
}
