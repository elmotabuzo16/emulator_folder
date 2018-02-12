package com.vitalityactive.va;

import android.content.Context;
import android.support.annotation.NonNull;

import com.vitalityactive.va.appconfig.AppConfigRepository;

public class AppConfigRepositoryWithFixedVersionNumber extends AppConfigRepository {
    public AppConfigRepositoryWithFixedVersionNumber(Context context) {
        super(null, context);
    }

    @NonNull
    @Override
    public String getReleaseVersion() {
        return "0.0";
    }
}
