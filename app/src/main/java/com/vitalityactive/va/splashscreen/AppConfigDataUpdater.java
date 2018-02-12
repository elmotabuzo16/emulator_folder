package com.vitalityactive.va.splashscreen;

public interface AppConfigDataUpdater {
    void updateAppConfig();

    void handleEmptyAppConfigResponse();

    boolean didUpdateAppConfigData();

    void fetchLogo();
}
