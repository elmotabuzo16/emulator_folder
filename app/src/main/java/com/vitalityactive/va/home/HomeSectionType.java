package com.vitalityactive.va.home;

public enum HomeSectionType {
    KNOW_YOUR_HEALTH("1"),
    IMPROVE_YOUR_HEALTH("2"),
    GET_REWARDED("3"),
    UNKNOWN("-1");

    private String typeKey;

    HomeSectionType(String typeKey) {
        this.typeKey = typeKey;
    }

    public String getTypeKey() {
        return typeKey;
    }

    public static HomeSectionType fromValue(String value) {
        switch (value) {
            case "1":
                return KNOW_YOUR_HEALTH;
            case "2":
                return IMPROVE_YOUR_HEALTH;
            case "3":
                return GET_REWARDED;
            default:
                return UNKNOWN;
        }
    }
}
