package com.vitalityactive.va.pushnotification;

import com.google.gson.Gson;
import com.vitalityactive.va.VitalityActiveApplication;
import com.vitalityactive.va.dependencyinjection.DependencyInjector;
import com.vitalityactive.va.events.EventDispatcher;

import javax.inject.Inject;

/**
 * Created by kerry.e.lawagan on 1/11/2018.
 */

public class PushNotificationBroadcaster {

    @Inject
    EventDispatcher eventDispatcher;
    @Inject
    InAppPreferences inAppPreferences;

    DependencyInjector dependencyInjector;

    public PushNotificationBroadcaster() {
        dependencyInjector = ((VitalityActiveApplication) VitalityActiveApplication.getContext()).getDependencyInjector();
        dependencyInjector.inject(this);
    }

    public void processInAppNotification(PushNotificationData pushNotificationData, String message) {
        showInAppMessage(message);
    }

    public void incrementBadge(int screen) {
        switch (screen) {
            case PushNotificationData.SCREEN_MENU_HOME:
                inAppPreferences.incrementMenuHomeBadgePreferences();
                eventDispatcher.dispatchEvent(new MenuBadgeEvent(InAppType.HOME));
                break;
            case PushNotificationData.SCREEN_MENU_POINTS:
                inAppPreferences.incrementMenuPointsBadgePreferences();
                eventDispatcher.dispatchEvent(new MenuBadgeEvent(InAppType.POINTS));
                break;
            case PushNotificationData.SCREEN_MENU_MY_HEALTH:
                inAppPreferences.incrementMenuMyHealthBadgePreferences();
                eventDispatcher.dispatchEvent(new MenuBadgeEvent(InAppType.MY_HEALTH));
                break;
            case PushNotificationData.SCREEN_MENU_PROFILE:
                inAppPreferences.incrementMenuProfileBadgePreferences();
                eventDispatcher.dispatchEvent(new MenuBadgeEvent(InAppType.PROFILE));
                break;
        }
    }

    private void showInAppMessage(String message) {
        inAppPreferences.addInAppMessage(message);
        eventDispatcher.dispatchEvent(new InAppMessageEvent());
    }

    private void showInAppScreenMessage(int screen, String message) {
        switch (screen) {
            case PushNotificationData.SCREEN_MENU_HOME:
                inAppPreferences.addHomeMessage(message);
                eventDispatcher.dispatchEvent(new InAppScreenMessageEvent(InAppType.HOME));
                break;
            case PushNotificationData.SCREEN_MENU_POINTS:
                inAppPreferences.addPointsMessage(message);
                eventDispatcher.dispatchEvent(new InAppScreenMessageEvent(InAppType.POINTS));
                break;
            case PushNotificationData.SCREEN_MENU_MY_HEALTH:
                inAppPreferences.addMyHealthMessage(message);
                eventDispatcher.dispatchEvent(new InAppScreenMessageEvent(InAppType.MY_HEALTH));
                break;
            case PushNotificationData.SCREEN_MENU_PROFILE:
                inAppPreferences.addProfileMessage(message);
                eventDispatcher.dispatchEvent(new InAppScreenMessageEvent(InAppType.PROFILE));
                break;
        }
    }

    public void sendActivitySwtichScreenEvent(ActivitySwitchScreenEvent event) {
        eventDispatcher.dispatchEvent(event);
    }

}
