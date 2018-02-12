package com.vitalityactive.va.testutilities;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Environment;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.util.Log;
import android.view.View;

import com.jraska.falcon.Falcon;
import com.vitalityactive.va.utilities.TimeUtilities;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;

import java.io.File;
import java.util.Locale;

/* modified from http://stackoverflow.com/a/29929248/112705 for using Falcon */

public class ScreenshotAction implements ViewAction {
    private static final String TAG = "ScreenshotAction";
    private static final String SCREENSHOTS_DIRECTORY = "screenshots";
    private final String screenshotName;

    public ScreenshotAction(String testDetails) {
        this.screenshotName = String.format(Locale.getDefault(), "%d-%s.png", TimeUtilities.getCurrentTimestamp(), testDetails).replaceAll(" ", "-");
    }

    private static Activity getActivity(View view) {
        Context context = view.getContext();
        while (!(context instanceof Activity)) {
            if (context instanceof ContextWrapper) {
                context = ((ContextWrapper) context).getBaseContext();
            } else {
                throw new IllegalStateException("Got a context of class " + context.getClass() + " cannot get the Activity from it");
            }
        }
        return (Activity) context;
    }

    @Override
    public Matcher<View> getConstraints() {
        return Matchers.isA(View.class);
    }

    @Override
    public String getDescription() {
        return "Taking a screenshot using Falcon";
    }

    @Override
    public void perform(UiController uiController, View view) {
        File dir = new File(Environment.getExternalStorageDirectory(), SCREENSHOTS_DIRECTORY);
        if (!dir.exists() && !dir.mkdirs()) {
            Log.w(TAG, "failed to create screenshot directory " + dir.getAbsolutePath());
            return;
        }

        try {
            File file = new File(dir, screenshotName);
            Falcon.takeScreenshot(getActivity(view), file);
            Log.d(TAG, "took screenshot " + file.getAbsolutePath());
        } catch (Throwable t) {
            Log.w(TAG, "failed to take screenshot", t);
        }
    }
}
