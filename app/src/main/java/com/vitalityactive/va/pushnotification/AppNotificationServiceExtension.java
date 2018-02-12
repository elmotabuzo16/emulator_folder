package com.vitalityactive.va.pushnotification;

import android.util.Log;

import com.pushwoosh.badge.PushwooshBadge;
import com.pushwoosh.notification.NotificationServiceExtension;
import com.pushwoosh.notification.PushMessage;

/**
 * Created by kerry.e.lawagan on 12/4/2017.
 */

public class AppNotificationServiceExtension extends NotificationServiceExtension {

    @Override
    protected void startActivityForPushMessage(PushMessage message) {
        // super.startActivityForPushMessage() starts default launcher activity
        // or activity marked with ${applicationId}.MESSAGE action.
        // Simply do not call it to override this behaviour.
        // super.startActivityForPushMessage(message);

        PushwooshBadge.setBadgeNumber(0);
        Log.d("Kerry Pushwoosh",  message.getCustomData());
        Log.d("Kerry Pushwoosh message",  message.toJson().toString());

        super.startActivityForPushMessage(message);
    }
}
