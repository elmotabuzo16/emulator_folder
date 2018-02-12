package com.vitalityactive.va.login.getstarted;

import android.app.Activity;

import com.vitalityactive.va.BasePresenter;
import com.vitalityactive.va.dependencyinjection.BaseLoginUrlProvider;
import com.vitalityactive.va.dependencyinjection.BaseUrl;

public class UKEGetStartedPresenter extends BasePresenter<UKEGetStartedPresenter.UserInterface> {
    private UKEWebBreakoutController webBreakoutController;

    public UKEGetStartedPresenter(UKEWebBreakoutController webBreakoutController) {
        this.webBreakoutController = webBreakoutController;
    }

    public void onGetStarted(Activity activity, BaseUrl webURL) {
        BaseLoginUrlProvider urlProvider = new BaseLoginUrlProvider(webURL);
        webBreakoutController.show(activity,  urlProvider);
    }

    public interface UserInterface {
    }
}
