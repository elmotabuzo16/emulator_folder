package com.vitalityactive.va.eventsfeed;

/**
 * Created by jayellos on 11/27/17.
 */

public class EventCategoryList {
    public static final int LOGIN =1;
    public static final int AGREEMENT = 2;
    public static final int SERVICING = 3;
    public static final int TARGET = 4;
    public static final int REWARD = 5;
    public static final int ASSESSMENT = 6;
    public static final int ERROR = 7;
    public static final int NOTIFICATION = 8;
    public static final int FINANCE = 9;
    public static final int SOCIAL = 10;
    public static final int POINTS = 11;
    public static final int DEVICE = 12;
    public static final int INTEGRATION = 13;
    public static final int PRODUCT = 14;
    public static final int STATUS = 15;
    public static final int DOCUMENTMANAGEMENT = 16;
    public static final int LEGAL = 17;
    public static final int HEALTHATTRIBUTE = 18;
    public static final int TERMSANDCONDITIONS = 19;
    public static final int DATAPRIVACY = 20;
    public static final int ENROLLMENT = 21;
    public static final int VHC_CAT = 22;
    public static final int SCREENINGS = 23;
    public static final int VACCINATIONS = 24;
    public static final int SCREENANDVACC = 25;
    public static final int NUTRITION = 26;
    public static final int FITNESS = 27;
    public static final int DISCLAIMER = 28;
    public static final int UNKNOWN_CAT = -1;


    public static class EventCategory{
        private int key;
        private String name;

        public EventCategory(int key, String name) {
            this.key = key;
            this.name = name;
        }

        public int getKey() {
            return key;
        }

        public String getName() {
            return name;
        }
    }
}
