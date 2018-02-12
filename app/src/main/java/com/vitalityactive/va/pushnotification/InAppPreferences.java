package com.vitalityactive.va.pushnotification;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by kerry.e.lawagan on 1/22/2018.
 */

public class InAppPreferences {
    public static final String SHARED_PREFERENCES_FILENAME = "com.vitalityactive.va.pushnotification.InAppPreferences";

    private static final String MENU_HOME_BADGE = "PushNotification.MENU_HOME_BADGE";
    private static final String MENU_POINTS_BADGE = "PushNotification.MENU_POINTS_BADGE";
    private static final String MENU_MY_HEALTH_BADGE = "PushNotification.MENU_MY_HEALTH_BADGE";
    private static final String MENU_PROFILE_BADGE = "PushNotification.MENU_PROFILE_BADGE";

    private static final String IN_APP_MESSAGES = "PushNotification.IN_APP_MESSAGES";
    private static final String HOME_MESSAGES = "PushNotification.HOME_MESSAGES";
    private static final String POINTS_MESSAGES = "PushNotification.POINTS_MESSAGES";
    private static final String MY_HEALTH_MESSAGES = "PushNotification.MY_HEALTH_MESSAGES";
    private static final String PROFILE_MESSAGES = "PushNotification.PROFILE_MESSAGES";

    private final SharedPreferences sharedPreferences;

    public InAppPreferences(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    @SuppressLint("ApplySharedPref")
    public void clearAll() {
        sharedPreferences.edit().clear().commit();
    }

    private SharedPreferences.Editor getEditor() {
        return sharedPreferences.edit();
    }

    private void setBadgeSharedPreference(String key, int value) {
        getEditor().putInt(key, value).apply();
    }

    public int currentMenuHomeBadgePreferences() {
        return sharedPreferences.getInt(MENU_HOME_BADGE, 0);
    }

    public int currentMenuPointsBadgePreferences() {
        return sharedPreferences.getInt(MENU_POINTS_BADGE, 0);
    }
    public int currentMenuMyHealthBadgePreferences() {
        return sharedPreferences.getInt(MENU_MY_HEALTH_BADGE, 0);
    }

    public int currentMenuProfileBadgePreferences() {
        return sharedPreferences.getInt(MENU_PROFILE_BADGE, 0);
    }

    public void incrementMenuHomeBadgePreferences() {
        int currentCount = currentMenuHomeBadgePreferences();
        currentCount++;
        setBadgeSharedPreference(MENU_HOME_BADGE, currentCount);
    }

    public void incrementMenuPointsBadgePreferences() {
        int currentCount = currentMenuPointsBadgePreferences();
        currentCount++;
        setBadgeSharedPreference(MENU_POINTS_BADGE, currentCount);
    }

    public void incrementMenuMyHealthBadgePreferences() {
        int currentCount = currentMenuMyHealthBadgePreferences();
        currentCount++;
        setBadgeSharedPreference(MENU_MY_HEALTH_BADGE, currentCount);
    }

    public void incrementMenuProfileBadgePreferences() {
        int currentCount = currentMenuProfileBadgePreferences();
        currentCount++;
        setBadgeSharedPreference(MENU_PROFILE_BADGE, currentCount);
    }

    public void resetMenuHomeBadge() {
        setBadgeSharedPreference(MENU_HOME_BADGE, 0);
    }

    public void resetMenuPointsBadge() {
        setBadgeSharedPreference(MENU_POINTS_BADGE, 0);
    }

    public void resetMenuMyHealthBadge() {
        setBadgeSharedPreference(MENU_MY_HEALTH_BADGE, 0);
    }

    public void resetMenuProfileBadge() {
        setBadgeSharedPreference(MENU_PROFILE_BADGE, 0);
    }

    public Set<String> getInAppMessages() {
        return sharedPreferences.getStringSet(IN_APP_MESSAGES, new HashSet<String>());
    }

    public Set<String> getHomeMessages() {
        return sharedPreferences.getStringSet(HOME_MESSAGES, new HashSet<String>());
    }

    public Set<String> getPointsMessages() {
        return sharedPreferences.getStringSet(POINTS_MESSAGES, new HashSet<String>());
    }

    public Set<String> getMyHealthMessages() {
        return sharedPreferences.getStringSet(MY_HEALTH_MESSAGES, new HashSet<String>());
    }

    public Set<String> getProfileMessages() {
        return sharedPreferences.getStringSet(PROFILE_MESSAGES, new HashSet<String>());
    }

    private void setMessagesSharedPreference(String key, Set<String> messages) {
        getEditor().putStringSet(key, messages).apply();
    }

    public void addInAppMessage(String message) {
        Set<String> messages = getInAppMessages();
        messages.add(message);
        setMessagesSharedPreference(IN_APP_MESSAGES, messages);
    }

    public void addHomeMessage(String message) {
        Set<String> messages = getHomeMessages();
        messages.add(message);
        setMessagesSharedPreference(HOME_MESSAGES, messages);
    }

    public void addPointsMessage(String message) {
        Set<String> messages = getPointsMessages();
        messages.add(message);
        setMessagesSharedPreference(POINTS_MESSAGES, messages);
    }

    public void addMyHealthMessage(String message) {
        Set<String> messages = getMyHealthMessages();
        messages.add(message);
        setMessagesSharedPreference(MY_HEALTH_MESSAGES, messages);
    }

    public void addProfileMessage(String message) {
        Set<String> messages = getProfileMessages();
        messages.add(message);
        setMessagesSharedPreference(PROFILE_MESSAGES, messages);
    }

    public void resetInAppMessage() {
        setMessagesSharedPreference(IN_APP_MESSAGES, new HashSet<String>());
    }

    public void resetHomeMessage() {
        setMessagesSharedPreference(HOME_MESSAGES, new HashSet<String>());
    }

    public void resetPointsMessage() {
        setMessagesSharedPreference(POINTS_MESSAGES, new HashSet<String>());
    }

    public void resetMyHealthMessage() {
        setMessagesSharedPreference(MY_HEALTH_MESSAGES, new HashSet<String>());
    }

    public void resetProfileMessage() {
        setMessagesSharedPreference(PROFILE_MESSAGES, new HashSet<String>());
    }
}
