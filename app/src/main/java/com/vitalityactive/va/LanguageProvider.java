package com.vitalityactive.va;

import android.content.res.Resources;
import android.os.Build;

import java.util.Locale;

public class LanguageProvider {
    private final Resources resources;

    public LanguageProvider(Resources resources) {
        this.resources = resources;
    }

    @SuppressWarnings("deprecation")
    private Locale getLocale() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return resources.getConfiguration().getLocales().get(0);
        } else {
            return resources.getConfiguration().locale;
        }
    }

    public String getCurrentSystemLanguage() {
        return getLocale().getLanguage();
    }

    /*
        There doesn't seem to be a way to get the locale that Android chose at runtime if we don't
        have a strings file for the device locale, so we add a string to each strings file
        containing its locale identifier and send that to the backend.

        All of the below return the system locale:
            Locale.getDefault()
            resources.getConfiguration().getLocales().get(0)
            resources.getConfiguration().locale
    */
    public String getCurrentAppLocale() {
        return resources.getString(R.string.current_locale);
    }
}
