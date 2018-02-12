package com.vitalityactive.va.menu;

import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;

import com.vitalityactive.va.BuildConfig;
import com.vitalityactive.va.R;

public class MenuItem {
    private final MenuItemType type;
    private final int logoResourceId;
    private final String text;
    private final int textResourceId;
    private int tintedColor;
    private int badgeCount;

    public MenuItem(MenuItemType type, int logoResourceId, int textResourceId) {
        this.type = type;
        this.logoResourceId = logoResourceId;
        this.textResourceId = textResourceId;
        this.text = null;
    }

    public MenuItem(MenuItemType type, int logoResourceId, String text) {
        this.type = type;
        this.logoResourceId = logoResourceId;
        this.textResourceId = 0;
        this.text = text;
    }

    public MenuItem(MenuItemType type, int logoResourceId, @ColorInt int tintedColor, int textResourceId) {
        this(type, logoResourceId, textResourceId);
        this.tintedColor = tintedColor;
    }

    public MenuItem(int badgeCount, MenuItemType type, int logoResourceId, int textResourceId) {
        this(type, logoResourceId, textResourceId);
        this.badgeCount = badgeCount;
    }

    public int getLogoResourceId() {
        return logoResourceId;
    }

    public int getTextResourceId() {
        return textResourceId;
    }

    public MenuItemType getType() {
        return type;
    }

    public String getText() {
        return text;
    }

    public int getTintedColor() {
        return tintedColor;
    }

    public int getBadgeCount() {
        return badgeCount;
    }

    public static class Builder {
        @NonNull
        public static MenuItem activity() {
            return new MenuItem(MenuItemType.Activity, R.drawable.activity, R.string.AR_landing_activity_cell_title_711);
        }

        @NonNull
        public static MenuItem rewards() {
            return rewards(0);
        }

        @NonNull
        public static MenuItem rewards(int badgeCount) {
            return new MenuItem(badgeCount, MenuItemType.Rewards, R.drawable.rewards, R.string.AR_landing_rewards_cell_title_695);
        }

        @NonNull
        public static MenuItem learnMore() {
            return new MenuItem(MenuItemType.LearnMore, R.drawable.learn_more, R.string.learn_more_button_title_104);
        }


        @NonNull
        public static MenuItem help() {
            if (BuildConfig.SHOW_HELP) return new MenuItem(MenuItemType.Help, R.drawable.help, R.string.help_button_141);
            return null;
        }

        @NonNull
        public static MenuItem help(@ColorInt int color) {
            if (BuildConfig.SHOW_HELP) return new MenuItem(MenuItemType.Help, R.drawable.help, color, R.string.help_button_141);
            return null;
        }

        @NonNull
        public static MenuItem history() {
            return new MenuItem(MenuItemType.History, R.drawable.history, R.string.AR_rewards_history_segment_title_670);
        }

        @NonNull
        public static MenuItem healthCarePDF() {
            return new MenuItem(MenuItemType.HealthCarePDF, R.drawable.healthcare_pdf_24, R.string.landing_screen_healthcare_pdf_label_248);
        }

        @NonNull
        public static MenuItem disclaimer() {
            return new MenuItem(MenuItemType.Disclaimer, R.drawable.benefit_guides, R.string.generic_disclaimer_button_title_265);
        }

        public static MenuItem bodyMassIndex(String title, MenuItemType menuItemType) {
            return new MenuItem(menuItemType, R.drawable.health_measure_bmi, title);
        }

        public static MenuItem waistCircumference(String title, MenuItemType menuItemType) {
            return new MenuItem(menuItemType, R.drawable.health_measure_waist_circumference, title);
        }

        public static MenuItem bloodGlucose(String title, MenuItemType menuItemType) {
            return new MenuItem(menuItemType, R.drawable.health_measure_bloodglucose, title);
        }

        public static MenuItem bloodPressure(String title, MenuItemType menuItemType) {
            return new MenuItem(menuItemType, R.drawable.health_measure_bloodpressure, title);
        }

        public static MenuItem cholesterol(String title, MenuItemType menuItemType) {
            return new MenuItem(menuItemType, R.drawable.health_measure_cholesterol, title);
        }

        public static MenuItem hbA1C(String title, MenuItemType menuItemType) {
            return new MenuItem(menuItemType, R.drawable.health_measure_hba_1_c, title);
        }

        public static MenuItem urinaryProtein(String title, MenuItemType menuItemType) {
            return new MenuItem(menuItemType, R.drawable.health_measure_urine, title);
        }

        @NonNull
        public static MenuItem getStarted() {
            // todo: import icon, strings
            return new MenuItem(MenuItemType.GetStarted, R.drawable.get_started, R.string.get_started_button_title_103);
        }

        @NonNull
        public static MenuItem termsAndConditions() {
            // todo: import icon, strings
            return new MenuItem(MenuItemType.TermsAndConditions, R.drawable.benefit_guides, R.string.terms_and_conditions_screen_title_94);
        }
    }
}
