package com.vitalityactive.va.utilities;

import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

public class TextUtilities {
    private static final String TAG = TextUtilities.class.getSimpleName();

    public static boolean isNullOrWhitespace(CharSequence text) {
        return text == null || text.length() == 0 || isWhitespace(text);
    }

    public static String toCamelCase(String string) {
        String[] parts = string.split(" ");
        String camelCaseString = "";
        for (String part : parts) {
            if (part != null && part.trim().length() > 0)
                camelCaseString = camelCaseString + toProperCase(part);
            else
                camelCaseString = camelCaseString + part + " ";     // todo: this is returning with an extra space
        }
        return camelCaseString;
    }

    public static String toProperCase(String s) {
        String temp = s.trim();
        String spaces = "";
        if (temp.length() != s.length()) {
            int startCharIndex = s.charAt(temp.indexOf(0));
            spaces = s.substring(0, startCharIndex);
        }
        temp = temp.substring(0, 1).toUpperCase() +
                spaces + temp.substring(1).toLowerCase() + " ";
        return temp;

    }

    private static boolean isWhitespace(CharSequence text) {
        for (int i = 0, length = text.length(); i < length; ) {
            int codePoint = Character.codePointAt(text, i);
            if (!Character.isWhitespace(codePoint)) {
                return false;
            }
            i += Character.charCount(codePoint);
        }
        return true;
    }

    public static String getFormattedString(View rootView, @StringRes int stringId, Object... formatArgs) {
        return getString(rootView.getResources(), stringId, formatArgs);
    }

    public static String getString(Resources resources, @StringRes int resId) {
        return resources.getString(resId);
    }

    public static String getString(Resources resources, @StringRes int resId, Object... formatArgs) {
        return resources.getString(resId, formatArgs);
    }

    public static Integer getIntegerFromString(String string) {
        try {
            if (TextUtilities.isNullOrWhitespace(string)) {
                return 0;
            }
            return Integer.valueOf(string);
        } catch (NumberFormatException e) {
            Log.e(TAG, "getIntegerFromString(): ", e);
        }
        return 0;
    }

    public static boolean isNullOrEmpty(String s) {
        return s == null || s.isEmpty();
    }

    public static double getDoubleFromString(String value) {
        try {
            if (!TextUtils.isEmpty(value)) {
                return Double.parseDouble(value);
            }
        } catch (NumberFormatException e) {
            Log.e(TAG, "getDoubleFromString(): ", e);
        }
        return 0;
    }

    public static long getLongFromString(String value) {
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return 0L;
        }
    }

    @NonNull
    public static <T> String toStringOrEmpty(T value) {
        return value == null ? "" : value.toString();
    }

    public static boolean isNotNullAndHasAnyInput(CharSequence text) {
        return text != null && !isWhitespace(text) && text.length() > 0;
    }
}
