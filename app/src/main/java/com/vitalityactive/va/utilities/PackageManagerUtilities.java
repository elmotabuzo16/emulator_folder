package com.vitalityactive.va.utilities;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;

import java.util.List;

public class PackageManagerUtilities {
    private Context context;

    public PackageManagerUtilities(Context context) {
        this.context = context;
    }

    public boolean isPackageInstalled(String packageName) {
        try {
            context.getPackageManager().getPackageInfo(packageName, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    public void installPackageFromPlayStore(String packageName) {
        Intent rateIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("market://details?id=" + packageName));
        boolean playStoreFound = false;

        final List<ResolveInfo> otherApps = context.getPackageManager()
                .queryIntentActivities(rateIntent, 0);
        for (ResolveInfo otherApp : otherApps) {
            if (otherApp.activityInfo.applicationInfo.packageName
                    .equals("com.android.vending")) {

                ActivityInfo otherAppActivity = otherApp.activityInfo;
                ComponentName componentName = new ComponentName(
                        otherAppActivity.applicationInfo.packageName,
                        otherAppActivity.name
                );
                rateIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                rateIntent.addFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                rateIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                rateIntent.setComponent(componentName);
                context.startActivity(rateIntent);
                playStoreFound = true;
                break;

            }
        }

        if (!playStoreFound) {
            Intent webIntent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id=" + packageName));
            context.startActivity(webIntent);
        }
    }
}
