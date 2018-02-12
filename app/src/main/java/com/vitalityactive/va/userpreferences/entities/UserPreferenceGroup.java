package com.vitalityactive.va.userpreferences.entities;

import com.vitalityactive.va.DeviceSpecificPreferences;
import com.vitalityactive.va.R;
import com.vitalityactive.va.userpreferences.RememberMePreferencePresenter;
import com.vitalityactive.va.userpreferences.UserPreferencePresenter;

import java.util.ArrayList;

import javax.inject.Inject;

public class UserPreferenceGroup {
    private int title;
    private int description;
    public ArrayList<DefaultUserPreferencePresenter> userPreferenceItems;
    private final boolean hasPrivacyStatementButton;
    private UserPreferenceType userPreferenceType;

    private DefaultUserPreferencePresenter fingerprintItem;

    public enum UserPreferenceType{
        PRIVACY,
        COMMUNICATION,
        SECURITY
    }

    public UserPreferenceGroup(int title,
                               int description,
                               ArrayList<DefaultUserPreferencePresenter> userPreferenceItems,
                               boolean hasPrivacyStatementButton, UserPreferenceType userPreferenceType) {
        this.title = title;
        this.description = description;
        this.userPreferenceItems = userPreferenceItems;
        this.hasPrivacyStatementButton = hasPrivacyStatementButton;
        this.userPreferenceType = userPreferenceType;
    }

    public UserPreferenceGroup(int title,
                               int description,
                               ArrayList<DefaultUserPreferencePresenter> userPreferenceItems,
                               boolean hasPrivacyStatementButton, UserPreferenceType userPreferenceType,
                               DefaultUserPreferencePresenter fingerprintItem) {
        this.title = title;
        this.description = description;
        this.userPreferenceItems = userPreferenceItems;
        this.hasPrivacyStatementButton = hasPrivacyStatementButton;
        this.userPreferenceType = userPreferenceType;
        this.fingerprintItem = fingerprintItem;
    }

    public int getTitle() {
        return title;
    }

    public void setTitle(int title) {
        this.title = title;
    }

    public int getDescription() {
        return description;
    }

    public boolean hasPrivacyStatementButton() {
        return hasPrivacyStatementButton;
    }

    public UserPreferenceType getUserPreferenceType() {
        return userPreferenceType;
    }

    public static class Builder {
        public static UserPreferenceGroup communication(DefaultUserPreferencePresenter emailPreferencePresenter) {
            ArrayList<DefaultUserPreferencePresenter> userPreferenceItems = new ArrayList<>();

            userPreferenceItems.add(emailPreferencePresenter);

            userPreferenceItems.add(new DefaultUserPreferencePresenter(R.string.user_prefs_push_message_toggle_title_67,
                    R.string.user_prefs_push_message_toggle_message_68,
                    false, R.drawable.icn_notification, UserPreferencePresenter.Type.Notifications, false, true, null));

            return new UserPreferenceGroup(R.string.user_prefs_communication_group_header_title_64,
                    R.string.user_prefs_communication_group_header_message_65,
                    userPreferenceItems,
                    false,
                    UserPreferenceType.COMMUNICATION);
        }


        public static UserPreferenceGroup security(final DeviceSpecificPreferences deviceSpecificPreferences,
                                                   UserPreferencePresenter.SettingsToggleChangeListener listener,
                                                   boolean withFingerprint, RememberMePreferencePresenter rememberMePreferencePresenter) {
            ArrayList<DefaultUserPreferencePresenter> userPreferenceItems = new ArrayList<>();

            // fingerprint option is not in L1
//            userPreferenceItems.add(new DefaultUserPreferencePresenter("Use Fingerprint",
//                    "Fingerprint is a secure way to avoid having to type in your password every time Vitality needs to verify your identity to access private information.",
//                    false, R.drawable.icn_fingerprint, null));

            DefaultUserPreferencePresenter fingerprintItem = null;
            if(withFingerprint) {

                fingerprintItem = new DefaultUserPreferencePresenter(R.string.user_prefs_fingerprint_title_92,
                        R.string.user_prefs_fingerprint_message_93,
                        deviceSpecificPreferences.isFingerprint(),
                        R.drawable.icn_fingerprint, UserPreferencePresenter.Type.Fingerprint, true, false, listener);

                userPreferenceItems.add(fingerprintItem);
            }

            userPreferenceItems.add(rememberMePreferencePresenter);

            return new UserPreferenceGroup(R.string.user_prefs_security_group_header_title_77,
                    R.string.user_prefs_security_group_header_message_78,
                    userPreferenceItems,
                    false,
                    UserPreferenceType.SECURITY,
                    fingerprintItem);
        }


        public static UserPreferenceGroup privacy(DeviceSpecificPreferences deviceSpecificPreferences) {
            ArrayList<DefaultUserPreferencePresenter> userPreferenceItems = new ArrayList<>();

            userPreferenceItems.add(new DefaultUserPreferencePresenter(R.string.user_prefs_analytics_toggle_title_73,
                    R.string.user_prefs_analytics_toggle_message_74,
                    deviceSpecificPreferences.isAnalyticsEnabled(),
                    R.drawable.icn_analytics, UserPreferencePresenter.Type.Analytics, true, false, null));

            userPreferenceItems.add(new DefaultUserPreferencePresenter(R.string.user_prefs_crash_reports_toggle_title_75,
                    R.string.user_prefs_crash_reports_toggle_message_76,
                    deviceSpecificPreferences.isCrashReportsEnabled(),
                    R.drawable.icn_crashreports, UserPreferencePresenter.Type.CrashReports, true, false, null));

            return new UserPreferenceGroup(R.string.user_prefs_privacy_group_header_title_70,
                    R.string.user_prefs_privacy_group_header_message_71,
                    userPreferenceItems,
                    true,
                    UserPreferenceType.PRIVACY);
        }

        public static UserPreferenceGroup ukePrivacy(DeviceSpecificPreferences deviceSpecificPreferences,
                                                     DefaultUserPreferencePresenter shareVitalityStatusPresenter) {

            ArrayList<DefaultUserPreferencePresenter> userPreferenceItems = new ArrayList<>();

            userPreferenceItems.add(shareVitalityStatusPresenter);

            userPreferenceItems.add(new DefaultUserPreferencePresenter(R.string.user_prefs_analytics_toggle_title_73,
                    R.string.user_prefs_analytics_toggle_message_74,
                    deviceSpecificPreferences.isAnalyticsEnabled(),
                    R.drawable.icn_analytics, UserPreferencePresenter.Type.Analytics, true, false, null));

            userPreferenceItems.add(new DefaultUserPreferencePresenter(R.string.user_prefs_crash_reports_toggle_title_75,
                    R.string.user_prefs_crash_reports_toggle_message_76,
                    deviceSpecificPreferences.isCrashReportsEnabled(),
                    R.drawable.icn_crashreports, UserPreferencePresenter.Type.CrashReports, true, false, null));

            return new UserPreferenceGroup(R.string.user_prefs_privacy_group_header_title_70,
                    R.string.user_prefs_privacy_group_header_message_71,
                    userPreferenceItems,
                    true,
                    UserPreferenceType.PRIVACY);
        }

    }
}
