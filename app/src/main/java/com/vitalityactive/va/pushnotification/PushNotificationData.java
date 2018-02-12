package com.vitalityactive.va.pushnotification;

import com.google.gson.annotations.SerializedName;

/**
 * Created by kerry.e.lawagan on 1/22/2018.
 */

public class PushNotificationData {

    /** Message will be displayed as banner notification that will appear on the phone’s status drawer/lock screen regardless if the app is currently open or not */
    public static final int BANNER_NOTIFICATION = 1;
    /**
     * When message arrives, message will be shown inside the app regardless of any screen. May also trigger notification badge.
     * If the app is not opened when the message arrives, the messages will be displayed once the application is opened.
     * */
    public static final int IN_APP_NOTIFICATION = 2;
    /**
     * When a message arrives: triggers a  notification badge; message will only be displayed once the user visit the specific screen.
     * If the app is not opened when the message arrives, the messages will be displayed once the user opened the app and navigate to the specific screen.
     */
    public static final int IN_APP_SCREEN_NOTIFICATION = 3;
    /**
     * When a message arrives and the app is not opened, message will be displayed as banner notification that will appear on the phone’s status drawer/lock screen.
     * When a message arrives and the app is currently open, message will be shown inside the app regardless of any screen. May also trigger notification badge.
     */
    public static final int MOBILE_PUSH_IN_APP_NOTIFICATION = 4;
    /**
     * When a message arrives and the app is not opened, message will be displayed as banner notification that will appear on the phone’s status drawer/lock screen.
     * When a message arrives and the app is currently open, triggers a  notification badge; message will only be displayed once the user visit the specific screen.
     */
    public static final int MOBILE_PUSH_IN_APP_SCREEN_NOTIFICATION = 5;

    /** Menu home UI section for badge. */
    public static final int SCREEN_MENU_HOME = 1;
    /** Menu points UI section for badge. */
    public static final int SCREEN_MENU_POINTS = 2;
    /** Menu my health UI section for badge. */
    public static final int SCREEN_MENU_MY_HEALTH = 3;
    /** Menu profile UI section for badge. */
    public static final int SCREEN_MENU_PROFILE = 4;
    public static final int SCREEN_ACTIVE_REWARDS_REWARDS = 5;
    public static final int SCREEN_ACTIVE_REWARDS_LEARN_MORE = 6;
    public static final int SCREEN_ACTIVE_REWARDS_ACTIVATE = 7;

    /**
     * the types of Notification
     */
    @SerializedName("type")
    private int type;
    /**
     * the section of the UI to update badge count:
     */
    @SerializedName("screen")
    private int screen;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getScreen() {
        return screen;
    }

    public void setScreen(int screen) {
        this.screen = screen;
    }

}
