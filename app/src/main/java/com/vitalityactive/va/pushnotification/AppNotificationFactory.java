package com.vitalityactive.va.pushnotification;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.pushwoosh.badge.PushwooshBadge;
import com.pushwoosh.notification.PushMessage;
import com.pushwoosh.notification.PushwooshNotificationFactory;
import com.vitalityactive.va.R;
import com.vitalityactive.va.VitalityActiveActivityLifecycleCallbacks;

/**
 * Created by kerry.e.lawagan on 12/15/2017.
 */

public class AppNotificationFactory extends PushwooshNotificationFactory {

    private static PushNotificationBroadcaster pushNotificationBroadcaster;
    private Gson gson;
    private NotificationCompat.Builder notificationBuilder;
    private NotificationChannel notificationChannel;

    @Override
    public Notification onGenerateNotification(@NonNull PushMessage pushMessage) {
        Log.d("Kerry Pushwoosh", "onGenerateNotification...");

        PushwooshBadge.addBadgeNumber(1);
        Notification notification = null;
        if (!TextUtils.isEmpty(pushMessage.getCustomData())) {
            Log.d("Kerry Pushwoosh", pushMessage.getCustomData());
            if (pushNotificationBroadcaster==null) {
                pushNotificationBroadcaster = new PushNotificationBroadcaster();
            }
            if (gson==null) {
                gson = new Gson();
            }

            PushNotificationData pushNotificationData = gson.fromJson(pushMessage.getCustomData(), PushNotificationData.class);

            if (pushNotificationData == null) {
                notification = buildNotification(getContentFromHtml(pushMessage.getHeader()), getContentFromHtml(pushMessage.getMessage()), getContentFromHtml(pushMessage.getTicker()));
            } else {
                ShowScreenUtility.screen = pushNotificationData.getScreen();
                if (VitalityActiveActivityLifecycleCallbacks.isAppInForeground()) {
                    pushNotificationBroadcaster.processInAppNotification(pushNotificationData, pushMessage.getMessage());
                } else {
                    notification = buildNotification(getContentFromHtml(pushMessage.getHeader()), getContentFromHtml(pushMessage.getMessage()), getContentFromHtml(pushMessage.getTicker()));
                }
            }
        } else {
            notification = buildNotification(getContentFromHtml(pushMessage.getHeader()), getContentFromHtml(pushMessage.getMessage()), getContentFromHtml(pushMessage.getTicker()));
        }

        return notification;
    }

    private Notification buildNotification(CharSequence contentTitle, CharSequence contentText, CharSequence ticker) {
        if (notificationBuilder==null) {
            notificationBuilder = new NotificationCompat.Builder(getApplicationContext(), "VA_PUSH");
        }

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            createNotificationChannel();
        }

        return notificationBuilder.setContentTitle(contentTitle)
                .setContentText(contentText)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(contentText))
                .setLargeIcon(BitmapFactory.decodeResource(getApplicationContext().getResources(), R.mipmap.ic_launcher))
                .setSmallIcon(R.drawable.ic_notification)
                .setTicker(ticker)
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(true)
                .build();
    }

    @TargetApi(android.os.Build.VERSION_CODES.O)
    private void createNotificationChannel() {
        if (notificationChannel==null) {
            notificationChannel = new NotificationChannel("VA_PUSH", "VA_PUSH", NotificationManager.IMPORTANCE_DEFAULT);

            NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }
}
