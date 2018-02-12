package com.vitalityactive.va.utilities;

import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.customtabs.CustomTabsCallback;
import android.support.customtabs.CustomTabsClient;
import android.support.customtabs.CustomTabsIntent;
import android.support.customtabs.CustomTabsServiceConnection;
import android.support.customtabs.CustomTabsSession;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.vitalityactive.va.R;

public class CustomTabHelper {
    private static final String PACKAGE_NAME = "com.android.chrome";
    private static final String TAG = "CustomTabHelper";
    private final Context context;
    private CustomTabsServiceConnection connection;
    private CustomTabsClient customTabsClient;
    private boolean isCustomTabsAvailable;
    private CustomTabsSession customTabsSession;

    public CustomTabHelper(Context context) {
        this.context = context;
    }

    public void bind(final String url) {
        bind(url, new CustomTabsCallback());
    }

    public void bind(final String url, final CustomTabsCallback customTabsCallback) {
        connection = new CustomTabsServiceConnection() {
            @Override
            public void onCustomTabsServiceConnected(ComponentName name, CustomTabsClient client) {
                customTabsClient = client;
                prepareBrowserPage(url, customTabsCallback);
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                customTabsClient = null;
            }
        };

        isCustomTabsAvailable = CustomTabsClient.bindCustomTabsService(context, PACKAGE_NAME, connection);
    }

    public void unbind() {
        if (connection != null && customTabsClient != null) {
            context.unbindService(connection);
            connection = null;
            customTabsClient = null;
        }
    }

    public void launch(String url) {
        launch(url, new EmptyPreLaunchIntentHandler());
    }

    public void launch(String url, PreLaunchIntentHandler handler) {
        launch(context, url, handler);
    }

    public void launch(Context context, String url, PreLaunchIntentHandler intentHandler) {
        try {
            CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder(customTabsSession)
                    .setToolbarColor(ContextCompat.getColor(context, R.color.colorPrimaryDark));
            CustomTabsIntent tabsIntent = builder.build();
            intentHandler.builtIntent(tabsIntent.intent);
            tabsIntent.launchUrl(context, Uri.parse(url));
        } catch (ActivityNotFoundException ex) {
            Log.d(TAG, "failed to launch activity", ex);
        }
    }

    private void prepareBrowserPage(String url, CustomTabsCallback customTabsCallback) {
        customTabsClient.warmup(0);

        customTabsSession = customTabsClient.newSession(customTabsCallback);
        customTabsSession.mayLaunchUrl(Uri.parse(url), null, null);
    }

    public interface PreLaunchIntentHandler {
        void builtIntent(Intent intent);
    }

    static class EmptyPreLaunchIntentHandler implements CustomTabHelper.PreLaunchIntentHandler {
        @Override
        public void builtIntent(Intent intent) {
        }
    }

    public static class ReferrerPreLaunchIntentHandler implements CustomTabHelper.PreLaunchIntentHandler {
        private final Uri referrer;

        public ReferrerPreLaunchIntentHandler(Uri referrer) {
            this.referrer = referrer;
        }

        @Override
        public void builtIntent(Intent intent) {
            intent.putExtra(Intent.EXTRA_REFERRER, referrer);
        }
    }
}
