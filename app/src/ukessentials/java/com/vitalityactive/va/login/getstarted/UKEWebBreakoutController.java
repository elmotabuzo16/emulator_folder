package com.vitalityactive.va.login.getstarted;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.vitalityactive.va.dependencyinjection.BaseLoginUrlProvider;
import com.vitalityactive.va.utilities.CustomTabHelper;

public class UKEWebBreakoutController {
    private final Context context;
    private final CustomTabHelper customTabHelper;

    public UKEWebBreakoutController(Context context) {
        this.context = context;
        this.customTabHelper = new CustomTabHelper(context);
    }

    public void show(Activity activity, BaseLoginUrlProvider baseLoginUrlProvider) {
        customTabHelper.bind(baseLoginUrlProvider.getUrl());
        Log.d("UKE ENUM Login ", baseLoginUrlProvider.getCurrentWebURL().toString());
        Log.d("UKE page Login ", baseLoginUrlProvider.getUrl());

        Uri referrer = Uri.parse(2 + "//" + context.getPackageName());
        customTabHelper.launch(activity, baseLoginUrlProvider.getUrl(), new CustomTabHelper.ReferrerPreLaunchIntentHandler(referrer));
    }
}
