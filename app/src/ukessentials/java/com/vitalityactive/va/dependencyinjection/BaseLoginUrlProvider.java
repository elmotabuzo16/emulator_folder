package com.vitalityactive.va.dependencyinjection;

import com.vitalityactive.va.BuildConfig;

public class BaseLoginUrlProvider {
    private BaseUrl envValue = Enum.valueOf(BaseUrl.class, BuildConfig.UKE_INITIAL_WEB_LOGIN_ENUM_ID);

    public BaseLoginUrlProvider(BaseUrl value){
        this.envValue = value;
    }

    public String getUrl() {
        switch (envValue){
            case DEV:
            case TEST:
                return BuildConfig.TEST_LOGIN_URL;
            case QA:
                return BuildConfig.QA_LOGIN_URL;
            case QA_FF:
                return BuildConfig.QA_FF_LOGIN_URL;
            case PROD:
                return BuildConfig.PROD_LOGIN_URL;
            default:
                return BuildConfig.PROD_LOGIN_URL;
        }
    }

    public void setCurrentWebURL(BaseUrl value) {
        this.envValue = value;
    }

    public BaseUrl getCurrentWebURL() {
        return envValue;
    }
}
